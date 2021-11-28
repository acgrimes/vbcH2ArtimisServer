package com.ll.vbc.messageService.response;

import com.ll.vbc.domain.Serialization;
import com.ll.vbc.domain.Voter;
import com.ll.vbc.enums.Response;
import com.ll.vbc.enums.ReturnCode;

import java.io.Serializable;

public class GeneralResponse extends Serialization implements Serializable {

    private Response response;
    private ReturnCode returnCode;
    private Voter voter;

    public GeneralResponse() {}
    public GeneralResponse(Voter voter, ReturnCode returnCode, Response response) {
        this.voter = voter;
        this.returnCode = returnCode;
        this.response = response;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    @Override
    public String toString() {
        return "GeneralResponse{" +
                "voter=" + voter +
                ", returnCode=" + returnCode +
                ", response=" + response +
                '}';
    }
}
