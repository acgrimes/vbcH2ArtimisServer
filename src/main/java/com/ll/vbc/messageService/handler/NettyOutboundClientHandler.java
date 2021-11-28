package com.ll.vbc.messageService.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NettyOutboundClientHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyOutboundClientHandler.class);
    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
//        log.debug("Outbound handler added - CONNECT_TIMEOUT_MILLIS: "+ctx.channel().config().getOption(ChannelOption.CONNECT_TIMEOUT_MILLIS));
        this.ctx = ctx;
    }

    public void sendMessage(ByteBuf msg) {
        try {
//            log.debug("sendMessage - readableBytes: "+msg.readableBytes());
//            Thread.sleep(100);
            ChannelPromise cp = new DefaultChannelPromise(ctx.channel());
            msg.retain();
            while(!ctx.channel().isWritable()) {
                Thread.sleep(10);
            }
            write(ctx, msg, cp);
            flush(ctx);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
