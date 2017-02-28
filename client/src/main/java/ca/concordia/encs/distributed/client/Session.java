package ca.concordia.encs.distributed.client;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Calendar;
import java.util.Date;

public class Session {
    private static Logger logger = LoggerFactory.getLogger(Session.class);

    private User user;
    private RemoteServices endpoint;
    private Date date = new Date();

    public Session(User user, RemoteServices endpoint) {
        this.user = user;
        this.endpoint = endpoint;
    }

    public Message createDoctorRecord(String firstName, String lastName, String address, String phone, String specialization) {
        MDC.put(ClientContextHolder.LoggingFileDiscriminatorKey, user.toString());
        logger.info("Invoking createDRecord: {}, {}, {}, {}, {}", firstName, lastName, address, phone, specialization);

        Message message = endpoint.createDoctorRecord(user.getId(), firstName, lastName, address, phone, specialization);

        logger.info("Response from server: {}, {}", message.Method, message.Status);
        MDC.remove(ClientContextHolder.LoggingFileDiscriminatorKey);

        return message;
    }

    public Message createNurseRecord(String firstName, String lastName, String designation, String status, Calendar statusDate) {
        MDC.put(ClientContextHolder.LoggingFileDiscriminatorKey, user.toString());
        logger.info("Invoking createNRecord: {}, {}, {}, {}, {}", firstName, lastName, designation, status, statusDate);

        Message message = endpoint.createNurseRecord(user.getId(), firstName, lastName, designation, status, statusDate);

        logger.info("Response from server: {}, {}", message.Method, message.Status);
        MDC.remove(ClientContextHolder.LoggingFileDiscriminatorKey);

        return message;
    }

    public Message getTotalRecords(String recordType) {
        MDC.put(ClientContextHolder.LoggingFileDiscriminatorKey, user.toString());
        logger.info("Invoking getRecordCounts: {}", recordType);

        Message message = endpoint.getTotalRecords(user.getId(), recordType);

        logger.info("Response from server: {}, {}", message.Method, message.Status);
        MDC.remove(ClientContextHolder.LoggingFileDiscriminatorKey);

        return message;
    }

    public Message editRecordById(String recordID, String fieldName, String newValue) {
        MDC.put(ClientContextHolder.LoggingFileDiscriminatorKey, user.toString());
        logger.info("Invoking editRecord: {}, {}, {}", recordID, fieldName, newValue);

        Message message = endpoint.editRecordById(user.getId(), recordID, fieldName, newValue);

        logger.info("Response from server: {}, {}", message.Method, message.Status);
        MDC.remove(ClientContextHolder.LoggingFileDiscriminatorKey);

        return message;
    }

    public Message transferRecord(String recordId, String location) {
        MDC.put(ClientContextHolder.LoggingFileDiscriminatorKey, user.toString());
        logger.info("Invoking transferRecord: {}, {}", recordId, location);

        Message message = endpoint.transferRecord(user.getId(), recordId, location);

        logger.info("Response from server: {}, {}", message.Method, message.Status);
        MDC.remove(ClientContextHolder.LoggingFileDiscriminatorKey);

        return message;
    }

    public User getUser() {
        return this.user;
    }
}
