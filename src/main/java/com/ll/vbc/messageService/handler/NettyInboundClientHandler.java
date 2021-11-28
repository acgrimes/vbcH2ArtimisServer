package com.ll.vbc.messageService.handler;

import com.ll.vbc.messageService.response.GeneralResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyInboundClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(NettyInboundClientHandler.class);

    private ChannelHandlerContext ctx;

    private int referenceCount;
    /**
     * Creates a client-side handler.
     */
    public NettyInboundClientHandler() { }

    public void sendMessage(ByteBuf message) {

        ctx.writeAndFlush(message);

    }

    public void flushMessage() {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        GeneralResponse generalResponse = null;
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = null;
        try {
            if (byteBuf.hasArray()) {
                bytes = byteBuf.array();
            } else {
                bytes = new byte[byteBuf.readableBytes()];
                byteBuf.duplicate().readBytes(bytes);
            }
            generalResponse = (GeneralResponse) SerializationUtils.deserialize(bytes);
            log.info("method - channelRead0: referenceCount - "+(++referenceCount)+", " + generalResponse.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
