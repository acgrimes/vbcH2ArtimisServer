package com.ll.vbc.domain;

import java.util.UUID;

/**
 * This domain class associates the client uuid with the current rolled up blockchainhash
 * the blockchainhash consists of the previous blockchain hash and the current merkle root
 * hash.
 */
public class UUIDBlockchainHash implements DomainObject {

    private UUID uuid;
    private Long logIndex;
    private Server server;
    private byte[] uuidBlockchainHash;

    public UUIDBlockchainHash() {}
    public UUIDBlockchainHash(UUID uuid, Long logIndex, byte[] uuidBlockchainHash, Server server) {
        this.uuid = uuid;
        this.logIndex = logIndex;
        this.uuidBlockchainHash = uuidBlockchainHash;
        this.server = server;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(Long logIndex) {
        this.logIndex = logIndex;
    }

    public byte[] getUuidBlockchainHash() {
        return uuidBlockchainHash;
    }

    public void setUuidBlockchainHash(byte[] uuidBlockchainHash) {
        this.uuidBlockchainHash = uuidBlockchainHash;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
