package org.example;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);

    public String mainMenu() {
        System.out.println("-- Advanced Encryption Standard menu --");
        System.out.println("---------------------------------------");
        System.out.println("1. Encrypt file.");
        System.out.println("2. Decrypt file.");
        System.out.println("3. Exit program.");
        System.out.println();
        System.out.println("Input an option next line:");
        return scanner.nextLine();
    }

    public void encryptMenu() {

    }

    public void decryptMenu() {

    }
}
