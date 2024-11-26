package org.example;


public class Main {
    public static void main(String[] args) {
        Menu menus = new Menu();

        boolean isRunning = true;
        while (isRunning) {
            String option = menus.mainMenu();
            switch (option) {
                case "1":
                    menus.encryptMenu();
                    break;
                case "2":
                    menus.decryptMenu();
                    break;
                case "3":
                    System.out.println("Exiting program...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }


    }
}