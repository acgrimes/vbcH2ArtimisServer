package com.ll.vbc.messageService.netty;

import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.messageService.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class NettyServer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private final boolean SSL = false;
    private final int PORT = ConsensusServer.getReactivePort();
    private final NettyServerHandler nettyServerHandler;
    private final WriteBufferWaterMark writeBufferWaterMark;

    public NettyServer() {
        nettyServerHandler = new NettyServerHandler();
        writeBufferWaterMark = new WriteBufferWaterMark(8 * 1024, 64 * 1024);
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public void run() {

        try {
            // Configure SSL.
            final SslContext sslCtx;
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            // Configure the server.
            bossGroup = new EpollEventLoopGroup(1);
            workerGroup = new EpollEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.WRITE_BUFFER_WATER_MARK, writeBufferWaterMark)
                    .option(ChannelOption.SO_BACKLOG, 1000)
                    .option(ChannelOption.SO_RCVBUF, 500000)
                    .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, new DefaultMessageSizeEstimator(500000))
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(500000));
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
//                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(nettyServerHandler);
                        }
                    });
            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();
            f.addListener(new ServerConnectionListener());
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch(CertificateException | InterruptedException | SSLException ex) {
            ex.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    protected class ServerConnectionListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {//if is not successful, reconnect
                log.debug("Server Started on PORT: " + PORT);
            }
        }
    }
}
