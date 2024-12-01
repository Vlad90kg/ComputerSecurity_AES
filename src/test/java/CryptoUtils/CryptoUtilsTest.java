package CryptoUtils;

import org.junit.jupiter.api.Test;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @org.junit.jupiter.api.Test
    void getKeyFromKeyGenerator128() throws NoSuchAlgorithmException {
        int keySize = 128;

        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);

        assertNotNull(key);
        assertEquals(keySize / 8, key.getEncoded().length);
    }
    @org.junit.jupiter.api.Test
    void getKeyFromKeyGenerator256() throws NoSuchAlgorithmException {
        int keySize = 256;

        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(keySize);

        assertNotNull(key);
        assertEquals(keySize / 8, key.getEncoded().length);
    }

    @org.junit.jupiter.api.Test
    void getIncorrectKeyFromKeyGenerator()  {
        int invalidKeySize = 25;

        assertThrows(IllegalArgumentException.class, () -> {
            CryptoUtils.getKeyFromKeyGenerator(invalidKeySize);
        });
    }


    @org.junit.jupiter.api.Test
    void testInvalidAlgorithm() throws NoSuchAlgorithmException {
        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(128);
        IvParameterSpec iv = CryptoUtils.generateIv();
        File inputFile = new File("plaintext.txt");
        File outputFile = new File("output.txt");

        assertThrows(NoSuchAlgorithmException.class, () -> {
            CryptoUtils.encryptFile("INVALID/ALGO", key, iv, inputFile, outputFile);
        });
    }

    @org.junit.jupiter.api.Test
    void testDecryptEmptyFile() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(128);
        IvParameterSpec iv = CryptoUtils.generateIv();
        File emptyFile = new File("empty.txt");
        File encrypted = new File("encryptedEmpty.txt");
        File decrypted = new File("decryptedEmpty.txt");

        CryptoUtils.encryptFile("AES/CBC/PKCS5Padding", key, iv, emptyFile, encrypted);
        CryptoUtils.decryptFile("AES/CBC/PKCS5Padding", key, iv, encrypted, decrypted);

        assertThat(emptyFile).hasSameTextualContentAs(decrypted);
    }

    @org.junit.jupiter.api.Test
    void testIVGeneration() {
        // Act
        IvParameterSpec ivParameterSpec = CryptoUtils.generateIv();

        // Assert
        assertNotNull(ivParameterSpec);
        assertEquals(16, ivParameterSpec.getIV().length);
    }

    @org.junit.jupiter.api.Test
    void encryptDecryptFile() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(128);
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = CryptoUtils.generateIv();
        File plainText = new File("plaintext.txt");
        File encrypted = new File("encrypted.txt");
        File decrypted = new File("decrypted.txt");
        CryptoUtils.encryptFile(algorithm,key,ivParameterSpec,plainText,encrypted);
        CryptoUtils.decryptFile(algorithm,key,ivParameterSpec,encrypted,decrypted);
        assertThat(plainText).hasSameTextualContentAs(decrypted);
    }

    @org.junit.jupiter.api.Test
    void testIOExceptionDuringEncryption() throws NoSuchAlgorithmException {

        SecretKey key = CryptoUtils.getKeyFromKeyGenerator(128);
        IvParameterSpec iv = CryptoUtils.generateIv();
        File inputFile = new File("plaintext.txt");
        File outputFile = new File("directory/encrypted.txt");


        assertThrows(IOException.class, () -> {
            CryptoUtils.encryptFile("AES/CBC/PKCS5Padding", key, iv, inputFile, outputFile);
        });
    }


    @Test
    void printSecretKeyAndIV() throws NoSuchAlgorithmException {
        SecretKey secretKey = CryptoUtils.getKeyFromKeyGenerator(128);

    }
}