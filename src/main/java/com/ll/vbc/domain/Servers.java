package com.ll.vbc.domain;

import java.util.List;

public class Servers {

    private List<Server> servers;

    public Servers() {}
    public Servers(List<Server> servers) {
        this.servers = servers;
    }
    public List<Server> getServers() {
        return servers;
    }
}

