package com.repoachiever.repository;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.entity.repository.SecretEntity;
import com.repoachiever.exception.QueryEmptyResultException;
import com.repoachiever.exception.QueryExecutionFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.repository.executor.RepositoryExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * Represents repository implementation to handle secret table.
 */
@ApplicationScoped
public class SecretRepository {
    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryExecutor repositoryExecutor;

    /**
     * Inserts given values into the provider table.
     *
     * @param name    given provider name.
     * @param session given internal secret.
     * @param credentials   given optional external credentials.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String name, Integer session, Optional<String> credentials) throws RepositoryOperationFailureException {
        String query;

        if (credentials.isPresent()) {
            query = String.format(
                    "INSERT INTO %s (name, session, credentials) VALUES ('%s', %d, '%s')",
                    properties.getDatabaseSecretTableName(),
                    name,
                    session,
                    credentials.get());
        } else {
            query = String.format(
                    "INSERT INTO %s (name, session) VALUES ('%s', %d)",
                    properties.getDatabaseSecretTableName(),
                    name,
                    session);
        }

        try {
            repositoryExecutor.performQuery(query);
        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if secret entity with the given session and credentials is present.
     *
     * @param session given session of the secrets set.
     * @param credentials   given optional external credentials.
     * @return result of the check.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public Boolean isPresentBySessionAndCredentials(Integer session, Optional<String> credentials) throws RepositoryOperationFailureException {
        String query;

        if (credentials.isPresent()) {
            query = String.format(
                    "SELECT t.id FROM %s as t WHERE t.session = %d AND t.credentials = '%s'",
                    properties.getDatabaseSecretTableName(),
                    session,
                    credentials.get());
        } else {
            query = String.format(
                    "SELECT t.id FROM %s as t WHERE t.session = %d",
                    properties.getDatabaseSecretTableName(),
                    session);
        }

        try {
            ResultSet resultSet = repositoryExecutor.performQueryWithResult(query);

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
     * Attempts to retrieve secret entity by the given session and credentials.
     *
     * @param session given session of the secrets set.
     * @param credentials   given optional external credentials.
     * @return retrieved secret entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public SecretEntity findBySessionAndCredentials(Integer session, Optional<String> credentials) throws RepositoryOperationFailureException {
        String query;

        if (credentials.isPresent()) {
            query = String.format(
                    "SELECT t.id FROM %s as t WHERE t.session = %d AND t.credentials = '%s'",
                    properties.getDatabaseSecretTableName(),
                    session,
                    credentials.get());
        } else {
            query = String.format(
                    "SELECT t.id FROM %s as t WHERE t.session = %d",
                    properties.getDatabaseSecretTableName(),
                    session);
        }

        ResultSet resultSet;

        try {
            resultSet = repositoryExecutor.performQueryWithResult(query);
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

        return SecretEntity.of(id, session, credentials);
    }
}
