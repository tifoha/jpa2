package ua.tifoha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

@Configuration
@Import ( {DataConfig.class})
@ComponentScan ({"ua.tifoha"})
@PropertySource (ignoreResourceNotFound = true, value = {
		"classpath:app.properties",
		"classpath:local.app.properties"
})
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

}
