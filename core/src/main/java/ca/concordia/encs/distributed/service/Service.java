package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Service implements MessageListener, Callable<Message> {
    protected Module module;
    protected AtomicBoolean received = new AtomicBoolean(false);
    protected Message response;

    public Service(Module module) {
        this.module = module;
    }

    protected abstract void execute();
    protected abstract void process(IdentifiableMessage message);
    protected boolean validate(IdentifiableMessage message) {
        return !module.server.id().equals(message.getServerId())
                && (message.To == null || module.server.id().equals(message.To));
    }
    protected abstract void clean();
    public void stop() {
        received.set(true);
    }

    @Override
    public boolean receive(IdentifiableMessage message) {
        if(validate(message)) {
            process(message);
            return true;
        }
        return false;
    }

    protected void loop() throws InterruptedException {
        while (!this.received.get() && !Thread.currentThread().isInterrupted()) {
            Thread.sleep(500);
        }
    }

    @Override
    public Message call() throws Exception {
        try {
            execute();
            loop();
        }
        catch (InterruptedException e) {
            ExceptionManager.handle(e);
        }
        finally {
            clean();
        }
        return response;
    }
}
