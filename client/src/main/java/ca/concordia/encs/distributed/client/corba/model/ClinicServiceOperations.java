package ca.concordia.encs.distributed.client.corba.model;


/**
* ca/concordia/encs/distributed/client/corba/model/ClinicServiceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicServices.idl
* Monday, July 4, 2016 5:06:05 PM CDT
*/

public interface ClinicServiceOperations 
{
  ca.concordia.encs.distributed.client.corba.model.Message createDRecord (String managerID, String firstName, String lastName, String address, String phone, String specialization);
  ca.concordia.encs.distributed.client.corba.model.Message createNRecord (String managerID, String firstName, String lastName, String designation, String status, int statusDate);
  ca.concordia.encs.distributed.client.corba.model.Message getRecordCounts (String managerID, String recordType);
  ca.concordia.encs.distributed.client.corba.model.Message editRecord (String managerID, String recordID, String fieldName, String newValue);
  ca.concordia.encs.distributed.client.corba.model.Message transferRecord (String managerID, String recordID, String remoteClinicServerName);
} // interface ClinicServiceOperations
