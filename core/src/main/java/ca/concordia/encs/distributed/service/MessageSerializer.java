package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

import java.io.IOException;

public interface MessageSerializer {
    byte[] serialize(IdentifiableMessage message) throws IOException;
    IdentifiableMessage deserialize(byte[] serialized, int offset, int length);
}
