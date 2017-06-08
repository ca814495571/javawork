package com.cqfc.test.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;







public class Send {
	

	public final static String server_url = "http://182.254.212.164/jweb_access/verification/verify";
	public final static String private_scret_url = Send.getClassPath()
			+ "19_private";
	public final static String alias = "cqfc";
	public final static String key_store_pass = "123456";
	public final static String PARM_TIME = "time";
	public final static String PARM_PARNERID = "partnerid";
	public final static String PARM_KEY = "key";
	public final static String PARM_MSG = "msg";
	public final static String PARM_VERSION = "version";
	public final static String PARM_TRANSCODE = "transcode";
	public final static String DATE_FORMAT = "yyyyMMDDHHmmSS";
	public final static String DEFAULT_VERSION = "1.0";
	public final static String CONVERT_TYPE = "http";

	public String send(String msg, String transcode, String partner_id) {
   
		
	 
		
		PostMethod method = null;
		HttpClient client = null;
		String str = null;

		String result = null;
		int status = 0;

		try {
			String key = Send.sig((transcode + msg).getBytes("utf-8"),
					key_store_pass, alias, private_scret_url);
			client = new HttpClient();

			method = new PostMethod(server_url);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter(PARM_VERSION, "1.0");
			method.setParameter(PARM_PARNERID, partner_id);
			method.setParameter(PARM_MSG, msg);
			method.setParameter(PARM_KEY, key);
			method.setParameter(PARM_TRANSCODE, transcode);
			
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			managerParams.setConnectionTimeout(500000000);
			managerParams.setSoTimeout(500000000);
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			
		
			
			str = new StringBuilder().append(transcode + "返回的消息体内容:")
					.append(result).toString();
			
		} catch (HttpException e) {
			System.out.println("HttpException");
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		} finally {
			if (method != null && client != null) {
				method.releaseConnection();
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).closeIdleConnections(0);
				method = null;
				client = null;
			}

			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				client = null;
			}

			

		}
		return str;

	}
	
	
		/**
		 * 得到当前工程的classPath
		 * @return
		 */
		public static String getClassPath() {
			return Send.class.getResource("/").getPath();
		}
	
	

     
		
		@SuppressWarnings("restriction")
		public static String sig(byte[] sigText, String storePass, String keypair,
				String path) {
			String encodeStr = null;
			try {
				PrivateKey priv = Send.getPrivateKey(keypair,
						storePass, path);
				if (priv == null) {
					/*Log.run.error("HttpMsgSendClient.send priv is null.path=%s",
							path);*/
				}
				Signature rsa = Signature.getInstance("MD5withRSA");
				rsa.initSign(priv);
				rsa.update(sigText);
				byte[] sig = rsa.sign();
				sun.misc.BASE64Encoder base64 = new sun.misc.BASE64Encoder();
				encodeStr = base64.encode(sig);

			} catch (Exception e) {
				/*Log.run.error("Digit.sig have an Exception.path=%s", path);
				Log.run.debug("", e);*/
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
		 * @param path
		 * @return
		 */
		public static boolean veriSig(byte[] updateData, byte[] sigedText,
				String path) {
			boolean verifies = true;
			try {
				PublicKey pub = Send.getPublicKey(path);
				Signature rsa = Signature.getInstance("MD5withRSA");
				rsa.initVerify(pub);
				rsa.update(updateData);

				verifies = rsa.verify(sigedText);
				return verifies;
			} catch (Exception e) {
				verifies = false;
				/*Log.run.error("Digit.veriSig have an Exception.path=%s", path);
				Log.run.debug("", e);*/
			}
			return verifies;
		}
	
	
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
				/*Log.run.error("getPublicKey have an Exception.path=%s,error=%s", path,e.toString());
				Log.run.debug("", e);*/
			} catch (Exception e) {
				/*Log.run.error("getPublicKey have an Exception.path=%s,error=%s", path,e.toString());
				Log.run.debug("", e);*/
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
				/*Log.run.error("getPrivateKey have an Exception.path=%s,error=%s", path,e.toString());
				Log.run.debug("", e);*/
			} catch (Exception e) {
				/*Log.run.error("getPrivateKey have an Exception.path=%s,error=%s", path,e.toString());
				Log.run.debug("", e);*/
			} finally {
			}

			return priv;
		}
	
	

}
