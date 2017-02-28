package distributed.model;

import ca.concordia.encs.distributed.model.DoctorRecord;
import ca.concordia.encs.distributed.model.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoctorRecordTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAddress() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        assertEquals(doctorRecord.getAddress(), "3475 St Urbain");
    }

    @Test
    public void testSetAddress() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        doctorRecord.setAddress("315 3475 St Urbain, H2X2N4");
        assertEquals(doctorRecord.getAddress(), "315 3475 St Urbain, H2X2N4");
    }

    @Test
    public void testGetPhone() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        assertEquals(doctorRecord.getPhone(), "5142488457");
    }

    @Test
    public void testSetPhone() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        doctorRecord.setPhone("5142484447");
        assertEquals(doctorRecord.getPhone(), "5142484447");
    }

    @Test
    public void testGetSpecialization() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        assertEquals(doctorRecord.getSpecialization(), "Surgeon");
    }

    @Test
    public void testSetSpecialization() throws Exception {
        DoctorRecord doctorRecord = new DoctorRecord("DR00001", "Manfred", "Diaz",
                Location.Montreal, "3475 St Urbain", "5142488457", "Surgeon");
        doctorRecord.setSpecialization("Dentist");
        assertEquals(doctorRecord.getSpecialization(), "Dentist");
    }
}