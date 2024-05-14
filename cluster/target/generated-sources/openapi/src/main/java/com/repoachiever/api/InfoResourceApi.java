package com.repoachiever.api;

import com.repoachiever.ApiClient;

import com.repoachiever.model.ClusterInfoUnit;
import com.repoachiever.model.VersionInfoResult;

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
public class InfoResourceApi {
    private ApiClient apiClient;

    public InfoResourceApi() {
        this(new ApiClient());
    }

    @Autowired
    public InfoResourceApi(ApiClient apiClient) {
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
     * <p><b>200</b> - General information about running clusters
     * @return List&lt;ClusterInfoUnit&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1InfoClusterGetRequestCreation() throws WebClientResponseException {
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

        ParameterizedTypeReference<ClusterInfoUnit> localVarReturnType = new ParameterizedTypeReference<ClusterInfoUnit>() {};
        return apiClient.invokeAPI("/v1/info/cluster", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running clusters
     * @return List&lt;ClusterInfoUnit&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<ClusterInfoUnit> v1InfoClusterGet() throws WebClientResponseException {
        ParameterizedTypeReference<ClusterInfoUnit> localVarReturnType = new ParameterizedTypeReference<ClusterInfoUnit>() {};
        return v1InfoClusterGetRequestCreation().bodyToFlux(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running clusters
     * @return ResponseEntity&lt;List&lt;ClusterInfoUnit&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<ClusterInfoUnit>>> v1InfoClusterGetWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<ClusterInfoUnit> localVarReturnType = new ParameterizedTypeReference<ClusterInfoUnit>() {};
        return v1InfoClusterGetRequestCreation().toEntityList(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running clusters
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1InfoClusterGetWithResponseSpec() throws WebClientResponseException {
        return v1InfoClusterGetRequestCreation();
    }
    /**
     * 
     * 
     * <p><b>200</b> - A set of Prometheus samples used by Grafana instance
     * @return String
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1InfoTelemetryGetRequestCreation() throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "text/plain"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI("/v1/info/telemetry", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A set of Prometheus samples used by Grafana instance
     * @return String
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<String> v1InfoTelemetryGet() throws WebClientResponseException {
        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return v1InfoTelemetryGetRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A set of Prometheus samples used by Grafana instance
     * @return ResponseEntity&lt;String&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<String>> v1InfoTelemetryGetWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return v1InfoTelemetryGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A set of Prometheus samples used by Grafana instance
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1InfoTelemetryGetWithResponseSpec() throws WebClientResponseException {
        return v1InfoTelemetryGetRequestCreation();
    }
    /**
     * 
     * 
     * <p><b>200</b> - General information about running API Server
     * @return VersionInfoResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1InfoVersionGetRequestCreation() throws WebClientResponseException {
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

        ParameterizedTypeReference<VersionInfoResult> localVarReturnType = new ParameterizedTypeReference<VersionInfoResult>() {};
        return apiClient.invokeAPI("/v1/info/version", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running API Server
     * @return VersionInfoResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<VersionInfoResult> v1InfoVersionGet() throws WebClientResponseException {
        ParameterizedTypeReference<VersionInfoResult> localVarReturnType = new ParameterizedTypeReference<VersionInfoResult>() {};
        return v1InfoVersionGetRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running API Server
     * @return ResponseEntity&lt;VersionInfoResult&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<VersionInfoResult>> v1InfoVersionGetWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<VersionInfoResult> localVarReturnType = new ParameterizedTypeReference<VersionInfoResult>() {};
        return v1InfoVersionGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - General information about running API Server
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1InfoVersionGetWithResponseSpec() throws WebClientResponseException {
        return v1InfoVersionGetRequestCreation();
    }
}
