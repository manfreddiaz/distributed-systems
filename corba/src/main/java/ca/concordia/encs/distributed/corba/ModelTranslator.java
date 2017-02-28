package ca.concordia.encs.distributed.corba;

import ca.concordia.encs.distributed.corba.model.Message;
import ca.concordia.encs.distributed.serialization.json.JsonSerializer;

import java.util.Calendar;

public class ModelTranslator {
    static Message fromModelMessage(ca.concordia.encs.distributed.messaging.Message modelMessage) {
        return new Message(modelMessage.Method, modelMessage.Status.shortValue(), fromModelContent(modelMessage.Content));
    }
    static String fromModelContent(Object modelRecord) {
        return JsonSerializer.toJSON(modelRecord, modelRecord.getClass());
    }

    static Calendar fromMilliseconds(long time) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time);

        return calendar;
    }
}
