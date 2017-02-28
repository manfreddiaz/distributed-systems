package ca.concordia.encs.distributed.service.frontend.services;

import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;


public abstract class InvocationService extends Service {
    protected IdentifiableMessage origin;
    private String method;
    private Object content;

    public InvocationService(Module module, String method, Object content) {
        super(module);
        this.method = method;
        this.content = content;
    }

    @Override
    protected void execute() {
        module.subscribe(method, this);
        origin = module.request(method, content);
    }

    @Override
    protected void process(IdentifiableMessage message) {
        response = message;
        stop();
    }

    @Override
    protected boolean validate(IdentifiableMessage message) {
        return super.validate(message) && origin.Id.equals(message.RepliesTo);
    }

    @Override
    protected void clean() {
        module.unsubscribe(method, this);
    }
}
