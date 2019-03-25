package KeyMakerService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.bc.BcPGPObjectFactory;

public class PubKeyRing {

	protected List<PGPPublicKeyRing> keys = new ArrayList<>();

	public PGPPublicKey getPGPPublicKey() {
		return keys.get(0).getPublicKey();
	}

	/**
	 * Loads all keys from the specified armored text.
	 */
	public PubKeyRing(String armor) throws IOException, PGPException {

		load(armor);
	}

	/**
	 * Loads all keys from the specified file.
	 */
	public PubKeyRing(File file) throws IOException, PGPException {

		load(file);
	}

	/**
	 * Loads all keys from the specified input stream.
	 */
	public PubKeyRing(InputStream stream) throws IOException, PGPException {

		load(stream);
	}

	/**
	 * Display string for this ring, including listing each key on the ring,
	 * with each subkey's usage flags, short ID, and user IDs.
	 */
	@Override
	public String toString() {

		StringBuilder b = new StringBuilder();
		int count = 0;
		for (PGPPublicKeyRing key : keys) {
			if (count++ > 0)
				b.append("\n\n");
			b.append(key.toString());
		}
		return b.toString();
	}

	/**
	 * Loads all keys from the specified armored text, and adds them to this
	 * ring's existing list of keys.
	 */
	public List<PGPPublicKeyRing> load(String armor)
			throws IOException, PGPException {
		return load(new ByteArrayInputStream(armor.getBytes("ASCII")));
	}

	/**
	 * Loads all keys from the specified file, and adds them to this ring's
	 * existing list of keys.
	 */
	public List<PGPPublicKeyRing> load(File file)
			throws IOException, PGPException {
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(file), 0x1000);
			return load(stream);
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Loads all keys from the specified input stream, and adds them to this
	 * ring's existing list of keys.
	 */
	public List<PGPPublicKeyRing> load(InputStream stream)
			throws IOException, PGPException {
		ArrayList<PGPPublicKeyRing> keys = new ArrayList<PGPPublicKeyRing>();

		Iterator packets = parse(unarmor(stream));
		while (packets.hasNext()) {
			Object packet = packets.next();

			if (packet instanceof PGPPublicKeyRing)
				keys.add((PGPPublicKeyRing) packet);
			// else if (packet instanceof PGPPublicKeyRing)
			// keys.add(newKey((PGPPublicKeyRing) packet));
		}

		this.keys.addAll(keys);
		return keys;
	}

	/**
	 * Wraps stream with ArmoredInputStream if necessary (to convert
	 * ascii-armored content back into binary data).
	 */
	protected InputStream unarmor(InputStream stream)
			throws IOException, PGPException {
		return PGPUtil.getDecoderStream(stream);
	}

	/**
	 * Separates stream into PGP packets.
	 * 
	 * @see PGPObjectFactory
	 */
	protected Iterator parse(InputStream stream)
			throws IOException, PGPException {
		return new BcPGPObjectFactory(stream).iterator();
	}

}
