package com.ll.vbc.messageService.netty.httpServer;

import com.ll.vbc.messageService.netty.HttpFragment;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class CustomHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    byte[] byteResponse;
    HttpFragment fragment;
    HttpRequest request;
    private LoginHandler loginHandler = new LoginHandler();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {

        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            if(request.decoderResult().isSuccess() && request.method()== HttpMethod.POST) {
                System.out.println("Request Method: " + request.method().toString());
                System.out.println("Request URI: " + request.uri());
                fragment = HttpFragment.fromName(request.uri());
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            switch(fragment) {
                case Login: {
                    byteResponse = loginHandler.handle(httpContent);
                    break;
                }
                default: {
                    writeResponse(ctx);
                    break;
                }
            }

            if (msg instanceof LastHttpContent) {
                HttpObject httpObject = msg;
                if(httpObject.decoderResult().isSuccess()) {
                    writeResponse(ctx, request, byteResponse);
                } else {
                    writeResponse(ctx);
                }
            }
        }
    }

    private void writeResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.EMPTY_BUFFER);
        ctx.write(response);
    }

    private void writeResponse(ChannelHandlerContext ctx, HttpRequest request, byte[] byteResponse) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(byteResponse));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);

        if (keepAlive) {
            httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ctx.write(httpResponse);

        if (!keepAlive) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
