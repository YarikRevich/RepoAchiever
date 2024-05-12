package com.repoachiever.repository;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.entity.repository.ConfigEntity;
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
 * Represents repository implementation to handle config table.
 */
@ApplicationScoped
public class ConfigRepository {
    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryExecutor repositoryExecutor;

    /**
     * Inserts given values into the config table.
     *
     * @param name given name of the configuration.
     * @param hash given hash of the configuration.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String name, String hash) throws RepositoryOperationFailureException {
        try {
            repositoryExecutor.performQuery(
                            String.format(
                                    "INSERT INTO %s (name, hash) VALUES ('%s', '%s')",
                                    properties.getDatabaseConfigTableName(),
                                    name,
                                    hash));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if config entity with the given name is present.
     *
     * @param name given name of the configuration.
     * @return result of the check.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public Boolean isPresentByName(String name) throws RepositoryOperationFailureException {
        try {
            ResultSet resultSet = repositoryExecutor.performQueryWithResult(
                    String.format(
                            "SELECT t.id, t.hash FROM %s as t WHERE t.name = '%s'",
                            properties.getDatabaseConfigTableName(),
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
     * Attempts to retrieve config entity by the given name.
     *
     * @param name given name of the configuration.
     * @return retrieved config entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public ConfigEntity findByName(String name) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.id, t.hash FROM %s as t WHERE t.name = '%s'",
                                    properties.getDatabaseConfigTableName(),
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

        String hash;

        try {
            hash = resultSet.getString("hash");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return ConfigEntity.of(id, name, hash);
    }
}
