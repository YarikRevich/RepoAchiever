package com.repoachiever.service.config.common;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ConfigValidationException;
import com.repoachiever.service.config.ConfigService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents service for config validation. */
@Service
public class ValidConfigService {
  @Autowired private ConfigService configService;

  /**
   * Validates parsed local configuration file.
   *
   * @throws ConfigValidationException if the configuration validation is not passed.
   */
  public void validate() throws ConfigValidationException {
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = validatorFactory.getValidator();

      Set<ConstraintViolation<ConfigEntity>> validationResult =
          validator.validate(configService.getConfig());

      if (!validationResult.isEmpty()) {
        throw new ConfigValidationException(
            validationResult.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", ")));
      }
    }
  }
}
