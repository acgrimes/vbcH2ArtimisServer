package com.ll.vbc.domain;

import java.util.concurrent.atomic.AtomicLong;

public final class BlockChainMetadata implements DomainObject {

    public static final int ELECTION_TX_PER_BLOCK = 8;

    private static byte[] blockChainHash;
    private static AtomicLong activeBlock;
    private static AtomicLong activeBlockTxCount;

    public static byte[] getBlockChainHash() {
        return blockChainHash;
    }

    public static void setBlockChainHash(byte[] blockChainHash) {
        BlockChainMetadata.blockChainHash = blockChainHash;
    }

    public static AtomicLong getActiveBlock() {
        return activeBlock;
    }

    public static void setActiveBlock(AtomicLong activeBlock) {
        BlockChainMetadata.activeBlock = activeBlock;
    }

    public static AtomicLong getActiveBlockTxCount() {
        return activeBlockTxCount;
    }

    public static void setActiveBlockTxCount(AtomicLong activeBlockTxCount) {
        BlockChainMetadata.activeBlockTxCount = activeBlockTxCount;
    }
}
