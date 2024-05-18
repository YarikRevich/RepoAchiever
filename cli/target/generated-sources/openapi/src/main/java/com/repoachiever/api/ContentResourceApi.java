package com.repoachiever.api;

import com.repoachiever.ApiClient;

import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.model.ContentWithdrawal;
import java.io.File;

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
public class ContentResourceApi {
    private ApiClient apiClient;

    public ContentResourceApi() {
        this(new ApiClient());
    }

    @Autowired
    public ContentResourceApi(ApiClient apiClient) {
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
     * <p><b>204</b> - Given content configuration was successfully applied
     * <p><b>400</b> - Given content configuration was not applied
     * @param contentApplication Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ContentApplyPostRequestCreation(ContentApplication contentApplication) throws WebClientResponseException {
        Object postBody = contentApplication;
        // verify the required parameter 'contentApplication' is set
        if (contentApplication == null) {
            throw new WebClientResponseException("Missing the required parameter 'contentApplication' when calling v1ContentApplyPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/v1/content/apply", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully applied
     * <p><b>400</b> - Given content configuration was not applied
     * @param contentApplication Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> v1ContentApplyPost(ContentApplication contentApplication) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentApplyPostRequestCreation(contentApplication).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully applied
     * <p><b>400</b> - Given content configuration was not applied
     * @param contentApplication Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> v1ContentApplyPostWithHttpInfo(ContentApplication contentApplication) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentApplyPostRequestCreation(contentApplication).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully applied
     * <p><b>400</b> - Given content configuration was not applied
     * @param contentApplication Content configuration application
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ContentApplyPostWithResponseSpec(ContentApplication contentApplication) throws WebClientResponseException {
        return v1ContentApplyPostRequestCreation(contentApplication);
    }
    /**
     * 
     * 
     * <p><b>201</b> - Content with the given configuration was successfully deleted
     * <p><b>400</b> - Content with the given configuration was not deleted
     * @param contentCleanup Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ContentCleanPostRequestCreation(ContentCleanup contentCleanup) throws WebClientResponseException {
        Object postBody = contentCleanup;
        // verify the required parameter 'contentCleanup' is set
        if (contentCleanup == null) {
            throw new WebClientResponseException("Missing the required parameter 'contentCleanup' when calling v1ContentCleanPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/v1/content/clean", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content with the given configuration was successfully deleted
     * <p><b>400</b> - Content with the given configuration was not deleted
     * @param contentCleanup Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> v1ContentCleanPost(ContentCleanup contentCleanup) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentCleanPostRequestCreation(contentCleanup).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content with the given configuration was successfully deleted
     * <p><b>400</b> - Content with the given configuration was not deleted
     * @param contentCleanup Content configuration application
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> v1ContentCleanPostWithHttpInfo(ContentCleanup contentCleanup) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentCleanPostRequestCreation(contentCleanup).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>201</b> - Content with the given configuration was successfully deleted
     * <p><b>400</b> - Content with the given configuration was not deleted
     * @param contentCleanup Content configuration application
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ContentCleanPostWithResponseSpec(ContentCleanup contentCleanup) throws WebClientResponseException {
        return v1ContentCleanPostRequestCreation(contentCleanup);
    }
    /**
     * 
     * 
     * <p><b>200</b> - A content was successfully retrieved
     * @param location Name of content location to be downloaded
     * @return File
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ContentDownloadGetRequestCreation(String location) throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "location", location));

        final String[] localVarAccepts = { 
            "application/octet-stream"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<File>() {};
        return apiClient.invokeAPI("/v1/content/download", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A content was successfully retrieved
     * @param location Name of content location to be downloaded
     * @return File
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<File> v1ContentDownloadGet(String location) throws WebClientResponseException {
        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<File>() {};
        return v1ContentDownloadGetRequestCreation(location).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A content was successfully retrieved
     * @param location Name of content location to be downloaded
     * @return ResponseEntity&lt;File&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<File>> v1ContentDownloadGetWithHttpInfo(String location) throws WebClientResponseException {
        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<File>() {};
        return v1ContentDownloadGetRequestCreation(location).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - A content was successfully retrieved
     * @param location Name of content location to be downloaded
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ContentDownloadGetWithResponseSpec(String location) throws WebClientResponseException {
        return v1ContentDownloadGetRequestCreation(location);
    }
    /**
     * 
     * 
     * <p><b>204</b> - A list of all available content
     * @param contentRetrievalApplication Content retrieval application
     * @return ContentRetrievalResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ContentPostRequestCreation(ContentRetrievalApplication contentRetrievalApplication) throws WebClientResponseException {
        Object postBody = contentRetrievalApplication;
        // verify the required parameter 'contentRetrievalApplication' is set
        if (contentRetrievalApplication == null) {
            throw new WebClientResponseException("Missing the required parameter 'contentRetrievalApplication' when calling v1ContentPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<ContentRetrievalResult> localVarReturnType = new ParameterizedTypeReference<ContentRetrievalResult>() {};
        return apiClient.invokeAPI("/v1/content", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - A list of all available content
     * @param contentRetrievalApplication Content retrieval application
     * @return ContentRetrievalResult
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ContentRetrievalResult> v1ContentPost(ContentRetrievalApplication contentRetrievalApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ContentRetrievalResult> localVarReturnType = new ParameterizedTypeReference<ContentRetrievalResult>() {};
        return v1ContentPostRequestCreation(contentRetrievalApplication).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - A list of all available content
     * @param contentRetrievalApplication Content retrieval application
     * @return ResponseEntity&lt;ContentRetrievalResult&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<ContentRetrievalResult>> v1ContentPostWithHttpInfo(ContentRetrievalApplication contentRetrievalApplication) throws WebClientResponseException {
        ParameterizedTypeReference<ContentRetrievalResult> localVarReturnType = new ParameterizedTypeReference<ContentRetrievalResult>() {};
        return v1ContentPostRequestCreation(contentRetrievalApplication).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - A list of all available content
     * @param contentRetrievalApplication Content retrieval application
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ContentPostWithResponseSpec(ContentRetrievalApplication contentRetrievalApplication) throws WebClientResponseException {
        return v1ContentPostRequestCreation(contentRetrievalApplication);
    }
    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully withdrawn
     * <p><b>400</b> - Given content configuration was not withdrawn
     * @param contentWithdrawal Content withdraw application. Does not remove persisted content.
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec v1ContentWithdrawDeleteRequestCreation(ContentWithdrawal contentWithdrawal) throws WebClientResponseException {
        Object postBody = contentWithdrawal;
        // verify the required parameter 'contentWithdrawal' is set
        if (contentWithdrawal == null) {
            throw new WebClientResponseException("Missing the required parameter 'contentWithdrawal' when calling v1ContentWithdrawDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/v1/content/withdraw", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully withdrawn
     * <p><b>400</b> - Given content configuration was not withdrawn
     * @param contentWithdrawal Content withdraw application. Does not remove persisted content.
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> v1ContentWithdrawDelete(ContentWithdrawal contentWithdrawal) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentWithdrawDeleteRequestCreation(contentWithdrawal).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully withdrawn
     * <p><b>400</b> - Given content configuration was not withdrawn
     * @param contentWithdrawal Content withdraw application. Does not remove persisted content.
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> v1ContentWithdrawDeleteWithHttpInfo(ContentWithdrawal contentWithdrawal) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return v1ContentWithdrawDeleteRequestCreation(contentWithdrawal).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>204</b> - Given content configuration was successfully withdrawn
     * <p><b>400</b> - Given content configuration was not withdrawn
     * @param contentWithdrawal Content withdraw application. Does not remove persisted content.
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec v1ContentWithdrawDeleteWithResponseSpec(ContentWithdrawal contentWithdrawal) throws WebClientResponseException {
        return v1ContentWithdrawDeleteRequestCreation(contentWithdrawal);
    }
}
