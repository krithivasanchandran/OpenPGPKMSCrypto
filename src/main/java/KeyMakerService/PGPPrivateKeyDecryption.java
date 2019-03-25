package KeyMakerService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.paypal.custom.integrity.IntegrityChecker;

public class PGPPrivateKeyDecryption {

	public static Map<String, String> decryptPGP(String privateKey) throws Exception {

		String filePath = "C:/encryptedInputFilePath";
		String decryptedFilePath = "C:/DecryptedOutputFilePath";
		final String userId = "";

		Map<String, String> encodedBytes = new HashMap<String, String>();

		SecKeyRing secretKeyRing = new SecKeyRing(new String(Base64.getDecoder().decode(privateKey.getBytes())));

		FileInputStream fin = null;
		FileOutputStream fos = null;

		try {
			fin = new FileInputStream(filePath);

			fos = new FileOutputStream(decryptedFilePath);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		PGPUitls.decryptFile(fin, fos, secretKeyRing.getPGPSecretKeyRing(userId));

		fos.close();
		
		
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		StringBuilder builder = new StringBuilder();
		 String st; 
		  while ((st = reader.readLine()) != null){
			  builder.append(st);
		  } 
		  
		  BufferedReader r = new BufferedReader(new FileReader(decryptedFilePath));
			StringBuilder decryptedText = new StringBuilder();
			 String s; 
			  while ((s = r.readLine()) != null){
				  decryptedText.append(s);
			  } 

		encodedBytes.put("encrypted", builder.toString());
		encodedBytes.put("decrypted", decryptedText.toString());

		/*
		 * Compares the contents of two files - LossLess enc/dec
		 * Character Level File Contents Comparison
		 */

		IntegrityChecker integrityCheck = new IntegrityChecker();
		boolean fileCompare = integrityCheck
				.checkFileIntegritry("C:/filePathDecryption", decryptedFilePath);

		if (fileCompare) {

			System.out.println("Success !! File comparison is same . Integrity Check is Sucess !! ");
			
			encodedBytes.put("integrity", "Success !! File comparison is same . Integrity Check is Sucess !! ");

		} else {
			
			System.out.println(" Failure !! Files are not same. Integrity Check is failure !!");
			
			encodedBytes.put("integrity", "Failure !! Files are not same. Integrity Check is failure !!");
		}

		return encodedBytes;

	}

}
