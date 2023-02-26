package carsharing;




public class Main {

    public static void main(String[] args) {
        // write your code here
          if(args.length > 1 && args[0].equals("-databaseFileName")){
              DBconnection.dbName = args[1].trim();
          }
          Menu menu = new Menu();
          menu.initLogManager();

    }


    }
