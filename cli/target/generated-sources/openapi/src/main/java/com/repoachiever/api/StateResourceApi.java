package com.repoachiever.api;

import com.repoachiever.ApiClient;

import com.repoachiever.model.ContentStateApplication;
import com.repoachiever.model.ContentStateApplicationResult;

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

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-17T10:39:25.510760+02:00[Europe/Warsaw]")
public class StateResourceApi {
    private ApiClient apiClient;

    public StateResourceApi() {
        this(new ApiClient());
    }

    @Autowired
    public StateResourceApi(ApiClient apiClient) {
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
     * <p><b>201</b> - Content state hash is retrieved successfully
     * @param contentStateApplication Given content state key
     * @return ContentStateApplicationResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1StateContentPostRequestCreation(ContentStateApplication contentStateApplication) throws WebClientResponseException {
        Object postBody = contentStateApplication;
        // verify the required parameter 'contentStateApplication' is set
        if (contentStateApplication == null) {
            throw new WebClientResponseException("Missing the required parameter 'contentStateApplication' when calling v1StateContentPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<ContentStateApplicationResult> localVarReturnType = new ParameterizedTypeReference<ContentStateApplicationResult>() {};
        return apiClient.invokeAPI("/v1/state/content", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content state hash is retrieved successfully
     * @param contentStateApplication Given content state key
     * @return ContentStateApplicationResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ContentStateApplicationResult> v1StateContentPost(ContentStateApplication contentStateApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ContentStateApplicationResult> localVarReturnType = new ParameterizedTypeReference<ContentStateApplicationResult>() {};
        return v1StateContentPostRequestCreation(contentStateApplication).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content state hash is retrieved successfully
     * @param contentStateApplication Given content state key
     * @return ResponseEntity&lt;ContentStateApplicationResult&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<ContentStateApplicationResult>> v1StateContentPostWithHttpInfo(ContentStateApplication contentStateApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ContentStateApplicationResult> localVarReturnType = new ParameterizedTypeReference<ContentStateApplicationResult>() {};
        return v1StateContentPostRequestCreation(contentStateApplication).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content state hash is retrieved successfully
     * @param contentStateApplication Given content state key
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1StateContentPostWithResponseSpec(ContentStateApplication contentStateApplication) throws WebClientResponseException {
        return v1StateContentPostRequestCreation(contentStateApplication);
    }
}
