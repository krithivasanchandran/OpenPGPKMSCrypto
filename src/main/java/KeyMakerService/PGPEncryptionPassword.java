package KeyMakerService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PGPEncryptionPassword {

	public static Map<String, byte[]> pinto(String decodedSecretkey) throws Exception {
		
		
		final String filePath = "C:/yourInputfilePath";
		final String encryptedFilePath = "C:/EncryptedOutputFilePath";
		final String userId = "user_name_v1";
		
		Map<String, byte[]> encodedBytes = new HashMap<String, byte[]>();

		FileInputStream fin = null;
		File file = new File(filePath);
		byte fileContent[] = new byte[(int) file.length()];

		try {
			fin = new FileInputStream(filePath);
			fileContent = Files.readAllBytes(file.toPath());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		PubKeyRing publicKeyRing = new PubKeyRing(
				new String(Base64.getDecoder()
						.decode(decodedSecretkey.getBytes())));

		ByteArrayOutputStream byteArrayOutputStream = PGPUitls.encrypt(
				fileContent, publicKeyRing.getPGPPublicKey(),
				filePath, true, true,
				userId);

		try (OutputStream outputStream = new FileOutputStream(
				encryptedFilePath)) {
			byteArrayOutputStream.writeTo(outputStream);
		}catch(Exception exception){
			System.out.println(" Writing Output File to the Path " + exception.getMessage());
		}
		
		
		String str = new String(byteArrayOutputStream.toByteArray());
		System.out.println("Encrypted Text is :: " + str);

		encodedBytes.put("encrypted", byteArrayOutputStream.toByteArray());
		
		encodedBytes.put("decrypted", byteArrayOutputStream.toByteArray());

		return encodedBytes;

	}
	
}
