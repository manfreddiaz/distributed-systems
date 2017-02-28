package ca.concordia.encs.distributed.service.messaging;

import ca.concordia.encs.distributed.messaging.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupResponseMessage extends Message {

    public GroupResponseMessage(String method, Integer status) {
        super(method, status, Collections.synchronizedList(new ArrayList<IdentifiableMessage>()));
    }

    public void addMessage(Message message) {
        ((List)(this.Content)).add(message);
    }

    public List<IdentifiableMessage> getIdentifiedResponses() {
        return  (List<IdentifiableMessage>) this.Content;
    }
}
