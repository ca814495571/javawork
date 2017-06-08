package com.cqfc.access.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Digit {
	public static Log logger = LogFactory.getLog(Digit.class);

	public static String sig(byte[] sigText, String storePass, String keypair,
			String path) {
		String encodeStr = null;
		try {
			PrivateKey priv = PartnerKeyGenerator.getPrivateKey(keypair,
					storePass, path);
			if (priv == null) {
				logger.error("HttpMsgSendClient.send priv is null." + path);
			}
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initSign(priv);
			rsa.update(sigText);
			byte[] sig = rsa.sign();
			sun.misc.BASE64Encoder base64 = new sun.misc.BASE64Encoder();
			encodeStr = base64.encode(sig);

		} catch (Exception e) {
			logger.error("Digit.sig have an otherException." + path, e);
			e.printStackTrace();
		}
		if (encodeStr == null) {
			encodeStr = "error";
		}

		return encodeStr;
	}

	public static boolean veriSig(byte[] updateData, byte[] sigedText,
			String path) {
		boolean verifies = true;
		try {
			PublicKey pub = PartnerKeyGenerator.getPublicKey(path);
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initVerify(pub);
			rsa.update(updateData);

			verifies = rsa.verify(sigedText);
			return verifies;
		} catch (Exception e) {
			verifies = false;
			e.printStackTrace();
		}
		return verifies;
	}
}
