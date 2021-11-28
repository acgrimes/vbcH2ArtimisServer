package com.ll.vbc.messageService.netty;

import com.ll.vbc.messageService.handler.NettyOutboundClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class NettyClient implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private String id = "A";
    private String host = "127.0.0.1";
    private int port = 8090;
    private boolean SSL = false;
    private Channel channel;
    private final Bootstrap bootstrap;
    private final Timer timer;
    private InetSocketAddress addr;
    private final WriteBufferWaterMark writeBufferWaterMark;

    public NettyClient setId(String id) {
        this.id = id;
        return this;
    }

    public NettyClient setHost(String host) {
        this.host = host;
        return this;
    }
    public NettyClient setPort(int port) {
        this.port = port;
        return this;
    }
    public NettyClient setSSL(boolean ssl) {
        this.SSL = ssl;
        return this;
    }

    public String getId() {
        return id;
    }

    private NettyOutboundClientHandler nettyOutboundClientHandler;

    public NettyClient() {
        nettyOutboundClientHandler = new NettyOutboundClientHandler();
        bootstrap = new Bootstrap();
        timer = new Timer();
        writeBufferWaterMark = new WriteBufferWaterMark(8 * 1024, 32 * 1024);
    }

    public NettyOutboundClientHandler getNettyOutboundClientHandler() {
        log.debug("getNettyOutboundClientHandler - out port: "+port);
        return nettyOutboundClientHandler;
    }

    @Override
    public void run() {

        addr = new InetSocketAddress(host, port);

        // Configure SSL.git
        final SslContext sslCtx;
        EventLoopGroup groupOL = null;
        try {
            if (SSL) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }

        // Configure the client.
            EpollEventLoopGroup group = new EpollEventLoopGroup();

            bootstrap.group(group)
                    .channel(EpollSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, false)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.WRITE_BUFFER_WATER_MARK, writeBufferWaterMark)
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
//                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(nettyOutboundClientHandler);
                        }
                    });

            scheduleConnect( 1000 );

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            groupOL.shutdownGracefully();
        }
    }

    private void doConnect() {

        try {
            ChannelFuture f = bootstrap.connect(addr);
            f.addListener(new ClientConnectionListener());
        } catch( Exception ex ) {
            scheduleConnect( 1000 );
        }
    }

    private void scheduleConnect(long millis) {
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                doConnect();
            }
        }, millis );
    }

    protected class ClientConnectionListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {//if is not successful, reconnect
                future.channel().close();
                bootstrap.connect(addr).addListener(this);
            } else {//good, the connection is ok
                channel = future.channel();
                //add a listener to detect the connection lost
                addCloseDetectListener(channel);
            }
        }

        private void addCloseDetectListener(Channel channel) {
            //if the channel connection is lost, the ChannelFutureListener.operationComplete() will be called
            channel.closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future ) throws Exception {
                    scheduleConnect( 1000 );
                }
            });
        }
    }
}
