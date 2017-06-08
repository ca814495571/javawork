package com.cqfc.util;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.jami.util.Log;

public class SchemaValidator {
	
	/**
	 * 校验xml符合xsd规范
	 * @param xsd
	 * @param xmlstr
	 * @return
	 */
	public static boolean validate(InputStream xsd, String xmlstr) {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator validator = schema.newValidator();
			StringReader reader = new StringReader(xmlstr);
			validator.validate(new StreamSource(reader));
			return true;
		} catch (Exception ex) {
			Log.run.error("validate have an Exception.error=%s", ex.toString());
			Log.run.debug("", ex);
			return false;
		}
	}
}
