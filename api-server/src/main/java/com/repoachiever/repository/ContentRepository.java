package com.repoachiever.repository;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.entity.repository.ContentEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.exception.QueryEmptyResultException;
import com.repoachiever.exception.QueryExecutionFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.repository.executor.RepositoryExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents repository implementation to handle content table.
 */
@ApplicationScoped
public class ContentRepository {
    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryExecutor repositoryExecutor;

    /**
     * Inserts given values into the content table.
     *
     * @param location given content location.
     * @param provider given provider used for content retrieval.
     * @param secret   given secret, which allows content retrieval.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String location, Integer provider, Integer secret) throws RepositoryOperationFailureException {
        try {
            repositoryExecutor.performQuery(
                    String.format(
                            "INSERT INTO %s (location, provider, secret) VALUES ('%s', %d, %d)",
                            properties.getDatabaseContentTableName(),
                            location,
                            provider,
                            secret));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if content entity with the given location is present.
     *
     * @param location given location of the content.
     * @return result of the check.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public Boolean isPresentByLocation(String location) throws RepositoryOperationFailureException {
        try {
            ResultSet resultSet = repositoryExecutor.performQueryWithResult(
                    String.format(
                            "SELECT t.id FROM %s as t WHERE t.location = '%s'",
                            properties.getDatabaseContentTableName(),
                            location));

            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RepositoryOperationFailureException(e.getMessage());
            }
        } catch (QueryEmptyResultException e) {
            return false;
        } catch (QueryExecutionFailureException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return true;
    }

    /**
     * Retrieves all the persisted content entities.
     *
     * @return retrieved content entities.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public List<ContentEntity> findAll() throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.id, t.location, t.provider, t.secret FROM %s",
                                    properties.getDatabaseContentTableName()));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        List<ContentEntity> result = new ArrayList<>();

        Integer id;
        String location;
        Integer provider;
        Integer secret;

        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                location = resultSet.getString("location");
                provider = resultSet.getInt("provider");
                secret = resultSet.getInt("secret");

                result.add(ContentEntity.of(id, location, provider, secret));
            }
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return result;
    }
}
