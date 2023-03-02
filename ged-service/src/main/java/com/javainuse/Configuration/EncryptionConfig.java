package com.javainuse.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.codec.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class EncryptionConfig {

    @Bean
    public SecretKey aesKey() throws NoSuchAlgorithmException {
        // Generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    @Bean
    public IvParameterSpec iv() {
        // Generate IV (Initialization Vector)
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Bean
    public byte[] encryptedResource(SecretKey aesKey, IvParameterSpec iv) throws Exception {
        // Encrypt the resource with AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
        byte[] resourceBytes = "My super secret resource".getBytes();
        return aesCipher.doFinal(resourceBytes);
    }

    @Bean
    public String encryptedResourceHex(byte[] encryptedResource) {
        // Convert encrypted resource to hex string for easy storage and transmission
        return new String(Hex.encode(encryptedResource));
    }

    @Bean
    public SecretKey aesKeyFromString(String aesKeyString) {
        // Convert AES key string (hex format) back to SecretKey object
        byte[] keyBytes = Hex.decode(aesKeyString);
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Bean
    public byte[] decryptedResource(SecretKey aesKey, IvParameterSpec iv, byte[] encryptedResource) throws Exception {
        // Decrypt the resource with AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, iv);
        return aesCipher.doFinal(encryptedResource);
    }

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        // Generate key pair
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

}
