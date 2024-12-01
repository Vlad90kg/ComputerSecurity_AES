package main;

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
    private SecretKey actualKey = null;
    IvParameterSpec actualParameterSpec = null;

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
        System.out.println("-- Encryption menu --");
        System.out.println("Input a path to file you would like to encrypt: ");
        String path = scanner.nextLine();
        File plainText = new File(path);
        File encrypted = new File("encrypted.txt");
        int keySize = -1;
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.println("-- Key size --");
            System.out.println("---------------------------------------");
            System.out.println("1. 128 bits.");
            System.out.println("2. 192 bits.");
            System.out.println("3. 256 bits.");
            System.out.println("4. Return to main menu.");

            String option = scanner.nextLine();
            switch (option) {
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
            if (option.matches("[123]")) {
                SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);
                actualKey = key;
                IvParameterSpec ivParameterSpec = CryptoUtils.generateIv();
                actualParameterSpec = ivParameterSpec;
                String algorithm = "AES/CBC/PKCS5Padding";
                CryptoUtils.encryptFile(algorithm, key, ivParameterSpec, plainText, encrypted);
                System.out.println("The file has been successfully encrypted. Check the encrypted.txt");
                CryptoUtils.printSecretKeyAndIV(key, ivParameterSpec);

                stayInMenu = false;
            }
        }
    }

    public void decryptMenu() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.println("-- Decryption menu --");
            System.out.println("Input a path to file you would like to decrypt: ");
            String path = scanner.nextLine();
            String algorithm = "AES/CBC/PKCS5Padding";

            File encryptedFile = new File(path);
            File decryptedFile = new File("decrypted.txt");

            System.out.println("Input the key and Initialization Vector");
            System.out.println("Key:");
            String base64Key = scanner.nextLine();
            SecretKey secretKey = CryptoUtils.decodeBase64ToSecretKey(base64Key,"AES");
            System.out.println("IV:");
            String base64iv = scanner.nextLine();
            IvParameterSpec iv = CryptoUtils.decodeBase64ToIv(base64iv);
            CryptoUtils.decryptFile(algorithm, secretKey, iv, encryptedFile, decryptedFile);
            System.out.println("The file has been successfully decrypted. Check the decrypted.txt");

            stayInMenu = false;
        }
    }

}
