package ca.concordia.encs.distributed.service.services.availability;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

public class HeartbeatService extends Service {
    private static long DEFAULT_HEARTBEAT_TIME = 5 * 1000; // 1/5 the endpoint number
    private Long startTime;

    public HeartbeatService(Module module) {
        super(module);
    }

    @Override
    protected void execute() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void process(IdentifiableMessage message) {

    }

    @Override
    protected boolean validate(IdentifiableMessage message) {
        return false;
    }

    @Override
    protected void clean() {

    }

    @Override
    public Message call() throws Exception {
        execute();
        while(!received.get()) {
            module.replicate(DefaultSystemMessages.ALIVE.Method, ClusterContextHolder.Id);
            Thread.sleep(DEFAULT_HEARTBEAT_TIME);
        }
        return  response;
    }
}
