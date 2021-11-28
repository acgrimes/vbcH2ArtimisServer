package com.ll.vbc.utils;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 * This class creates an object that is unique across a distributed system. The
 * InetAddress object will be unique between physical host servers and the UID
 * object will be unique with a JVM running on a host server. Therefore,
 * uniqueness is achieved across a distributed system.
 */
public class GUID implements Serializable {

  private InetAddress inetAddress;
  private UUID uid;

  /**
   * package level constructor that should not be called by any client, should
   * use the GUIDFactory to get a GUID.
   * @param inetAddress - this is the host IP address that this JVM is running on.
   * @param uid - a unique id derived for this server this JVM is running on.
   */
  protected GUID(InetAddress inetAddress, UUID uid) {
    this.inetAddress = inetAddress;
    this.uid = uid;
  }

  /**
   * package access level constructor used by the GUIDFactory when the
   * inetAddress is unavailable for some reason.
   * @param uid - unique id for a JVM running on a server
   */
  protected GUID(UUID uid) {
    this.uid = uid;
  }

  /**
   * The equals method for this GUID class. The UID and the InetAddress attributes
   * must be equal in order for these GUID objects to be equal. If the InetAddress
   * attribute is null, not assigned, then the uids must be equal.
   * @param object - should be an object of type GUID.
   * @return boolean - returns true if the objects are equal, that is, the uid
   * and inetAddress are equal.
   */
  public boolean equals(Object object) {
    boolean result = false;
    GUID guid = null;
    try {
      guid = (GUID) object;
      result = inetAddress.equals(guid.getInetAddress()) &&
               uid.equals(guid.getUid());
    }
    catch(ClassCastException cce) { } // not equal
    catch(NullPointerException npe) {
      if(guid != null) {
        if((inetAddress == null) && (uid != null)) {
          result = uid.equals(guid.getUid());
        }
      }
    }
    return result;
  }

  /**
   * Accessor for the inetAddress attribute
   * @return InetAddress - inetAddress attribute
   */
  public InetAddress getInetAddress() {
    return inetAddress;
  }

  /**
   * Accessor for the uid attribute
   * @return UID - uid attribute
   */
  public UUID getUid() {
    return uid;
  }

  /**
   * hashcode for this GUID object is derived from the uid object hash code and
   * the inetAddress object hash codes.
   * @return int - returns the hash code for this GUID object.
   */
  public int hashcode() {
    return uid.hashCode() ^ inetAddress.hashCode();
  }

  /**
   * A String object representation of the GUID
   * @return String - GUID as a String object.
   */
  public String toString() {
    StringBuffer tmpBuffer = new StringBuffer(40);
    tmpBuffer.append(inetAddress.toString());
    tmpBuffer.append(":");
    tmpBuffer.append(uid.toString() );
    return tmpBuffer.toString();
  }

}
