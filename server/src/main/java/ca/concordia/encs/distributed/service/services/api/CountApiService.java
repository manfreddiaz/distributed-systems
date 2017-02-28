package ca.concordia.encs.distributed.service.services.api;

import ca.concordia.encs.distributed.service.ClinicServer;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.HighAvailableServer;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

public class CountApiService extends Service {
    public CountApiService(Module module) {
        super(module);
    }

    @Override
    public void execute() {
        module.subscribe(DefaultSystemMessages.COUNT.Method, this);
    }

    @Override
    protected void process(IdentifiableMessage message) {
        ClinicServer server = (ClinicServer) module.server();
        module.reply(DefaultSystemMessages.COUNT.Method, server.getTotalRecords(), message.Id, message.getServerId());
        module.replicate(message);
    }

    @Override
    protected void clean() {
        module.unsubscribe(DefaultSystemMessages.COUNT.Method, this);
    }
}
