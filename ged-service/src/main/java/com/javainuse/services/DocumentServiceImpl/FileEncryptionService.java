package com.javainuse.services.DocumentServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

@Service
@Slf4j
public class FileEncryptionService {
    private SecretKey aesKey;
    private KeyPair rsaKeyPair;

    public FileEncryptionService(SecretKey aesKey, KeyPair rsaKeyPair) {
        this.aesKey = aesKey;
        this.rsaKeyPair = rsaKeyPair;
    }

    public byte[] encrypt(byte[] plaintext) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Encrypt the resource with AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedResource = aesCipher.doFinal(plaintext);

        // Get the initialization vector
        byte[] iv = aesCipher.getIV();

        // Encrypt the AES key with RSA public key
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // Combine the encrypted AES key, IV, and resource into a single byte array
        byte[] combined = new byte[encryptedAesKey.length + iv.length + encryptedResource.length];
        System.arraycopy(encryptedAesKey, 0, combined, 0, encryptedAesKey.length);
        System.arraycopy(iv, 0, combined, encryptedAesKey.length, iv.length);
        System.arraycopy(encryptedResource, 0, combined, encryptedAesKey.length + iv.length, encryptedResource.length);

        return combined;
    }


    public byte[] decrypt(byte[] ciphertext) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        // Separate the encrypted AES key, IV, and resource from the combined byte array
        byte[] encryptedAesKey = new byte[256];
        byte[] iv = new byte[16];
        byte[] encryptedResource = new byte[ciphertext.length - 256 - 16];
        System.arraycopy(ciphertext, 0, encryptedAesKey, 0, 256);
        System.arraycopy(ciphertext, 256, iv, 0, 16);
        System.arraycopy(ciphertext, 256 + 16, encryptedResource, 0, ciphertext.length - 256 - 16);

        // Decrypt the AES key with RSA private key
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
        byte[] decryptedAesKey = rsaCipher.doFinal(encryptedAesKey);
        SecretKey aesKey = new SecretKeySpec(decryptedAesKey, "AES");

        // Decrypt the resource with AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
        return aesCipher.doFinal(encryptedResource);
    }
}
