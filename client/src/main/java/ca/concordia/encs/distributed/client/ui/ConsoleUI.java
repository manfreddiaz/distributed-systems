package ca.concordia.encs.distributed.client.ui;

import ca.concordia.encs.distributed.client.ClientManager;
import ca.concordia.encs.distributed.client.Session;
import ca.concordia.encs.distributed.exception.AccessViolationException;
import ca.concordia.encs.distributed.exception.ConfigurationException;
import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.messaging.MessageStatus;
import ca.concordia.encs.distributed.model.Location;
import ca.concordia.encs.distributed.model.Record;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleUI {
    public static ClientManager clientManager;
    public static Random randomNumberGenerator = new Random();
    public static List<Location> locations = new ArrayList<Location>();
    public static ExecutorService threadPool = Executors.newFixedThreadPool(16);
    public static Integer NUMBER_OF_THREADS = 50;
    public static ConcurrentHashMap<Location, ConcurrentHashMap<String, Record>> records = new ConcurrentHashMap<Location, ConcurrentHashMap<String, Record>>();
    public static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws AccessViolationException {
        try {
            Integer threadsArgument = null;
            if(args.length > 0)
                threadsArgument = Integer.parseInt(args[0]);
            clientManager = new ClientManager();
            for(Location location: Location.values()) { locations.add(location); }
            for(int i = 0; i < (threadsArgument != null ? threadsArgument : NUMBER_OF_THREADS) ; i++) {
                generateRandomRequests();
            }
        }
        catch (ConfigurationException e) {
            System.out.print("There is a problem with the configuration");
            e.printStackTrace();
        }
    }
    private static void generateRandomRequests() {
        int locationIndex = randomNumberGenerator.nextInt(3);
        int userIdNumber = randomNumberGenerator.nextInt(9999);
        String userId = String.format("%s%04d", locations.get(locationIndex).abbr().toUpperCase(), userIdNumber);

        Runnable creationalRequests = new Runnable() {
            @Override
            public void run() {
                performCreationalOperations(userId);
            }
        };
        Runnable editionRequests = new Runnable() {
            @Override
            public void run() {
                performEditionOperations(userId);
            }
        };
        Runnable transferRequests = new Runnable() {
            @Override
            public void run() {
                performTransferOperations(userId);
            }
        };

        threadPool.execute(creationalRequests);
//        threadPool.execute(editionRequests);
//        threadPool.execute(transferRequests);
    }
    private static void performTransferOperations(String userId) {
        try {
            Session session = clientManager.authenticate(userId);
            ConcurrentHashMap<String, Record> locationRecords = records.get(session.getUser().getLocation());
            if(locationRecords != null) {
                for(String recordId : locationRecords.keySet()) {
                    session.transferRecord(recordId, locations.get(randomNumberGenerator.nextInt(3)).name());
                }
            }
        } catch (AccessViolationException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    private static void performEditionOperations(String userId) {
        try {
            Session session = clientManager.authenticate(userId);
            ConcurrentHashMap<String, Record> locationRecords = records.get(session.getUser().getLocation());
            if(locationRecords != null) {
                for(String recordId : locationRecords.keySet()) {
                    session.editRecordById(recordId, "firstName", "XOXO");
                }
            }
        } catch (AccessViolationException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    private static void performCreationalOperations(String userId) {
        try {
            Session session = clientManager.authenticate(userId);
            int numberOfRecord = randomNumberGenerator.nextInt(16);
            ConcurrentHashMap<String, Record> locationRecords = records.computeIfAbsent(session.getUser().getLocation(), x ->
                new ConcurrentHashMap<String, Record>()
            );
            Message message;
            for(int i = 0; i < numberOfRecord; i++) {
                message = session.createDoctorRecord(RandomStringUtils.randomAlphanumeric(8),
                        RandomStringUtils.randomAlphanumeric(8),
                        RandomStringUtils.randomAlphanumeric(20),
                        RandomStringUtils.randomNumeric(11),
                        RandomStringUtils.randomAscii(8));
                if(message.Status == MessageStatus.Ok.code()) {
                    //locationRecords.put(((Record) message.Content).getId(), (Record) message.Content);
                    count.incrementAndGet();
                }
            }
            for(int i = 0; i < numberOfRecord; i++) {
                message = session.createNurseRecord(RandomStringUtils.randomAlphanumeric(8),
                        RandomStringUtils.randomAlphanumeric(8),
                        i % 2 == 0 ? "Junior" : "Senior",
                        i % 2 == 0 ? "Active" : "Terminated",
                        Calendar.getInstance());
                if(message.Status == MessageStatus.Ok.code()) {
                    //locationRecords.put(((Record) message.Content).getId(), (Record) message.Content);
                    count.incrementAndGet();
                }
            }
            Message recordCountMessage = session.getTotalRecords("");
            /*if(recordCountMessage.Status == MessageStatus.Ok.code()) {
                HashMap<String, Integer> allRecordsCount = (HashMap<String, Integer>) recordCountMessage.Content;
                System.out.print("All Servers Recount: ");
                for(String key : allRecordsCount.keySet()) {
                    System.out.printf("%s %d", key, allRecordsCount.get(key));
                }
                System.out.println();
            }*/
        } catch (AccessViolationException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
