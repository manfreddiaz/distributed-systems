package ca.concordia.encs.distributed.service.frontend.services;

import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;

public class CreateRecordDRService extends InvocationService {
    public CreateRecordDRService(Module module, Object content) {
        super(module, DefaultSystemMessages.CREATEDR.Method, content);
    }
}
