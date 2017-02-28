package ca.concordia.encs.distributed.service.frontend;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.Server;
import ca.concordia.encs.distributed.service.frontend.modules.FrontEndModule;
import ca.concordia.encs.distributed.service.frontend.services.CountService;
import ca.concordia.encs.distributed.service.frontend.services.CreateRecordDRService;
import ca.concordia.encs.distributed.service.dtos.DoctorRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FrontEndServer implements Server {
    private static Logger logger = LoggerFactory.getLogger(FrontEndServer.class);
    private FrontEndModule module;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private static final long RESPONSE_TIMEOUT = 60 * 1000; //30 seconds, like Web

    public FrontEndServer() {

    }

    @Override
    public void start() {
        if(!isRunning.get()) {
            logger.debug("[FRONTEND] Starting server...");
            try {
                this.module = new FrontEndModule(this);
                this.module.start();
                this.isRunning.set(true);
                logger.debug("[FRONTEND] Server started.");
            } catch (ConfigurationException e) {
                logger.error("[FRONTEND] Server failed, Reason = {}", e);
            }
        }
    }

    @Override
    public void stop() {
        if(isRunning.get()) {
            logger.debug("[FRONTEND] Stopping server...");
            this.module.stop();
            this.isRunning.set(false);
            logger.debug("[FRONTEND] Server stopped.");
        }
    }

    @Override
    public Integer id() {
        return FrontEndContextHolder.Id;
    }

    @Override
    public void activate() throws ConfigurationException {

    }

    @Override
    public void passivate() throws ConfigurationException {

    }
    public Message createDoctorRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        logger.debug("User:{}, Invoking createDRecord with: {}, {}, {}, {}, {}", managerId, firstName, lastName, address, phone, specialization);

        DoctorRecordDTO record = new DoctorRecordDTO(managerId, null, firstName, lastName, address, phone, specialization);

        try {
            CreateRecordDRService createRecordDRService = new CreateRecordDRService(module, record);
            Future<Message> promise = module.execute(createRecordDRService);
            promise.get(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
            return DefaultMessages.OK;
        } catch (InterruptedException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        } catch (ExecutionException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        } catch (TimeoutException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        }
    }

    public Message createNurseRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
       /* logger.debug("User:{}, Invoking createNRecord with: {}, {}, {}, {}, {}", managerId, firstName, lastName, designation, status, statusDate);
        Record nurseRecord;
        try {
            nurseRecord = clinic.addRecord(firstName, lastName, designation, status, statusDate);
        }
        catch (IllegalArgumentException e) {
            return ExceptionManager.handleAndTranslate(new InvalidOperationException());
        }
        catch (NullPointerException e) {
            return ExceptionManager.handleAndTranslate(new InvalidOperationException());
        }

        logger.debug("Responding with createDRecord with: ", MessageStatus.Ok);
        return new Message("OK", MessageStatus.Ok.code(), nurseRecord);*/
        return DefaultMessages.SERVER_ERROR;
    }

    public Message getTotalRecords(String managerId, String recordType) {
        logger.debug("User:{}, Invoking getRecordCounts with: {}", managerId, recordType);

        try {
            CountService countService = new CountService(module, 0);
            Future<Message> promise = module.execute(countService);
            Message response = promise.get(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
            return response;
        } catch (InterruptedException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        } catch (ExecutionException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        } catch (TimeoutException e) {
            logger.error("[FRONTEND] Error invoking CountService, Reason = {}", e);
            return ExceptionManager.handleAndTranslate(e);
        }
    }

    public Message editRecordById(String managerId, String recordID, String fieldName, String newValue) {
        /*logger.debug("User: {}, Invoking editRecord with: {}, {}, {}", managerId, recordID, fieldName, newValue);
        Record record = this.clinic.findRecord(recordID);

        if(record == null) {
            logger.debug("Record with id: {}, not found", recordID);
            return DefaultMessages.NOTFOUND;
        }
        try {
            record.edit(fieldName, newValue);
            logger.debug("Responding editRecord with: ", MessageStatus.Ok);
            return DefaultMessages.OK;
        }
        catch (InvalidOperationException e) {
            logger.debug("Responding editRecord with: ", MessageStatus.BadRequest);
            return ExceptionManager.handleAndTranslate(e);
        }
        catch (AccessViolationException e) {
            logger.debug("Responding editRecord with: ", MessageStatus.Forbidden);
            return ExceptionManager.handleAndTranslate(e);
        }*/

        return DefaultMessages.OK;
    }

    public Message transferRecordById(String managerId, String recordId, String remoteClinicServerName) {
        /*logger.debug("User: {}, Invoking transferRecord with: {}, {}", managerId, recordId, remoteClinicServerName);

        Record record = this.clinic.findRecord(recordId);
        if(record == null) {
            logger.debug("Record with id: {}, not found", recordId);
            return DefaultMessages.NOTFOUND;
        }
        try {
            Message message = new Message();//highAvailableServer.//sendTransferRequest(record, Location.valueOf(remoteClinicServerName));
            if(message.Status == MessageStatus.Ok.code()) {
                record.setLocation(Location.valueOf(remoteClinicServerName));
                this.clinic.removeRecord(recordId);
            }
        } catch (Exception e) {
            logger.debug("Exception while processing getCountRecords", e);
            return ExceptionManager.handleAndTranslate(e);
        }

        logger.debug("Responding getRecords with: ", MessageStatus.Ok);*/
        return DefaultMessages.SERVER_ERROR;
    }


}
