package com.ll.vbc.business.services.client.consensus.candidate.events;

import java.util.EventObject;

public class RequestVoteEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public RequestVoteEvent(Object source) {
        super(source);
    }
}
