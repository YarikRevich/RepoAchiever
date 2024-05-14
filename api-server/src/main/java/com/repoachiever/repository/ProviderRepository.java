package com.repoachiever.repository;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.entity.repository.ConfigEntity;
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

/**
 * Represents repository implementation to handle provider table.
 */
@ApplicationScoped
public class ProviderRepository {
    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryExecutor repositoryExecutor;

    /**
     * Inserts given values into the provider table.
     *
     * @param name given provider name.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String name) throws RepositoryOperationFailureException {
        try {
            repositoryExecutor.performQuery(
                    String.format(
                            "INSERT INTO %s (name) VALUES ('%s')",
                            properties.getDatabaseProviderTableName(),
                            name));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if provider entity with the given name is present.
     *
     * @param name given name of the provider.
     * @return result of the check.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public Boolean isPresentByName(String name) throws RepositoryOperationFailureException {
        try {
            ResultSet resultSet = repositoryExecutor.performQueryWithResult(
                    String.format(
                            "SELECT t.id FROM %s as t WHERE t.name = '%s'",
                            properties.getDatabaseProviderTableName(),
                            name));

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
     * Attempts to retrieve provider entity by the given name.
     *
     * @param name given name of the provider.
     * @return retrieved config entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public ProviderEntity findByName(String name) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.id FROM %s as t WHERE t.name = '%s'",
                                    properties.getDatabaseProviderTableName(),
                                    name));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        Integer id;

        try {
            id = resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return ProviderEntity.of(id, name);
    }

    /**
     * Attempts to retrieve provider entity by the given identificator.
     *
     * @param id given identificator of the configuration.
     * @return retrieved provider entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public ProviderEntity findById(Integer id) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.name FROM %s as t WHERE t.id = '%s'",
                                    properties.getDatabaseProviderTableName(),
                                    id));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        String name;

        try {
            name = resultSet.getString("name");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return ProviderEntity.of(id, name);
    }
}
