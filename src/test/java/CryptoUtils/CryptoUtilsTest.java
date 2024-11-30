package CryptoUtils;

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
    void getKeyFromKeyGenerator() {
    }

    @org.junit.jupiter.api.Test
    void generateIv() {
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
    void decryptFile() {
    }

}