package com.betrybe.agrix.service;

import com.betrybe.agrix.entity.Crop;
import com.betrybe.agrix.entity.Farm;
import com.betrybe.agrix.exception.FarmNotFoundException;
import com.betrybe.agrix.repository.CropRepository;
import com.betrybe.agrix.repository.FarmRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Farm service.
 */
@Service
public class FarmService {
  private final FarmRepository farmRepository;
  private final CropRepository cropRepository;

  /**
   * Instantiates a new Farm service.
   *
   * @param farmRepository the farm repository
   * @param cropRepository the crop repository
   */
  @Autowired
  public FarmService(FarmRepository farmRepository, CropRepository cropRepository) {
    this.farmRepository = farmRepository;
    this.cropRepository = cropRepository;
  }

  /**
   * Find all list.
   *
   * @return the list
   */
  public List<Farm> findAll() {
    return farmRepository.findAll();
  }

  /**
   * Find by id farm.
   *
   * @param farmId the farm id
   * @return the farm
   * @throws FarmNotFoundException the farm not found exception
   */
  public Farm findById(Integer farmId) throws FarmNotFoundException {
    return farmRepository.findById(farmId).orElseThrow(FarmNotFoundException::new);
  }

  /**
   * Create farm.
   *
   * @param farm the farm
   * @return the farm
   */
  public Farm create(Farm farm) {
    return farmRepository.save(farm);
  }

  /**
   * Update farm.
   *
   * @param farmId the farm id
   * @param farm   the farm
   * @return the farm
   * @throws FarmNotFoundException the farm not found exception
   */
  public Farm update(Integer farmId, Farm farm) throws FarmNotFoundException {
    Farm requestedFarm = findById(farmId);
    requestedFarm.setName(farm.getName());
    requestedFarm.setSize(farm.getSize());
    return farmRepository.save(requestedFarm);
  }

  /**
   * Delete farm.
   *
   * @param farmId the farm id
   * @return the farm
   * @throws FarmNotFoundException the farm not found exception
   */
  public Farm delete(Integer farmId) throws FarmNotFoundException {
    Farm requestedFarm = findById(farmId);
    farmRepository.delete(requestedFarm);
    return requestedFarm;
  }

  /**
   * Create crop farm crop.
   *
   * @param farmId the farm id
   * @param crop   the crop
   * @return the crop
   * @throws FarmNotFoundException the farm not found exception
   */
  public Crop createCropFarm(Integer farmId, Crop crop) throws FarmNotFoundException {
    Farm foundFarm = findById(farmId);
    crop.setFarm(foundFarm);
    return cropRepository.save(crop);
  }

  /**
   * Gets crop farm.
   *
   * @param farmId the farm id
   * @return the crop farm
   * @throws FarmNotFoundException the farm not found exception
   */
  public List<Crop> getCropsByFarmId(Integer farmId) throws FarmNotFoundException {
    Farm foundFarm = findById(farmId);
    return cropRepository.findAll().stream()
        .filter(crop -> Objects.equals(crop.getFarm().getId(), farmId))
        .toList();
  }
}
