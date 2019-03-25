package KeyMakerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.SignatureSubpacket;
import org.bouncycastle.bcpg.SignatureSubpacketTags;
import org.bouncycastle.bcpg.sig.PreferredAlgorithms;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketVector;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;

public class PGPUitls {
	/**
	 * Decrypt the passed in message stream
	 * Decryption working without passphrase - Private key.
	 */

	public static void decryptFile(InputStream in2, OutputStream out,
			PGPSecretKey pKey) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		InputStream in = PGPUtil.getDecoderStream(in2);

		PGPObjectFactory pgpF = new PGPObjectFactory(in,
				new BcKeyFingerprintCalculator());
		PGPEncryptedDataList enc;

		Object o = pgpF.nextObject();
		//
		// the first object might be a PGP marker packet.
		//
		if (o instanceof PGPEncryptedDataList) {
			enc = (PGPEncryptedDataList) o;
		} else {
			enc = (PGPEncryptedDataList) pgpF.nextObject();
		}

		//
		// find the secret key
		//
		Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();

		PGPPublicKeyEncryptedData pbe = null;
		PGPPrivateKey sKey = null;

		while (it.hasNext()) {

			pbe = it.next();
			System.out.println(pbe.getKeyID());
			PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(
					new BcPGPDigestCalculatorProvider())
							.build("".toCharArray());

			sKey = pKey.extractPrivateKey(decryptor);

		}

		if (sKey == null) {
			throw new IllegalArgumentException(
					"Secret key for message not found.");
		}

		InputStream clear = pbe
				.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));

		PGPObjectFactory plainFact = new PGPObjectFactory(clear,
				new BcKeyFingerprintCalculator());

		Object message = plainFact.nextObject();

		if (message instanceof PGPCompressedData) {
			PGPCompressedData cData = (PGPCompressedData) message;
			PGPObjectFactory pgpFact = new PGPObjectFactory(
					cData.getDataStream(), new BcKeyFingerprintCalculator());

			message = pgpFact.nextObject();
		}

		if (message instanceof PGPLiteralData) {
			PGPLiteralData ld = (PGPLiteralData) message;
			InputStream unc = ld.getDataStream();
			byte[] buffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = unc.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} else if (message instanceof PGPOnePassSignatureList) {
			throw new PGPException(
					"Encrypted message contains a signed message - not literal data.");
		} else {
			throw new PGPException(
					"Message is not a simple encrypted file - type unknown.");
		}

		if (pbe.isIntegrityProtected()) {
			if (!pbe.verify()) {
				throw new PGPException("Message failed integrity check");
			}
		}
	}

	public static ByteArrayOutputStream encrypt(byte[] clearData,
			PGPPublicKey encKey, String fileName, boolean withIntegrityCheck,
			boolean armor, String userId) throws IOException, PGPException,
					NoSuchProviderException {

		if (fileName == null) {
			fileName = PGPLiteralData.CONSOLE;
		}

		ByteArrayOutputStream encOut = new ByteArrayOutputStream();

		OutputStream out = encOut;
		if (armor) {
			out = new ArmoredOutputStream(encOut);
		}

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(
				PGPCompressedDataGenerator.ZIP);
		OutputStream cos = comData.open(bOut); // open it with the final
		// destination
		PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();

		// we want to generate compressed data. This might be a user option
		// later,
		// in which case we would pass in bOut.
		OutputStream pOut = lData.open(cos, // the compressed output stream
				PGPLiteralData.BINARY, fileName, // "filename" to store
				clearData.length, // length of clear data
				new Date() // current time
		);
		pOut.write(clearData);

		lData.close();
		comData.close();

		BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(
				getPreferredSymmetricAlgorithm(userId, encKey));
		dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
		dataEncryptor.setSecureRandom(new SecureRandom());

		PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
				dataEncryptor);
		encryptedDataGenerator
				.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(encKey));

		byte[] bytes = bOut.toByteArray();
		System.out.println(new String(bytes));

		OutputStream cOut = encryptedDataGenerator.open(out, bytes.length);

		cOut.write(bytes); // obtain the actual bytes from the compressed stream

		cOut.close();

		out.close();

		return encOut;
	}

	private static int getPreferredSymmetricAlgorithm(String userId,
			PGPPublicKey encKey) {
		int[] preferredSymmetricAlgorithms = { PGPEncryptedData.TRIPLE_DES };

		if (encKey.isMasterKey()) {
			@SuppressWarnings("rawtypes")
			Iterator v = encKey.getSignatures();
			while (v.hasNext()) {
				PGPSignature sig = (PGPSignature) v.next();
				PGPSignatureSubpacketVector hashedSubPackets = sig
						.getHashedSubPackets();
				SignatureSubpacket p = hashedSubPackets
						.getSubpacket(
								SignatureSubpacketTags.PREFERRED_SYM_ALGS);
				if (p != null) {
					preferredSymmetricAlgorithms = ((PreferredAlgorithms) p)
							.getPreferences();
				}
			}
		}
		return preferredSymmetricAlgorithms[0];
	}

}
