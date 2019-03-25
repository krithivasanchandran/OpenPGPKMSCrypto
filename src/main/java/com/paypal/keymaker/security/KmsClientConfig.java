package com.paypal.keymaker.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import com.paypal.keymaker.security.exceptions.CryptoRuntimeException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class KmsClientConfig {

	private static Logger kmsClientConfigLogger = Logger
			.getLogger(KmsResponseProcessor.class.getName());

	private static SSLContext sc;
	private static Object LOCK = new Object();
	
	/*
	 * Keymaker Connection Timeout -- 30 seconds
	 */
	
	private final static String keymakerConnectionTimeout = "30000";
	
	/*
	 * Keymaker ReadTimeout -- 50 seconds
	 */
	
	private final static String keymakerReadTimeout = "50000";
	
	
	private static void keymakerProperties() throws Exception {
		
		System.setProperty("keymaker.keystore.password",
				"");
		System.setProperty("keymaker.keystore",
				"C:/Users/krichandran/Desktop/Mandatory/keymaker-qa.jks");
	}

	public static Client getClient() throws Exception {
		
		keymakerProperties();
		
		ClientConfig config = new DefaultClientConfig();
		if (sc == null) {
			synchronized (LOCK) {
				if (sc == null) {

					try {
						// Create SSL context from keystore on disk
						Properties p = System.getProperties();

						String keystorePassword = System
								.getenv("keymaker.keystore.password");
						if (keystorePassword == null) {
							keystorePassword = System
									.getProperty("keymaker.keystore.password");
						}

						final KeyStore keyStore = KeyStore.getInstance("JKS");
						String keystoreFilePath = System
								.getProperty("keymaker.keystore");
						
						System.out.println("Printing the path to the jks file " + keystoreFilePath);
						
						if (keystoreFilePath == null
								|| keystoreFilePath.isEmpty()) {
							throw new CryptoRuntimeException(
									"Cannot initialize SSLContext to call KMS - keystore unavailable");
						}
						try (final InputStream is = new FileInputStream(
								keystoreFilePath)) {
							if (keystorePassword == null
									|| keystorePassword.isEmpty()) {
								keyStore.load(is, null);
							} else {
								keyStore.load(is,
										keystorePassword.toCharArray());
							}
						}
						final KeyManagerFactory kmf = KeyManagerFactory
								.getInstance(KeyManagerFactory
										.getDefaultAlgorithm());

						if (keystorePassword == null
								|| keystorePassword.isEmpty()) {
							kmf.init(keyStore, null);
						} else {
							kmf.init(keyStore, keystorePassword.toCharArray());
						}

						final TrustManagerFactory tmf = TrustManagerFactory
								.getInstance(TrustManagerFactory
										.getDefaultAlgorithm());
						tmf.init(keyStore);

						sc = SSLContext.getInstance("TLS");
						sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
								new java.security.SecureRandom());

						HttpsURLConnection.setDefaultSSLSocketFactory(sc
								.getSocketFactory());

					} catch (GeneralSecurityException | IOException e) {
						kmsClientConfigLogger.log(
								Level.SEVERE,
								"Cannot initialize SSLContext to call KMS "
										+ e.getMessage());
						throw new CryptoRuntimeException(e);
					}
				}
			}
		}
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(new HostnameVerifier() {
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				}, sc));
		Client client = Client.create(config);
		String connectTimeout = System.getProperty("keymaker.connect.timeout",
				keymakerConnectionTimeout);
		String readTimeout = System.getProperty("keymaker.read.timeout",
				keymakerReadTimeout);

		client.setConnectTimeout(Integer.parseInt(connectTimeout));
		client.setReadTimeout(Integer.parseInt(readTimeout));

		return client;
	}
}
