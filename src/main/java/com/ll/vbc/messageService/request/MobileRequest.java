package com.ll.vbc.messageService.request;

import com.ll.vbc.domain.ElectionTransaction;
import com.ll.vbc.domain.LoginCredentials;
import com.ll.vbc.domain.Serialization;
import com.ll.vbc.domain.Voter;
import com.ll.vbc.enums.Request;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;

public class MobileRequest extends Serialization implements Serializable {

    private Request request;
    private LoginCredentials loginCredentials;
    private Voter voter;
    private ElectionTransaction electionTransaction;
    private byte[] publicKey;
    private byte[] digitalSignature;

    public MobileRequest() {
        request = Request.Last;
        loginCredentials = new LoginCredentials();
        voter = new Voter();
        electionTransaction = new ElectionTransaction();
        publicKey = new byte[0];
        digitalSignature = new byte[0];
    }
    public MobileRequest(Request request,
                         LoginCredentials loginCredentials,
                         Voter voter,
                         ElectionTransaction electionTransaction,
                         byte[] publicKey,
                         byte[] digitalSignature) {
        this.request = request;
        this.loginCredentials = loginCredentials;
        this.voter = voter;
        this.electionTransaction = electionTransaction;
        this.publicKey = publicKey;
        this.digitalSignature = digitalSignature;
    }


    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public LoginCredentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public ElectionTransaction getElectionTransaction() {
        return electionTransaction;
    }

    public void setElectionTransaction(ElectionTransaction electionTransaction) {
        this.electionTransaction = electionTransaction;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public byte[] serialize() {

        byte[] etBytes = null;
        byte[] bytes = concatenateBytes(request.serialize());
        if(loginCredentials!=null) {
            bytes = concatenateBytes(bytes, loginCredentials.serialize());
        }
        if(voter!=null) {
            bytes = concatenateBytes(bytes, voter.serialize());
        }
        if(electionTransaction!=null) {
            etBytes = electionTransaction.serialize();
            bytes = concatenateBytes(bytes, etBytes);
        }
        if(publicKey!=null) {
//        KeyPair keyPair = Utils.generateECDSAKeyPair();
//        publicKey = keyPair.getPublic().getEncoded();
            byte[] pkLength = serializeInt(publicKey.length);
            bytes = concatenateBytes(bytes, pkLength, publicKey);
        }
        if(digitalSignature!=null) {
//        digitalSignature = Utils.digitalSignature(keyPair.getPrivate(), etBytes);
            byte[] dsLength = serializeInt(digitalSignature.length);
            bytes = concatenateBytes(bytes, dsLength, digitalSignature);
        }
        return bytes;
    }

    public int deserialize(byte[] byteRequest) {
        int index=0;
        request = Request.fromOrdinal(deserializeInt(Arrays.copyOfRange(byteRequest, 0, index=index+4)));
        loginCredentials = new LoginCredentials();
        index =loginCredentials.deserialize(byteRequest, index);
        voter = new Voter();
        index = voter.deserialize(byteRequest, index);
        electionTransaction = new ElectionTransaction();
        index = electionTransaction.deserialize(byteRequest, index);
        int publicKeyLength = deserializeInt(Arrays.copyOfRange(byteRequest, index, index=index+4));
        publicKey = Arrays.copyOfRange(byteRequest, index, index=index+publicKeyLength);
        int dsLength = deserializeInt(Arrays.copyOfRange(byteRequest, index, index=index+4));
        digitalSignature = Arrays.copyOfRange(byteRequest, index, index=index+dsLength);

        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || this.getClass() != o.getClass()) return false;

        final MobileRequest that = (MobileRequest) o;

        return new EqualsBuilder()
                .append(this.request, that.request)
                .append(this.loginCredentials, that.loginCredentials)
                .append(this.voter, that.voter)
                .append(this.electionTransaction, that.electionTransaction)
                .append(this.publicKey, that.publicKey)
                .append(this.digitalSignature, that.digitalSignature)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.request)
                .append(this.loginCredentials)
                .append(this.voter)
                .append(this.electionTransaction)
                .append(this.publicKey)
                .append(this.digitalSignature)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "MobileRequest{" +
                "request=" + request +
                ", loginCredentials=" + loginCredentials +
                ", voter=" + voter +
                ", electionTransaction=" + electionTransaction +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", digitalSignature=" + Arrays.toString(digitalSignature) +
                '}';
    }
}
