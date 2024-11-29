package CryptoUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {

    public static SecretKey getKeyFromKeyGenerator(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
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
        byte[] buffer = new byte[64];
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

        try(FileWriter fileWriter = new FileWriter(outputFile)){
            fileWriter.write(base64EncodedData);
        }
        byteStream.close();
    }
}
