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
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastReceiver implements Runnable, Manageable {
    private static Logger logger = LoggerFactory.getLogger(MulticastReceiver.class);

    private static int BUFFER_SIZE = 10 * 1024; //10k
    private byte[] buffer = new byte[BUFFER_SIZE];
    private AtomicBoolean stopped = new AtomicBoolean(false);
    private DatagramPacket datagramPacket;
    private MulticastSocket multicastSocket;
    private MessageQueue deliveryQueue;
    @Inject
    private MessageSerializer serializer;

    MulticastReceiver(InetAddress multicastAddress, Integer port) throws ClusterCommunicationException {
        this.datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE);
        try {
            this.deliveryQueue = new SequentialMessageQueue();
            this.multicastSocket = new MulticastSocket(port);
            this.multicastSocket.joinGroup(multicastAddress);
        } catch (IOException e) {
            ExceptionManager.handle(e);
            throw new ClusterCommunicationException();
        }
    }

    private void onMessageReceived() {
        IdentifiableMessage message = null;
        synchronized (datagramPacket) {
            message = serializer.deserialize(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
            this.datagramPacket.setLength(BUFFER_SIZE);
        }
        logger.debug("[COMM-RECEIVER] Received message, Id = {}, Action = {}, To = {}", message.Id, message.Method, message.To);
        deliveryQueue.enqueue(message);
    }

    MessageQueue incoming() {
        return deliveryQueue;
    }

    @Override
    public void run() {
        logger.debug("[COMM-RECEIVER] Started.");
        while(!stopped.get()) {
            try {
                this.multicastSocket.receive(this.datagramPacket);
                onMessageReceived();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.debug("[COMM-RECEIVER] Stopped.");
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        stopped.set(true);
    }
}
