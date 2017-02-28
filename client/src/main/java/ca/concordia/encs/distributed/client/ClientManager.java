package ca.concordia.encs.distributed.client;

import ca.concordia.encs.distributed.client.corba.ClientCORBAEndpoint;
import ca.concordia.encs.distributed.client.rmi.ClientRMIEndpoint;
import ca.concordia.encs.distributed.client.web.ClientWebEndpoint;
import ca.concordia.encs.distributed.logging.LoggingManager;
import ca.concordia.encs.distributed.exception.AccessViolationException;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.security.Roles;
import ca.concordia.encs.distributed.security.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {

    private static final Pattern UserId_Pattern = Pattern.compile("(?<location>[A-Z]{3})(?<id>\\d{4})");
    private ConcurrentHashMap<Location, RemoteServices> remoteServers = new ConcurrentHashMap<Location, RemoteServices>();

    public ClientManager() throws ConfigurationException {
        LoggingManager.configure(ClientContextHolder.LoggingConfigurationFile);
    }

    public Session authenticate(String userId) throws  AccessViolationException, ConfigurationException {
        if(userId == null)
            throw new AccessViolationException();

        if(!userId.matches(UserId_Pattern.pattern())) {
            throw new AccessViolationException();
        }

        Matcher userIdMatcher = UserId_Pattern.matcher(userId);
        if(!userIdMatcher.find()) {
            throw new AccessViolationException();
        }

        String locationAbbr = userIdMatcher.group("location");
        String id = userIdMatcher.group("id");

        Location location = null;
        try {
            location = Location.fromAbbreviation(locationAbbr.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new AccessViolationException();
        }

        User user = authorize(id, location);

        if(user == null) {
            throw new AccessViolationException();
        }

        return createSession(user, location);
    }

    private Session createSession(User user, Location location) throws ConfigurationException {
        return new Session(user, resolveServerForLocation(location));
    }

    private RemoteServices resolveServerForLocation(Location location) throws ConfigurationException {
        return remoteServers.computeIfAbsent(location, x -> {
            RemoteServices ipcTechnology = null;

            switch (ClientContextHolder.IPCTechnology) {
                case "rmi":
                    try {
                        ipcTechnology = new ClientRMIEndpoint(location); //TODO: Provide one instance per location
                    } catch (ConfigurationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "corba":
                    try {
                        ipcTechnology = new ClientCORBAEndpoint(location);
                    } catch (ConfigurationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "web":
                    try {
                        ipcTechnology = new ClientWebEndpoint(location);
                    }
                    catch (ConfigurationException e) {
                        e.printStackTrace();
                    }
            }
            return ipcTechnology;
        });
    }

    private User authorize(String id, Location location) {
        return new User(id, location, Roles.ClinicManager);
    }
}
