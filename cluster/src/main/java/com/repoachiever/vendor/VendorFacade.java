package com.repoachiever.vendor;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.repoachiever.converter.AgentContextToJsonConverter;
import com.repoachiever.converter.DeploymentRequestsToAgentContextConverter;
import com.repoachiever.converter.SecretsConverter;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.SecretsConversionException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.kafkastarter.KafkaStarterClientServiceFacade;
import com.repoachiever.service.kafka.KafkaService;
import com.repoachiever.service.vendor.aws.AWSVendorService;
import com.repoachiever.service.vendor.common.VendorWaiter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Provides high-level access to cloud vendor operations. */
@ApplicationScoped
public class VendorFacade {

  @Inject PropertiesEntity properties;

  @Inject AWSVendorService awsVendorService;

  /** Initializes backend Terraform state storage in the given provider. */
  public void initBackendStorage(TerraformDeploymentApplication terraformDeploymentApplication) {
    switch (terraformDeploymentApplication.getProvider()) {
      case AWS -> {
        AWSSecretsDto secretsDto =
            AWSSecretsDto.of(
                terraformDeploymentApplication.getCredentials().getSecrets().getAccessKey(),
                terraformDeploymentApplication.getCredentials().getSecrets().getSecretKey());

        AWSCredentialsProvider awsCredentialsProvider =
            AWSVendorService.getAWSCredentialsProvider(secretsDto);

        if (!awsVendorService.isS3BucketExist(
            properties.getRemoteStorageName(),
            awsCredentialsProvider,
            terraformDeploymentApplication.getCredentials().getRegion())) {
          awsVendorService.createS3Bucket(
              properties.getRemoteStorageName(),
              awsCredentialsProvider,
              terraformDeploymentApplication.getCredentials().getRegion());
        }
      }
    }
  }

  /** Destroys backend Terraform state storage in the given provider. */
  public void destroyBackendStorage(
      TerraformDestructionApplication terraformDestructionApplication) {
    switch (terraformDestructionApplication.getProvider()) {
      case AWS -> {
        AWSSecretsDto secretsDto =
            AWSSecretsDto.of(
                terraformDestructionApplication.getCredentials().getSecrets().getAccessKey(),
                terraformDestructionApplication.getCredentials().getSecrets().getSecretKey());

        AWSCredentialsProvider awsCredentialsProvider =
            AWSVendorService.getAWSCredentialsProvider(secretsDto);

        if (awsVendorService.isS3BucketExist(
            properties.getRemoteStorageName(),
            awsCredentialsProvider,
            terraformDestructionApplication.getCredentials().getRegion())) {
          awsVendorService.removeS3Bucket(
              properties.getRemoteStorageName(),
              awsCredentialsProvider,
              terraformDestructionApplication.getCredentials().getRegion());
        }
      }
    }
  }

  /**
   * Starts container execution for a certain cloud provider.
   *
   * @param input Terraform deployment output.
   * @param terraformDeploymentApplication given Terraform deployment application.
   * @return public ip for the Kafka container.
   */
  public String startContainerExecution(
      String input, TerraformDeploymentApplication terraformDeploymentApplication) {
    return switch (terraformDeploymentApplication.getProvider()) {
      case AWS -> {
        AWSSecretsDto secretsDto =
            AWSSecretsDto.of(
                terraformDeploymentApplication.getCredentials().getSecrets().getAccessKey(),
                terraformDeploymentApplication.getCredentials().getSecrets().getSecretKey());

        AWSCredentialsProvider awsCredentialsProvider =
            AWSVendorService.getAWSCredentialsProvider(secretsDto);

        AWSDeploymentResultDto awsDeploymentResult =
            awsVendorService.getEcsTaskInitializationDetails(input);

        awsVendorService.waitForEcsTaskReadiness(
            properties.getAwsResourceTrackerCommonFamily(),
            awsDeploymentResult.getEcsCluster().getValue(),
            awsDeploymentResult.getEcsTaskDefinition().getValue(),
            awsCredentialsProvider,
            terraformDeploymentApplication.getCredentials().getRegion(),
            properties.getAwsReadinessPeriod());

        String serviceMachineAddress =
            awsVendorService.getMachineAddress(
                properties.getAwsResourceTrackerCommonFamily(),
                awsDeploymentResult.getEcsCluster().getValue(),
                awsDeploymentResult.getEcsTaskDefinition().getValue(),
                awsCredentialsProvider,
                terraformDeploymentApplication.getCredentials().getRegion());

        String agentContext =
            AgentContextToJsonConverter.convert(
                DeploymentRequestsToAgentContextConverter.convert(
                    terraformDeploymentApplication.getRequests()));

        AWSTaskDefinitionRegistrationDto ecsTaskDefinitionRegistrationDto =
            AWSTaskDefinitionRegistrationDto.of(
                AWSAgentTaskDefinitionRegistrationDto.of(
                    properties.getResourceTrackerAgentImage(),
                    properties.getAwsResourceTrackerAgentName(),
                    agentContext,
                    properties.getResourceTrackerAgentContextAlias(),
                    properties.getGitCommitId()),
                AWSKafkaTaskDefinitionRegistrationDto.of(
                    properties.getResourceTrackerKafkaImage(),
                    properties.getGitCommitId(),
                    properties.getAwsResourceTrackerKafkaName(),
                    properties.getResourceTrackerKafkaMainPort(),
                    properties.getResourceTrackerKafkaStarterPort(),
                    serviceMachineAddress,
                    properties.getResourceTrackerKafkaHostAlias(),
                    properties.getKafkaTopic(),
                    properties.getResourceTrackerKafkaCreateTopicsAlias(),
                    properties.getKafkaPartitions(),
                    properties.getResourceTrackerKafkaPartitionsAlias()),
                properties.getAwsResourceTrackerCommonFamily(),
                awsDeploymentResult.getEcsTaskExecutionRole().getValue(),
                properties.getAwsResourceTrackerCommonCPUUnits(),
                properties.getAwsResourceTrackerCommonMemoryUnits());

        String ecsTaskDefinitionsArn =
            awsVendorService.registerEcsTaskDefinitions(
                ecsTaskDefinitionRegistrationDto,
                awsCredentialsProvider,
                terraformDeploymentApplication.getCredentials().getRegion());

        awsVendorService.runEcsTask(
            awsDeploymentResult.getEcsCluster().getValue(),
            awsDeploymentResult.getEcsService().getValue(),
            ecsTaskDefinitionsArn,
            awsCredentialsProvider,
            terraformDeploymentApplication.getCredentials().getRegion());

        awsVendorService.waitForEcsTaskReadiness(
            properties.getAwsResourceTrackerCommonFamily(),
            awsDeploymentResult.getEcsCluster().getValue(),
            ecsTaskDefinitionsArn,
            awsCredentialsProvider,
            terraformDeploymentApplication.getCredentials().getRegion(),
            properties.getAwsReadinessPeriod());

        serviceMachineAddress =
            awsVendorService.getMachineAddress(
                properties.getAwsResourceTrackerCommonFamily(),
                awsDeploymentResult.getEcsCluster().getValue(),
                ecsTaskDefinitionsArn,
                awsCredentialsProvider,
                terraformDeploymentApplication.getCredentials().getRegion());

        KafkaStarterClientServiceFacade kafkaStarterClientServiceFacade =
            new KafkaStarterClientServiceFacade(
                serviceMachineAddress, properties.getResourceTrackerKafkaStarterPort());

        kafkaStarterClientServiceFacade.deploy(serviceMachineAddress);

        KafkaService kafkaService = new KafkaService(serviceMachineAddress, properties);

        VendorWaiter.awaitFor(kafkaService::isAvailable, properties.getKafkaReadinessPeriod());

        yield serviceMachineAddress;
      }
    };
  }

  /**
   * Stops container execution for a certain cloud provider.
   *
   * @param terraformDestructionApplication given Terraform destruction application.
   */
  public void stopContainerExecution(
      TerraformDestructionApplication terraformDestructionApplication) {
    switch (terraformDestructionApplication.getProvider()) {
      case AWS -> {
        AWSSecretsDto secretsDto =
            AWSSecretsDto.of(
                terraformDestructionApplication.getCredentials().getSecrets().getAccessKey(),
                terraformDestructionApplication.getCredentials().getSecrets().getSecretKey());

        AWSCredentialsProvider awsCredentialsProvider =
            AWSVendorService.getAWSCredentialsProvider(secretsDto);

        awsVendorService.deregisterEcsTaskDefinitions(
            properties.getAwsResourceTrackerCommonFamily(),
            awsCredentialsProvider,
            terraformDestructionApplication.getCredentials().getRegion());

        //        awsVendorService.removeEcsExecutionRole(
        //            properties.getAwsResourceTrackerExecutionRole(),
        //            awsCredentialsProvider,
        //            terraformDestructionApplication.getCredentials().getRegion());
      }
    }
  }

  /**
   * Checks if the given provider credentials are valid.
   *
   * @return result of provider credentials validation.
   */
  public ValidationSecretsApplicationResult isCredentialsValid(
      ValidationSecretsApplication validationSecretsApplication) throws SecretsConversionException {
    return switch (validationSecretsApplication.getProvider()) {
      case AWS -> {
        AWSSecretsDto secrets =
            SecretsConverter.convert(AWSSecretsDto.class, validationSecretsApplication.getFile());

        AWSCredentialsProvider awsCredentialsProvider =
            AWSVendorService.getAWSCredentialsProvider(secrets);

        yield ValidationSecretsApplicationResult.of(
            awsVendorService.isCallerValid(
                awsCredentialsProvider, properties.getAwsDefaultRegion()),
            ValidationSecretsApplicationResultSecrets.of(
                secrets.getAccessKey(), secrets.getSecretKey()));
      }
    };
  }
}
