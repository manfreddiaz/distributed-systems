package ca.concordia.encs.distributed.corba.jvm;

import ca.concordia.encs.distributed.exception.ConfigurationException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.Servant;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class CorbaJvmManager {
    private ORB ObjectRequestBroker;
    private POA PortableObjectAdapter;
    private NamingContextExt NamingService;
    private AtomicBoolean started = new AtomicBoolean(false);

    public CorbaJvmManager() {

    }

    public void configure(String[] args, Properties properties) throws ConfigurationException {
        if(!started.get()) {
            this.ObjectRequestBroker = ORB.init(args, properties);
            try {
                this.PortableObjectAdapter = POAHelper.narrow(ObjectRequestBroker.resolve_initial_references("RootPOA"));
                this.PortableObjectAdapter.the_POAManager().activate();
                this.NamingService = NamingContextExtHelper.narrow(ObjectRequestBroker.resolve_initial_references("NameService"));
                started.compareAndSet(false, true);
            } catch (InvalidName invalidName) {
                invalidName.printStackTrace();
                throw new ConfigurationException();
            } catch (AdapterInactive adapterInactive) {
                adapterInactive.printStackTrace();
                throw new ConfigurationException();
            }
        }
    }
    public void register(Servant servant, String name) throws ConfigurationException {
        try {
            Object object = servant._this_object(ObjectRequestBroker);
            NameComponent[] paths = NamingService.to_name(name);
            NamingService.rebind(paths, object);
        } catch (NotFound notFound) {
            notFound.printStackTrace();
            throw new ConfigurationException();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
            throw new ConfigurationException();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
            throw new ConfigurationException();
        }
    }

    public void run() {
        this.ObjectRequestBroker.run();
    }
}
