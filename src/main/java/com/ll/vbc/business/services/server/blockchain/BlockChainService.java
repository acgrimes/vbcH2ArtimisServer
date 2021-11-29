package com.ll.vbc.business.services.server.blockchain;

import com.ll.vbc.dao.blockChain.MerkleTreeDao;
import com.ll.vbc.dao.blockChain.VoterTokenBlockMapDao;
import com.ll.vbc.dao.blockChain.VotingTxBlockDao;
import com.ll.vbc.dao.consensus.ConsensusLogDao;
import com.ll.vbc.domain.*;
import com.ll.vbc.merkle.HashType;
import com.ll.vbc.merkle.Merkle;
import com.ll.vbc.utils.BinaryHexConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.ll.vbc.utils.ByteArrayUtils.concatenatingTwoByteArrays;


public class BlockChainService {

    private static final Logger log = LoggerFactory.getLogger(BlockChainService.class);

    private final ConsensusLogDao consensusLogDao;
    private final MerkleTreeDao merkleTreeDao;
    private final VotingTxBlockDao votingTxBlockDao;
    private final VoterTokenBlockMapDao voterTokenBlockMapDao;


    public BlockChainService() {
        consensusLogDao = new ConsensusLogDao();
        merkleTreeDao = new MerkleTreeDao();
        votingTxBlockDao = new VotingTxBlockDao();
        voterTokenBlockMapDao = new VoterTokenBlockMapDao();
    }

    /**
     * The Follower response when a Leader requests a commit of an ElectionTransaction.
     * @param index - log index used to retrieve the associated consensusLog
     * @return - AppendEntry including commit=true and blockChain hash
     */
    public Optional<AppendEntry> followerCommitEntryResponse(final Long index) {

        if(log.isDebugEnabled())
            log.debug("Entering followerCommitEntryResponse: "+index.toString());

        return voterBlockChainProcess(index);
    }

    /**
     *
     * @param index
     * @return
     */
    public Optional<AppendEntry> voterBlockChainProcess(final Long index) {
        if(log.isDebugEnabled())
            log.debug("Entering voterBlockChainProcess: "+index.toString());

        ConsensusLog consensusLog = null;
        try {
            Optional<ConsensusLog> opConsensusLog = consensusLogDao.find(index);
            if (opConsensusLog.isEmpty()) {
                log.warn("voterBlockChainProcess - INVALID consensusLog: " + index.toString());
                return Optional.empty();
            } else {
                consensusLog = opConsensusLog.get();
                if (!consensusLog.validConsensusLog()) {
                    log.warn("INVALID consensusLog: " + consensusLog.toString());
                }
                extendBallotBlockchain(consensusLog);
                MerkleTree merkleTree = generateMerkleTree(consensusLog).get();
                VotingTxBlock vtb = updateTxBlockHeader(merkleTree);
                createVoterTokenBlockMap(vtb, consensusLog);
                AppendEntry leaderAppendEntry = new AppendEntry(ConsensusServer.getServerInstance(),
                        consensusLog.getvToken(),
                        consensusLog.getLogIndex(),
                        consensusLog.getLogTerm(),
                        consensusLog.getElectionTransaction(),
                        BlockChainMetadata.getBlockChainHash());
                return Optional.ofNullable(leaderAppendEntry);
            }
        } catch(SQLException ex) {

        }
        return Optional.empty();
    }

    /**
     *
     * @param consensusLog
     * @return
     */
    protected void extendBallotBlockchain(final ConsensusLog consensusLog) {

        if(log.isDebugEnabled())
            log.debug("Entering extendBallotBlockchain: "+consensusLog.toString());

        if (BlockChainMetadata.ELECTION_TX_PER_BLOCK >= BlockChainMetadata.getActiveBlockTxCount().incrementAndGet()) {
            updateTxBlock(consensusLog);
        } else {
            createNewTxBlock(consensusLog);
        }
    }

    /**
     * Create a VoterTokenBlockMap and saves to database
     * @param votingTxBlock
     * @return Mono<VoterTokenBlockMap>
     */
    protected VoterTokenBlockMap createVoterTokenBlockMap(final VotingTxBlock votingTxBlock, final ConsensusLog consensusLog) {

        if(log.isDebugEnabled())
            log.debug("Entering createVoterTokenBlockMap: "+votingTxBlock.toString()+", "+consensusLog.toString());

        VoterTokenBlockMap map = new VoterTokenBlockMap(consensusLog.getvToken(), votingTxBlock.getBlockId());
        voterTokenBlockMapDao.save(map);
//        log.info("createVoterTokenBlockMap: "+consensusLog.toString()+", "+map.toString());
        return map;
    }

    /**
     *
     * @param consensusLog
     * @return
     */
    protected void updateTxBlock(final ConsensusLog consensusLog) {

        votingTxBlockDao.find(BlockChainMetadata.getActiveBlock().get()).
            ifPresentOrElse(votingTxBlock -> {
                votingTxBlock.getTransactionMap().put(consensusLog.getvToken(),
                                                      consensusLog.getElectionTransaction());
                votingTxBlock.getVotingBlockHeader().setTxCount(BlockChainMetadata.getActiveBlockTxCount().get());
                if(log.isDebugEnabled()) log.debug("updateTxBlock: "+votingTxBlock.toString());
                votingTxBlockDao.update(votingTxBlock);
            }, () -> createNewTxBlock(consensusLog));
    }

    /**
     *
     * @param consensusLog
     * @return
     */
    protected Optional<MerkleTree> generateMerkleTree(final ConsensusLog consensusLog) {

        if(log.isDebugEnabled()) log.debug("Entering generateMerkleTree: "+consensusLog.toString());

        Optional<MerkleTree> result;
        Optional<MerkleTree> merkleTree = merkleTreeDao.find(BlockChainMetadata.getActiveBlock().get());
        if(merkleTree.isPresent()) {
            result = updateMerkleTree(merkleTree.get(), consensusLog);
            merkleTreeDao.update(result.get());
        } else {
            result = (generateNewMerkleTree(consensusLog));
            if(result.isPresent()) {
                merkleTreeDao.insert(result.get());
            } else {
                log.warn("MerkleTree object is Null");
            }
        }
        return result;
    }

    /**
     *
     * @param consensusLog
     * @return
     */
    protected Optional<MerkleTree> generateNewMerkleTree(final ConsensusLog consensusLog) {

        if(log.isDebugEnabled()) log.debug("Entering generateNewMerkleTree: "+consensusLog.toString());

        MerkleTree merkleTree = new MerkleTree(null, null, null, new HashMap<>());
        merkleTree = updateMerkleTree(merkleTree, consensusLog).get();
        return Optional.of(merkleTree);
    }

    /**
     *
     * @param merkleTree
     * @param consensusLog
     * @return
     */
    protected Optional<MerkleTree> updateMerkleTree(final MerkleTree merkleTree, final ConsensusLog consensusLog) {

        if(log.isDebugEnabled())
            log.debug("Entering updateMerkleTree: "+merkleTree.toString()+", electionTransaction: "+ BinaryHexConverter.bytesToHex(consensusLog.getElectionTransaction()));

        merkleTree.getNodeMap().put(consensusLog.getvToken(), consensusLog.getElectionTransaction());
        Collection<byte[]> nodes = merkleTree.getNodeMap().values();
        Merkle merkleEngine = new Merkle(HashType.DOUBLE_SHA256);
        byte[] tree = merkleEngine.makeTree(new ArrayList<>(nodes));
        merkleTree.setMerkleRoot(tree);
        merkleTree.setBlockId(BlockChainMetadata.getActiveBlock().get());
        merkleTree.setTxCount(BlockChainMetadata.getActiveBlockTxCount().get());
        return Optional.of(merkleTree);
    }

    /**
     *
     * @param merkleTree
     * @return
     */
    protected VotingTxBlock updateTxBlockHeader(final MerkleTree merkleTree) throws SQLException {

        if(log.isDebugEnabled()) log.debug("Entering updateTxBlockHeader: "+merkleTree.toString());

        return votingTxBlockDao.find(BlockChainMetadata.getActiveBlock().get()).
                flatMap(vtb -> {
                vtb.getVotingBlockHeader().setMerkleRoot(merkleTree.getMerkleRoot());
                blockChainHash(vtb);
                votingTxBlockDao.update(vtb);
                return Optional.of(vtb);
            }).get();
    }

    /**
     * This method generates the blockchain hash. This hash is created from the top level block parent hash and
     * merkle root. The two byte[]s are concatenated and that byte[] is hashed.
     * @param votingTxBlock
     */
    protected void blockChainHash(final VotingTxBlock votingTxBlock) {

        if(log.isDebugEnabled()) log.debug("Entering blockChainHash: "+votingTxBlock.toString());

        if(votingTxBlock.getVotingBlockHeader().getPreviousBlockHash()==null) {
            votingTxBlock.getVotingBlockHeader().setPreviousBlockHash(votingTxBlock.getVotingBlockHeader().getMerkleRoot());
        }
        byte[] blockchain = concatenatingTwoByteArrays(votingTxBlock.getVotingBlockHeader().getMerkleRoot(),
                                                       votingTxBlock.getVotingBlockHeader().getPreviousBlockHash());
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest hash = MessageDigest.getInstance("SHA512", "BC");
            byte[] blockchainHash = hash.digest(blockchain);
            BlockChainMetadata.setBlockChainHash(blockchainHash);
        } catch(NoSuchAlgorithmException | NoSuchProviderException ex) {
            ex.printStackTrace();
        }
    }

    protected void createNewTxBlock(final ConsensusLog consensusLog) {

        VotingTxBlock votingTxBlock = new VotingTxBlock();
        Version version = new Version(1, 2);
        VotingBlockHeader votingBlockHeader = new VotingBlockHeader();
        BlockChainMetadata.setActiveBlockTxCount(new AtomicLong(1L));
        votingTxBlock.getTransactionMap().put(consensusLog.getvToken(),
                    consensusLog.getElectionTransaction());
        votingBlockHeader.setTxCount(BlockChainMetadata.getActiveBlockTxCount().get());
        votingBlockHeader.setPreviousBlockHash(BlockChainMetadata.getBlockChainHash());
        votingBlockHeader.setVersion(version);
        votingBlockHeader.setDateTime(new Date());
        votingTxBlock.setVotingBlockHeader(votingBlockHeader);
        votingTxBlock.setBlockId(BlockChainMetadata.getActiveBlock().incrementAndGet());
        if(log.isDebugEnabled()) log.debug("createNewTxBlock method: "+votingTxBlock.toString());
        votingTxBlockDao.save(votingTxBlock);

    }
}
