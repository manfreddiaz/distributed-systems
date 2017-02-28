package ca.concordia.encs.distributed.rmi;

import ca.concordia.encs.distributed.service.ClinicServer;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.frontend.FrontEndServer;

import java.rmi.RemoteException;
import java.util.Calendar;

public class ClinicServerRMI extends FrontEndServer implements ClinicServices {

    public ClinicServerRMI() throws ClusterCommunicationException {
        super();
    }

    @Override
    public Message createDRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        return super.createDoctorRecord(managerId, firstName, lastName, address, phone, specialization);
    }

    @Override
    public Message createNRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
        return super.createNurseRecord(managerId, firstName, lastName, designation, status, statusDate);
    }

    @Override
    public Message getRecordCounts(String managerId, String recordType) {
        return super.getTotalRecords(managerId, recordType);
    }

    @Override
    public Message editRecord(String managerId, String recordID, String fieldName, String newValue) {
        return super.editRecordById(managerId, recordID, fieldName, newValue);
    }

    @Override
    public Message transferRecord(String managerId, String recordId, String remoteClinicServerName) throws RemoteException {
        return super.transferRecordById(managerId, recordId, remoteClinicServerName);
    }
}
