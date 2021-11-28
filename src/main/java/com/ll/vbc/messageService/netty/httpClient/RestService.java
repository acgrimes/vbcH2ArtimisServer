package com.ll.vbc.messageService.netty.httpClient;

import com.ll.vbc.messageService.request.GeneralRequest;
import com.ll.vbc.messageService.response.GeneralResponse;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RestService {

    private final String host;
    private final int port;

    public RestService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public GeneralResponse postMessage(GeneralRequest generalRequest) {

        GeneralResponse generalResponse = null;
        try {
            TlsContextBuilder tlsContextBuilder = new TlsContextBuilder();
            String fragment = generalRequest.getRequest().name();
            System.out.println("Post message fragment: "+fragment);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://"+host+":"+port+"/"+fragment))
                    .header("Content-Type", "application/octal-stream")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(SerializationUtils.serialize(generalRequest)))
                    .build();

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .sslContext(tlsContextBuilder.build())
//                    .authenticator(new Authenticator() {
//                        @Override
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(
//                                    "user",
//                                    "password".toCharArray());
//                        }
//                    })
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            System.out.println(response.statusCode());
            generalResponse = new GeneralResponse();
            generalResponse = SerializationUtils.deserialize(response.body());
            System.out.println(generalResponse.getResponse().name());
        } catch(IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return generalResponse;
    }

}
