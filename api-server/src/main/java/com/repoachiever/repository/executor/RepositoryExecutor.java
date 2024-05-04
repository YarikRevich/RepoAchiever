package com.repoachiever.repository.executor;

import com.repoachiever.exception.QueryEmptyResultException;
import com.repoachiever.exception.QueryExecutionFailureException;
import com.repoachiever.service.cluster.resource.ClusterClientResource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Service used to perform low-level database related operations.
 */
@ApplicationScoped
public class RepositoryExecutor {
    private static final Logger logger = LogManager.getLogger(ClusterClientResource.class);

    @Inject
    DataSource dataSource;

    private Connection connection;

    @PostConstruct
    public void configure() {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.fatal(new QueryExecutionFailureException(e.getMessage()).getMessage());
        }
    }

    /**
     * Performs given SQL query without result.
     *
     * @param query given SQL query to be executed.
     * @throws QueryExecutionFailureException if query execution is interrupted by failure.
     * @throws QueryEmptyResultException if result is empty.
     */
    public void performQuery(String query) throws QueryExecutionFailureException, QueryEmptyResultException {
        Statement statement;

        try {
            statement = this.connection.createStatement();
        } catch (SQLException e) {
            throw new QueryExecutionFailureException(e.getMessage());
        }

        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new QueryExecutionFailureException(e.getMessage());
        }
    }

    /**
     * Performs given SQL query and returns raw result.
     *
     * @param query given SQL query to be executed.
     * @return retrieved raw result.
     * @throws QueryExecutionFailureException if query execution is interrupted by failure.
     * @throws QueryEmptyResultException if result is empty.
     */
    public ResultSet performQueryWithResult(String query) throws QueryExecutionFailureException, QueryEmptyResultException {
        Statement statement;

        try {
            statement = this.connection.createStatement();
        } catch (SQLException e) {
            throw new QueryExecutionFailureException(e.getMessage());
        }

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new QueryExecutionFailureException(e.getMessage());
        }

        try {
            if (!resultSet.isBeforeFirst()) {
                throw new QueryEmptyResultException();
            }
        } catch (SQLException e) {
            throw new QueryExecutionFailureException(e.getMessage());
        }

        return resultSet;
    }

    /**
     * Closes opened database connection.
     */
    @PreDestroy
    private void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }
}
