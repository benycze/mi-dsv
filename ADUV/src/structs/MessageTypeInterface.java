/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

/**
 * Interface which represents message type.
 * @author pavel
 */
public interface MessageTypeInterface {
    /**
     * Type of the message
     */
    public enum MessageType{
      START,
      TOKEN,
      GET_WORK,
      STOP,
      WORK_TERMINATED
      }
}
