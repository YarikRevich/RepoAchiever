package com.repoachiever.terraform.common;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Contains helpful tools used for general Terraform configuration. */
public class TerraformConfigurationHelper {
  /**
   * Composes backend configuration.
   *
   * @param attributes attributes to be included.
   * @return composed backend configuration.
   */
  public static String getBackendConfig(Map<String, String> attributes) {
    return attributes.entrySet().stream()
        .map(
            element ->
                String.format("-backend-config='%s=%s'", element.getKey(), element.getValue()))
        .collect(Collectors.joining(" "));
  }

  /**
   * Composes environment variables.
   *
   * @param attributes attributes to be included.
   * @return composed environment variables.
   */
  public static String getEnvironmentVariables(Map<String, String> attributes) {
    return attributes.entrySet().stream()
        .map(element -> String.format("%s='%s'", element.getKey(), element.getValue()))
        .collect(Collectors.joining(" "));
  }

  /**
   * Composes input variables.
   *
   * @param attributes attributes to be included.
   * @return composed content variables.
   */
  public static String getVariables(Map<String, String> attributes) {
    return attributes.entrySet().stream()
        .map(element -> String.format("-var %s='%s'", element.getKey(), element.getValue()))
        .collect(Collectors.joining(" "));
  }

  /**
   * Composes variables from the given content variables.
   *
   * @param agentContext given ResourceTracker Agent context.
   * @param agentVersion given ResourceTracker Agent version.
   * @return composed environment variables.
   */
  public static String getContentEnvironmentVariables(String agentContext, String agentVersion) {
    return getEnvironmentVariables(
        new HashMap<>() {
          {
            put("TF_VAR_resourcetracker_agent_context", agentContext);
            put("TF_VAR_resourcetracker_agent_version", agentVersion);
          }
        });
  }

  //  /**
  //   * Composes variable file entity.
  //   *
  //   * @param agentContext given ResourceTracker Agent context to be converted.
  //   * @param agentVersion given ResourceTracker Agent version to be converted.
  //   * @return composed variable file entity.
  //   */
  //  public static VariableFileEntity getVariablesFile(String agentContext, String agentVersion) {
  //
  //
  //    return VariableFileEntity.of(
  //        List.of(
  //            VariableFileEntity.VariableFileUnit.of("resourcetracker_agent_context",
  // agentContext),
  //            VariableFileEntity.VariableFileUnit.of("resourcetracker_agent_version",
  // agentVersion)));
  //  }
}
