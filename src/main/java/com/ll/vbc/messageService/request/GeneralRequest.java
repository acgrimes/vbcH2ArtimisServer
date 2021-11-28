package com.ll.vbc.messageService.request;


import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.enums.Request;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;

public class GeneralRequest implements Serializable {

    private static final long serialVersionUID = 4L;

    private final Request request;
    private final AppendEntry appendEntry;
    private final byte[] publicKey;
    private final byte[] digitalSignature;

    public GeneralRequest(Request request, AppendEntry appendEntry, byte[] publicKey, byte[] digitalSignature) {
        this.request = request;
        this.appendEntry = appendEntry;
        this.publicKey = publicKey;
        this.digitalSignature = digitalSignature;
    }

    public Request getRequest() {
        return request;
    }

    public AppendEntry getAppendEntry() {
        return appendEntry;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GeneralRequest that = (GeneralRequest) o;

        return new EqualsBuilder().append(request, that.request).append(appendEntry, that.appendEntry).append(publicKey, that.publicKey).append(digitalSignature, that.digitalSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(request).append(appendEntry).append(publicKey).append(digitalSignature).toHashCode();
    }

    @Override
    public String toString() {
        return "GeneralRequest{" +
                "request=" + request +
                ", appendEntry=" + appendEntry +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", digitalSignature=" + Arrays.toString(digitalSignature) +
                '}';
    }
}
