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
     * @param name given provider name.
     * @param session given internal secret.
     * @param token given optional external secret.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String name, Integer session, Optional<String> token) throws RepositoryOperationFailureException {
        try {
            String query;

            if (token.isPresent()) {
                query = String.format(
                        "INSERT INTO %s (name, session, token) VALUES ('%s', %d, '%s')",
                        properties.getDatabaseSecretTableName(),
                        name,
                        session,
                        token.get());
            } else {
                query = String.format(
                        "INSERT INTO %s (name, session) VALUES ('%s', %d)",
                        properties.getDatabaseSecretTableName(),
                        name,
                        session);
            }

            repositoryExecutor.performQuery(query);

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Attempts to retrieve secret entity by the given id.
     *
     * @param id given identification of the secrets set.
     * @return retrieved secret entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public SecretEntity findById(Integer id) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.session, t.token FROM %s as t WHERE t.id = %s",
                                    properties.getDatabaseProviderTableName(),
                                    id));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        Integer session;

        try {
            session = resultSet.getInt("session");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        String token;

        try {
            token = resultSet.getString("token");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return SecretEntity.of(id, session, token);
    }
}
