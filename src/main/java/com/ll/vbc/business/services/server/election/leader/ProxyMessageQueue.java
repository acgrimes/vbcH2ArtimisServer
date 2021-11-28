package com.ll.vbc.business.services.server.election.leader;

import com.ll.vbc.messageService.request.GeneralRequest;

import java.util.concurrent.ArrayBlockingQueue;

public interface ProxyMessageQueue {

    ArrayBlockingQueue<GeneralRequest> generalRequestQueue = new ArrayBlockingQueue<GeneralRequest>(1000000);

}
