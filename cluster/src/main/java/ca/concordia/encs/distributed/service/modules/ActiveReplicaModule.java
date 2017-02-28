package ca.concordia.encs.distributed.service.modules;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.HighAvailableServer;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.communication.multicast.ReliableMulticastComponent;
import ca.concordia.encs.distributed.service.services.availability.HeartbeatService;

public class ActiveReplicaModule extends Module {
    public ActiveReplicaModule(HighAvailableServer server, Module passive) throws ConfigurationException {
        super(server,
              new ReliableMulticastComponent(ClusterContextHolder.FrontEndMulticastAddress,
                                                ClusterContextHolder.FrontEndPort,
                                                    ClusterContextHolder.Id),
                passive
        );
    }

    @Override
    protected void configure() {
        services.add(new HeartbeatService(this));
    }
}
