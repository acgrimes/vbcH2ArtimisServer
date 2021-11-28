package com.ll.vbc.enums;



import com.ll.vbc.domain.Serialization;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BallotItemType implements Serializable {

    OFFICE,
    QUESTION,
    AMENDMENT,
    OTHER,
    LAST;

    private static final Map<Integer, BallotItemType> lookup = new HashMap<Integer, BallotItemType>();

    static{
        int ordinal = 0;
        for (BallotItemType type : EnumSet.allOf(BallotItemType.class)) {
            lookup.put(ordinal, type);
            ordinal+= 1;
        }
    }

    public final static BallotItemType fromOrdinal(int ordinal) {
        return lookup.get(ordinal);
    }

    public byte[] serialize() {
        Serialization serial = new Serialization();
        byte[] typeBytes = serial.serializeInt(ordinal());
        return typeBytes;
    }

    public static BallotItemType deserialize(byte[] type) {
        Serialization serial = new Serialization();
        int enumValue = serial.deserializeInt(type);
        return BallotItemType.fromOrdinal(enumValue);
    }
}
