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
import java.util.Optional;

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
     * @param additional given content additional option.
     * @param provider given provider used for content retrieval.
     * @param exporter given exporter used for content retrieval.
     * @param secret   given secret, which allows content retrieval.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String location, Boolean additional, Integer provider, Optional<Integer> exporter, Integer secret) throws RepositoryOperationFailureException {
        String query;

        if (exporter.isPresent()) {
            query = String.format(
                    "INSERT INTO %s (location, additional, provider, exporter, secret) VALUES ('%s', %b, %d, %d, %d)",
                    properties.getDatabaseContentTableName(),
                    location,
                    additional,
                    provider,
                    exporter.get(),
                    secret);
        } else {
            query = String.format(
                    "INSERT INTO %s (location, additional, provider, secret) VALUES ('%s', %b, %d, %d)",
                    properties.getDatabaseContentTableName(),
                    location,
                    additional,
                    provider,
                    secret);
        }

        try {
            repositoryExecutor.performQuery(query);

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves all the persisted content entities with the given provider and secret.
     *
     * @return retrieved content entities.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public List<ContentEntity> findByProviderAndSecret(Integer provider, Integer secret) throws
            RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.id, t.location, t.additional, t.exporter FROM %s as t WHERE t.provider = %d AND t.secret = %d",
                                    properties.getDatabaseContentTableName(),
                                    provider,
                                    secret));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        List<ContentEntity> result = new ArrayList<>();

        Integer id;
        String location;
        Boolean additional;
        Integer exporterRaw;
        Optional<Integer> exporter;

        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                location = resultSet.getString("location");
                additional = resultSet.getBoolean("additional");

                exporterRaw = resultSet.getInt("exporter");
                if (resultSet.wasNull()) {
                    exporter = Optional.empty();
                } else {
                    exporter = Optional.of(exporterRaw);
                }

                result.add(ContentEntity.of(id, location, additional, provider, exporter, secret));
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
                                    "SELECT t.id, t.location, t.additional, t.provider, t.exporter, t.secret FROM %s as t",
                                    properties.getDatabaseContentTableName()));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        List<ContentEntity> result = new ArrayList<>();

        Integer id;
        String location;
        Boolean additional;
        Integer provider;
        Integer exporterRaw;
        Optional<Integer> exporter;
        Integer secret;

        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                location = resultSet.getString("location");
                additional = resultSet.getBoolean("additional");
                provider = resultSet.getInt("provider");

                exporterRaw = resultSet.getInt("exporter");
                if (resultSet.wasNull()) {
                    exporter = Optional.empty();
                } else {
                    exporter = Optional.of(exporterRaw);
                }

                secret = resultSet.getInt("secret");

                result.add(ContentEntity.of(id, location, additional, provider, exporter, secret));
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

    /**
     * Deletes all entities with the given secret from content table.
     *
     * @param secret given secret, which allows content retrieval.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void deleteBySecret(Integer secret) throws RepositoryOperationFailureException {
        try {
            repositoryExecutor.performQuery(
                    String.format(
                            "DELETE FROM %s as t WHERE t.secret = %d",
                            properties.getDatabaseContentTableName(),
                            secret));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }
}
