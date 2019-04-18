package app;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

	@Value("${url.send.decrypt.data}")
	private String url;

	@PostMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestBody String body){
		try {
			String decrypt = decryptService.decrypt(body);

			HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
			httpRequestFactory.setConnectTimeout(5000);
			httpRequestFactory.setReadTimeout(30000);

			ResponseEntity<String> responseEntity = new RestTemplate(httpRequestFactory).postForEntity(url, decrypt, String.class);

			if(HttpStatus.Series.SUCCESSFUL.equals(responseEntity.getStatusCode().series())){
				return ResponseEntity.ok().body("Item processado com sucesso");
			} else {
				log.error("", responseEntity.getBody());
				return ResponseEntity.badRequest().body(responseEntity.getBody());
			}
		} catch (Exception e) {
			log.error("", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
