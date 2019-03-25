package CryptoService;

import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

public class FileEncryptionService {

	public static byte[] createKeyAgreeEncryptedObject(PGPPublicKey recipientKey, byte[] data)
			throws Exception {
		
		if(recipientKey == null){
			System.out.println("The public key is null");
			throw new Exception();
		}
		// we save the data to be encrypted in PGP format here
		Security.addProvider(new BouncyCastleProvider());
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
		OutputStream pOut = lData.open(bOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length, new Date());
		pOut.write(data);
		pOut.close();
		byte[] plainText = bOut.toByteArray();
		// now we encrypt it
		ByteArrayOutputStream encOut = new ByteArrayOutputStream();
		PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
				new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256).setWithIntegrityPacket(true)
						.setSecureRandom(new SecureRandom())
						.setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()));
		encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(recipientKey).setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()));
		OutputStream cOut = encGen.open(encOut, plainText.length);
		cOut.write(plainText);
		cOut.close();
		return encOut.toByteArray();
	}
	
}
