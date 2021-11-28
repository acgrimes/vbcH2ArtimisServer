package com.ll.vbc.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ConsensusState {

    private static AtomicLong currentTerm;
    private static AtomicLong lastCurrentTerm;
    private static AtomicLong currentIndex;
    private static AtomicLong lastCurrentIndex;
    private static Server votedFor;
    private static ConsensusLog currentConsensusLog;
    private static AtomicLong commitIndex;
    private static List<Server> serverList;
    private static Server vbcProxyServer;
    private static Map<Long, List<Server>> indexServerMap;
    private static Map<Long, List<AppendEntry>> logEntryMap;
    private static Map<Long, List<AppendEntry>> commitEntryMap;

    static {

        logEntryMap = new HashMap<>();
        commitEntryMap = new HashMap<>();
        indexServerMap = new HashMap<>();
        initializeIndexAndTerm();

    }

    public final static void initializeServerList() {

        try {
            if(serverList==null) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                Servers servers = mapper.readValue(new File("src/main/resources/vbcServers.yml"), Servers.class);
                ConsensusState.setServerList(servers.getServers());
            }
        } catch(IOException  ex) {
            ex.printStackTrace();
        }
    }

    public final static void initializeProxyServer() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            vbcProxyServer = mapper.readValue(new File("src/main/resources/vbcProxyServer.yml"), Server.class);

        } catch(IOException  ex) {
            ex.printStackTrace();
        }
    }

    public final static void initializeIndexAndTerm() {
        //TODO these values need to be retrieved from the consensusLog collection in mongodb
        currentIndex = new AtomicLong(0L);
        commitIndex = new AtomicLong(0L);
        currentTerm = new AtomicLong(1L);
    }

    public static AtomicLong getCurrentTerm() {
        return currentTerm;
    }

    public static void setCurrentTerm(AtomicLong currentTerm) {
        ConsensusState.currentTerm = currentTerm;
    }

    public static AtomicLong getLastCurrentTerm() {
        return lastCurrentTerm;
    }

    public static void setLastCurrentTerm(AtomicLong lastCurrentTerm) {
        ConsensusState.lastCurrentTerm = lastCurrentTerm;
    }

    public static AtomicLong getCurrentIndex() {
        return currentIndex;
    }

    public static void setCurrentIndex(AtomicLong currentIndex) {
        ConsensusState.currentIndex = currentIndex;
    }

    public static AtomicLong getLastCurrentIndex() {
        return lastCurrentIndex;
    }

    public static void setLastCurrentIndex(AtomicLong lastCurrentIndex) {
        ConsensusState.lastCurrentIndex = lastCurrentIndex;
    }

    public static Server getVotedFor() {
        return votedFor;
    }

    public static void setVotedFor(Server votedFor) {
        ConsensusState.votedFor = votedFor;
    }

    public static ConsensusLog getCurrentConsensusLog() {
        return currentConsensusLog;
    }

    public static void setCurrentConsensusLog(ConsensusLog currentConsensusLog) {
        ConsensusState.currentConsensusLog = currentConsensusLog;
    }

    public static AtomicLong getCommitIndex() {
        return commitIndex;
    }

    public static void setCommitIndex(AtomicLong commitIndex) {
        ConsensusState.commitIndex = commitIndex;
    }

    public static List<Server> getServerList() {
        return serverList;
    }

    public static void setServerList(List<Server> serverList) {
        ConsensusState.serverList = serverList;
    }

    public static Server getVbcProxyServer() {
        return vbcProxyServer;
    }

    public static void setVbcProxyServer(Server vbcProxyServer) {
        ConsensusState.vbcProxyServer = vbcProxyServer;
    }

    public static Map<Long, List<Server>> getIndexServerMap() {
        return indexServerMap;
    }

    public static void setIndexServerMap(Map<Long, List<Server>> indexServerMap) {
        ConsensusState.indexServerMap = indexServerMap;
    }

    public static Map<Long, List<AppendEntry>> getLogEntryMap() {
        return logEntryMap;
    }

    public static void setLogEntryMap(Map<Long, List<AppendEntry>> logEntryMap) {
        ConsensusState.logEntryMap = logEntryMap;
    }

    public static Map<Long, List<AppendEntry>> getCommitEntryMap() {
        return commitEntryMap;
    }

    public static void setCommitEntryMap(Map<Long, List<AppendEntry>> commitEntryMap) {
        ConsensusState.commitEntryMap = commitEntryMap;
    }
}
