package app;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import app.decrypt.DecryptService;

@Slf4j
@RestController
public class DecryptController {

	@Autowired
	private DecryptService decryptService;

	@Autowired
	private EndpointConfig config;

	@PostMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestBody String body) {
		try {
			String decrypt = decryptService.decrypt(body);

			if(!StringUtils.isEmpty(config.getEndpointUrl())) {
				HttpHeaders httpHeaders = new HttpHeaders();
				config.getHeaders().forEach(httpHeaders::add);

				HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
				httpRequestFactory.setConnectTimeout(5000);
				httpRequestFactory.setReadTimeout(30000);

				ResponseEntity<String> responseEntity = new RestTemplate(httpRequestFactory)
						.exchange(config.getEndpointUrl(),
								HttpMethod.POST,
								new HttpEntity<>(decrypt, httpHeaders),
								String.class);

				if (HttpStatus.Series.SUCCESSFUL.equals(responseEntity.getStatusCode().series())) {
					return ResponseEntity.ok().body("Item descriptografado e enviado com sucesso");
				} else {
					log.error("", responseEntity.getBody());
					return ResponseEntity.badRequest().body(responseEntity.getBody());
				}
			} else {
				log.error("request received without url to send: {}", decrypt);
				return ResponseEntity.ok().body("Item descriptografado, mas não enviado");
			}
		} catch (Exception e) {
			log.error("", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
