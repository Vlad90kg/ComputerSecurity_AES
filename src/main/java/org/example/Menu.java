package org.example;

import CryptoUtils.CryptoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    public void encryptMenu() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        File plainText = new File("plaintext.txt");
        File encrypted = new File("encrypted.txt");
        int keySize = -1;
        boolean stayInMenu = true;
        while (stayInMenu){
            System.out.println("-- Key size --");
            System.out.println("---------------------------------------");
            System.out.println("1. 128 bits.");
            System.out.println("2. 192 bits.");
            System.out.println("3. 256 bits.");
            System.out.println("4. Exit.");

            String option = scanner.nextLine();
            switch (option){
                case "1":
                    keySize = 128;
                    break;
                case "2":
                    keySize = 192;
                    break;
                case "3":
                    keySize = 256;
                    break;
                case "4":
                    stayInMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
            if(option.matches("[123]")){
                SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);
                IvParameterSpec ivParameterSpec = CryptoUtils.generateIv();
                String algorithm = "AES/CBC/PKCS5Padding";
                CryptoUtils.encryptFile(algorithm,key,ivParameterSpec,plainText,encrypted);
            }

        }


    }

    public void decryptMenu() {

    }
}
