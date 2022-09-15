package com.graphql.emp.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public class CipherSecurity {

    Cipher eCipher;
    Cipher dCipher;

    byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03,};
    int iterationNo = 19;

    public CipherSecurity(String passPhrase) {
        try {
            // create key
            PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            eCipher = Cipher.getInstance(key.getAlgorithm());
            dCipher = Cipher.getInstance(key.getAlgorithm());
            // Prepare parameter to the cipher
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationNo);
            // create the cipher
            eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeySpecException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("error in cipher security {} ", e.getMessage());
        }
    }

    public String encrypt(String str) {
        byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] enc = eCipher.doFinal(utf8);
            return new Base64().encodeAsString(enc);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("error in encryption {} ", e.getMessage());
        }
        return "";
    }

    public String decrypt(String str) {

        try {
            // Decode base64 to get bytes
            byte[] dec = new Base64().decode(str);
            // Decrypt
            byte[] utf8 = dCipher.doFinal(dec);
            return new String(utf8, StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            log.error("error in decryption {} ", e.getMessage());
        }
        return "";
    }

}
