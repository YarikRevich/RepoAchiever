package com.repoachiever.api;

import com.repoachiever.ApiClient;

import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.ReadinessCheckApplication;
import com.repoachiever.model.ReadinessCheckResult;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-14T13:47:18.147711+02:00[Europe/Warsaw]")
public class HealthResourceApi {
    private ApiClient apiClient;

    public HealthResourceApi() {
        this(new ApiClient());
    }

    @Autowired
    public HealthResourceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @return HealthCheckResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1HealthGetRequestCreation() throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<HealthCheckResult> localVarReturnType = new ParameterizedTypeReference<HealthCheckResult>() {};
        return apiClient.invokeAPI("/v1/health", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @return HealthCheckResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<HealthCheckResult> v1HealthGet() throws WebClientResponseException {
        ParameterizedTypeReference<HealthCheckResult> localVarReturnType = new ParameterizedTypeReference<HealthCheckResult>() {};
        return v1HealthGetRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @return ResponseEntity&lt;HealthCheckResult&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<HealthCheckResult>> v1HealthGetWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<HealthCheckResult> localVarReturnType = new ParameterizedTypeReference<HealthCheckResult>() {};
        return v1HealthGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1HealthGetWithResponseSpec() throws WebClientResponseException {
        return v1HealthGetRequestCreation();
    }
    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @param readinessCheckApplication Check if API Server is ready to serve for the given user
     * @return ReadinessCheckResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ReadinessPostRequestCreation(ReadinessCheckApplication readinessCheckApplication) throws WebClientResponseException {
        Object postBody = readinessCheckApplication;
        // verify the required parameter 'readinessCheckApplication' is set
        if (readinessCheckApplication == null) {
            throw new WebClientResponseException("Missing the required parameter 'readinessCheckApplication' when calling v1ReadinessPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<ReadinessCheckResult> localVarReturnType = new ParameterizedTypeReference<ReadinessCheckResult>() {};
        return apiClient.invokeAPI("/v1/readiness", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @param readinessCheckApplication Check if API Server is ready to serve for the given user
     * @return ReadinessCheckResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ReadinessCheckResult> v1ReadinessPost(ReadinessCheckApplication readinessCheckApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ReadinessCheckResult> localVarReturnType = new ParameterizedTypeReference<ReadinessCheckResult>() {};
        return v1ReadinessPostRequestCreation(readinessCheckApplication).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @param readinessCheckApplication Check if API Server is ready to serve for the given user
     * @return ResponseEntity&lt;ReadinessCheckResult&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<ReadinessCheckResult>> v1ReadinessPostWithHttpInfo(ReadinessCheckApplication readinessCheckApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ReadinessCheckResult> localVarReturnType = new ParameterizedTypeReference<ReadinessCheckResult>() {};
        return v1ReadinessPostRequestCreation(readinessCheckApplication).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General health information about running API Server
     * @param readinessCheckApplication Check if API Server is ready to serve for the given user
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ReadinessPostWithResponseSpec(ReadinessCheckApplication readinessCheckApplication) throws WebClientResponseException {
        return v1ReadinessPostRequestCreation(readinessCheckApplication);
    }
}
