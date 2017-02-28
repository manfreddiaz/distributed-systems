package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.model.Clinic;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.service.dtos.DoctorRecordDTO;
import ca.concordia.encs.distributed.service.modules.ActiveReplicaModule;
import ca.concordia.encs.distributed.service.modules.PassiveReplicaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class HighAvailableServer implements Server {
    private static Logger logger = LoggerFactory.getLogger(HighAvailableServer.class);
    private static final Pattern UserId_Pattern = Pattern.compile("(?<location>[A-Z]{3})(?<id>\\d{4})");
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private ServerState state = ServerState.Passive;
    private Module runningModule;
    private HashMap<Location, Clinic> clinics = new HashMap();

    public HighAvailableServer() {
        for(Location location : Location.values()) {
            clinics.put(location, new Clinic(location));
        }
    }
    @Override
    public void start()  {
        if(!isRunning.get()) {
            try {
                logger.debug("[SERVER] Starting server...");
                passivate();
                isRunning.set(state == ServerState.Passive);
                if(isRunning.get())
                    logger.debug("[SERVER] Server started, State = {}.", state);
            } catch (ConfigurationException e) {
                logger.debug("[SERVER] Start failed, Reason = {}");
            }
        }
    }
    @Override
    public void stop() {
        if(isRunning.get()) {
            logger.debug("[SERVER] Stopping server...");
            runningModule.stop();
            runningModule = null;
            isRunning.set(false);
            logger.debug("[SERVER] Server stopped.");
        }
    }
    @Override
    public Integer id() {
        return ClusterContextHolder.Id;
    }
    @Override
    public void activate() throws ConfigurationException {
        logger.debug("[SERVER] Transitioning to Active, State = {}", state);
        try {
            if(state == ServerState.Passive) {
                runningModule = new ActiveReplicaModule(this, runningModule);
            }
            if(state == ServerState.Active && runningModule == null)
                runningModule = new ActiveReplicaModule(this, new PassiveReplicaModule(this));
            runningModule.start();
            state = ServerState.Active;
            logger.debug("[SERVER] Transition completed, State = {}", state);
        }
        catch (Exception e) {
            state = ServerState.Failed;
            logger.error("[SERVER] Failed transition, State = {}", state);
            logger.error("[SERVER] Failure, Reason = {}", e);
        }

    }
    @Override
    public void passivate() throws ConfigurationException {
        logger.debug("[SERVER] Transitioning to Passive, State = {}", state);
        try {
            if(state == ServerState.Active) {
                Module activeModule = runningModule;
                runningModule = activeModule.passive();
                activeModule.stop();
            }
            if(state == ServerState.Passive && runningModule == null) {
                runningModule = new PassiveReplicaModule(this);
            }
            runningModule.start();
            state = ServerState.Passive;
            logger.debug("[SERVER] Transition completed, State = {}", state);
        }
        catch (Exception e) {
            state = ServerState.Failed;
            logger.error("[SERVER] Failed transition, State = {}", state);
            logger.error("[SERVER] Failure, Reason = {}", e);
        }

    }

    public void createDoctorRecord(DoctorRecordDTO doctorRecordDTO) {

    }
    public long getTotalRecords() {
        return clinics
                .values()
                .stream()
                .mapToInt(clinic -> clinic.countRecords())
                .sum();
    }
}
