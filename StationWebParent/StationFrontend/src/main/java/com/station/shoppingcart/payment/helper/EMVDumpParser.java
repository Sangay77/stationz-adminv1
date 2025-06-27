package com.station.shoppingcart.payment.helper;

import java.util.LinkedHashMap;
import java.util.Map;

public class EMVDumpParser {

    // Hex string to byte array helper (remove spaces)
    public static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll("\\s+", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // Convert bytes to hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // Basic TLV parser
    public static Map<String, String> parseTLV(byte[] data, int startPos) {
        Map<String, String> map = new LinkedHashMap<>();
        int pos = startPos;

        while (pos < data.length) {
            // Parse tag
            int tag = data[pos++] & 0xFF;
            if ((tag & 0x1F) == 0x1F) { // multi-byte tag
                tag = (tag << 8) | (data[pos++] & 0xFF);
            }

            // Parse length
            int length = data[pos++] & 0xFF;
            if ((length & 0x80) != 0) { // long form length
                int numBytes = length & 0x7F;
                length = 0;
                for (int i = 0; i < numBytes; i++) {
                    length = (length << 8) | (data[pos++] & 0xFF);
                }
            }

            // Check bounds
            if (pos + length > data.length) {
                System.err.println("Invalid TLV length, ends beyond data size");
                break;
            }

            // Extract value
            byte[] value = new byte[length];
            System.arraycopy(data, pos, value, 0, length);
            pos += length;

            map.put(Integer.toHexString(tag).toUpperCase(), bytesToHex(value));
        }

        return map;
    }

    public static void main(String[] args) {
        String hexDump = "30 30 31 30 36 34 43 30 30 30 30 30 30 30 30 36 34 30 37 31 30 30";

        byte[] data = hexStringToByteArray(hexDump);

        // You can skip initial ascii data and try to find TLV start
        // For demo, just try from index 10 (skip first 10 bytes ASCII)u
        int startIndex = 10;

        Map<String, String> parsedTLV = parseTLV(data, startIndex);

        parsedTLV.forEach((tag, value) -> System.out.println("Tag: " + tag + ", Value: " + value));
    }
}
