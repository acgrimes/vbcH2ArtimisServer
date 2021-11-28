package com.ll.vbc.domain;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Serialization {

    private final Logger log = Logger.getLogger(Serialization.class.getSimpleName());

    public final byte[] serializeString(String str) {
        if(str==null) str="";
        int length = str.length();
        byte[] lengthBytes = serializeInt(length);
        byte[] valueBytes = str.getBytes();
        return concatenateBytes(lengthBytes, valueBytes);
    }
    public final String deserializeString(byte[] bytes) {
        if(bytes.length>=4) {
            int length = deserializeInt(Arrays.copyOfRange(bytes, 0, 4));
            return new String(Arrays.copyOfRange(bytes, 4, length + 4));
        }
        return "";
    }

    public final byte[] serializeInt(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    public final int deserializeInt(byte[] bytes) {
        if(bytes.length==4) {
            return ((bytes[0] & 0xFF) << 24) |
                    ((bytes[1] & 0xFF) << 16) |
                    ((bytes[2] & 0xFF) << 8) |
                    ((bytes[3] & 0xFF) << 0);
        }
        return 0;
    }

    public final byte[] serializeLong(long value) {
        byte[] b = new byte[] {
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24),
                (byte) (value >> 32),
                (byte) (value >> 40),
                (byte) (value >> 48),
                (byte) (value >> 56)};
        return b;
    }
    public final long deserializeLong(byte[] b) {
        long l = 0L;
        if(b.length==8) {
        l = ((long) b[7] << 56)
                | ((long) b[6] & 0xff) << 48
                | ((long) b[5] & 0xff) << 40
                | ((long) b[4] & 0xff) << 32
                | ((long) b[3] & 0xff) << 24
                | ((long) b[2] & 0xff) << 16
                | ((long) b[1] & 0xff) << 8
                | ((long) b[0] & 0xff);
        }
        return l;
    }

    /**
     *
     * @param keyValue
     * @return
     */
    public final byte[] serializeMap(Map<String, String> keyValue) {

        byte[] mapArray = new byte[0];
        Set<Map.Entry<String, String>> mapSet = keyValue.entrySet();
        for(Map.Entry<String, String> entry : mapSet) {
            byte[] key = serializeString(entry.getKey());
            mapArray = ArrayUtils.addAll(mapArray, key);
            byte[] value = serializeString(entry.getValue());
            mapArray = ArrayUtils.addAll(mapArray, value);
        }
        return mapArray;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public final Map<String, String> deserializeMap(byte[] bytes) {

        Map<String, String> theMap = new HashMap<>();
        for(int i=0;i<bytes.length;) {
            int keyLength = deserializeInt(Arrays.copyOfRange(bytes, i, i+4));
            String key = deserializeString(Arrays.copyOfRange(bytes, i, i=i+keyLength+4));
            log.info("key: "+key);
            int valueLength = deserializeInt(Arrays.copyOfRange(bytes, i, i+4));
            String value = deserializeString(Arrays.copyOfRange(bytes, i, i=i+valueLength+4));
            log.info("value: "+value);
            theMap.put(key, value);
        }
        return theMap;
    }

    public byte[] serializeFile(String directory, String file) {
        byte[] bytes = null;
        try {
            byte[] dirBytes = serializeString(directory);
            byte[] fileBytes = serializeString(file);
            File theFile = new File(directory, file);
            byte[] fxmlBytes =  Files.readAllBytes(theFile.toPath());
            byte[] fxmlBytesLength = serializeInt(fxmlBytes.length);
            bytes = concatenateBytes(dirBytes, fileBytes, fxmlBytesLength, fxmlBytes);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Fxml file byte array length: "+bytes.length);
        return bytes;
    }

    public File deserializeFile(byte[] bytes) {
        System.out.println("Fxml file byte array length: "+bytes.length);
        int ind = 0;
        int length = deserializeInt(Arrays.copyOfRange(bytes, 0, 4));
        String directory = deserializeString(Arrays.copyOfRange(bytes, ind, ind=ind+length+4));
        length = deserializeInt(Arrays.copyOfRange(bytes, ind, ind+4));
        String file = deserializeString(Arrays.copyOfRange(bytes, ind, ind=ind+length+4));
        length = deserializeInt(Arrays.copyOfRange(bytes, ind, ind=ind+4));
        File theFile = new File(directory+file);
        try {
            Path path = Paths.get(theFile.toURI());
            Files.write(path, Arrays.copyOfRange(bytes, ind, ind+length), StandardOpenOption.CREATE ,StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return theFile;
    }

    public final byte[] concatenateBytes(byte[]... bytes) {

        byte[] total = new byte[0];
        for(byte[] byteArr: bytes) {
            total = ArrayUtils.addAll(total, byteArr);
        }
        return total;
    }
}
