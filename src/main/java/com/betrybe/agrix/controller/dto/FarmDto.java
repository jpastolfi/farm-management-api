package com.betrybe.agrix.controller.dto;

import com.betrybe.agrix.entity.Farm;

/**
 * The type Farm dto.
 */
public record FarmDto(Integer id, String name, Double size) {

  /**
   * From entity farm dto.
   *
   * @param farm the farm
   * @return the farm dto
   */
  public static FarmDto fromEntity(Farm farm) {
    return new FarmDto(
      farm.getId(),
      farm.getName(),
      farm.getSize()
    );
  }
}
