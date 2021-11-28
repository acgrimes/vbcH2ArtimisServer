package com.ll.vbc.business.services.server.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ValidateDigitalSignature {

    private static Logger log = LoggerFactory.getLogger(ValidateDigitalSignature.class.getSimpleName());

    /**
     *
     * @param message - message that has a digital signature
     * @param encodedPublicKey
     * @param signature
     * @return
     */
    public boolean isValid(byte[] message, byte[] encodedPublicKey, byte[] signature) {

        boolean result = false;
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            Signature signVerify = Signature.getInstance("ECDSA", "BC");

            signVerify.initVerify(pubKey);

            signVerify.update(message);

            if (signVerify.verify(signature)) {
                log.debug("signature verification succeeded.");
                result = true;
            } else {
                log.debug("signature verification failed.");
                result = false;
            }
        } catch(NoSuchAlgorithmException |
                NoSuchProviderException |
                InvalidKeyException |
                InvalidKeySpecException |
                SignatureException ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }
}