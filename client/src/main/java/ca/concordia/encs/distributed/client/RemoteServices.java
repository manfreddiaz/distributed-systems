package ca.concordia.encs.distributed.client;

import ca.concordia.encs.distributed.messaging.Message;

import java.util.Calendar;

public interface RemoteServices {
    Message createDoctorRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization);
    Message createNurseRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate);
    Message getTotalRecords(String managerId, String recordType);
    Message editRecordById(String managerId, String recordID, String fieldName, String newValue);
    Message transferRecord(String managerId, String recordId, String remoteClinicServerName);
}
