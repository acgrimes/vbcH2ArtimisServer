package com.ll.vbc.messageService.netty;

import com.ll.vbc.messageService.request.MobileRequest;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class RequestUtils {

    static StringBuilder formatParams(HttpRequest request) {
        StringBuilder responseData = new StringBuilder();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            for (Entry<String, List<String>> p : params.entrySet()) {
                String key = p.getKey();
                List<String> vals = p.getValue();
                for (String val : vals) {
                    responseData.append("Parameter: ")
                            .append(key.toUpperCase())
                            .append(" = ")
                            .append(val.toUpperCase())
                            .append("\r\n");
                }
            }
            responseData.append("\r\n");
        }
        return responseData;
    }

    static byte[] formatBody(HttpContent httpContent) {
        StringBuilder responseData = new StringBuilder();
        byte[] byteResponse = null;
        ByteBuf content = httpContent.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.getBytes(0, bytes);
        if (content.isReadable()) {
            MobileRequest mobileRequest = new MobileRequest();
            mobileRequest.deserialize(bytes);
            System.out.println(mobileRequest.toString());

            responseData.append(byteResponse);
            responseData.append("\r\n");
        }
        return byteResponse;
    }

    static StringBuilder evaluateDecoderResult(HttpObject o) {
        StringBuilder responseData = new StringBuilder();
        DecoderResult result = o.decoderResult();

        if (!result.isSuccess()) {
            responseData.append("..Decoder Failure: ");
            responseData.append(result.cause());
            responseData.append("\r\n");
        }

        return responseData;
    }

    static StringBuilder prepareLastResponse(HttpRequest request, LastHttpContent trailer) {
        StringBuilder responseData = new StringBuilder();
        responseData.append("Good Bye!\r\n");

        if (!trailer.trailingHeaders()
                .isEmpty()) {
            responseData.append("\r\n");
            for (CharSequence name : trailer.trailingHeaders().names()) {
                for (CharSequence value : trailer.trailingHeaders()
                        .getAll(name)) {
                    responseData.append("P.S. Trailing Header: ");
                    responseData.append(name)
                            .append(" = ")
                            .append(value)
                            .append("\r\n");
                }
            }
            responseData.append("\r\n");
        }
        return responseData;
    }

}