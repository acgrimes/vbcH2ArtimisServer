package com.ll.vbc.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class SerialUtil {

    public final static byte[] stringToBytesUTFNIO(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
        for(int i = 0; i < buffer.length; i++)
            cBuffer.put(buffer[i]);
        return b;
    }

    public final static String bytesToStringUTFNIO(byte[] bytes) {
        CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
        return cBuffer.toString();
    }

    public final static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public final static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public final static int objectByteArrayToInt(Byte[] bytes) {
        return ByteBuffer.wrap(ArrayUtils.toPrimitive(bytes)).getInt();
    }

    public final static byte[] concatenateBytes(byte[]... bytes) {

        List<List<Byte>> byteArray = new ArrayList<>();
        for(int i=0;i<bytes.length;i++) {
            byteArray.add(Arrays.asList(ArrayUtils.toObject(bytes[i])));
        }
        List<Byte> byteList = byteArray.stream().flatMap((ba -> ba.stream())).collect(toList());
        Byte[] aByteArray = byteList.toArray(new Byte[byteList.size()]);
        return ArrayUtils.toPrimitive(aByteArray);
    }

    public final static byte[] concatenateBytes(byte[] lengthBytes, byte[] valueBytes) {
        byte[] bytes = new byte[lengthBytes.length + valueBytes.length];
        System.arraycopy(lengthBytes, 0, bytes, 0, lengthBytes.length);
        System.arraycopy(valueBytes, 0, bytes, lengthBytes.length, valueBytes.length);
        return bytes;
    }

    public final static byte[] serialize(String value) {

        byte[] lengthBytes = intToByteArray(value.length());
        byte[] valueBytes = value.getBytes();
        return concatenateBytes(lengthBytes, valueBytes);

    }

    public final static byte[] serialize(Map<String, String> keyValue) {
        List<byte[]> bytes = new ArrayList<>();
        keyValue.forEach((k,v) -> {
            byte[] length = stringToBytesUTFNIO(k);
            bytes.add(length);
            byte[] value = stringToBytesUTFNIO(v);
            bytes.add(value);
        });
        return ArrayUtils.toPrimitive(bytes.toArray(new Byte[bytes.size()]));
    }

    public final static Map<String, String> deserialize(byte[] bytes) {

        Map<String, String> theMap = new HashMap<>();
        List<Byte> byteList = Arrays.asList(ArrayUtils.toObject(bytes));
        int lastInd = byteList.size();
        for(int i=0;i<=lastInd;) {
            Byte[] lengthBytes = byteList.subList(i, i+3).toArray(new Byte[4]);
            i=i+3;
            int j = objectByteArrayToInt(lengthBytes);
            Byte[] keyArray = byteList.subList(i, j).toArray(new Byte[j]);
            i=j;
            String key = String.valueOf(keyArray);
            lengthBytes = byteList.subList(i, i+3).toArray(new Byte[4]);
            i=i+3;
            j = objectByteArrayToInt(lengthBytes);
            Byte[] valueArray = byteList.subList(i, j).toArray(new Byte[j]);
            i=j;
            String value = String.valueOf(valueArray);
            theMap.put(key, value);
        }
        return theMap;
    }

    public final static byte[] objectToByteArray(Object anObject) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(anObject);
        oos.flush();
        return bos.toByteArray();

    }
}
