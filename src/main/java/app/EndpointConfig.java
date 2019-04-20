package app;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class EndpointConfig {

	private String endpointUrl;

	private Map<String, String> headers = new HashMap<>();
}
