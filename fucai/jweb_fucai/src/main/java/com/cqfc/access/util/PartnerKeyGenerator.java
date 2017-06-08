package com.cqfc.access.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PartnerKeyGenerator {
	public static Log logger = LogFactory.getLog(PartnerKeyGenerator.class);

	public static synchronized PublicKey getPublicKey(String path) {
		PublicKey pub = null;
		try {
			FileInputStream in = new FileInputStream(path);// 公钥路径
			CertificateFactory certificatefactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) certificatefactory
					.generateCertificate(in);
			pub = certificate.getPublicKey();

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		return pub;
	}

	public static synchronized PrivateKey getPrivateKey(String keypair,
			String storePass, String path) {

		PrivateKey priv = null;
		try {

			FileInputStream in = new FileInputStream(path);
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
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

		return priv;
	}

}
