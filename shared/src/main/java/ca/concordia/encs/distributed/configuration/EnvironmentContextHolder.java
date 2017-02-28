package ca.concordia.encs.distributed.configuration;

import ca.concordia.encs.distributed.model.Location;


public class EnvironmentContextHolder {
    public static final String INVOCATION_NAMESPACE = "ca.concordia.encs.distributed";

    public static String getDynamicServerName(Location location) {
        return String.format("%s.%s", INVOCATION_NAMESPACE, location.abbr().toLowerCase());
    }
}
