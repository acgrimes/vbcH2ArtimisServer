package com.ll.vbc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will create a globally unique identifier in a  STRING
 * representation. This identifier is guaranteed to be unique across processes and
 * servers.
 */
public class GUIDFactory {

  // reference to the jvm system logger:
  private static final Logger log = Logger.getLogger(GUIDFactory.class.getName());

  /**
   * Instance varaible for the singleton.
   */
  private static GUIDFactory instance = null;

  private static Object mutx = new Object();

    /**
     * The default constructor for GUIDFactory. This should not ever
     * be invoked.
     */
    private GUIDFactory() { }

  /**
   * GUID singleton instance
   *
   * @return GUIDFactory
   */
  public static GUIDFactory getInstance() {
    if (instance == null) {
      synchronized (mutx) {
        if (instance==null) {
          instance = new GUIDFactory();
        }
      }
    }
    return instance;
  }

  /**
   * This method will create a globally unique identifier in a STRING format.
   *
   * @return java.lang.String
   */
  public GUID createGUID() {

    final String MTD = "createGUID";
    GUID guid = null;
    try {
      InetAddress inetAddress = InetAddress.getLocalHost();
      UUID uid = UUID.randomUUID();
      guid = new GUID(inetAddress, uid);
    }
    catch(UnknownHostException uhe) {
      UID uid = new UID();
      log.logp(Level.WARNING, log.getName(), MTD, uhe.getMessage());
    }
    return guid;
  }

    public GUID createGUID(InetAddress inetAddress, UUID uuid) {
        return new GUID(inetAddress, uuid);
    }
}
