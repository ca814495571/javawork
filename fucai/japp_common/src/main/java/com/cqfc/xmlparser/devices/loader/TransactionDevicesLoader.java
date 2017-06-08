package com.cqfc.xmlparser.devices.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cqfc.xmlparser.devices.Device;
import com.cqfc.xmlparser.devices.Devices;



public class TransactionDevicesLoader {
	private static final JAXBContext jaxbContext = initContext();

    private static JAXBContext initContext() {
        try {
			return JAXBContext.newInstance("com.cqfc.xmlparser.devices");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        return null;
    }
    public static Devices loadDevices(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Devices) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public static Devices xmlToMsg(String xmlstr) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Devices) unmarshaller.unmarshal(new StringReader(xmlstr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String devices2xml(Devices devices){
        try {
			Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(devices, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
    	File file = new File(TransactionDevicesLoader.class.getResource("/").getPath()+"devices.xml");
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	
    	String content = "";
    	String line = "";
    	
    	try {
			while((line = br.readLine()) != null){
				content += line + "\n";
			}
	    	br.close();
	    	System.out.println(content);
	    	
	    	Devices devices = xmlToMsg(content);
	    	for(Device device :devices.getDevice()){
	    		System.out.println(device.getThresholdSize() + "; " + device.getPort());
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 	
    }
}
