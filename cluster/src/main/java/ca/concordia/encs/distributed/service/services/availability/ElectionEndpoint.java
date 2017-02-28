package ca.concordia.encs.distributed.service.services.availability;

import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

public class ElectionEndpoint extends Service {
    public ElectionEndpoint(Module module) {
        super(module);
    }

    @Override
    protected void execute() {
        module.subscribe(DefaultSystemMessages.ELECTION.Method, this);
        module.subscribe(DefaultSystemMessages.LEADER.Method, this);
    }

    @Override
    protected void process(IdentifiableMessage message) {
        long priority = ((Double)message.Content).longValue();
        if(ClusterContextHolder.Priority < priority) {
            module.reply(DefaultSystemMessages.BULLIED.Method, ClusterContextHolder.Priority, message.Id, message.getServerId());
        }
    }

    @Override
    protected void clean() {
        module.subscribe(DefaultSystemMessages.ELECTION.Method, this);
        module.subscribe(DefaultSystemMessages.LEADER.Method, this);
    }
}
