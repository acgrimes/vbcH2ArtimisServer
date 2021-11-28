package com.ll.vbc.messageService.netty.httpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class TlsContextBuilder {

    public static final String CLIENT_NAME = "client";
    public static final char[] CLIENT_PASSWORD = "clientPassword".toCharArray();

    public static final String TRUST_STORE_NAME = "trustStore";
    public static final char[] TRUST_STORE_PASSWORD = "trustPassword".toCharArray();

    protected SSLContext build() {

        SSLContext sslContext = null;
        try {
            KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream("src/main/resources/client.p12"), CLIENT_PASSWORD);
//            clientStore.load(Presenter.class.getResourceAsStream("client.P12"), CLIENT_PASSWORD);
            mgrFact.init(clientStore, CLIENT_PASSWORD);

            // set up a trust manager so we can recognize the server
            TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("src/main/resources/trustStore.jks"), TRUST_STORE_PASSWORD);
//            trustStore.load(Presenter.class.getResourceAsStream("trustStore.jks"), TRUST_STORE_PASSWORD);
            trustFact.init(trustStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(mgrFact.getKeyManagers(), trustFact.getTrustManagers(), new SecureRandom());
            sslContext.getDefaultSSLParameters().setNeedClientAuth(false);
            sslContext.getDefaultSSLParameters().setProtocols(new String[]{"TLS", "TLSv1.3"});
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException ioe) {
            ioe.printStackTrace();
        }
        return sslContext;

    }

}
