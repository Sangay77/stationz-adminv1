package com.station.shoppingcart.payment.helper;

import java.util.LinkedHashMap;
import java.util.Map;

public class EMVTag55Parser {

    // Convert hex string to byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // Convert byte array to hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // TLV parser
    public static Map<String, String> parseTLV(byte[] data) {
        Map<String, String> result = new LinkedHashMap<>();
        int index = 0;

        while (index < data.length) {
            // --- Parse Tag ---
            int tagStart = index;
            int tag = data[index++] & 0xFF;

            if ((tag & 0x1F) == 0x1F) { // Multi-byte tag
                tag = (tag << 8) | (data[index++] & 0xFF);
                if ((tag & 0x80) == 0x80) {
                    tag = (tag << 8) | (data[index++] & 0xFF);
                }
            }

            String tagHex = Integer.toHexString(tag).toUpperCase();
            if (tagHex.length() % 2 != 0) {
                tagHex = "0" + tagHex;
            }

            // --- Parse Length ---
            int lenByte = data[index++] & 0xFF;
            int length;
            if ((lenByte & 0x80) == 0x80) {
                int numBytes = lenByte & 0x7F;
                length = 0;
                for (int i = 0; i < numBytes; i++) {
                    length = (length << 8) | (data[index++] & 0xFF);
                }
            } else {
                length = lenByte;
            }

            // --- Check bounds ---
            if (index + length > data.length) {
                System.err.printf("Invalid length for tag %s: %d bytes requested, but only %d bytes remain.%n",
                        tagHex, length, data.length - index);
                break; // or throw exception, or return partial map
            }

            // --- Parse Value ---
            byte[] valueBytes = new byte[length];
            System.arraycopy(data, index, valueBytes, 0, length);
            index += length;

            String valueHex = bytesToHex(valueBytes);
            result.put(tagHex, valueHex);
        }

        return result;
    }

    // Test main method
    public static void main(String[] args) {
        // EMV Tag 55 hex (sample with spaces removed)
        String hex = "34 30 37 31 30 30 32 30 36 34 43 30 30 30 30 30".replace(" ", "");
        byte[] tlvBytes = hexStringToByteArray(hex);

        Map<String, String> parsed = parseTLV(tlvBytes);

        System.out.println("Parsed EMV Tag 55 TLV data:");
        parsed.forEach((tag, value) -> {
            System.out.printf("Tag: %s => Value: %s%n", tag, value);
        });
    }

}
