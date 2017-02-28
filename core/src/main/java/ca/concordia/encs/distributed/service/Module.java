package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.messaging.MessageStatus;
import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Module implements Manageable {
    private static Logger logger = LoggerFactory.getLogger(Module.class);

    protected AtomicBoolean started = new AtomicBoolean(false);
    protected CommunicationComponent communicationComponent;
    protected Server server;
    protected ArrayList<Service> services = new ArrayList<>();
    protected ExecutorService serviceHost = Executors.newFixedThreadPool(16);
    protected Module passive;

    public Module(Server server, CommunicationComponent communicationComponent, Module passive) {
        this.server = server;
        this.communicationComponent = communicationComponent;
        this.passive = passive;
    }

    protected abstract void configure();

    public Module passive() {
        return passive;
    }
    public void subscribe(String topic, MessageListener listener) {
        this.communicationComponent.subscribe(topic, listener);
    }
    public void unsubscribe(String topic, MessageListener listener) {
        this.communicationComponent.unsubscribe(topic, listener);
    }
    public IdentifiableMessage request(String method, Object content) {
        return this.communicationComponent.request(new Message(method, MessageStatus.Request.code(), content));
    }
    public void reply(String method, Object content, Integer messageId, Integer to) {
        this.communicationComponent.reply(new Message(method, MessageStatus.Reply.code(), content), messageId, to);
    }
    public void replicate(String method, Object content) {
        if(this.passive != null) {
            logger.debug("[MODULE] {} Piping call: {}, {}", this.getClass().getName(), method, content);
            this.passive.request(method, content);
        }
    }
    public void replicate(IdentifiableMessage message) {
        if(this.passive != null) {
            logger.debug("[MODULE] Piping call: {}, {}", message.Method, message.Content);
            this.passive.request(message.Method, message.Content);
        }
    }
    public Server server() {
        return server;
    }
    public Future execute(Service service) {
        logger.debug("[MODULE] {} Submitting service for execution: {}", this.getClass().getName(), service.getClass().getSimpleName());
        return serviceHost.submit(service);
    }

    @Override
    public void start() {
        if(!started.get()) {
            logger.debug("[MODULE] {} Starting module...", this.getClass().getName());
            configure();
            logger.debug("[MODULE] {} Configuration finished...", this.getClass().getName());
            logger.debug("[MODULE] {} Starting communication...", this.getClass().getName());
            communicationComponent.start();
            logger.debug("[MODULE] {} Starting all services...", this.getClass().getName());
            services.forEach(service -> serviceHost.submit(service));
            logger.debug("[MODULE] {} Services started.", this.getClass().getName());
            if(this.passive != null) {
                logger.debug("[MODULE] {} Starting submodule...", this.getClass().getName());
                this.passive.start();
                logger.debug("[MODULE] {} Submodule started", this.getClass().getName());
            }
            started.set(true);
            logger.debug("[MODULE] {} Module Started.", this.getClass().getName());
        }
    }
    @Override
    public void stop() {
        if(started.get()) {
            logger.debug("[MODULE] Stopping module...");
            communicationComponent.stop();
            logger.trace("[MODULE] Stopping services...");
            stopServices();
            logger.debug("[MODULE] Services stopped.");
            started.set(false);
            logger.debug("[MODULE] Stopped...");
        }
    }
    private void stopServices() {
        services.forEach(service -> service.stop());
        services.clear();
        serviceHost.shutdown();
        services = null;
        serviceHost = null;
    }
}
