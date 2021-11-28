package com.ll.vbc.business.services.client.consensus.leader.event;

import com.ll.vbc.messageService.request.GeneralRequest;

import java.util.EventObject;

public class LogEntryEvent extends EventObject {

    public LogEntryEvent(GeneralRequest source) {
        super(source);
    }
}
