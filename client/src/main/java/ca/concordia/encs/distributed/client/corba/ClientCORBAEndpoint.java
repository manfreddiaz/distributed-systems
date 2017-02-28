package ca.concordia.encs.distributed.client.corba;

import ca.concordia.encs.distributed.client.RemoteServices;
import ca.concordia.encs.distributed.client.corba.model.ClinicService;
import ca.concordia.encs.distributed.client.corba.model.ClinicServiceHelper;
import ca.concordia.encs.distributed.configuration.EnvironmentContextHolder;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.model.DoctorRecord;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.model.NurseRecord;
import ca.concordia.encs.distributed.model.Record;
import ca.concordia.encs.distributed.serialization.json.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;

public class ClientCORBAEndpoint implements RemoteServices {
    private ClinicService clinicService;
    private ORB ObjectRequestBroker;
    private NamingContextExt NamingService;
    private static final Type HashMapType = new TypeToken<HashMap<String, Integer>>() {}.getType();

    public ClientCORBAEndpoint(Location location) throws ConfigurationException {
        String[] args = new String[1];
        ObjectRequestBroker = ORB.init(args, System.getProperties());
        try {
            NamingService = NamingContextExtHelper.narrow(ObjectRequestBroker.resolve_initial_references("NameService"));
            NameComponent[] paths = NamingService.to_name("ca.concordia.distributed.frontend");
            clinicService = ClinicServiceHelper.narrow(NamingService.resolve(paths));
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
            throw new ConfigurationException();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
            throw new ConfigurationException();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
            throw new ConfigurationException();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
            throw new ConfigurationException();
        }
    }

    @Override
    public Message createDoctorRecord(String managerId, String firstName, String lastName, String address, String phone, String specialization) {
        ca.concordia.encs.distributed.client.corba.model.Message corbaMessage = clinicService.createDRecord(managerId, firstName, lastName, address, phone, specialization);
        return DefaultMessages.OK;
    }

    @Override
    public Message createNurseRecord(String managerId, String firstName, String lastName, String designation, String status, Calendar statusDate) {
        ca.concordia.encs.distributed.client.corba.model.Message corbaMessage = clinicService.createNRecord(managerId, firstName, lastName, designation, status, (int)statusDate.getTimeInMillis());
        return DefaultMessages.OK;
    }

    @Override
    public Message getTotalRecords(String managerId, String recordType) {
        ca.concordia.encs.distributed.client.corba.model.Message corbaMessage = clinicService.getRecordCounts(managerId,recordType);
        return DefaultMessages.OK;
    }

    @Override
    public Message editRecordById(String managerId, String recordID, String fieldName, String newValue) {
        ca.concordia.encs.distributed.client.corba.model.Message corbaMessage = clinicService.editRecord(managerId, recordID, fieldName, newValue);
        return DefaultMessages.OK;
    }

    @Override
    public Message transferRecord(String managerId, String recordId, String remoteClinicServerName) {
        ca.concordia.encs.distributed.client.corba.model.Message corbaMessage = clinicService.transferRecord(managerId, recordId, remoteClinicServerName);
        return DefaultMessages.OK;
    }
}