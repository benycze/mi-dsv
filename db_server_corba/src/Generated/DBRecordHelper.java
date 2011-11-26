package Generated;


/**
* Generated/DBRecordHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /home/pavel/Desktop/Skola/DSV/projects/db_server_corba/src/server.idl
* Saturday, November 26, 2011 10:07:34 PM CET
*/

abstract public class DBRecordHelper
{
  private static String  _id = "IDL:Generated/DBRecord:1.0";

  public static void insert (org.omg.CORBA.Any a, Generated.DBRecord that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Generated.DBRecord extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [5];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "key",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_boolean);
          _members0[1] = new org.omg.CORBA.StructMember (
            "recordLocked",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[2] = new org.omg.CORBA.StructMember (
            "message",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[3] = new org.omg.CORBA.StructMember (
            "tscreate",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[4] = new org.omg.CORBA.StructMember (
            "tsmodify",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (Generated.DBRecordHelper.id (), "DBRecord", _members0);
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

  public static Generated.DBRecord read (org.omg.CORBA.portable.InputStream istream)
  {
    Generated.DBRecord value = new Generated.DBRecord ();
    value.key = istream.read_long ();
    value.recordLocked = istream.read_boolean ();
    value.message = istream.read_string ();
    value.tscreate = istream.read_string ();
    value.tsmodify = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Generated.DBRecord value)
  {
    ostream.write_long (value.key);
    ostream.write_boolean (value.recordLocked);
    ostream.write_string (value.message);
    ostream.write_string (value.tscreate);
    ostream.write_string (value.tsmodify);
  }

}
