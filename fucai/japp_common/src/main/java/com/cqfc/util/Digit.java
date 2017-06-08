package com.cqfc.util;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.jami.util.Log;

public class Digit {

	/**
	 * 签名
	 * 
	 * @param sigText
	 * @param storePass
	 * @param keypair
	 * @param in
	 * @return
	 */
	public static String sig(byte[] sigText, String storePass, String keypair,
			InputStream in) {
		String encodeStr = null;
		try {
			PrivateKey priv = PartnerKeyGenerator.getPrivateKey(keypair,
					storePass, in);
			if (priv == null) {
				Log.run.error("Digit.sig priv is null.");
			}
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initSign(priv);
			rsa.update(sigText);
			byte[] sig = rsa.sign();
			sun.misc.BASE64Encoder base64 = new sun.misc.BASE64Encoder();
			encodeStr = base64.encode(sig);

		} catch (Exception e) {
			Log.run.error("Digit.sig have an Exception.error=%s", e);
			Log.run.debug("", e);
		}
		if (encodeStr == null) {
			encodeStr = "error";
		}

		return encodeStr;
	}

	/**
	 * 校验签名
	 * 
	 * @param updateData
	 * @param sigedText
	 * @param in
	 * @return
	 */
	public static boolean veriSig(byte[] updateData, byte[] sigedText,
			InputStream in) {
		boolean verifies = true;
		try {
			PublicKey pub = PartnerKeyGenerator.getPublicKey(in);
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initVerify(pub);
			rsa.update(updateData);

			verifies = rsa.verify(sigedText);
			return verifies;
		} catch (Exception e) {
			verifies = false;
			Log.run.error("Digit.veriSig have an Exception.error=%s", e);
			Log.run.debug("", e);
		}
		return verifies;
	}
}
