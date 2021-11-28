package com.ll.vbc.utils;


import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.domain.ElectionTransaction;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;
import java.util.logging.Logger;

public class BuildGeneralRequest {

    private static final Logger log = Logger.getLogger(BuildGeneralRequest.class.getSimpleName());

    public static GeneralRequest build() {

        return build(Request.ElectionTransaction);

    }

    public static GeneralRequest build(Request request) {

        ElectionTransaction electionTransaction = BuildElectionTransaction.build();

        Security.addProvider(new BouncyCastleProvider());

        byte[] et = SerializationUtils.serialize(electionTransaction);

        KeyPair keyPair = Utils.generateECDSAKeyPair();
        byte[] signedTx = Utils.digitalSignature(keyPair.getPrivate(), et);

        AppendEntry appendEntry = new AppendEntry(ConsensusServer.getServerInstance(),
                UUID.randomUUID(),
                100l, 10L,
                et, et);

        GeneralRequest generalRequest = new GeneralRequest(request,
                appendEntry,
                keyPair.getPublic().getEncoded(),
                signedTx);

        byte[] electionTx = generalRequest.getAppendEntry().getElectionTransaction();
        byte[] encodedPubKey = generalRequest.getPublicKey();
        byte[] txSignature = generalRequest.getDigitalSignature();

        if(isValid(electionTx, encodedPubKey, txSignature)) {
            log.info("ElectionTransaction is Valid");
        } else {
            log.info("ElectionTransaction is InValid");
        }

        return generalRequest;

    }

    public static boolean isValid(byte[] message, byte[] encodedPublicKey, byte[] signature) {

        boolean result = false;
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            Signature signVerify = Signature.getInstance("ECDSA", "BC");

            signVerify.initVerify(pubKey);

            signVerify.update(message);

            if (signVerify.verify(signature)) {
                System.out.println("signature verification succeeded.");
                result = true;
            } else {
                System.out.println("signature verification failed.");
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
