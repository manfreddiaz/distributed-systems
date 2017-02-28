package ca.concordia.encs.distributed.client;

public class ClientContextHolder {
    public static String LoggingConfigurationFile = "logging-client-config.xml";
    public static String LoggingFileDiscriminatorKey = "userid";
    public static String IPCTechnology = System.getProperty("client.ipc", "rmi");
}
