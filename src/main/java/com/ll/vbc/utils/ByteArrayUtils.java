package com.ll.vbc.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayUtils {

    /**
     * Convert a byte array of 8 bit characters into a String.
     *
     * @param bytes the array containing the characters
     * @param length the number of bytes to process
     * @return a String representation of bytes
     */
    public static String toString(
            byte[] bytes,
            int    length)
    {
        char[]	chars = new char[length];

        for (int i = 0; i != chars.length; i++)
        {
            chars[i] = (char)(bytes[i] & 0xff);
        }

        return new String(chars);
    }

    /**
     * Convert a byte array of 8 bit characters into a String.
     *
     * @param bytes the array containing the characters
     * @return a String representation of bytes
     */
    public static String toString(
            byte[]	bytes)
    {
        return toString(bytes, bytes.length);
    }

    /**
     * Convert the passed in String to a byte array by
     * taking the bottom 8 bits of each character it contains.
     *
     * @param string the string to be converted
     * @return a byte array representation
     */
    public static byte[] toByteArray(
            String string)
    {
        byte[]	bytes = new byte[string.length()];
        char[]  chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }

        return bytes;
    }

    private static String digits = "0123456789abcdef";

    /**
     * Return length many bytes of the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @param length the number of bytes in the data block to be converted.
     * @return a hex representation of length bytes of data.
     */
    public static String toHex(byte[] data, int length)
    {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i != length; i++)
        {
            int	v = data[i] & 0xff;

            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }

        return buf.toString();
    }

    /**
     * Return the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @return a hex representation of data.
     */
    public static String toHex(byte[] data)
    {
        return toHex(data, data.length);
    }

    /**
     * Concatenates two byte[]s.
     * @param a
     * @param b
     * @return
     */
    public static byte[] concatenatingTwoByteArrays(byte[] a, byte[] b) {

        byte[] c = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(a);
            outputStream.write(b);

            c = outputStream.toByteArray();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return c;
    }

    /**
     * compares two byte arrays, looking for equality
     * @param a - byte array
     * @param b - byte array
     * @return if a[]==b[] return true, otherwise return false
     */
    public static boolean compareByteArray(byte[] a, byte[] b) {
        if(a.length==b.length) {
            for(int i=0;i<a.length;i++) {
                if(a[i]!=b[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

