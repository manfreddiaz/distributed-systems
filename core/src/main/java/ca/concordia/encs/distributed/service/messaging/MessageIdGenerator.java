package ca.concordia.encs.distributed.service.messaging;

import ca.concordia.encs.distributed.model.Location;


import java.util.concurrent.atomic.AtomicInteger;

public class MessageIdGenerator {
    private static final int MESSAGE_MASK_BITS = 26; // 2^26 messages and Up to 64 (6 bits) servers in the cluster
    private static final int MESSAGE_ID_MASK = 0x3FFFFFF;
    private static final int MAX_SERVER_ID = 0xFC000000;

    private AtomicInteger messageId;
    private Integer serverId;

    public MessageIdGenerator(Integer serverId) {
        this.messageId = new AtomicInteger(0);
        this.serverId = serverId << MESSAGE_MASK_BITS;
    }

    public Integer getNextId() {
        return serverId | (messageId.incrementAndGet() & MESSAGE_ID_MASK);
    }

    public static Location getServerLocation(Integer id) {
        Integer serverId = getServerId(id);
        Location location = null;

        for(Location value : Location.values()) {
            if(serverId == value.index()) {
                location = value;
                break;
            }
        }

        return location;
    }

    public static Integer getMessageId(Integer id) {
        return id & MESSAGE_ID_MASK;
    }

    public static Integer  getServerId(Integer id) {
        return  (id & MAX_SERVER_ID) >> MESSAGE_MASK_BITS;
    }
}
