package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class UserCredentials implements Serializable {

    private final String username;
    private final String password;
    private final String voterId;

    public UserCredentials(String username, String password, String voterId) {
        this.username = username;
        this.password = password;
        this.voterId = voterId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVoterId() {
        return voterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(password, that.password)
                .append(voterId, that.voterId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(password)
                .append(voterId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", voterId='" + voterId + '\'' +
                '}';
    }
}
