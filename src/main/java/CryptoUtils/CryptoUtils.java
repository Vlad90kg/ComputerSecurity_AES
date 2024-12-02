package CryptoUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {

    public static SecretKey getKeyFromKeyGenerator(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    public static void printSecretKeyAndIV(SecretKey secretKey, IvParameterSpec iv) {
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        String base64Iv = Base64.getEncoder().encodeToString(iv.getIV());
        System.out.println("Secret Key (Base64): " + base64Key);
        System.out.println("Initializing Vector (Base64): " + base64Iv);
    }

    public static void printSecretKey(SecretKey secretKey) {
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Secret Key (Base64): " + base64Key);
    }


    public static SecretKey decodeBase64ToSecretKey(String base64Key, String algorithm) {
        // Decode the Base64 string into a byte array
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);

        // Create and return the SecretKey using the decoded byte array
        return new SecretKeySpec(decodedKey, algorithm);
    }

    public static IvParameterSpec decodeBase64ToIv(String encodedIv) {
        byte[] iv = Base64.getDecoder().decode(encodedIv);

        return new IvParameterSpec(iv);
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                byteStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            byteStream.write(outputBytes);
        }
        inputStream.close();
        byte[] encryptedData = byteStream.toByteArray();
        String base64EncodedData = Base64.getEncoder().encodeToString(encryptedData);

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(base64EncodedData);
        }
        byteStream.close();
    }

    public static void encryptFile(String algorithm, SecretKey key,
                                   File inputFile, File outputFile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        FileInputStream inputStream = new FileInputStream(inputFile);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                byteStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            byteStream.write(outputBytes);
        }
        inputStream.close();
        byte[] encryptedData = byteStream.toByteArray();
        String base64EncodedData = Base64.getEncoder().encodeToString(encryptedData);

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(base64EncodedData);
        }
        byteStream.close();
    }

    public static void decryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
             InputStream inputStream = Base64.getDecoder().wrap(new FileInputStream(inputFile))) {
            byte[] buffer = new byte[512];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fileOutputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fileOutputStream.write(outputBytes);
            }
        }
    }

    public static void decryptFile(String algorithm, SecretKey key,
                                   File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
             InputStream inputStream = Base64.getDecoder().wrap(new FileInputStream(inputFile))) {
            byte[] buffer = new byte[512];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fileOutputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fileOutputStream.write(outputBytes);
            }
        }
    }
}

