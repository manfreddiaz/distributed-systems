package ca.concordia.encs.distributed.service.services.availability;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class HeartbeatEndpoint extends Service {
    private static Logger logger = LoggerFactory.getLogger(HeartbeatEndpoint.class);
    private static long DEFAULT_HEARTBEAT_TIME = 10 * 1000;

    private Long startTime;

    public HeartbeatEndpoint(Module module) {
        super(module);
    }

    @Override
    protected void execute() {
        module.subscribe(DefaultSystemMessages.ALIVE.Method, this);
        module.subscribe(DefaultSystemMessages.LEADER.Method, this);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    protected void process(IdentifiableMessage message) {
        logger.debug("[HEARTBEAT-DETECTOR] Leader is alive.");
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void clean() {
        module.unsubscribe(DefaultSystemMessages.ALIVE.Method, this);
        module.subscribe(DefaultSystemMessages.LEADER.Method, this);
    }

    @Override
    protected boolean validate(IdentifiableMessage message) {
        return message.Method.equalsIgnoreCase(DefaultSystemMessages.ALIVE.Method) ? true : super.validate(message);
    }

    @Override
    public Message call() throws Exception {
       execute();
       long lastHeardOfLeader;
       while(!received.get()) {
           lastHeardOfLeader = System.currentTimeMillis() - startTime;
           if(lastHeardOfLeader > DEFAULT_HEARTBEAT_TIME) {
               logger.debug("[HEARTBEAT-DETECTOR] Leader may be dead, last heard: {} ms", lastHeardOfLeader);
               logger.debug("[HEARTBEAT-DETECTOR] Starting election...");
               ElectionService electionService = new ElectionService(module);
               Future<Message> election = module.execute(electionService);
               Message response = election.get();
               logger.debug("[HEARTBEAT-DETECTOR] I ended up being, {}", response.Method);
               startTime = System.currentTimeMillis(); //Reset the counter;
               logger.debug("[HEARTBEAT-DETECTOR] Timer refresh.");
           }
           Thread.sleep(DEFAULT_HEARTBEAT_TIME);
       }
       return response;
    }


}
