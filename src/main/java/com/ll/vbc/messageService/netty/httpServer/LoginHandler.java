package com.ll.vbc.messageService.netty.httpServer;

import com.ll.vbc.messageService.request.MobileRequest;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;

public class LoginHandler {

    public byte[] handle(HttpContent httpContent) {

        byte[] byteResponse = null;
        ByteBuf content = httpContent.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.getBytes(0, bytes);
        if (content.isReadable()) {
            MobileRequest mobileRequest = new MobileRequest();
            mobileRequest.deserialize(bytes);
            System.out.println(mobileRequest.toString());

//            MobileResponse mobileResponse = new MobileResponse("Server said THIS IS AMAZING!");
//            byteResponse = mobileResponse.serialize();
        }
        return byteResponse;
    }
}
