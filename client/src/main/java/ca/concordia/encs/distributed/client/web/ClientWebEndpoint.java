package ca.concordia.encs.distributed.client.web;


import ca.concordia.encs.distributed.client.RemoteServices;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.model.DoctorRecord;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.model.NurseRecord;
import ca.concordia.encs.distributed.serialization.json.JsonSerializer;
import ca.concordia.encs.distributed.web.ClinicServices;
import ca.concordia.encs.distributed.web.WebMessage;
import com.google.gson.reflect.TypeToken;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

public class ClientWebEndpoint implements RemoteServices {
    private static Integer DefaultWebPort = 5900;
    private static final Type HashMapType = new TypeToken<HashMap<String, Integer>>() {}.getType();

    ClinicServices clinicServices;

    public ClientWebEndpoint(Location location) throws ConfigurationException {
        try {
            URL serverUrl = new URL(String.format("http://localhost:%d/clinic/?wsdl", DefaultWebPort + location.index()));
            QName qualifiedName = new QName("http://web.distributed.encs.concordia.ca/", "ClinicServerWebService");
            Service service = Service.create(serverUrl, qualifiedName);
            clinicServices = service.getPort(ClinicServices.class);
        } catch (MalformedURLException e) {
            throw new ConfigurationException();
        }
    }
    @Override
    public Message createDoctorRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        WebMessage webMessage = clinicServices.createDRecord(managerId, firstName, lastName, address, phone, specialization);
        return new Message(webMessage.Method, webMessage.Status, JsonSerializer.fromJSON(webMessage.Content, DoctorRecord.class));
    }

    @Override
    public Message createNurseRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
        WebMessage webMessage = clinicServices.createNRecord(managerId, firstName, lastName, designation, status, statusDate);
        return new Message(webMessage.Method, webMessage.Status, JsonSerializer.fromJSON(webMessage.Content, NurseRecord.class));
    }

    @Override
    public Message getTotalRecords(String managerId, String recordType) {
        WebMessage webMessage = clinicServices.getRecordCounts(managerId, recordType);
        return new Message(webMessage.Method, webMessage.Status, JsonSerializer.fromJSON(webMessage.Content, HashMapType));
    }

    @Override
    public Message editRecordById(String managerId, String recordID, String fieldName, String newValue) {
        WebMessage webMessage = clinicServices.editRecord(managerId, recordID, fieldName, newValue);
        return new Message(webMessage.Method, webMessage.Status, webMessage.Content);
    }

    @Override
    public Message transferRecord(String managerId, String recordId, String remoteClinicServerName) {
        WebMessage webMessage = clinicServices.transferRecord(managerId, recordId, remoteClinicServerName);
        return new Message(webMessage.Method, webMessage.Status, webMessage.Content);
    }
}
