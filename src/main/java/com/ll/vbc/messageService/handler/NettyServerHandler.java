package com.ll.vbc.messageService.handler;

import com.ll.vbc.messageService.request.GeneralRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    private static int referenceCount;
    public  NettyServerHandler() {
    }

    @Override
    public final void channelRead0(final ChannelHandlerContext ctx, final ByteBuf byteBuf) {

        try {
            log.debug("channelRead0 - readableBytes: "+byteBuf.readableBytes());
            GeneralRequest generalRequest = null;
            byte[] bytes = ByteBufUtil.getBytes(byteBuf);
            generalRequest = SerializationUtils.deserialize(bytes);
//            Application.generalRequestQueue.put(generalRequest);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
