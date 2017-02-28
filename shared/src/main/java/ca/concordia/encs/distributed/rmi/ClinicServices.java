package ca.concordia.encs.distributed.rmi;

import ca.concordia.encs.distributed.messaging.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface ClinicServices extends Remote {
    Message createDRecord (String managerId, String firstName, String lastName, String address, String phone,
    		String specialization) throws RemoteException;
    Message createNRecord (String managerId, String firstName, String lastName, String designation, String status,
    		Calendar statusDate) throws RemoteException;
    Message getRecordCounts (String managerId, String recordType) throws RemoteException;
    Message editRecord (String managerId, String recordID, String fieldName, String newValue) throws RemoteException;
    Message transferRecord(String managerId, String recordId, String remoteClinicServerName) throws RemoteException;
}