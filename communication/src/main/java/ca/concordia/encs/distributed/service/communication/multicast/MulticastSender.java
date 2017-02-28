package ca.concordia.encs.distributed.service.communication.multicast;

import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.service.Manageable;
import ca.concordia.encs.distributed.service.MessageSerializer;
import ca.concordia.encs.distributed.service.communication.MessageQueue;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.communication.multicast.queueing.SequentialMessageQueue;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastSender implements Runnable, Manageable {
    private static Logger logger = (Logger) LoggerFactory.getLogger(MulticastSender.class);
    private static int BUFFER_SIZE = 10 * 1024;

    private byte[] buffer = new byte[BUFFER_SIZE];
    private AtomicBoolean stopped = new AtomicBoolean(false);
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private MessageQueue sendMessageQueue;
    @Inject
    private MessageSerializer serializer;

    MulticastSender(InetAddress multicastAddress, Integer port) throws ClusterCommunicationException {
        try {
            this.datagramSocket = new DatagramSocket();
            this.datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE, multicastAddress, port);
            this.sendMessageQueue = new SequentialMessageQueue();
        } catch (SocketException e) {
            ExceptionManager.handle(e);
            throw new ClusterCommunicationException();
        }
    }

    MessageQueue outgoing() {
        return this.sendMessageQueue;
    }

    private void send(IdentifiableMessage message) throws ClusterCommunicationException {
        byte[] serializedMessage;
        try {
            logger.trace("[COMM-SENDER] Try serializing message: {}", message);
            serializedMessage = serializer.serialize(message);
            logger.trace("[COMM-SENDER] Message serialized: {}", serializedMessage);
        } catch (IOException e) {
            logger.error("[COMM-SENDER] Serializing message failed: {}", e);
            return;
        }
        try {
            synchronized (datagramPacket) {
                datagramPacket.setData(serializedMessage);
                datagramSocket.send(datagramPacket);
            }
            logger.debug("[COMM-SENDER] Message sent: {}", message);
        } catch (IOException e) {
            ExceptionManager.handle(e);
            logger.error("[COMM-SENDER] Error sending: {}, {}", serializedMessage, e);
            throw new ClusterCommunicationException();
        }
    }

    @Override
    public void run() {
        start();
        logger.debug("[COMM-SENDER] Stopped.");
    }

    @Override
    public void start() {
        try {
            logger.debug("[COMM-SENDER] Started...");
            while (!stopped.get()) {
                this.send(sendMessageQueue.take());
                logger.debug("[COMM-SENDER] Sending...");
            }
        } catch (InterruptedException e) {
            logger.error("[COMM-SENDER] Sender interrupted: {}", e);
        } catch (ClusterCommunicationException e) {
            logger.error("[COMM-SENDER] Communication error: {}", e);
        }
    }

    @Override
    public void stop() {
        stopped.set(true);
    }
}
