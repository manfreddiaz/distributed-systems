package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.model.Location;

public class ClusterContextHolder {
    public static final String ClusterMulticastAddress = System.getProperty("cluster.address");
    public static final String FrontEndMulticastAddress = System.getProperty("frontend.address");
    public static final Long Priority = System.nanoTime();
    public static final Integer ClusterPort = Integer.parseInt(System.getProperty("cluster.port"));
    public static final Integer FrontEndPort = Integer.parseInt(System.getProperty("frontend.port"));
    public static final String LoggingConfigurationFile = "logging-server-config.xml";
    public static final Integer Id = Integer.parseInt(System.getProperty("cluster.id"));
    public static final Location CurrentLocation = Location.Montreal;
}
