package com.ll.vbc;

import com.ll.vbc.business.services.client.consensus.scheduling.Scheduler;
import com.ll.vbc.business.services.server.election.leader.ProcessGeneralRequestQueue;
import com.ll.vbc.domain.BlockChainMetadata;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.messageService.artemis.JmsManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Scheduler scheduler;
    private final ProcessGeneralRequestQueue processGeneralRequestQueue;

    public Application() {
        scheduler = Scheduler.getInstance();
        processGeneralRequestQueue = new ProcessGeneralRequestQueue();
    }

    public static final void main(String[] args) throws Exception {

        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        System.setProperty("java.library.path", "/usr/lib/x86_64-linux-gnu");
        Security.addProvider(new BouncyCastleProvider());

        ConsensusServer.setId(args[0]);

        ConsensusState.initializeServerList();
        ConsensusState.initializeProxyServer();

        ConsensusState.getServerList().stream().
                forEach(serv -> {
                    if(serv.getId().equals(args[0])) {
                        ConsensusServer.setId(serv.getId());
                        ConsensusServer.setHost(serv.getHost());
                        ConsensusServer.setHttpPort(serv.getHttpPort());
                        ConsensusServer.setReactivePort(serv.getReactivePort());
                        ConsensusServer.setState(serv.getState());
                    }
                });
        log.debug("Server Id is: "+ConsensusServer.getId()+", Server Port is: "+ConsensusServer.getReactivePort());

        //TODO: these values need to be read from database.
        ConsensusState.setCurrentIndex(new AtomicLong(0L));
        ConsensusState.setCurrentTerm(new AtomicLong(1L));

        //TODO: these values need to be read from database:
        BlockChainMetadata.setActiveBlock(new AtomicLong(0L));
        BlockChainMetadata.setActiveBlockTxCount(new AtomicLong(0L));

        Application application = new Application();
        application.run( args);

    }

    public void run(String[] args) throws Exception {

        JmsManager.getInstance().configureJMS();

        scheduler.startFollowerHeartBeatTimeoutTimer();

        ExecutorService proxyMessageListenerTask = Executors.newSingleThreadExecutor();
        Future<?> messageFuture = proxyMessageListenerTask.submit(processGeneralRequestQueue);

    }
}
