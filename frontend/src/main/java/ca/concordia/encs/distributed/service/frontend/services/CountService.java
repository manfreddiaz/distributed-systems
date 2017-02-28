package ca.concordia.encs.distributed.service.frontend.services;

import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;


public class CountService extends InvocationService {
    private IdentifiableMessage origin;

    public CountService(Module module, Object content) {
        super(module, DefaultSystemMessages.COUNT.Method, content);
    }

}
