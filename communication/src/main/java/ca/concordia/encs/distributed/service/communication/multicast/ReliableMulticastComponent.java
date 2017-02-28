package ca.concordia.encs.distributed.service.communication.multicast;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.messaging.MessageStatus;
import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.messaging.MessageIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReliableMulticastComponent implements ca.concordia.encs.distributed.service.CommunicationComponent {
    private static Logger logger = (Logger) LoggerFactory.getLogger(ReliableMulticastComponent.class);

    private ExecutorService threadPool = Executors.newFixedThreadPool(16);
    private AtomicBoolean hasStarted = new AtomicBoolean(false);
    private InetAddress multicastAddress;
    private Integer port;
    private Integer serverId;
    private MessageIdGenerator idGenerator;
    private MulticastSender multicastSender;
    private MulticastReceiver multicastReceiver;
    private ReliableMessageDispatcher messageDispatcher;
    private ReliableMessageSender messageSender;

    public ReliableMulticastComponent(String multicastAddress, Integer port, Integer serverId) throws ConfigurationException {
        try {
            this.multicastAddress = InetAddress.getByName(multicastAddress);
            this.port = port;
            this.serverId = serverId;
        } catch (UnknownHostException e) {
            ExceptionManager.handle(e);
            throw new ConfigurationException();
        }
    }

    MulticastReceiver receiver() {
        return multicastReceiver;
    }
    public MulticastSender sender() {
        return multicastSender;
    }

    @Override
    public IdentifiableMessage request(String method, Object content) {
        Message message = new Message(method, MessageStatus.Request.code(), content);
        return request(message);
    }
    @Override
    public IdentifiableMessage request(Message message) {
        IdentifiableMessage messageToSend = IdentifiableMessage.fromMessage(message, idGenerator.getNextId());

        this.messageSender.enqueue(messageToSend);

        return messageToSend;
    }
    @Override
    public IdentifiableMessage reply(Message message, Integer replyToId, Integer to) {
        IdentifiableMessage messageToReply = IdentifiableMessage.fromMessage(message, idGenerator.getNextId());
        messageToReply.RepliesTo = replyToId;
        messageToReply.To = to;

        this.messageSender.enqueue(messageToReply);

        return messageToReply;
    }
    @Override
    public void subscribe(String topic, MessageListener listener) {
        this.messageDispatcher.addListener(listener, topic);
    }
    @Override
    public void unsubscribe(String topic, MessageListener listener) {
        this.messageDispatcher.removeListener(listener, topic);
    }

    public void start() {
        if(!hasStarted.get()) {
            try {
                logger.debug("[COMMUNICATION] Communication starting at: {}:{}.", multicastAddress, port);

                this.multicastSender = new MulticastSender(this.multicastAddress, port);
                this.multicastReceiver = new MulticastReceiver(this.multicastAddress, port);
                this.messageDispatcher = new ReliableMessageDispatcher(this);
                this.messageSender = new ReliableMessageSender(this);
                this.idGenerator = new MessageIdGenerator(serverId);

                threadPool.execute(multicastSender);
                threadPool.execute(multicastReceiver);
                threadPool.execute(messageDispatcher);
                threadPool.execute(messageSender);

                hasStarted.set(true);
                logger.debug("[COMMUNICATION] Communication started at: {}:{}.", multicastAddress, port);
            }  catch (ClusterCommunicationException e) {
                logger.error("[COMMUNICATION] Could not be started.", e);
            }
        }
    }

    public void stop() {
        if(hasStarted.get()) {
            logger.debug("[COMMUNICATION] Stopping all components.");
            this.multicastReceiver.stop();
            this.messageDispatcher.stop();
            this.multicastSender.stop();
            try {
                logger.debug("[COMMUNICATION] Awaiting for all components termination.");
                threadPool.awaitTermination(10, TimeUnit.SECONDS);
                logger.debug("[COMMUNICATION] All components terminated.");
                this.multicastReceiver = null;
                this.multicastSender = null;
                this.messageDispatcher = null;
                logger.debug("[COMMUNICATION] All components cleaned-up.");
                hasStarted.set(false);
                logger.debug("[COMMUNICATION] Communication has stopped.");
            } catch (InterruptedException e) {
                logger.error("[COMMUNICATION] Error while awaiting for thread pool", e);
            }
        }
    }


    public Integer id() {
        return serverId;
    }

}
