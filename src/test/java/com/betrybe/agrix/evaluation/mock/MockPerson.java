package com.betrybe.agrix.evaluation.mock;

import java.util.HashMap;
import java.util.Map;

public class MockPerson extends HashMap<String, Object> {

  public <K, V> MockPerson() {
    super();
  }

  public <K, V> MockPerson(Map<K, V> source) {
    super((Map<String, Object>) source);
  }

  public MockPerson clone() {
    return new MockPerson(this);
  }
}
