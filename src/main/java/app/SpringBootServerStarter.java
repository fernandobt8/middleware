package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.decrypt.Key;

@Configuration
@ComponentScan("app")
@SpringBootApplication
@EnableConfigurationProperties({ Key.class , EndpointConfig.class})
public class SpringBootServerStarter {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootServerStarter.class, args);
	}

}
