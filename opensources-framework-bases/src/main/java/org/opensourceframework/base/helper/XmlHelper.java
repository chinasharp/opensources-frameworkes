package org.opensourceframework.base.helper;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * XML与POJO相互转化帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class XmlHelper {
	/**
	 * POJO对象转化为xml 默认UTF-8编码 非格式化输出
	 *
	 * @param pojo
	 * @return
	 * @throws JAXBException
	 */
	public static String pojoToXml(Object pojo) throws JAXBException {
		return pojoToXml(pojo, "UTF-8");
	}

	/**
	 * POJO对象转化为xml  非格式化输出
	 *
	 * @param pojo
	 * @param encoding
	 * @return
	 * @throws JAXBException
	 */
	public static String pojoToXml(Object pojo, String encoding) throws JAXBException {
		return pojoToXml(pojo, encoding, false);
	}

	/**
	 * POJO对象转化为xml
	 *
	 * @param pojo
	 * @param encoding
	 * @param formated
	 * @return
	 * @throws JAXBException
	 */
	public static String pojoToXml(Object pojo, String encoding, boolean formated) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(pojo.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(formated));
		marshaller.setProperty("jaxb.encoding", encoding);
		marshaller.setProperty("jaxb.fragment", Boolean.valueOf(true));

		StringWriter writer = new StringWriter();
		marshaller.marshal(pojo, writer);
		return writer.toString();
	}

	/**
	 * POJO对象转化为xml UTF-8编码 格式化输出
	 *
	 * @param obj
	 * @return
	 * @throws JAXBException
	 */
	public static String pojoToFormatedXml(Object obj) throws JAXBException {
		return pojoToXml(obj, "UTF-8", true);
	}

	/**
	 * xml字符串转化为POJO对象
	 *
	 * @param xml
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T xmlToPojo(String xml, Class<T> clazz) throws JAXBException {
		return readerXmlToPojo(new StringReader(xml), clazz);
	}

	/**
	 * xml文件转化为POJO对象
	 *
	 * @param file
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static <T> T xmlFileToPojo(File file, Class<T> clazz)
			throws FileNotFoundException, JAXBException, UnsupportedEncodingException {
		return streamXmlToPojo(new FileInputStream(file), clazz);
	}

	/**
	 * XML 文件流转化为POJO
	 *
	 * @param inputStream
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static <T> T streamXmlToPojo(InputStream inputStream,
			Class<T> clazz) throws JAXBException, UnsupportedEncodingException {
		return readerXmlToPojo(new InputStreamReader(inputStream, StandardCharsets.UTF_8), clazz);
	}

	/**
	 * XML reader转化为POJO
	 *
	 * @param reader
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T readerXmlToPojo(Reader reader, Class<T> clazz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(reader);
	}
}
