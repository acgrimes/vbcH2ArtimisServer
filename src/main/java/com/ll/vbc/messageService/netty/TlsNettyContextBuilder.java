package com.ll.vbc.messageService.netty;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class TlsNettyContextBuilder {

    public static final String CLIENT_NAME = "client";
    public static final char[] CLIENT_PASSWORD = "clientPassword".toCharArray();

    public static final String TRUST_STORE_NAME = "trustStore";
    public static final char[] TRUST_STORE_PASSWORD = "trustPassword".toCharArray();


    public SslContext build() {
        SslContext sslContext = null;
        try {
            KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

//                        clientStore.load(new FileInputStream("client.p12"), CLIENT_PASSWORD);
            clientStore.load(TlsNettyContextBuilder.class.getResourceAsStream("client.p12"), CLIENT_PASSWORD);
            mgrFact.init(clientStore, CLIENT_PASSWORD);

            // set up a trust manager so we can recognize the server
            TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustStore = KeyStore.getInstance("JKS");

//                        trustStore.load(new FileInputStream("trustStore.jks"), TRUST_STORE_PASSWORD);
            trustStore.load(TlsNettyContextBuilder.class.getResourceAsStream("trustStore.jks"), TRUST_STORE_PASSWORD);
            trustFact.init(trustStore);
            sslContext = SslContextBuilder.forServer(mgrFact).
                    sslProvider(SslProvider.JDK).
                    protocols("TLSv1.3").
                    keyManager(mgrFact).
                    trustManager(trustFact).
//                    clientAuth(ClientAuth.REQUIRE).
                    build();
        } catch(KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException ex) {
            ex.printStackTrace();
        }
        return sslContext;
    }

}
