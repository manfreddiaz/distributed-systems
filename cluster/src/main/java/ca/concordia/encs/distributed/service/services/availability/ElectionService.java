package ca.concordia.encs.distributed.service.services.availability;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Service;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElectionService extends Service {
    private static Logger logger = LoggerFactory.getLogger(HeartbeatEndpoint.class);
    private static long ELECTION_COMPLETION_TIMEOUT = 5 * 1000;
    private long startTime;

    public ElectionService(Module module) {
        super(module);
    }

    @Override
    protected void execute() {
        logger.debug("[ELECTION-SERVICE] Starting election");
        module.subscribe(DefaultSystemMessages.BULLIED.Method, this);
        startTime = System.currentTimeMillis();
        module.request(DefaultSystemMessages.ELECTION.Method, ClusterContextHolder.Priority);
    }
    @Override
    protected void process(IdentifiableMessage message) {
        received.set(true);
        logger.debug("[ELECTION-SERVICE] Bullied by {}", message.getServerId());
    }

    @Override
    protected void loop() throws InterruptedException {
        Thread.sleep(ELECTION_COMPLETION_TIMEOUT);
     }
    @Override
    protected void clean() {
        module.unsubscribe(DefaultSystemMessages.BULLIED.Method, this);

        if(!received.get()) {
            try {
                logger.debug("[ELECTION-SERVICE] None bullied, announcing LEADER");
                module.request(DefaultSystemMessages.LEADER.Method, ClusterContextHolder.Priority);
                logger.debug("[ELECTION-SERVICE] Switching server mode...");
                module.server().activate();
                response = DefaultSystemMessages.LEADER;
                logger.debug("[ELECTION-SERVICE] WON :P");
            } catch (ConfigurationException e) {
                logger.error("[ELECTION-SERVICE] Failed switching");
            }
        }
        else {
            logger.debug("[ELECTION-SERVICE] Bullied, Luck Chief ;-)");
            response = DefaultSystemMessages.BULLIED;
        }
    }
}
