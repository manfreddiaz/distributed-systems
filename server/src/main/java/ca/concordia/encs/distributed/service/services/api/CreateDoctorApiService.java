package ca.concordia.encs.distributed.service.services.api;

import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.service.ClinicServer;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.dtos.DoctorRecordDTO;
import ca.concordia.encs.distributed.service.serialization.ContentSerializer;
import com.google.gson.internal.LinkedTreeMap;

public class CreateDoctorApiService extends Service {
    public CreateDoctorApiService(Module module) {
        super(module);
    }

    @Override
    protected void execute() {
        module.subscribe(DefaultSystemMessages.CREATEDR.Method, this);
    }

    @Override
    protected void process(IdentifiableMessage message) {
        if(module.passive() != null) {
            ClinicServer server = (ClinicServer) module.server();
            server.createDoctorRecord((DoctorRecordDTO) ContentSerializer.deserialize((LinkedTreeMap) message.Content, new DoctorRecordDTO()));
        }
        module.reply(DefaultSystemMessages.CREATEDR.Method, DefaultMessages.OK.Status, message.Id, message.getServerId());
        module.replicate(message);
    }

    @Override
    protected void clean() {
        module.unsubscribe(DefaultSystemMessages.CREATEDR.Method, this);
    }
}
