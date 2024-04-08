package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.CropDto;
import com.betrybe.agrix.controller.dto.FertilizerDto;
import com.betrybe.agrix.exception.CropNotFoundException;
import com.betrybe.agrix.exception.FertilizerNotFoundException;
import com.betrybe.agrix.service.CropService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Crop controller.
 */
@RestController
@RequestMapping("/crops")
public class CropController {
  private final CropService cropService;

  /**
   * Instantiates a new Crop controller.
   *
   * @param cropService the crop repository
   */
  @Autowired
  public CropController(CropService cropService) {
    this.cropService = cropService;
  }

  /**
   * Gets all crops.
   *
   * @return the all crops
   */
  @GetMapping
  public List<CropDto> getAllCrops() {
    return cropService.findAll().stream()
        .map(CropDto::fromEntity)
        .toList();
  }

  /**
   * Gets crop by id.
   *
   * @param cropId the crop id
   * @return the crop by id
   * @throws CropNotFoundException the crop not found exception
   */
  @GetMapping("/{cropId}")
  public CropDto getCropById(@PathVariable Integer cropId) throws CropNotFoundException {
    return CropDto.fromEntity(cropService.findById(cropId));
  }

  /**
   * Gets crops by harvest date.
   *
   * @param start the start
   * @param end   the end
   * @return the crops by harvest date
   */
  @GetMapping("/search")
  public List<CropDto> getCropsByHarvestDate(
      @RequestParam LocalDate start,
      @RequestParam LocalDate end
  ) {
    return cropService.getCropsByHarvestDate(start, end).stream()
        .map(CropDto::fromEntity)
        .toList();
  }

  /**
   * Create crop fertilizer string.
   *
   * @param cropId       the crop id
   * @param fertilizerId the fertilizer id
   * @return the string
   * @throws CropNotFoundException       the crop not found exception
   * @throws FertilizerNotFoundException the fertilizer not found exception
   */
  @PostMapping("/{cropId}/fertilizers/{fertilizerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public String createCropFertilizer(
      @PathVariable Integer cropId,
      @PathVariable Integer fertilizerId
  ) throws CropNotFoundException, FertilizerNotFoundException {
    cropService.createCropFertilizer(cropId, fertilizerId);
    return "Fertilizante e plantação associados com sucesso!";
  }

  /**
   * Gets fertilizers by crop.
   *
   * @param cropId the crop id
   * @return the fertilizers by crop
   * @throws CropNotFoundException the crop not found exception
   */
  @GetMapping("/{cropId}/fertilizers")
  public List<FertilizerDto> getFertilizersByCrop(@PathVariable Integer cropId)
      throws CropNotFoundException {
    return cropService.getFertilizersByCrop(cropId).stream()
        .map(FertilizerDto::fromEntity)
        .toList();
  }
}
