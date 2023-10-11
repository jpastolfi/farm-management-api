package com.betrybe.agrix.evaluation.mock;

import java.util.Map;

public class PersonFixtures {

  public final static MockPerson person_user = new MockPerson(Map.of(
      "username", "maria",
      "password", "senhasecreta",
      "role", "USER"
  ));

  public final static MockPerson person_manager = new MockPerson(Map.of(
      "username", "joao",
      "password", "meuaniversario",
      "role", "MANAGER"
  ));

  public final static MockPerson person_admin = new MockPerson(Map.of(
      "username", "mrrobot",
      "password", "hacker",
      "role", "ADMIN"
  ));
}
