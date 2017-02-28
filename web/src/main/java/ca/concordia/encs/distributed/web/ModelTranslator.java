package ca.concordia.encs.distributed.web;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.serialization.json.JsonSerializer;

public class ModelTranslator {
    public static WebMessage fromModel(Message message) {
        return new WebMessage(message.Status, message.Method, JsonSerializer.toJSON(message.Content, message.Content.getClass()));
    }
    public static Message toModel(WebMessage webMessage, Class clazz) {
        return new Message(webMessage.Method, webMessage.Status, JsonSerializer.fromJSON(webMessage.Content, clazz));
    }
}
