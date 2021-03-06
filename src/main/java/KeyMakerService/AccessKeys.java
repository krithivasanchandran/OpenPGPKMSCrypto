package KeyMakerService;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.common.keymaker.response.KeyMakerJsonResponse;
import com.common.keymaker.response.Nonkey;
import com.common.keymaker.response.Nonkey_;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.keymaker.security.KmsClientConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/*
 * Fetching BootStrap Token : Bootstrap Token from KeyMaker (KMS or Google Cloud Key Management Service)
 *                            256 or 512 character String - AES 256 Encoded.
 *                    
 * Note : The bootstrap keys rotate for every 30 minutes.
 *                            
 * 
 * KeyMaker QA Endpoint : QA End point: example.vcd.live.amazoninc.com:443
 * 
 * example.vcd.live.amazoninc.com.com  -> QA Endpoint.
 * 
 */

public class AccessKeys {

	private final static Logger accesslogger = Logger
			.getLogger(AccessKeys.class.getName());
	private final static String enabled = "enabled";
	private final static String keymakerQAEndpoint = "https://yourKeymakerURL.com/keyobject/all";
	private final static String httpHeaderKeyMakerBootStrapKeys = "HTTP-HEADER-SET-AS-CONTENT-TYPE";

	public Map<String, String> accessKeyMakerKeys(String appname)
			throws Exception {

		Properties keymakerProperties = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(
					"yourlocalPath/keymaker.properties");

			// Load the properties file
			keymakerProperties.load(input);

			final String bootstrapTokenID = keymakerProperties
					.getProperty("bootstraptoken").trim();

			System.out.println("Output :: " + bootstrapTokenID);

			return queryKeyMaker(bootstrapTokenID, appname);

		} catch (IOException e) {
			System.out
					.println(
							"IO Exception Occured when trying to read the keymaker.properties file "
									+ e.getMessage());
			accesslogger
					.warning(
							"IO Exception Occured when trying to read the keymaker.properties file "
									+ e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ex) {
					
					accesslogger.warning(ex.getMessage());
					ex.getMessage();
				}
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		queryKeyMaker(
				"",
				"yourApplicationName");

	}

	public static Map<String, String> queryKeyMaker(final String bootstrapKeys,
			final String applicationname) throws Exception {

		Map<String, String> keyMakerAPIResponse = new LinkedHashMap<String, String>();

		Client client = KmsClientConfig.getClient();

		WebResource res = client.resource(keymakerQAEndpoint);

		/*
		 * KeyMaker QA Endpoint Restful Service . Please make sure Bootstrap
		 * keys generated from here
		 * https://www.example.com/mykeys?app_name=
		 * if else the keys rotate for every 30 minutes.
		 * Get a new key if fails !!
		 */

		String response = res.accept(MediaType.APPLICATION_JSON)
				.header(httpHeaderKeyMakerBootStrapKeys, bootstrapKeys)
				.get(String.class);

		// KeymakerPGPExtractor pgpextractor = new KeymakerPGPExtractor();
		// System.out.println("Getting into processkeydata method :: ");
		// pgpextractor.processKeyData(response);
		// System.out.println("Getting out of processkeydata method :: ");

		ObjectMapper oj = new ObjectMapper();
		KeyMakerJsonResponse jsonResponse = oj.readValue(response,
				KeyMakerJsonResponse.class);

		List<Nonkey> nonkey = jsonResponse.getNonkeys();

		//Private Key - PGP 
		String finapp_test_private = "";
		
		//Public Key - PGP
		String finapp_test_public = "";
		
		//Public Password 
		String finapp_test_pass = "";

		for (Nonkey t : nonkey) {

			Nonkey_ nKey = t.getNonkey();

			if (nKey.getState().equalsIgnoreCase("disabled")) {
				continue;
			}

			String appname = nKey.getName();
			String state = nKey.getState();

			try {

				if (appname.equalsIgnoreCase("kcan_personal_pub"))
					finapp_test_private = nKey.getEncodedKeyData();
				if (appname.equalsIgnoreCase("finapp_test_public"))
					finapp_test_public = nKey.getEncodedKeyData();

				if (enabled.equalsIgnoreCase(state)
						&& (appname.equalsIgnoreCase(applicationname))) {

					accesslogger.info(
							"KeyMaker Secret Fetch is Enabled :: " + state);

					accesslogger.info(
							"Printing the app details of the enabled keys ");

					System.out.println("The json response is ================"
							+ "\n" + nKey.toString());
					
					// Response from the keymaker JWT Token
					// Converting from byteArray to String (OR) String to byteArray
					// Will lead to Key Tampering . Caution applied to not do that.

					String base64EcodedKey = nKey.getEncodedKeyData();

				        byte[] byteArray = Base64.decode(base64EcodedKey.getBytes());

					// Converting byte[] to String

					String decodedSecretKey = new String(byteArray);

					String encodeformat = nKey.getEncodeFormat();
					boolean isExportable = nKey.getExportableToApp();
					boolean isEncrypted = nKey.getEncrypted();
					String keyvalidity = nKey.getValidTo();
					String appName = appname;
					String attributes = nKey.getAttributes();
					String STATE = state;
					int version = nKey.getVersion();
					String encodedKeyData = nKey.getEncodedKeyData();

					keyMakerAPIResponse.put("encodeformat", encodeformat);
					keyMakerAPIResponse.put("exportable", isExportable + "");
					keyMakerAPIResponse.put("encryptable", isEncrypted + "");
					keyMakerAPIResponse.put("keyvalidity", keyvalidity);
					keyMakerAPIResponse.put("appname", appName);
					keyMakerAPIResponse.put("attributes", attributes);
					keyMakerAPIResponse.put("state", STATE);
					keyMakerAPIResponse.put("version", version + "");
					keyMakerAPIResponse.put("encodedKeyData", base64EcodedKey);

				} else {

					accesslogger
							.warning("KeyMaker Secret Key Fetch is disabled "
									+ AccessKeys.class.getName());

					accesslogger.info(
							"Printing the Key details of the disabled Keys");

					System.out.println("The json response is ================"
							+ "\n" + nKey.toString());

				}

			} catch (Exception exception) {

				String error = String.format(
						"App: %s failed to decrypt. Throwing IdDecryptionException. Exception message: %s",
						nKey.getName(), exception.getMessage());

				throw new RuntimeException(error, exception);
			}
		}

		return keyMakerAPIResponse;
	}
}
