package com.ll.vbc.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum HttpFragment {

    Login("/Login"),
    BallotRequest("/BallotRequest"),
    ElectionTransaction("/ElectionTransaction"),
    Last("/Last");

    private final String fragment;

    HttpFragment(String fragment) {
        this.fragment = fragment;
    }

    private String getName() {
        return fragment;
    }

    private static final Map<String, HttpFragment> lookup = new HashMap<String, HttpFragment>();

    static{
        int ordinal = 0;
        for (HttpFragment type : EnumSet.allOf(HttpFragment.class)) {
            lookup.put(type.getName(), type);
            ordinal+= 1;
        }
    }

    public final static HttpFragment fromName(String name) {
        return lookup.get(name);
    }
}
