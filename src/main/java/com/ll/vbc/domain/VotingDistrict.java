package com.ll.vbc.domain;

import java.io.Serializable;
import java.util.Objects;

public class VotingDistrict implements Serializable {

    private static final long serialVersionUID = -8479474212770412896L;
    private int congress;
    private int stSenate;
    private int stHouse;
    private String judicial="";
    private int commi;
    private int munib;
    private int ward;

    public VotingDistrict() {}
    public VotingDistrict(int congress, int stSenate, int stHouse, String judicial, int commi, int munib, int ward) {
        this.congress = congress;
        this.stSenate = stSenate;
        this.stHouse = stHouse;
        if(judicial==null) {
            this.judicial = "";
        }
        this.commi = commi;
        this.munib = munib;
        this.ward = ward;
    }

    public int getCongress() {
        return congress;
    }

    public int getStSenate() {
        return stSenate;
    }

    public int getStHouse() {
        return stHouse;
    }

    public String getJudicial() {
        return judicial;
    }

    public int getCommi() {
        return commi;
    }

    public int getMunib() {
        return munib;
    }

    public int getWard() {
        return ward;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingDistrict that = (VotingDistrict) o;
        return congress==(that.congress) &&
                stSenate==(that.stSenate) &&
                stHouse==(that.stHouse) &&
                judicial.equals(that.judicial) &&
                commi==(that.commi) &&
                munib==(that.munib) &&
                ward==(that.ward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(congress, stSenate, stHouse, judicial, commi, munib, ward);
    }

    @Override
    public String toString() {
        return "VotingDistrict{" +
                "congress=" + congress +
                ", stSenate=" + stSenate +
                ", stHouse=" + stHouse +
                ", judicial='" + judicial + '\'' +
                ", commi=" + commi +
                ", munib=" + munib +
                ", ward=" + ward +
                '}';
    }
}
