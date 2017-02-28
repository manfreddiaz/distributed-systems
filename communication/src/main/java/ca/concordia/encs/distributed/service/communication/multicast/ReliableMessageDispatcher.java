package ca.concordia.encs.distributed.service.communication.multicast;

import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.communication.MessageQueue;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.communication.multicast.queueing.PerProcessDeliveryQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReliableMessageDispatcher implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ReliableMessageDispatcher.class);

    private MessageQueue deliveryQueue;
    private AtomicBoolean stopped = new AtomicBoolean(false);
    private ExecutorService deliveryThread = Executors.newFixedThreadPool(16);
    private ConcurrentHashMap<String, ArrayList<MessageListener>> listenersByTopic = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, PerProcessDeliveryQueue> perProcessDeliveryQueue = new ConcurrentHashMap<>();

    private ReliableMulticastComponent component;

    ReliableMessageDispatcher(ReliableMulticastComponent component) {
        this.component = component;
        this.deliveryQueue = component.receiver().incoming();
    }

    public void addListener(MessageListener messageListener, String topic) {
        ArrayList<MessageListener> listeners = listenersByTopic.computeIfAbsent(topic, x -> new ArrayList<MessageListener>());
        if(listeners != null) {
            synchronized (listeners) {
                listeners.add(messageListener);
            }
        }
    }
    public void removeListener(MessageListener messageListener, String topic) {
        ArrayList<MessageListener> listeners = listenersByTopic.get(topic);
        if(listeners != null) {
            synchronized (listeners) {
                listeners.remove(messageListener);
            }
        }
    }

    private void dispatch(IdentifiableMessage message) {
        logger.debug("[R-DISPATCHER] Dispatching message, Id = {}, Action = {}, To = {}", message.Id, message.Method, message.To);
        PerProcessDeliveryQueue processDeliveryQueue = perProcessDeliveryQueue.computeIfAbsent(message.getServerId(), x ->
                new PerProcessDeliveryQueue(this)
        );
        DeliveryQueueState state = processDeliveryQueue.enqueue(message);
        if(state == DeliveryQueueState.OVERFLOW) {
            component.request(DefaultSystemMessages.NAK.Method, processDeliveryQueue.lastDelivered());
        }
        if(message.Ack && component.id() != message.getServerId()) {
            component.request(DefaultSystemMessages.ACK.Method, message.Id);
        }
    }

    public void deliver(IdentifiableMessage message) {
        logger.debug("[R-DISPATCHER] Delivering message, Id = {}, Action = {}, To = {}", message.Id, message.Method, message.To);
        deliveryThread.submit(() -> notifyListeners(message));
    }

    public void nak(IdentifiableMessage message) {
        component.request(DefaultSystemMessages.NAK.Method, message.Id);
    }
    private void notifyListeners(IdentifiableMessage message) {
        ArrayList<MessageListener> listeners = listenersByTopic.get(message.Method);
        if(listeners != null) {
            for (MessageListener listener : listeners) {
                try {
                    listener.receive(message);
                }catch (Exception e) {
                    logger.error("[R-DISPATCHER] Delivering message FAILED, Reason = {}", e);
                }
            }
        }
        logger.debug("[R-DISPATCHER] Delivered message, Id = {}, Action = {}, To = {}", message.Id, message.Method, message.To);
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        while(!stopped.get()) {
            try {
                dispatch(deliveryQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        stopped.set(true);
    }
}
