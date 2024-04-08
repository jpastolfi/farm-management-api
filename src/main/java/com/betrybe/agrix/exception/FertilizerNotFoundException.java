package com.betrybe.agrix.exception;

/**
 * The type Fertilizer not found exception.
 */
public class FertilizerNotFoundException extends NotFoundException {

  /**
   * Instantiates a new Not found exception.
   */
  public FertilizerNotFoundException() {
    super("Fertilizante não encontrado!");
  }
}
