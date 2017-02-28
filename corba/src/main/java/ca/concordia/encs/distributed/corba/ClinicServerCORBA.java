package ca.concordia.encs.distributed.corba;

import ca.concordia.encs.distributed.corba.model.*;
import ca.concordia.encs.distributed.corba.model.Message;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;
import ca.concordia.encs.distributed.service.frontend.FrontEndServer;

public class ClinicServerCORBA extends FrontEndServer implements ClinicServiceOperations {

    public ClinicServerCORBA() throws ClusterCommunicationException {
        super();
        start();
    }
    @Override
    public Message createDRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization) {
        return ModelTranslator.fromModelMessage(super.createDoctorRecord(managerID, firstName, lastName, address, phone, specialization));
    }

    @Override
    public Message createNRecord(String managerID, String firstName, String lastName, String designation, String status, int statusDate) {
        return ModelTranslator.fromModelMessage(super.createNurseRecord(managerID, firstName, lastName, designation, status, ModelTranslator.fromMilliseconds(statusDate)));
    }

    @Override
    public Message getRecordCounts(String managerID, String recordType) {
        return ModelTranslator.fromModelMessage(super.getTotalRecords(managerID, recordType));
    }

    @Override
    public Message editRecord(String managerID, String recordID, String fieldName, String newValue) {
        return ModelTranslator.fromModelMessage(super.editRecordById(managerID, recordID, fieldName, newValue));
    }

    @Override
    public Message transferRecord(String managerID, String recordID, String remoteClinicServerName) {
        return ModelTranslator.fromModelMessage(super.transferRecordById(managerID, recordID, remoteClinicServerName));
    }
}
