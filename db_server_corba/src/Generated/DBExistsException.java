package Generated;


/**
* Generated/DBExistsException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /home/pavel/Desktop/Skola/DSV/projects/db_server_corba/src/server.idl
* Saturday, November 26, 2011 10:07:34 PM CET
*/

public final class DBExistsException extends org.omg.CORBA.UserException
{
  public String message = null;

  public DBExistsException ()
  {
    super(DBExistsExceptionHelper.id());
  } // ctor

  public DBExistsException (String _message)
  {
    super(DBExistsExceptionHelper.id());
    message = _message;
  } // ctor


  public DBExistsException (String $reason, String _message)
  {
    super(DBExistsExceptionHelper.id() + "  " + $reason);
    message = _message;
  } // ctor

} // class DBExistsException
