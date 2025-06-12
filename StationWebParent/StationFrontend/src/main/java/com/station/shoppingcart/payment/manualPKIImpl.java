package com.station.shoppingcart.payment;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

@Component
public class manualPKIImpl {


    private static final String ALGORITHM = "SHA1withRSA";
    private final KeyLoader keyLoader;

    public manualPKIImpl(KeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }

    // Sign the data using the private key and return signature in hexadecimal
    public String signData(String data) throws Exception {

        // 1. Load the private key (using your key loader)
        RSAPrivateKey privateKey = (RSAPrivateKey) keyLoader.readPKCS8PrivateKey();

        // 2. Create the SHA-1 hash of the data (this is the message digest)
        byte[] hashedData = sha1(data);

        // 3. Encrypt the hashed data (sign the hash) using the private key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  // Ensure padding is correct
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        byte[] signedData = cipher.doFinal(hashedData);

        // 4. Return the signature in hexadecimal format
        return bytesToHex(signedData);

    }

    public static byte[] sha1(String message) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(message.getBytes());
    }

    public static String bytesToHex(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

}
