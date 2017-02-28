package ca.concordia.encs.distributed.client.corba.model;


/**
* ca/concordia/encs/distributed/client/corba/model/MessageHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicServices.idl
* Monday, July 4, 2016 5:06:05 PM CDT
*/

abstract public class MessageHelper
{
  private static String  _id = "IDL:interop/Message:1.0";

  public static void insert (org.omg.CORBA.Any a, ca.concordia.encs.distributed.client.corba.model.Message that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static ca.concordia.encs.distributed.client.corba.model.Message extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "Method",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[1] = new org.omg.CORBA.StructMember (
            "Status",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[2] = new org.omg.CORBA.StructMember (
            "Content",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (ca.concordia.encs.distributed.client.corba.model.MessageHelper.id (), "Message", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static ca.concordia.encs.distributed.client.corba.model.Message read (org.omg.CORBA.portable.InputStream istream)
  {
    ca.concordia.encs.distributed.client.corba.model.Message value = new ca.concordia.encs.distributed.client.corba.model.Message ();
    value.Method = istream.read_string ();
    value.Status = istream.read_short ();
    value.Content = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, ca.concordia.encs.distributed.client.corba.model.Message value)
  {
    ostream.write_string (value.Method);
    ostream.write_short (value.Status);
    ostream.write_string (value.Content);
  }

}
