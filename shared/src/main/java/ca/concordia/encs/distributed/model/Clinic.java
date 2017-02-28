package ca.concordia.encs.distributed.model;

import ca.concordia.encs.distributed.util.GlobalIdentifierGenerator;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

public class Clinic {
	private Location location;
	private GlobalIdentifierGenerator globalIdentifierGenerator;
	private final ConcurrentHashMap<Character, ConcurrentHashMap<String, Record>> records; //EXP: http://crunchify.com/hashmap-vs-concurrenthashmap-vs-synchronizedmap-how-a-hashmap-can-be-synchronized-in-java/

	public Clinic(Location location) {
		super();
		this.location = location;
		this.records = new ConcurrentHashMap<Character, ConcurrentHashMap<String, Record>>();
		this.globalIdentifierGenerator = new GlobalIdentifierGenerator();
	}

	public Location getLocation() {
		return location;
	}
	
	public Record addRecord(String firstName, String lastName, String address, String phone, String specialization) {
		return this.addRecord(new DoctorRecord(this.globalIdentifierGenerator.getNextDoctorId(), firstName, lastName,
				this.location, address, phone, specialization));
	}
	public Record addRecord(String firstName, String lastName, String designation, String status, Calendar statusDate) {
		return this.addRecord(new NurseRecord(this.globalIdentifierGenerator.getNextNurseRecordId(), firstName, lastName,
				this.location, Designation.valueOf(designation), Status.valueOf(status), statusDate));
	}

	private Record addRecord(Record record) {
		if(record == null)
			throw new IllegalArgumentException("record");

		Character firstLetter = record.getFirstName().charAt(0);
		ConcurrentHashMap<String, Record> letterList = records.computeIfAbsent(firstLetter, x -> new ConcurrentHashMap<String, Record>());
		letterList.putIfAbsent(record.getId(), record);

		return record;
	}
	public Record findRecord(String recordId) {
		Record result = null;
		for(ConcurrentHashMap<String, Record> records: this.records.values()) {
			result = records.search(1, (key, value) -> {
				if(key.equals(recordId))
					return value;
				return null;
			});
			if(result != null)
				return result;
		}
		return null;
	}
	
	public Record removeRecord(String recordId) {
		Record result = null;
		for(ConcurrentHashMap<String, Record> records: this.records.values()) {
			result = records.search(1, (key, value) -> {
				if(key.equals(recordId))
					return value;
				return null;
			});
		}
		if(result != null) {
			Character firstLetter = result.getFirstName().charAt(0);
			ConcurrentHashMap<String, Record> letterList = records.computeIfAbsent(firstLetter, x -> new ConcurrentHashMap<String, Record>());
			letterList.remove(result.getId());
		}
		return null;
	}
	
	public Integer countRecords() {
		int recordCount = 0;

		for(ConcurrentHashMap<String, Record> records: this.records.values()) {
			recordCount += records.size();
		}

		return recordCount;
	}
}
