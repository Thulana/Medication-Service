package com.client_name.medication.configuration.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up the database connection
 *     * currently using SQLite as the Database
 *     * Switching to different database is easy as adding dependencies and modifying this class with relevant configurations
 */
@Configuration
public class DataSourceConfiguration {

    @Value("${db.driverClassName}")
    String driverClassName;

    @Value("${db.url}")
    String dbUrl;

    @Value("${db.username}")
    String dbUser;

    @Value("${db.password}")
    String dbPassword;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}
