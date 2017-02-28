package ca.concordia.encs.distributed.util;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalIdentifierGenerator {
    private AtomicInteger doctorRecordSeed = new AtomicInteger(0);
    private AtomicInteger nurseRecordSeed = new AtomicInteger(0);

    public GlobalIdentifierGenerator() {

    }

    public String getNextDoctorId() {

        return String.format("DR%05d", this.doctorRecordSeed.incrementAndGet());
    }

    public String getNextNurseRecordId() {
        return String.format("NR%05d", this.nurseRecordSeed.incrementAndGet());
    }
}
