package ca.concordia.encs.distributed.web;

import ca.concordia.encs.distributed.messaging.Message;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Calendar;

@WebService
public interface ClinicServices {
    @WebMethod
    WebMessage createDRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization);
    @WebMethod
    WebMessage createNRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate);
    @WebMethod
    WebMessage getRecordCounts(String managerId, String recordType);
    @WebMethod
    WebMessage editRecord(String managerId, String recordID, String fieldName, String newValue);
    @WebMethod
    WebMessage transferRecord(String managerId, String recordId, String remoteClinicServerName);
}
