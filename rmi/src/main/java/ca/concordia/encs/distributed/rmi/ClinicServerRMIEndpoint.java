package ca.concordia.encs.distributed.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ca.concordia.encs.distributed.logging.LoggingManager;
import ca.concordia.encs.distributed.configuration.EnvironmentContextHolder;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;
import ca.concordia.encs.distributed.service.frontend.FrontEndContextHolder;

public class ClinicServerRMIEndpoint {
	static Registry registry;
	public static void main(String[] args) {
		String dynamicServerName = "ca.concordia.encs.distributed.FrontEnd";
		LoggingManager.configure(FrontEndContextHolder.LoggingConfigurationFile);

		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ClinicServices clinicServices = new ClinicServerRMI();
			ClinicServices clinicServicesStub = (ClinicServices) UnicastRemoteObject.exportObject(clinicServices, 0);
			registry = LocateRegistry.getRegistry();
			registry.rebind(dynamicServerName, clinicServicesStub);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClusterCommunicationException e) {
			e.printStackTrace();
		}
	}
}
