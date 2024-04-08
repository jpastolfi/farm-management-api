package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.FertilizerCreationDto;
import com.betrybe.agrix.controller.dto.FertilizerDto;
import com.betrybe.agrix.exception.FertilizerNotFoundException;
import com.betrybe.agrix.service.FertilizerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Fertilizer controller.
 */
@RestController
@RequestMapping("/fertilizers")
public class FertilizerController {
  private final FertilizerService fertilizerService;

  /**
   * Instantiates a new Fertilizer controller.
   *
   * @param fertilizerService the fertilizer service
   */
  @Autowired
  public FertilizerController(FertilizerService fertilizerService) {
    this.fertilizerService = fertilizerService;
  }

  /**
   * Create fertilizer fertilizer dto.
   *
   * @param fertilizerCreationDto the fertilizer creation dto
   * @return the fertilizer dto
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FertilizerDto createFertilizer(
      @RequestBody FertilizerCreationDto fertilizerCreationDto
  ) {
    return FertilizerDto
        .fromEntity(fertilizerService.createFertilizer(fertilizerCreationDto.toEntity()));
  }

  /**
   * Gets all fertilizers.
   *
   * @return the all fertilizers
   */
  @GetMapping
  public List<FertilizerDto> getAllFertilizers() {
    return fertilizerService.getAllFertilizers().stream()
        .map(FertilizerDto::fromEntity)
        .toList();
  }

  @GetMapping("/{fertilizerId}")
  public FertilizerDto getFertilizerById(@PathVariable Integer fertilizerId)
      throws FertilizerNotFoundException {
    return FertilizerDto.fromEntity(fertilizerService.getFertilizerById(fertilizerId));
  }
}
