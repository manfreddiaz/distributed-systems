package ca.concordia.encs.distributed.service.dtos;

import ca.concordia.encs.distributed.model.NurseRecord;

public class NurseRecordDTO {
    public String id;
    public String managerId;
    public String firstName;
    public String lastName;
    public String designation;
    public String status;
    public String statusDate;

    public NurseRecordDTO(String id, String managerId, String firstName, String lastName, String designation, String status, String statusDate) {
        this.id = id;
        this.managerId = managerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.designation = designation;
        this.status = status;
        this.statusDate = statusDate;
    }
}
