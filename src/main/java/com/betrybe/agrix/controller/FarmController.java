package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.CropCreationDto;
import com.betrybe.agrix.controller.dto.CropDto;
import com.betrybe.agrix.controller.dto.FarmCreationDto;
import com.betrybe.agrix.controller.dto.FarmDto;
import com.betrybe.agrix.entity.Crop;
import com.betrybe.agrix.entity.Farm;
import com.betrybe.agrix.exception.FarmNotFoundException;
import com.betrybe.agrix.service.FarmService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Farm controller.
 */
@RestController
@RequestMapping("/farms")
public class FarmController {
  private final FarmService farmService;

  /**
   * Instantiates a new Farm controller.
   *
   * @param farmService the farm service
   */
  @Autowired
  public FarmController(FarmService farmService) {
    this.farmService = farmService;
  }

  /**
   * Gets all farms.
   *
   * @return the all farms
   */
  @GetMapping
  public List<FarmDto> getAllFarms() {
    List<Farm> allFarms = farmService.findAll();
    return allFarms.stream()
        .map(FarmDto::fromEntity)
        .toList();
  }

  /**
   * Gets by id.
   *
   * @param farmId the id
   * @return the by id
   * @throws FarmNotFoundException the farm not found exception
   */
  @GetMapping("/{farmId}")
  public FarmDto getById(@PathVariable Integer farmId) throws FarmNotFoundException {
    return FarmDto.fromEntity(farmService.findById(farmId));
  }

  /**
   * Create farm farm dto.
   *
   * @param farmCreationDto the farm creation dto
   * @return the farm dto
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FarmDto createFarm(@RequestBody FarmCreationDto farmCreationDto) {
    return FarmDto.fromEntity(farmService.create(farmCreationDto.toEntity()));
  }

  /**
   * Update farm farm dto.
   *
   * @param farmId          the farm id
   * @param farmCreationDto the farm creation dto
   * @return the farm dto
   * @throws FarmNotFoundException the farm not found exception
   */
  @PutMapping("/{farmId}")
  public FarmDto updateFarm(
      @PathVariable Integer farmId,
      @RequestBody FarmCreationDto farmCreationDto
  ) throws FarmNotFoundException {
    return FarmDto.fromEntity(farmService.update(farmId, farmCreationDto.toEntity()));
  }

  /**
   * Delete farm farm dto.
   *
   * @param farmId the farm id
   * @return the farm dto
   * @throws FarmNotFoundException the farm not found exception
   */
  @DeleteMapping("/{farmId}")
  public FarmDto deleteFarm(@PathVariable Integer farmId) throws FarmNotFoundException {
    return FarmDto.fromEntity(farmService.delete(farmId));
  }

  /**
   * Create crop farm crop dto.
   *
   * @param farmId          the farm id
   * @param cropCreationDto the crop creation dto
   * @return the crop dto
   * @throws FarmNotFoundException the farm not found exception
   */
  @PostMapping("/{farmId}/crops")
  @ResponseStatus(HttpStatus.CREATED)
  public CropDto createCropFarm(
      @PathVariable Integer farmId,
      @RequestBody CropCreationDto cropCreationDto
  ) throws FarmNotFoundException {
    return CropDto.fromEntity(farmService.createCropFarm(farmId, cropCreationDto.toEntity()));
  }

  /**
   * Gets crop farm.
   *
   * @param farmId the farm id
   * @return the crop farm
   */
  @GetMapping("/{farmId}/crops")
  public List<CropDto> getCropsByFarmId(@PathVariable Integer farmId) throws FarmNotFoundException {
    List<Crop> allCropsById = farmService.getCropsByFarmId(farmId);
    return allCropsById.stream()
        .map(CropDto::fromEntity)
        .toList();
  }
}
