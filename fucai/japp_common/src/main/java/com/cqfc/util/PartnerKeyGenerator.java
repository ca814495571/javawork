package com.cqfc.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.jami.util.Log;


public class PartnerKeyGenerator {
	
	/**
	 * 获取公钥
	 * @param path
	 * @return
	 */
	public static synchronized PublicKey getPublicKey(InputStream in) {
		PublicKey pub = null;
		try {
			CertificateFactory certificatefactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) certificatefactory
					.generateCertificate(in);
			pub = certificate.getPublicKey();

			in.close();

		} catch (IOException e) {
			Log.run.error("getPublicKey have an Exception.error=%s", e);
			Log.run.debug("", e);
		} catch (Exception e) {
			Log.run.error("getPublicKey have an Exception.error=%s", e);
			Log.run.debug("", e);
		} finally {

		}

		return pub;
	}

	/**
	 * 获取私钥
	 * @param keypair
	 * @param storePass
	 * @param path
	 * @return
	 */
	public static synchronized PrivateKey getPrivateKey(String keypair,
			String storePass, InputStream in) {

		PrivateKey priv = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			BufferedInputStream ksbufin = new BufferedInputStream(in);
			char[] kpass;
			kpass = new char[storePass.length()];
			for (int i = 0; i < storePass.length(); i++)
				kpass[i] = storePass.charAt(i);
			ks.load(ksbufin, kpass);
			priv = (PrivateKey) ks.getKey(keypair, kpass);
			in.close();

		} catch (IOException e) {
			Log.run.error("getPrivateKey have an Exception.error=%s", e.toString());
			Log.run.debug("", e);
		} catch (Exception e) {
			Log.run.error("getPrivateKey have an Exception.error=%s", e.toString());
			Log.run.debug("", e);
		} finally {
		}

		return priv;
	}

}
