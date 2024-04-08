package com.betrybe.agrix.controller.dto;

import com.betrybe.agrix.entity.Person;
import com.betrybe.agrix.security.Role;

/**
 * The type Person creation dto.
 */
public record PersonCreationDto(String username, String password, Role role) {

  /**
   * To entity person.
   *
   * @return the person
   */
  public Person toEntity() {
    return new Person(
        username,
        password,
        Role.valueOf(String.valueOf(role))
    );
  }
}
