package CryptoService;

import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.util.io.Streams;

public class FileDecryptionService {

	public static byte[] extractKeyAgreeEncryptedObject(PGPPrivateKey recipientPrivateKey, byte[] pgpEncryptedData)
			throws PGPException, IOException {
		PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);
		PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpFact.nextObject();
		PGPPublicKeyEncryptedData encData = (PGPPublicKeyEncryptedData) encList.get(0);
		PublicKeyDataDecryptorFactory dataDecryptorFactory = new JcePublicKeyDataDecryptorFactoryBuilder()
				.setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()).build(recipientPrivateKey);
		
		InputStream clear = encData.getDataStream(dataDecryptorFactory);
		byte[] literalData = Streams.readAll(clear);
		
		if (encData.verify()) {
			
			PGPObjectFactory litFact = new JcaPGPObjectFactory(literalData);
			PGPLiteralData litData = (PGPLiteralData) litFact.nextObject();
			byte[] data = Streams.readAll(litData.getInputStream());
			return data;
		}
		throw new IllegalStateException("modification check failed");
	}

}
