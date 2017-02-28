package ca.concordia.encs.distributed.client.rmi;

import ca.concordia.encs.distributed.client.RemoteServices;
import ca.concordia.encs.distributed.configuration.EnvironmentContextHolder;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.exception.ExceptionManager;
import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.rmi.ClinicServices;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;

public class ClientRMIEndpoint implements RemoteServices {
    private Registry registry;
    private ClinicServices clinicServices;

    public ClientRMIEndpoint(Location location) throws ConfigurationException {
        try {
            this.registry = LocateRegistry.getRegistry();
            this.clinicServices = (ClinicServices) registry.lookup(EnvironmentContextHolder.getDynamicServerName(location));
        }
        catch (RemoteException e) {
            ExceptionManager.handle(e);
            throw new ConfigurationException();
        } catch (NotBoundException e) {
            ExceptionManager.handle(e);
            throw new ConfigurationException();
        }
    }
    @Override
    public Message createDoctorRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        Message message = DefaultMessages.SERVER_ERROR;
        try {
            message = this.clinicServices.createDRecord(managerId, firstName, lastName, address, phone, specialization);
        } catch (RemoteException e) {
            ExceptionManager.handle(e);
        }
        return message;
    }

    @Override
    public Message createNurseRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
        Message message = DefaultMessages.SERVER_ERROR;
        try {
            message = this.clinicServices.createNRecord(managerId, firstName, lastName, designation, status, statusDate);
        } catch (RemoteException e) {
            ExceptionManager.handle(e);
        }
        return message;
    }

    @Override
    public Message getTotalRecords(String managerId, String recordType) {
        Message message = DefaultMessages.SERVER_ERROR;
        try {
            message = this.clinicServices.getRecordCounts(managerId, recordType);
        } catch (RemoteException e) {
            ExceptionManager.handle(e);
        }
        return message;
    }

    @Override
    public Message editRecordById(String managerId, String recordID, String fieldName, String newValue) {
        Message message = DefaultMessages.SERVER_ERROR;
        try {
            message = this.clinicServices.editRecord(managerId, recordID, fieldName, newValue);
        } catch (RemoteException e) {
            ExceptionManager.handle(e);
        }
        return message;
    }

    @Override
    public Message transferRecord(String managerId, String recordId, String remoteClinicServerName) {
        Message message = DefaultMessages.SERVER_ERROR;
        try {
            message = this.clinicServices.transferRecord(managerId, recordId, remoteClinicServerName);
        } catch (RemoteException e) {
            ExceptionManager.handle(e);
        }
        return message;
    }
}
