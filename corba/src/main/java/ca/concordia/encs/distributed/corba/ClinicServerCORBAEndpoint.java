package ca.concordia.encs.distributed.corba;

import ca.concordia.encs.distributed.corba.jvm.CorbaJvmManager;
import ca.concordia.encs.distributed.corba.model.ClinicServicePOATie;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;
import ca.concordia.encs.distributed.service.frontend.FrontEndContextHolder;

public class ClinicServerCORBAEndpoint {
    public static void main(String[] args) {
        CorbaJvmManager corbaJvmManager = new CorbaJvmManager();

        try {
            corbaJvmManager.configure(args, null);
            ClinicServicePOATie clinicServicePOATie = new ClinicServicePOATie(new ClinicServerCORBA());
            corbaJvmManager.register(clinicServicePOATie, "ca.concordia.distributed.frontend");
            corbaJvmManager.run();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (ClusterCommunicationException e) {
            e.printStackTrace();
        }
    }
}
