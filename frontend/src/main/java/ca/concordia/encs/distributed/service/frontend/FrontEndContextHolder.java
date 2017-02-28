package ca.concordia.encs.distributed.service.frontend;


import ca.concordia.encs.distributed.service.serialization.MessageSerializer;

public class FrontEndContextHolder {
    public static final String MulticastAddress = System.getProperty("frontend.address");
    public static final Integer Port = Integer.parseInt(System.getProperty("frontend.port"));
    public static final Integer Id = 1;
    public static final String LoggingConfigurationFile = "logging-server-config.xml";
}
