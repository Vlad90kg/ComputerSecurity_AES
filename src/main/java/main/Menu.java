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

        int keySize = -1;
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.println("-- Encryption menu --");
            System.out.println("Input a path to file you would like to encrypt: ");
            String path = scanner.nextLine();
            if (doesFileExist(path)) {
                File plainText = new File(path);
                File encrypted = new File("encrypted.txt");

                System.out.println("-- Key size --");
                System.out.println("---------------------------------------");
                System.out.println("1. 128 bits.");
                System.out.println("2. 192 bits.");
                System.out.println("3. 256 bits.");
                System.out.println("4. Return to the main menu.");

                String option = scanner.nextLine().trim();
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
                        System.out.println("Invalid option.Please select a valid key size (1, 2, or 3)");
                        break;
                }
                if (option.matches("[123]")) {
                    System.out.println("-- Choose AES mode --\n1. ECB.\n2. CBC(recommended)\n3. Return to the main menu");
                    option = scanner.nextLine().trim();
                    boolean modes = true;
                    while (modes) {
                        switch (option) {
                            case "1":
                                ecbMenu(keySize, plainText, encrypted);
                                modes = false;
                                break;
                            case "2":
                                cbcMenu(keySize, plainText, encrypted);
                                modes = false;
                                break;
                            case "3":
                                modes = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please select a valid AES mode(1 or 2)");
                                break;
                        }
                    }
                    stayInMenu = false;
                }
            } else {
                stayInMenu = invalidPathMenu();
            }

        }
    }

    public void cbcMenu(int keySize, File plainText, File encrypted) {
        try {
            SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);
            IvParameterSpec ivParameterSpec = CryptoUtils.generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            CryptoUtils.encryptFile(algorithm, key, ivParameterSpec, plainText, encrypted);
            System.out.println("The file has been successfully encrypted. Check the encrypted.txt");
            CryptoUtils.printSecretKeyAndIV(key, ivParameterSpec);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                 InvalidAlgorithmParameterException | BadPaddingException | InvalidKeyException exception) {
            System.out.println("Encryption error: " + exception.getMessage());
        }

    }

    public void ecbMenu(int keySize, File plainText, File encrypted) {
        try {
            SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);

            String algorithm = "AES/ECB/PKCS5Padding";
            CryptoUtils.encryptFile(algorithm, key, plainText, encrypted);
            System.out.println("The file has been successfully encrypted. Check the encrypted.txt");
            CryptoUtils.printSecretKey(key);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                 InvalidKeyException | BadPaddingException exception) {
            System.out.println("Encryption error: " + exception.getMessage());
        }
    }

    public void decryptMenu() {
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.println("-- Decryption menu --");
            System.out.println("Input a path to file you would like to decrypt: ");
            String path = scanner.nextLine();

            if (doesFileExist(path)) {
                File encryptedFile = new File(path);
                File decryptedFile = new File("decrypted.txt");
                System.out.println("-- Choose AES mode --\n1. ECB.\n2. CBC(recommended)\n3. Return to the main menu");
                String option = scanner.nextLine().trim();
                boolean modes = true;
                while (modes) {
                    switch (option) {
                        case "1":
                            ecbDecrypt(encryptedFile, decryptedFile);
                            modes = false;
                            break;
                        case "2":
                            cbcDecrypt(encryptedFile, decryptedFile);
                            modes = false;
                            break;
                        case "3":
                            modes = false;
                            break;
                        default:
                            System.out.println("Invalid option. Please select a valid AES mode(1 or 2)");
                            break;
                    }
                }

                stayInMenu = false;
            } else {
                stayInMenu = invalidPathMenu();
            }

        }
    }

    public void cbcDecrypt(File encryptedFile, File decryptedFile) {
        String algorithm = "AES/CBC/PKCS5Padding";
        System.out.println("Input the key and Initialization Vector");
        try {
            System.out.println("Key:");
            String base64Key = scanner.nextLine().trim();
            SecretKey secretKey = CryptoUtils.decodeBase64ToSecretKey(base64Key, "AES");
            System.out.println("IV:");
            String base64iv = scanner.nextLine().trim();
            IvParameterSpec iv = CryptoUtils.decodeBase64ToIv(base64iv);
            CryptoUtils.decryptFile(algorithm, secretKey, iv, encryptedFile, decryptedFile);
            System.out.println("The file has been successfully decrypted. Check the decrypted.txt");
        } catch (IllegalArgumentException | InvalidAlgorithmParameterException | InvalidKeyException exception) {
            System.out.println("Invalid key/IV");
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                 BadPaddingException exception) {
            System.out.println("Decryption error: " + exception.getMessage());
        }
    }

    public void ecbDecrypt(File encryptedFile, File decryptedFile) {
        String algorithm = "AES/ECB/PKCS5Padding";
        System.out.println("Input the key");
        try {
            System.out.println("Key:");
            String base64Key = scanner.nextLine().trim();
            SecretKey secretKey = CryptoUtils.decodeBase64ToSecretKey(base64Key, "AES");

            CryptoUtils.decryptFile(algorithm, secretKey, encryptedFile, decryptedFile);
            System.out.println("The file has been successfully decrypted. Check the decrypted.txt");
        } catch (IllegalArgumentException | InvalidKeyException exception) {
            System.out.println("Invalid key/IV");
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                 BadPaddingException exception) {
            System.out.println("Decryption error: " + exception.getMessage());
        }
    }

    public boolean invalidPathMenu() {
        System.out.println("Invalid path. Please try again.\n1. Try again.\n2. To main menu.");
        String option = scanner.nextLine().trim();
        switch (option) {
            case "1":
                return true;

            case "2":
                return false;

            default:
                System.out.println("Invalid option");
                return true;

        }
    }

    public boolean doesFileExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

}
