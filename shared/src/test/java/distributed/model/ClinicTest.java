package distributed.model;

import ca.concordia.encs.distributed.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClinicTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetLocation() throws Exception {
        Clinic clinic = new Clinic(Location.Montreal);
        assertEquals(Location.Montreal, Location.Montreal);
    }

    @Test
    public void testAddRecord() throws Exception {
        Clinic clinic = new Clinic(Location.Montreal);
        clinic.addRecord("Manfred", "Diaz", "3475 St Urbain", "0996109579", "Dentistry");
        clinic.addRecord("Krysia", "Llull", "Junior", "Active", Calendar.getInstance());
        assertEquals((Integer) 2, (Integer) clinic.countRecords());
    }

    @Test
    public void testFindRecord() throws Exception {
        Clinic clinic = new Clinic(Location.Montreal);
        Record recordAdded = clinic.addRecord("Manfred", "Diaz", "3475 St Urbain", "0996109579", "Dentistry");
        Record record = clinic.findRecord(recordAdded.getId());
        assertNotNull(record);
        assertEquals("DR00001", record.getId());
    }

    @Test
    public void testCountRecords() throws Exception {
        Clinic clinic = new Clinic(Location.Montreal);
        clinic.addRecord("Manfred", "Diaz","3475 St Urbain", "0996109579", "Dentistry");
        clinic.addRecord("Krysia", "Llull", "Junior", "Active", Calendar.getInstance());
        assertEquals((Integer) 2,(Integer) clinic.countRecords());
    }
}