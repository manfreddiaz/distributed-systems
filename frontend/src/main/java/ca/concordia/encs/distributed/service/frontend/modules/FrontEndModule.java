package ca.concordia.encs.distributed.service.frontend.modules;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.Module;
import ca.concordia.encs.distributed.service.Server;
import ca.concordia.encs.distributed.service.communication.multicast.ReliableMulticastComponent;
import ca.concordia.encs.distributed.service.frontend.FrontEndContextHolder;

public class FrontEndModule extends Module {
    public FrontEndModule(Server server) throws ConfigurationException {
        super(server,
                new ReliableMulticastComponent(FrontEndContextHolder.MulticastAddress,
                                                FrontEndContextHolder.Port,
                                                    FrontEndContextHolder.Id),
                    null);
    }

    @Override
    protected void configure() {

    }
}
