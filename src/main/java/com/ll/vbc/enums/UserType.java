package com.ll.vbc.enums;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 */
public class UserType implements Serializable {

  private Long id;
  private String name;
  private static final HashMap idMapping = new HashMap();

  public static final UserType VBC = new UserType(1L, "vbc");
  public static final UserType ADMIN = new UserType(2L, "Admin");
  public static final UserType SYSTEM = new UserType(3L, "SYSTEM");

  private UserType(Long id, String name) {
    this.id = id;
    this.name = name;
    idMapping.put(id, this);
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
  
  /**
   * This method gets the original reference to this object when serialization
   * marshalling occurs.
   * @return UserType object with original reference.
   */
  private Object readResolve() {
    return idMapping.get(getId());
  }
}
