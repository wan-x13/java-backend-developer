
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        FlashCards flashCards = new FlashCards();
        Scanner sc = new Scanner(System.in);
        String optionUser = "";
        while (!optionUser.equals("exit")){
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            FlashCards.scanIO("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            optionUser = sc.nextLine();
            FlashCards.listInput.add(optionUser);
            FlashCards.scanIO(optionUser);
            switch (optionUser){
                case "add"-> flashCards.addTerm();
                case "remove"->flashCards.removeCard();
                case "exit"->{
                    System.out.println("Bye bye!");
                }
                case "hardest card"->flashCards.hardestCard();
                case "reset stats"->flashCards.resetStatisticsCard();
                case "log"->flashCards.logCard();
                case "ask"->flashCards.askCard();
                case "export"->flashCards.exportCard();
                case "import"->flashCards.importCard();
            }
        }
    }
}

class  FlashCards{
    private static final Map<String, List<String>> cardMap = new LinkedHashMap<>();
    private static final Map<String, Integer> errorMap = new LinkedHashMap<>();
    public static  final List<String> listInput = new ArrayList<>();
    public static  final List<String> listOutput = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);
    private static final StringBuilder logBuilder =new StringBuilder();

    public static void scanIO(String str){
        logBuilder.append("\n").append(str);
    }
    public void addTerm(){

        System.out.println("The card:");
        scanIO("The card:");
        String term = sc.nextLine();
        scanIO(term);
        if(cardMap.containsKey(term)){
            System.out.println("The card \""+term+"\" already exists.");
            scanIO("The card \""+term+"\" already exists.");

        }
        else{
            System.out.println("The definition of the card:");
            scanIO("The definition of the card:");
            String definition = sc.nextLine();
            scanIO(definition);
            if(isDefinitionExits(definition)){
                String res = "The definition \""+definition+"\" already exists." ;
                System.out.println(res);
                scanIO(res);
            }
            else {
                cardMap.put(term, List.of(definition));
                errorUtils(term, 0);
                System.out.println("The pair (\""+term+"\":\""+definition+"\") has been added.");
                scanIO("The pair (\""+term+"\":\""+definition+"\") has been added.");
            }
        }
        System.out.println();
    }
    public  void removeCard(){
        System.out.println("Which card?");
        scanIO("Which card?");
        String ans = sc.nextLine();
        scanIO(ans);
        if(cardMap.containsKey(ans)){
            cardMap.remove(ans);
            System.out.println("The card has been removed.");
            scanIO("The card has been removed.\"");
        }
        else {
            System.out.println("Can't remove \""+ans+"\": there is no such card.");
            scanIO("Can't remove \""+ans+"\": there is no such card.");
        }
        System.out.println();
    }
    public void importCard(){
        System.out.println("File name:");
        scanIO("File name:");
        String filename = sc.nextLine();
        scanIO(filename);
        File file = new File(filename);
        int count = 0;
        try(Scanner scanFile = new Scanner(file)){
            while (scanFile.hasNextLine()){
                List<String> item = Arrays.asList(scanFile.nextLine().split(":"));
                int value = 0;
                int index = value < item.size()? value : 0;
                String card = item.get(index);
                value = 1;
                index = value < item.size() ? value : 0;
                String definition =item.get(index).trim();
                value = 2;
                index = value < item.size() ? value : 0;
                String error = item.get(index);
                error = !error.equals(card) ? error  : "0";
                if(cardMap.containsKey(card)){
                    cardMap.replace(card, List.of(definition+":"+error));
                }
                else if(item.size() != 1){
                    cardMap.put(card, List.of(definition+":"+error));
                    count += 1;
                }

            }
            getErrorInFile();
            System.out.println(count+" cards have been loaded.");
            scanIO(count+" cards have been loaded.");
            System.out.println();

        }catch (FileNotFoundException e){
            System.out.println("File not found.");
            scanIO("File not found.");
        }


    }
    public void exportCard(){
        System.out.println("File name:");
        scanIO("File name:");
        String filename = sc.nextLine();
        scanIO(filename);
        File file = new File(filename);
        int count = 0;
        try(FileWriter writer = new FileWriter(file ,true)) {
            for(var entry : cardMap.entrySet()){
                var temp = entry.getValue().get(0).split(":");
                writer.write(entry.getKey()+":"+temp[0]+":"+getErrorMap(entry.getKey())+"\n");
                count += 1;
            }
            System.out.println(count+" cards have been saved.");
            scanIO(count+" cards have been saved.");
            System.out.println();

        }catch (IOException ignored){

        }


    }
    public void askCard(){
        System.out.println("How many times to ask?");
        scanIO("How many times to ask?");
        int time =Integer.parseInt(sc.nextLine());
        String str = ""+time;
        scanIO(str);
        int error = 0;
        while (time != 0){
            String random = randomCard();
            System.out.println("Print the definition of \""+random+"\":");
            scanIO("Print the definition of \""+random+"\":");
            String ans = sc.nextLine();
            scanIO(ans);
            String temp = cardMap.get(random).get(0).split(":")[0];
            if(temp.equals(ans)){
                System.out.println("Correct!");
                scanIO("Correct!");
            }else{
                List<String> tempList = new ArrayList<>();
                for(var entry : cardMap.entrySet()){
                    tempList.add(entry.getValue().get(0).split(":")[0]);
                }
                if(tempList.contains(ans)){
                    error += 1;
                    errorUtils(random, error);
                    System.out.println("Wrong. The right answer is \""+temp+"\", but your definition is correct for \""+findKey(ans)+"\" card.");
                    scanIO("Wrong. The right answer is \""+temp+"\", but your definition is correct for \""+findKey(ans)+"\" card.");
                    error = 0;
                }
                else{
                    error += 1;
                    errorUtils(random, error);
                    System.out.printf("Wrong. The right answer is \"%s\".\n", temp);
                    scanIO(String.format("Wrong. The right answer is \"%s\".\n",temp));
                    error = 0;
                }

            }
            time -= 1;
        }
        System.out.println();



    }
    private static String findKey(String definition){
        for( var entry : cardMap.entrySet()){
            var temp = entry.getValue().get(0).split(":")[0];
            if(temp.equals(definition)){
                return entry.getKey();
            }
        }
        return  "";

    }

    private String randomCard(){
        Random generator = new Random();
        Object[] optionRandom = cardMap.keySet().toArray();
        return (String) optionRandom[generator.nextInt(optionRandom.length)];
    }
    public void logCard(){
        System.out.println("File name:");
        scanIO("File name:");
        String filename = sc.nextLine();
        scanIO(filename);
        File file = new File(filename);
        try(FileWriter writer = new FileWriter(file ,true)) {
            scanIO("The log has been saved.");
            writer.write(logBuilder.toString());
            System.out.println("The log has been saved.");
            System.out.println();
        }catch (IOException ignored){

        }
    }
    public void hardestCard(){
        var copyList = hardestCardUtils();
        if (copyList.isEmpty()) {
            System.out.println("There are no cards with errors.");
            scanIO("There are no cards with errors.");
        }
        else if(copyList.size() == 1){
            System.out.println("The hardest card is \""+ copyList.get(0) +"\". You have "+errorMap.getOrDefault(copyList.get(0), 0) +" errors answering it.");
            scanIO("The hardest card is \""+ copyList.get(0) +"\". You have "+errorMap.getOrDefault(copyList.get(0), 0) +" errors answering it.");

        } else{
            String temp = "";
            for(String str : copyList){
                temp += String.format("\"%s\" ",str);
            }
            String[] arr = temp.trim().split(" ");
            System.out.println("The hardest cards are "+String.join(", ",arr)+". You have "+ copyList.size() +" errors answering them.");
            scanIO("The hardest cards are "+String.join(", ",arr)+". You have "+ copyList.size() +" errors answering them.");

        }

        System.out.println();

    }
    public List<String> hardestCardUtils(){
        int max = Collections.max(errorMap.values());
        List<String> maxValueList = new ArrayList<>();
        for(var entry : errorMap.entrySet()){
            if(entry.getValue()== 0){
                continue;
            }
            if(entry.getValue() == max){
                maxValueList.add(entry.getKey());
            }
        }
        System.out.println(maxValueList);

        return maxValueList;
    }
    private static boolean isDefinitionExits(String definition){
        List<String> list = new ArrayList<>();
        for(var entry : cardMap.values()){
            String temp = entry.get(0).split(":")[0];
            list.add(temp);
        }

        return list.contains(definition);
    }

    private static int getErrorMap(String card) {
        return errorMap.getOrDefault(card, 0);
    }

    private void errorUtils(String card , int error){
        if(errorMap.containsKey(card)){
            errorMap.replace(card, errorMap.get(card)+error);
        }else {
            errorMap.put(card, error);
        }
    }
    private void getErrorInFile(){
        for(var entry : cardMap.entrySet()){
            var temp=entry.getValue().get(0).split(":");
            int index = 1;
            String res = index < temp.length ? temp[1] :"0";
            int error = Integer.parseInt(res);
            errorMap.put(entry.getKey(), error);
        }
    }

    public  void resetStatisticsCard(){
        for(var entry : errorMap.entrySet()){
            errorMap.replace(entry.getKey(), 0);
        }
        System.out.println("Card statistics have been reset.");
        scanIO("Card statistics have been reset.");
        System.out.println();
    }


}

