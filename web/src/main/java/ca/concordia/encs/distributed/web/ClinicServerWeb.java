package ca.concordia.encs.distributed.web;

import ca.concordia.encs.distributed.service.ClinicServer;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;

import javax.jws.WebService;
import java.util.Calendar;

@WebService(endpointInterface = "ca.concordia.encs.distributed.web.ClinicServices")
public class ClinicServerWeb extends ClinicServer implements ClinicServices {
    public ClinicServerWeb() throws ClusterCommunicationException {
        super();
    }

    @Override
    public WebMessage createDRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        return ModelTranslator.fromModel(super.createDoctorRecord(managerId, firstName, lastName, address, phone, specialization));
    }

    @Override
    public WebMessage createNRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
        return ModelTranslator.fromModel(super.createNurseRecord(managerId, firstName, lastName, designation, status, statusDate));
    }

    @Override
    public WebMessage getRecordCounts(String managerId, String recordType) {
        return ModelTranslator.fromModel(super.getTotalRecords(managerId, recordType));
    }

    @Override
    public WebMessage editRecord(String managerId, String recordID, String fieldName, String newValue) {
        return ModelTranslator.fromModel(super.editRecordById(managerId, recordID, fieldName, newValue));
    }

    @Override
    public WebMessage transferRecord(String managerId, String recordId, String remoteClinicServerName) {
        return ModelTranslator.fromModel(super.transferRecordById(managerId, recordId, remoteClinicServerName));
    }
}
