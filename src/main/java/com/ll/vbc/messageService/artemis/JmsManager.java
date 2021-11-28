package com.ll.vbc.messageService.artemis;

import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.apache.activemq.artemis.core.config.impl.SecurityConfiguration;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.apache.activemq.artemis.spi.core.security.ActiveMQJAASSecurityManager;
import org.apache.activemq.artemis.spi.core.security.jaas.InVMLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsManager {

    private static final JmsManager INSTANCE = new JmsManager();
    public static JmsManager getInstance() {
        return INSTANCE;
    }

    private static final Logger log = LoggerFactory.getLogger(JmsManager.class);

    private static final String followerStateLeaderSelector = "(messageType='Heartbeat')OR(messageType='LeaderLogEntry')OR(messageType='LeaderLogCommit')";
    private static final String followerStateCandidateSelector = "messageType='RequestLeaderVote'";
    private static final String leaderStateFollowerSelector = "(messageType='HeartbeatFollowerResponse')" +
                                                            "OR(messageType='FollowerLogEntry')OR(messageType='FollowerLogCommit')" +
                                                            "OR(messageType='FollowerLogEntryFailure')OR(messageType='FollowerCommitEntryFailure')";
    private static final String leaderStateProxySelector = "messageType='ElectionTransaction'";
    private static final String candidateStateFollowerSelector = "(messageType='FollowerGrantsLeaderVote')OR(messageType='FollowerDeniesLeaderVote')";

    private InitialContext initialContext;
    private ActiveMQServer broker;
    private Connection connection;
    private MessageProducer candidatePublisher;
    private MessageProducer leaderPublisher;
    private MessageProducer followerPublisher;
    private MessageProducer proxyPublisher;
    private MessageConsumer candidateSubscriber;
    private MessageConsumer leaderSubscriber;
    private MessageConsumer followerSubscriber;
    private MessageConsumer proxySubscriber;
    private Session candidateSession;
    private Session leaderSession;
    private Session followerSession;
    private Session proxySession;
    private Topic leaderTopic;
    private Topic followerTopic;
    private Topic candidateTopic;
    private Topic proxyTopic;
    private final CompletionListener candidateCompletionListener;
    private final CompletionListener leaderCompletionListener;
    private final CompletionListener followerCompletionListener;
    private final CompletionListener proxyCompletionListener;
    private final CandidateMessageListener candidateMessageListener;
    private final LeaderMessageListener leaderMessageListener;
    private final FollowerMessageListener followerMessageListener;
    private final ProxyMessageListener proxyMessageListener;

    /**
     *
     */
    private JmsManager() {
        candidateMessageListener = new CandidateMessageListener();
        leaderMessageListener = new LeaderMessageListener();
        followerMessageListener = new FollowerMessageListener();
        proxyMessageListener = new ProxyMessageListener();
        candidateCompletionListener = new CandidatePublisherCompletionListener();
        leaderCompletionListener = new LeaderPublisherCompletionListener();
        followerCompletionListener = new FollowerPublisherCompletionListener();
        proxyCompletionListener = new ProxyPublisherCompletionListener();
    }

    /**
     *
     */
    public void configureJMS() {

        configureInitialContext();
        configureBroker();
        startBroker();
        configureTopics();
        configureConnection();
        configureCandidateSession();
        configureFollowerSession();
        configureLeaderSession();
        configureProxySession();
        configureCandidateSubscriber();
        configureLeaderSubscriber();
//        configureProxySubscriber();
//        configureFollowerSubscriber();
        configureCandidatePublisher();
        configureLeaderPublisher();
        configureFollowerPublisher();
        configureProxyPublisher();
    }

    private void configureBroker() {
        if(broker==null) {
            try {
                SecurityConfiguration securityConfig = new SecurityConfiguration();
                securityConfig.addUser("guest", "guest");
                securityConfig.addRole("guest", "guest");
                securityConfig.setDefaultUser("guest");
                ActiveMQJAASSecurityManager securityManager = new ActiveMQJAASSecurityManager(InVMLoginModule.class.getName(), securityConfig);
                broker = ActiveMQServers.newActiveMQServer("brokerTopicCluster.xml", null, securityManager);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void startBroker() {
        try {
            if(broker!=null) {
                broker.start();
                log.info("startBroker: ");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopBroker() {
        try {
            if(broker!=null) {
                broker.stop(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configureCandidateSession() {
        try {
            candidateSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void configureLeaderSession() {
        try {
            leaderSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void configureFollowerSession() {
        try {
            followerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void configureProxySession() {
        try {
            proxySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch(JMSException e) {
            e.printStackTrace();
        }
    }

    public void configureCandidateSubscriber() {

        try {
            switch(ConsensusServer.getState()) {
                case Follower: {
                    if(candidateSubscriber==null) {
                        candidateSubscriber = candidateSession.createDurableConsumer(candidateTopic, candidateTopic.getTopicName(), followerStateCandidateSelector, true);
                    }
                    break;
                }
                default: {
                    log.warn("configureCandidateSubscriber - State is NOT Follower Candidate Subscriber no configured: "+ConsensusServer.getState());
                }
            }
            if(candidateSubscriber!=null) {
                candidateSubscriber.setMessageListener(candidateMessageListener);
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void closeCandidateSubscriber() {

        try {
            if(candidateSubscriber!=null) {
                candidateSubscriber.close();
                candidateSubscriber = null;
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void configureLeaderSubscriber() {

        try {
            switch(ConsensusServer.getState()) {
                case Follower: {
                    if(leaderSubscriber==null) {
                        leaderSubscriber = leaderSession.createDurableConsumer(leaderTopic, leaderTopic.getTopicName(), followerStateLeaderSelector, true);
                    }
                    break;
                }
                default: {
                    log.warn("configureLeaderSubscriber - Not in Follower or Proxy state, LeaderSubscriber not configured: "+ConsensusServer.getState().name());
                    return;
                }
            }
            if(leaderSubscriber!=null) {
                leaderSubscriber.setMessageListener(leaderMessageListener);
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void closeLeaderSubscriber() {

        try {
            if(leaderSubscriber!=null) {
                leaderSubscriber.close();
                leaderSubscriber = null;
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void configureFollowerSubscriber() {

        try {
            switch(ConsensusServer.getState()) {
                case Candidate: {
                    if(followerSubscriber==null) {
                        followerSubscriber = followerSession.createDurableConsumer(followerTopic, followerTopic.getTopicName(), candidateStateFollowerSelector, true);
                    }
                    break;
                }
                case Leader: {
                    if(followerSubscriber==null) {
                        followerSubscriber = followerSession.createDurableConsumer(followerTopic, followerTopic.getTopicName(), leaderStateFollowerSelector, true);
                    }
                    break;
                }
                default: {
                    log.warn("configureFollowerSubscriber - Not in Candidate or Leader State, followerSubscriber NOT configured: "+ConsensusServer.getState().name());
                    return;
                }
            }
            if(followerSubscriber!=null) {
                followerSubscriber.setMessageListener(followerMessageListener);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closeFollowerSubscriber() {

        try {
            if(followerSubscriber!=null) {
                followerSubscriber.close();
                followerSubscriber = null;
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void configureProxySubscriber() {

        try {
            proxySubscriber = proxySession.createDurableConsumer(proxyTopic, proxyTopic.getTopicName(), null, true);
            proxySubscriber.setMessageListener(proxyMessageListener);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void closeProxySubscriber() {

        try {
            if(proxySubscriber!=null) {
                proxySubscriber.close();
                proxySubscriber = null;
            }
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    private void configureInitialContext() {
        try {
            initialContext = new InitialContext();
        } catch(NamingException ne) {
            ne.printStackTrace();
        }
    }

    private void configureTopics() {
        try {
            leaderTopic = (Topic) initialContext.lookup("topic/leaderTopic");
            followerTopic = (Topic) initialContext.lookup("topic/followerTopic");
            candidateTopic = (Topic) initialContext.lookup("topic/candidateTopic");
            proxyTopic = (Topic) initialContext.lookup("topic/proxyTopic");
        } catch(NamingException ne) {
            ne.printStackTrace();
        }
    }

    private void configureConnection() {

        try {
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            connection.setClientID(ConsensusServer.getId());
            connection.start();
        } catch(NamingException | JMSException ne) {
            ne.printStackTrace();
        }
    }

    private void configureCandidatePublisher() {

        try {
            candidatePublisher = candidateSession.createProducer(candidateTopic);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }

    }

    private void configureLeaderPublisher() {

        try {
            leaderPublisher = leaderSession.createProducer(leaderTopic);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    private void configureFollowerPublisher() {

        try {
            followerPublisher = followerSession.createProducer(followerTopic);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    private void configureProxyPublisher() {
        try {
            proxyPublisher = proxySession.createProducer(proxyTopic);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void sendCandidateMessage(GeneralRequest generalRequest) {

        log.info("sendCandidateMessage: request = "+generalRequest.getRequest().name());
        try {
            ObjectMessage candidateMessage = candidateSession.createObjectMessage(generalRequest);
            candidateMessage.setStringProperty("messageType", generalRequest.getRequest().name());
            candidatePublisher.send(candidateMessage, candidateCompletionListener);
        } catch(JMSException jms) {
            jms.printStackTrace();
        }
    }

    public void sendLeaderMessage(GeneralRequest generalRequest) {

        log.info("sendLeaderMessage: "+generalRequest.getRequest().name());
        try {
            ObjectMessage leaderMessage = leaderSession.createObjectMessage(generalRequest);
            leaderMessage.setStringProperty("messageType", generalRequest.getRequest().name());
            leaderPublisher.send(leaderMessage, leaderCompletionListener);
        } catch(JMSException jms) {
            jms.printStackTrace();
        }
    }

    public void sendFollowerMessage(GeneralRequest generalRequest) {

        log.info("sendFollowerMessage: request = "+generalRequest.toString());
        try {
            ObjectMessage followerMessage = followerSession.createObjectMessage(generalRequest);
            followerMessage.setStringProperty("messageType", generalRequest.getRequest().name());
            followerPublisher.send(followerMessage, followerCompletionListener);
        } catch(JMSException jms) {
            jms.printStackTrace();
        }
    }

    public void sendProxyMessage(GeneralRequest generalRequest) {
        log.info("sendProxyMessage: request = "+generalRequest.getRequest());
        try {
            ObjectMessage proxyMessage = proxySession.createObjectMessage(generalRequest);
//            proxyMessage.setStringProperty("messageType", generalRequest.getRequest().name());
            proxyPublisher.send(proxyMessage, proxyCompletionListener);
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }
}
