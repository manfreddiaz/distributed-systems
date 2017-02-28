package ca.concordia.encs.distributed.service.serialization;

import ca.concordia.encs.distributed.service.MessageSerializer;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import com.google.gson.Gson;

import java.io.IOException;

public class MessageJsonSerializer implements MessageSerializer {
    private Gson serializer = new Gson();
    public MessageJsonSerializer() {

    }
    @Override
    public byte[] serialize(IdentifiableMessage message) throws IOException {
        String jsonRepresentation = serializer.toJson(message, IdentifiableMessage.class);

        return jsonRepresentation.getBytes(); //TODO: Common encoding (not a problem now, same machine)
    }

    @Override
    public IdentifiableMessage deserialize(byte[] serialized, int offset, int length) {
        String jsonRepresetation = new String(serialized, offset, length); //TODO: Common encoding (not a problem now, same machine)

        return (IdentifiableMessage) serializer.fromJson(jsonRepresetation, IdentifiableMessage.class);
    }
}
