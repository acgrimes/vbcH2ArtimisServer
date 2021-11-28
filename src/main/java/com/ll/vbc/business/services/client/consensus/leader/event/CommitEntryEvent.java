package com.ll.vbc.business.services.client.consensus.leader.event;


import com.ll.vbc.domain.AppendEntry;

import java.util.EventObject;

public class CommitEntryEvent extends EventObject {

    public CommitEntryEvent(AppendEntry source) {
        super(source);
    }
}
