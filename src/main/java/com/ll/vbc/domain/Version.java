package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Version implements DomainObject, SQLData {

    private Integer major;
    private Integer minor;
    private String sqlType;

    public Version() {}
    public Version(Integer major, Integer minor) {
        this.major = major;
        this.minor = minor;
    }

    public Integer getMajor() {
        return major;
    }
    public Integer getMinor() {
        return minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        return new EqualsBuilder()
                .append(major, version.major)
                .append(minor, version.minor)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(major)
                .append(minor)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Version{" +
                "major=" + major +
                ", minor=" + minor +
                '}';
    }

    public static String getSQLType() {
        return "version";
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return "version";
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sqlType = typeName;
        major = stream.readInt();
        minor = stream.readInt();
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(major);
        stream.writeInt(minor);
    }
}
