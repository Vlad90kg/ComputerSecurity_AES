package main;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
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