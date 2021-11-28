package com.ll.vbc.domain;


import com.ll.vbc.enums.ServerConsensusState;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Server implements DomainObject {

    private String id;
    private String host;
    private int httpPort;
    private int reactivePort;
    private ServerConsensusState state;

    public Server() {}
    public Server(String id, String host, int httpPort, int reactivePort, ServerConsensusState state) {
        this.id = id;
        this.host = host;
        this.httpPort = httpPort;
        this.reactivePort = reactivePort;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getReactivePort() {
        return reactivePort;
    }

    public void setReactivePort(int reactivePort) {
        this.reactivePort = reactivePort;
    }

    public ServerConsensusState getState() {
        return state;
    }

    public void setState(ServerConsensusState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        return new EqualsBuilder()
                .append(id, server.id)
                .append(httpPort, server.httpPort)
                .append(reactivePort, server.reactivePort)
                .append(host, server.host)
                .append(state, server.state)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(host)
                .append(httpPort)
                .append(reactivePort)
                .append(state)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", httpPort=" + httpPort +
                ", reactivePort=" + reactivePort +
                ", state=" + state +
                '}';
    }
}
