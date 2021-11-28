package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;

public class LoginCredentials extends Serialization implements Serializable {

    private static final long serialVersionUID = 5673732176670753862L;
    private String username;
    private String password;

    public LoginCredentials() {
        username = "";
        password = "";
    }
    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] serialize() {

        return concatenateBytes(serializeString(username), serializeString(password));
    }

    public int deserialize(byte[] bytes, int index) {

        if(index<bytes.length) {
            int usernameLength = deserializeInt(Arrays.copyOfRange(bytes, index, index + 4));
            username = deserializeString(Arrays.copyOfRange(bytes, index, index = index + usernameLength+4));
        }
        if(index<bytes.length) {
            int passwordLength = deserializeInt(Arrays.copyOfRange(bytes, index, index + 4));
            password = deserializeString(Arrays.copyOfRange(bytes, index, index=index + passwordLength+4));
        }
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LoginCredentials that = (LoginCredentials) o;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(password, that.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(password)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
