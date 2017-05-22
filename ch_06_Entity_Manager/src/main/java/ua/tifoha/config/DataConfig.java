package ua.tifoha.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.class.name"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

//    private Properties getHibernateProperties() {
//        Properties properties = new Properties();
//        properties.put("hibernate.show_sql", "true");
//        properties.put("hibernate.format_sql", "true");
//        properties.put("hibernate.generate_statistics", "true");
//        properties.put("hibernate.hbm2ddl.auto", "update");
//        properties.put("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
//
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//        return properties;
//    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setDatabase(env.getProperty("em.database", Database.class));
        vendorAdapter.setGenerateDdl(env.getProperty("em.generate.ddl", Boolean.class, true));
        vendorAdapter.setShowSql(env.getProperty("em.show.sql", Boolean.class, true));
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.create-target", "/Users/user/IdeaProjects/craft-serv/server-config/1/create.sql");
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.drop-target", "/Users/user/IdeaProjects/craft-serv/server-config/1/drop.sql");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("ua.tifoha");
        factory.setDataSource(dataSource);

        return factory;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManager) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManager);
        return txManager;
    }
}
