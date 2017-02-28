package ca.concordia.encs.distributed.service.dtos;

public class DoctorRecordDTO {
    public String userId;
    public String id;
    public String firstName;
    public String lastName;
    public String location;
    public String address;
    public String phone;
    public String specialization;

    public DoctorRecordDTO() {

    }
    public DoctorRecordDTO(String userId, String id, String firstName, String lastName, String address, String phone, String specialization) {
        this.userId = userId;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
    }
}
