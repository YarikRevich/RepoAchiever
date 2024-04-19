package com.repoachiever.vendor.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.model.NetworkInterface;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;
import com.amazonaws.services.ecs.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AWSSecurityTokenServiceException;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.waiters.WaiterParameters;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.dto.AWSDeploymentResultDto;
import com.repoachiever.dto.AWSSecretsDto;
import com.repoachiever.dto.AWSTaskDefinitionRegistrationDto;
import com.repoachiever.service.vendor.common.VendorWaiter;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/** Provides access to AWS SDK to perform subprocesses needed for AWS provider management. */
@ApplicationScoped
public class AWSVendorService {
  private static final Logger logger = LogManager.getLogger(AWSVendorService.class);

  /**
   * Composes AWS Credentials Provider used by AWS SDK clients.
   *
   * @param secrets AWS client secrets.
   * @return composed AWS Credentials Provider.
   */
  public static AWSCredentialsProvider getAWSCredentialsProvider(AWSSecretsDto secrets) {
    return new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(secrets.getAccessKey(), secrets.getSecretKey()));
  }

  /**
   * Composes ECS Task initialization details into deployment readable format.
   *
   * @param src raw details returned by Terraform deployment application.
   * @return composed ECS Task details.
   */
  public AWSDeploymentResultDto getEcsTaskInitializationDetails(String src) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectReader reader = mapper.reader().forType(new TypeReference<AWSDeploymentResultDto>() {});
    try {
      return reader.<AWSDeploymentResultDto>readValues(src).readAll().getFirst();
    } catch (IOException e) {
      logger.fatal(e.getMessage());
    }

    return null;
  }

  /**
   * Registers task definitions for ResourceTracker Agent and Kafka containers.
   *
   * @param ecsTaskDefinitionRegistrationDto given dto with properties for containers definition.
   * @param awsCredentialsProvider given AWS credentials.
   * @param region given AWS region.
   * @return arn of the created ECS Task Definition.
   */
  public String registerEcsTaskDefinitions(
      AWSTaskDefinitionRegistrationDto ecsTaskDefinitionRegistrationDto,
      AWSCredentialsProvider awsCredentialsProvider,
      String region) {
    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    PortMapping mainPortMapping =
        new PortMapping()
            .withContainerPort(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerMainPort())
            .withHostPort(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerMainPort());

    PortMapping starterPortMapping =
        new PortMapping()
            .withContainerPort(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerStarterPort())
            .withHostPort(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerStarterPort());

    KeyValuePair kraftCreateTopicsKeyPair =
        new KeyValuePair()
            .withName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaCreateTopicsAlias())
            .withValue(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaCreateTopic());

    KeyValuePair kraftPartitionsPerTopicKeyPair =
        new KeyValuePair()
            .withName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaPartitionsAlias())
            .withValue(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaPartitions());

    ContainerDefinition kafkaContainerDefinition =
        new ContainerDefinition()
            .withName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerName())
            .withEssential(true)
            .withPortMappings(mainPortMapping, starterPortMapping)
            .withEnvironment(kraftCreateTopicsKeyPair, kraftPartitionsPerTopicKeyPair)
            .withImage(
                String.format(
                    "%s:%s",
                    ecsTaskDefinitionRegistrationDto
                        .getAwsKafkaTaskDefinitionRegistrationDto()
                        .getKafkaContainerImage(),
                    ecsTaskDefinitionRegistrationDto
                        .getAwsKafkaTaskDefinitionRegistrationDto()
                        .getKafkaVersion()));

    ContainerDependency containerDependency =
        new ContainerDependency()
            .withContainerName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsKafkaTaskDefinitionRegistrationDto()
                    .getKafkaContainerName())
            .withCondition(ContainerCondition.START);

    KeyValuePair agentContextKeyPair =
        new KeyValuePair()
            .withName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsAgentTaskDefinitionRegistrationDto()
                    .getAgentContextAlias())
            .withValue(
                ecsTaskDefinitionRegistrationDto
                    .getAwsAgentTaskDefinitionRegistrationDto()
                    .getAgentContext());

    ContainerDefinition agentContainerDefinition =
        new ContainerDefinition()
            .withName(
                ecsTaskDefinitionRegistrationDto
                    .getAwsAgentTaskDefinitionRegistrationDto()
                    .getAgentContainerName())
            .withEssential(true)
            .withDependsOn(containerDependency)
            .withEnvironment(agentContextKeyPair)
            .withImage(
                String.format(
                    "%s:%s",
                    ecsTaskDefinitionRegistrationDto
                        .getAwsAgentTaskDefinitionRegistrationDto()
                        .getAgentContainerImage(),
                    ecsTaskDefinitionRegistrationDto
                        .getAwsAgentTaskDefinitionRegistrationDto()
                        .getAgentVersion()));

    RegisterTaskDefinitionRequest registerTaskDefinitionRequest =
        new RegisterTaskDefinitionRequest()
            .withFamily(ecsTaskDefinitionRegistrationDto.getCommonFamilyName())
            .withContainerDefinitions(agentContainerDefinition, kafkaContainerDefinition)
            .withExecutionRoleArn(ecsTaskDefinitionRegistrationDto.getCommonTaskExecutionRole())
            .withCpu(ecsTaskDefinitionRegistrationDto.getAwsResourceTrackerCommonCPUUnits())
            .withMemory(ecsTaskDefinitionRegistrationDto.getAwsResourceTrackerCommonMemoryUnits())
            .withNetworkMode(NetworkMode.Awsvpc)
            .withRequiresCompatibilities(Compatibility.FARGATE);

    return ecsClient
        .registerTaskDefinition(registerTaskDefinitionRequest)
        .getTaskDefinition()
        .getTaskDefinitionArn();
  }

  /**
   * Registers task definitions for ResourceTracker Agent and Kafka containers.
   *
   * @param ecsTaskFamily family name of the ECS Task Definitions.
   * @param awsCredentialsProvider given AWS credentials.
   * @param region given AWS region.
   */
  public void deregisterEcsTaskDefinitions(
      String ecsTaskFamily, AWSCredentialsProvider awsCredentialsProvider, String region) {
    ListTaskDefinitionsRequest listTaskDefinitionsRequest =
        new ListTaskDefinitionsRequest().withFamilyPrefix(ecsTaskFamily);

    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    ListTaskDefinitionsResult listTasksResult =
        ecsClient.listTaskDefinitions(listTaskDefinitionsRequest);

    listTasksResult
        .getTaskDefinitionArns()
        .forEach(
            element -> {
              DeregisterTaskDefinitionRequest deregisterTaskDefinitionRequest =
                  new DeregisterTaskDefinitionRequest().withTaskDefinition(element);

              ecsClient.deregisterTaskDefinition(deregisterTaskDefinitionRequest);
            });
  }

  /**
   * Runs ECS Task with the given configuration properties.
   *
   * @param ecsClusterId cluster id of the ECS Task.
   * @param ecsServiceId service id of the ECS Task.
   * @param ecsTaskDefinitionArn arn of the ECS Task.
   * @param awsCredentialsProvider given credentials provider.
   * @param region given AWS region.
   */
  public void runEcsTask(
      String ecsClusterId,
      String ecsServiceId,
      String ecsTaskDefinitionArn,
      AWSCredentialsProvider awsCredentialsProvider,
      String region) {
    UpdateServiceRequest updateServiceRequest =
        new UpdateServiceRequest()
            .withCluster(ecsClusterId)
            .withService(ecsServiceId)
            .withTaskDefinition(ecsTaskDefinitionArn);

    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    ecsClient.updateService(updateServiceRequest);
  }

  /**
   * Retrieves remote machine address from the given ECS Task details.
   *
   * @param ecsTaskFamily family name of the ECS Task.
   * @param ecsClusterId cluster id of the ECS Task.
   * @return remote machine address.
   */
  public String getMachineAddress(
      String ecsTaskFamily,
      String ecsClusterId,
      String ecsTaskDefinitionId,
      AWSCredentialsProvider awsCredentialsProvider,
      String region) {
    String ecsTaskArn =
        mustGetEcsTaskArn(
            ecsTaskFamily, ecsClusterId, ecsTaskDefinitionId, awsCredentialsProvider, region);

    String ecsTaskNetworkInterfaceId =
        getEcsTaskNetworkInterfaceId(ecsClusterId, ecsTaskArn, awsCredentialsProvider, region);

    return getEcsTaskPublicIp(ecsTaskNetworkInterfaceId, awsCredentialsProvider, region);
  }

  /**
   * Waits for the given ECS Task to be ready to be used.
   *
   * @param ecsClusterId cluster id of the ECS Task.
   * @param ecsTaskDefinitionArn arn of the ECS Task.
   * @param awsCredentialsProvider given credentials provider.
   * @param region given AWS region.
   * @param readinessPeriod given readiness period for the readiness waiter.
   */
  public void waitForEcsTaskReadiness(
      String ecsTaskFamily,
      String ecsClusterId,
      String ecsTaskDefinitionArn,
      AWSCredentialsProvider awsCredentialsProvider,
      String region,
      Integer readinessPeriod) {
    ListTasksRequest listTasksRequest =
        new ListTasksRequest().withFamily(ecsTaskFamily).withCluster(ecsClusterId);

    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    VendorWaiter.awaitFor(
        () -> {
          ListTasksResult listTasksResult = ecsClient.listTasks(listTasksRequest);

          if (listTasksResult.getTaskArns().isEmpty()) {
            return false;
          }

          DescribeTasksRequest describeTasksRequest =
              new DescribeTasksRequest()
                  .withCluster(ecsClusterId)
                  .withTasks(listTasksResult.getTaskArns());

          List<String> describeTasksResult =
              ecsClient.describeTasks(describeTasksRequest).getTasks().stream()
                  .filter(element -> element.getTaskDefinitionArn().equals(ecsTaskDefinitionArn))
                  .filter(element -> element.getLastStatus().equals("RUNNING"))
                  .map(Task::getTaskDefinitionArn)
                  .toList();

          return !describeTasksResult.isEmpty();
        },
        readinessPeriod);
  }

  /**
   * Must retrieve ECS Task ARN started in the given cluster id.
   *
   * @param ecsTaskFamily family name of the ECS Task.
   * @param ecsClusterId cluster id of the ECS Task.
   * @param ecsTaskDefinitionArn arn of the ECS Task.
   * @param awsCredentialsProvider given credentials provider.
   * @param region given AWS region.
   * @return ECS Task ARN in the given cluster id.
   */
  private String mustGetEcsTaskArn(
      String ecsTaskFamily,
      String ecsClusterId,
      String ecsTaskDefinitionArn,
      AWSCredentialsProvider awsCredentialsProvider,
      String region) {
    ListTasksRequest listTasksRequest =
        new ListTasksRequest().withFamily(ecsTaskFamily).withCluster(ecsClusterId);

    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    ListTasksResult listTasksResult = ecsClient.listTasks(listTasksRequest);

    DescribeTasksRequest describeTasksRequest =
        new DescribeTasksRequest()
            .withCluster(ecsClusterId)
            .withTasks(listTasksResult.getTaskArns());

    List<String> describeTasksResult =
        ecsClient.describeTasks(describeTasksRequest).getTasks().stream()
            .filter(element -> element.getTaskDefinitionArn().equals(ecsTaskDefinitionArn))
            .map(Task::getTaskArn)
            .toList();

    return describeTasksResult.get(0);
  }

  /**
   * Retrieves network interface ID of the ECS Task container.
   *
   * @param ecsClusterId ECS Task cluster ID.
   * @param ecsTaskArn arn of the ECS Task.
   * @return network interface ID of the ECS Task container.
   */
  private String getEcsTaskNetworkInterfaceId(
      String ecsClusterId,
      String ecsTaskArn,
      AWSCredentialsProvider awsCredentialsProvider,
      String region) {
    DescribeTasksRequest describeTaskRequest =
        new DescribeTasksRequest().withCluster(ecsClusterId).withTasks(List.of(ecsTaskArn));

    AmazonECS ecsClient =
        AmazonECSClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    DescribeTasksResult describeTasksResult = ecsClient.describeTasks(describeTaskRequest);

    Task task = describeTasksResult.getTasks().get(0);

    String taskAttachmentId =
        task.getContainers().stream()
            .filter(element -> Objects.equals(element.getTaskArn(), ecsTaskArn))
            .map(element -> element.getNetworkInterfaces().get(0).getAttachmentId())
            .toList()
            .get(0);

    return task.getAttachments().stream()
        .filter(element -> Objects.equals(element.getId(), taskAttachmentId))
        .toList()
        .get(0)
        .getDetails()
        .stream()
        .filter(detail -> Objects.equals(detail.getName(), "networkInterfaceId"))
        .toList()
        .get(0)
        .getValue();
  }

  /**
   * Retrieves public IP of the ECS Task container.
   *
   * @param networkInterfaceId ECS Task interface ID.
   * @return public IP of the ECS Task container.
   */
  private String getEcsTaskPublicIp(
      String networkInterfaceId, AWSCredentialsProvider awsCredentialsProvider, String region) {
    DescribeNetworkInterfacesRequest describeNetworkInterfacesRequest =
        new DescribeNetworkInterfacesRequest().withNetworkInterfaceIds(List.of(networkInterfaceId));

    DescribeNetworkInterfacesResult describeNetworkInterfacesResult =
        AmazonEC2ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build()
            .describeNetworkInterfaces(describeNetworkInterfacesRequest);

    NetworkInterface networkInterfaces =
        describeNetworkInterfacesResult.getNetworkInterfaces().get(0);

    return networkInterfaces.getAssociation().getPublicIp();
  }

  /**
   * Checks if S3 bucket with the given name exists.
   *
   * @param name name of the S3 bucket.
   * @return result of the check.
   */
  public boolean isS3BucketExist(
      String name, AWSCredentialsProvider awsCredentialsProvider, String region) {
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(awsCredentialsProvider)
        .build()
        .doesBucketExistV2(name);
  }

  /**
   * Creates S3 bucket with the given name.
   *
   * @param name name of the S3 bucket.
   */
  public void createS3Bucket(
      String name, AWSCredentialsProvider awsCredentialsProvider, String region) {
    AmazonS3 simpleStorage =
        AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();
    simpleStorage.createBucket(name);

    AmazonS3Waiters simpleStorageWaiter = simpleStorage.waiters();
    simpleStorageWaiter.bucketExists().run(new WaiterParameters<>(new HeadBucketRequest(name)));
  }

  /**
   * Removes S3 bucket with the given name
   *
   * @param name name of the S3 bucket.
   */
  public void removeS3Bucket(
      String name, AWSCredentialsProvider awsCredentialsProvider, String region) {
    AmazonS3 simpleStorage =
        AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(awsCredentialsProvider)
            .build();

    ObjectListing objects = simpleStorage.listObjects(name);

    do {
      for (S3ObjectSummary summary : objects.getObjectSummaries()) {
        simpleStorage.deleteObject(name, summary.getKey());
      }

      objects = simpleStorage.listNextBatchOfObjects(objects);
    } while (objects.isTruncated());

    simpleStorage.deleteBucket(name);

    AmazonS3Waiters simpleStorageWaiter = simpleStorage.waiters();
    simpleStorageWaiter.bucketNotExists().run(new WaiterParameters<>(new HeadBucketRequest(name)));
  }

  /**
   * Checks if the selected credentials are valid.
   *
   * @return result of credentials validation.
   */
  public boolean isCallerValid(AWSCredentialsProvider awsCredentialsProvider, String region) {
    try {
      return !Objects.isNull(
          AWSSecurityTokenServiceClientBuilder.standard()
              .withRegion(region)
              .withCredentials(awsCredentialsProvider)
              .build()
              .getCallerIdentity(new GetCallerIdentityRequest())
              .getArn());
    } catch (AWSSecurityTokenServiceException e) {
      return false;
    }
  }
}
