package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;

public class PassiveReplicatedSchema {
    public static void main(String[] args) throws ClusterCommunicationException, ConfigurationException {
        HighAvailableServer highAvailableServer = new HighAvailableServer();
        highAvailableServer.start();
    }
}
