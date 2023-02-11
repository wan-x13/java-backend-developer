

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // write your code here

        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Hello, "+name);
        game.setName(name);
        String optionsList = scanner.nextLine();
        game.promptOptionsList(optionsList);
        //System.out.println(game.getOptions());
        System.out.println("Okay, let's start");
        if(!game.isPlayerExist()){
            game.setScore(0);
        }

        String input;

        do {
            input = scanner.nextLine().toLowerCase();
            if(game.getOptions().containsKey(input)){
                game.displayGame(input);
            }
            else if (input.equals("!rating")){
                System.out.println("Your rating: "+game.getScore());
            }
            else if(input.equals("!exit")){
                game.createFile();
                System.out.println("Bye!");
            }
            else System.out.println("Invalid input");
        } while (!input.equals("!exit"));



    }
}

class Game{

    private   String name ;
    private int score;
    private final Map<String, String> options = new HashMap<>();

    private final Scanner scanner = new Scanner(System.in);

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
            this.score = score;

    }

    public Map<String, String> getOptions() {
        return options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void displayGame(String player){
        System.out.println(winner(player, computerPay()));
    }
    private String computerPay(){
        Random generator = new Random();
        Object[] optionRandom = this.options.keySet().toArray();
        return (String) optionRandom[generator.nextInt(optionRandom.length)];
    }
    private  String winner (String player , String computer){
        int getWinner = comparator(player, computer);
        if(getWinner == 0){
            this.score += 50;
            return String.format("There is a draw %s", player );
        }
        else if(getWinner == 1){
            this.score += 100;
            return String.format("Well done. The computer chose %s and failed", computer);
        }
        else  return String.format("Sorry, but the computer chose %s", computer);
    }
    public void createFile(){
        String filename = "./rating.txt";
        File file = new File(filename);
        try(FileWriter writer = new FileWriter(filename , true)){
            String copy = readFileAsString(filename);
            Scanner scanner1 = new Scanner(file);
            int tempScore = 0;
            boolean res = false ;
            while (scanner1.hasNext()){
                String user = scanner1.next();
                if(user.equals(this.name)){
                    int temp = scanner1.nextInt();
                    tempScore += temp;
                    res = true;

                }
                else res = false;
            }
            if(res){
                deleteFile(filename);
                copy =  copy.replace(String.format("%s", tempScore), String.format("%d",this.score));
                writer.write(copy);

            }
            else writer.append(String.format("%s %d\n", this.name , this.score));

        }catch (IOException e){
            System.out.println("Something was wrong!");
        }

    }
    public Boolean isPlayerExist(){

        String pathFile = "./rating.txt";
        File file = new File(pathFile);
        try{
            Scanner scanFile = new Scanner(file);
            while (scanFile.hasNext()){
                String tempUser = scanFile.next();
                int tempScore = scanFile.nextInt();
                if(tempUser.equals(this.name)){
                    this.score = tempScore;
                    return true;
                }

            }
        }catch (IOException ignored){

        }
        return false;
    }
    private  String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    private void deleteFile(String filename) throws FileNotFoundException {
        new PrintWriter(filename).close();
    }
    private  int comparator(String player , String computer){
        if(player.equals(computer)){
            return  0;
        }
        return  this.options.get(player).contains(computer) ? 1 : -1;
    }

    public void promptOptionsList(String optionsList) {
        if (!optionsList.isEmpty()) {
            this.options.clear();

            String str = "rock,fire,scissors,snake,human,tree,wolf,sponge,paper,air,water,dragon,devil,lightning,gun";
            String[] optionsArray = str.split(",");
            int length = optionsArray.length;
            int half = length / 2;
            for (int i = 0; i < length; i++) {
                String[] losingTo = new String[half];
                for (int j = i + 1; j < i + half + 1; j++) {
                    losingTo[j - (i + 1)] = optionsArray[j % length];
                }
               this.options.put(optionsArray[i], Arrays.toString(losingTo));
            }
        } else {
            this.options.put("paper", "[rock]");
            this.options.put("scissors", "[paper]");
            this.options.put("rock", "[scissors]");
        }
    }
}
