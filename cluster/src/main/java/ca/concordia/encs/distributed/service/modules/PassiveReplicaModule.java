package ca.concordia.encs.distributed.service.modules;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.HighAvailableServer;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.services.api.CreateDoctorApiService;
import ca.concordia.encs.distributed.service.communication.multicast.ReliableMulticastComponent;
import ca.concordia.encs.distributed.service.services.api.CountApiService;
import ca.concordia.encs.distributed.service.services.availability.ElectionEndpoint;
import ca.concordia.encs.distributed.service.services.availability.HeartbeatEndpoint;

public class PassiveReplicaModule extends Module {
    public PassiveReplicaModule(HighAvailableServer server) throws ConfigurationException {
        super(server,
              new ReliableMulticastComponent(ClusterContextHolder.ClusterMulticastAddress,
                                                ClusterContextHolder.ClusterPort,
                                                    ClusterContextHolder.Id),
                null
        );
    }

    @Override
    protected void configure() {
        services.add(new CountApiService(this));
        services.add(new CreateDoctorApiService(this));
        services.add(new HeartbeatEndpoint(this));
        services.add(new ElectionEndpoint(this));
    }
}
