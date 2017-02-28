package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.exception.AccessViolationException;
import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.exception.InvalidOperationException;
import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.messaging.MessageStatus;
import ca.concordia.encs.distributed.model.*;

import ca.concordia.encs.distributed.service.dtos.DoctorRecordDTO;
import ca.concordia.encs.distributed.service.dtos.NurseRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Future;

public class ClinicServer extends HighAvailableServer {
    private static Logger logger = LoggerFactory.getLogger(ClinicServer.class);
    private static Integer DEFAULT_RESPONSE_TIMEOUT = 5 * 1000; //Response Timeout
    private Clinic clinic;


    public ClinicServer()  {
        this.clinic = new Clinic(Location.Montreal);
    }

    public Message createDoctorRecord(DoctorRecordDTO doctorRecord) {
        Record record = clinic.addRecord(doctorRecord.firstName, doctorRecord.lastName, doctorRecord.address,
                                            doctorRecord.phone, doctorRecord.specialization);
        return
    }

    public Message createNurseRecord(NurseRecordDTO nurseRecord) {
        Calendar statusDate = Calendar.getInstance();
        statusDate.setTimeInMillis(Integer.valueOf(nurseRecord.statusDate));

        Record record = clinic.addRecord(nurseRecord.firstName, nurseRecord.lastName, nurseRecord.designation,
                                            nurseRecord.status, statusDate);

        return new Message("OK", MessageStatus.Ok.code(), record);
    }

    public Message getTotalRecords(String managerId, String recordType) {
        /*logger.debug("User:{}, Invoking getRecordCounts with: {}", managerId, recordType);

        HashMap<String, Integer> response = new HashMap<String, Integer>();
        response.put(ServerContextHolder.CurrentLocation.abbr(), clinic.countRecords());

        CountApiService countService = new CountApiService(highAvailableServer);

        try {
            Future<Message> futureMessage = server.execute(countService);
            futureMessage.get(DEFAULT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            logger.debug("Exception while processing getCountRecords", e);
            return ExceptionManager.handleAndTranslate(e);
        }
        catch (ExecutionException e) {
            logger.debug("Exception while processing getCountRecords", e);
            return ExceptionManager.handleAndTranslate(e);
        }
        catch (TimeoutException e) {

        }
        finally {
            GroupResponseMessage groupResponseMessage = (GroupResponseMessage) countService.getResponse();
            for(IdentifiableMessage message : groupResponseMessage.getIdentifiedResponses()) {
                response.put(message.getServerLocation().abbr(), ((Double)message.Content).intValue());
            }
        }
        logger.debug("Responding getRecords with: ", MessageStatus.Ok);*/
        return new Message("OK", MessageStatus.Ok.code(), 200);
    }

    public Message editRecordById(String managerId, String recordID, String fieldName, String newValue) {
        logger.debug("User: {}, Invoking editRecord with: {}, {}, {}", managerId, recordID, fieldName, newValue);
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
        }
    }

    public Message transferRecordById(String managerId, String recordId, String remoteClinicServerName) {
        logger.debug("User: {}, Invoking transferRecord with: {}, {}", managerId, recordId, remoteClinicServerName);

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

        logger.debug("Responding getRecords with: ", MessageStatus.Ok);
        return new Message("OK", MessageStatus.Ok.code(), 0);
    }

    public Location getLocation() {
        return clinic.getLocation();
    }
}
