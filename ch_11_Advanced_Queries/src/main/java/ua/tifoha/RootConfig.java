package ua.tifoha;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.Map;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan ({"ua.tifoha"})
@PropertySource ("classpath:app.properties")
@EnableTransactionManagement
public class RootConfig {
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		final PropertySourcesPlaceholderConfigurer propertyPlaceholder = new PropertySourcesPlaceholderConfigurer();
		return propertyPlaceholder;
	}

//	@Bean
//	public FormattingConversionService conversionService() {
//		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
//		conversionService.addFormatter(new DurationStringFormatter());
//		conversionService.addFormatter(new GrandAuthorityFormatter());
////		conversionService.addFormatter(new GrantedAuthorityStringFormatter());
//		conversionService.addConverter(new Map2GrandAuthorityConverter());
//		return conversionService;
//	}
//
//	@Bean
//	public IdToDomainClassConverter<FormattingConversionService> idToDomainClassConverter(FormattingConversionService conversionService) {
//		return new IdToDomainClassConverter<>(conversionService);
//	}
//
//	@Bean
//	public ContentFileService contentFileService() {
//		return new ContentFileService(env.getProperty("filepath"));
//	}
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver.class.name"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

//	@Bean
//	public DataSource embeddedDerbyDataSource() {
//		return new EmbeddedDatabaseBuilder()
//				.setType(EmbeddedDatabaseType.DERBY) //.H2 or .DERBY
//				.addScript("sql/db.sql")
//				.build();
//	}
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
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

//		AbstractJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();

		vendorAdapter.setDatabase(env.getProperty("em.database", Database.class));
		vendorAdapter.setGenerateDdl(env.getProperty("em.generate.ddl", Boolean.class, true));
		vendorAdapter.setShowSql(env.getProperty("em.show.sql", Boolean.class, true));
		final Map<String, Object> jpaPropertyMap = (Map<String, Object>) vendorAdapter.getJpaPropertyMap();
		jpaPropertyMap.put("eclipselink.weaving", "true");
//		vendorAdapter.getPersistenceProvider().
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.create-target", "/Users/user/IdeaProjects/craft-serv/server-config/1/create.sql");
//        vendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.scripts.drop-target", "/Users/user/IdeaProjects/craft-serv/server-config/1/drop.sql");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("ua.tifoha");
		factory.setDataSource(dataSource());
		factory.setJpaPropertyMap(jpaPropertyMap);

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
