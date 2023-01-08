package dev.mattolivarez;

import dev.mattolivarez.Filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LimestudyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimestudyBackendApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter()
	{
		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*"); // fill in port after semicolon or * for any
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		//config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		registrationBean.setFilter(new CorsFilter(source));
		registrationBean.setOrder(0);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean()
	{
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/classes/*");
		return registrationBean;
	}

}
