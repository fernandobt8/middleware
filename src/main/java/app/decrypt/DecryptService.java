package app.decrypt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DecryptService {

	@Autowired
	private Key key;

	private SecretKeySpec secretKeySpec;

	@PostConstruct
	public void init(){
		try {
			String privateKey = new String(Files.readAllBytes(Paths.get(key.getPrivateKey())), "UTF-8");
			String privateKeyReplace = privateKey.replaceAll("\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyReplace));

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey pk = keyFactory.generatePrivate(privateKeySpec);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pk);

			String secretKey = new String(Files.readAllBytes(Paths.get(key.getSecretKey())), "UTF-8");
			byte[] secretKeyTemp = Base64.getDecoder().decode(secretKey);
			byte[] secretKeyByte = cipher.doFinal(secretKeyTemp);

			secretKeySpec = new SecretKeySpec(secretKeyByte, 0, secretKeyByte.length, "AES");
		} catch (Exception e) {
			throw new RuntimeException("error loading decrypt files", e);
		}
	}

	public String decrypt(String body) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		return new String(cipher.doFinal(Base64.getDecoder().decode(body.getBytes())), "UTF-8");
	}
}
