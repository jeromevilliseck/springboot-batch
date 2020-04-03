package fr.jervill.springbatch.entitiesrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration //permet d'utiliser Spring Data JDBC dans une application
@EnableJdbcRepositories //permet d'utiliser Spring Data JDBC dans une application
public class BatchRepositoryConfig extends AbstractJdbcConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    //indiquera à Spring Data JDBC où trouver la datasource de l'application.
    NamedParameterJdbcOperations operations(){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
