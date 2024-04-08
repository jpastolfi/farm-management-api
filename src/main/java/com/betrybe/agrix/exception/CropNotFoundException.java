package com.betrybe.agrix.exception;

/**
 * The type Crop not found exception.
 */
public class CropNotFoundException extends NotFoundException {


  /**
   * Instantiates a new Crop not found exception.
   */
  public CropNotFoundException() {
    super("Plantação não encontrada!");
  }
}
