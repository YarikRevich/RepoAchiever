package com.repoachiever.terraform.provider;

import com.repoachiever.exception.TerraformException;
import com.repoachiever.model.TerraformDeploymentApplication;
import com.repoachiever.model.TerraformDestructionApplication;

/** Interface for Terraform service to execute cloud providers. */
public interface ITerraformProvider {
  /**
   * Applies certain provider deployment configuration.
   *
   * @param terraformDeploymentApplication deployment application.
   * @return remote machine address.
   * @throws TerraformException when any deployment operation step failed.
   */
  String apply(TerraformDeploymentApplication terraformDeploymentApplication)
      throws TerraformException;

  /**
   * Destroys certain provider deployment configuration.
   *
   * @param terraformDestructionApplication destruction application.
   * @throws TerraformException when any destruction operation step failed.
   */
  void destroy(TerraformDestructionApplication terraformDestructionApplication)
      throws TerraformException;
}
