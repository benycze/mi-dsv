package Generated;

/**
* Generated/DuplicateKeyExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /home/pavel/Desktop/Skola/DSV/projects/db_server_corba/src/server.idl
* Saturday, November 26, 2011 10:07:34 PM CET
*/

public final class DuplicateKeyExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public Generated.DuplicateKeyException value = null;

  public DuplicateKeyExceptionHolder ()
  {
  }

  public DuplicateKeyExceptionHolder (Generated.DuplicateKeyException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Generated.DuplicateKeyExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Generated.DuplicateKeyExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Generated.DuplicateKeyExceptionHelper.type ();
  }

}
