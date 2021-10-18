package org.opensourceframework.base.helper;

import java.io.*;
import java.util.Base64;

/**
 * 文件处理工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class FileHelper {
	/**
	 * 把一个文件转化为byte字节数组。
	 *
	 * @return
	 */
	public static byte[] fileToByteArray(File file) throws Exception {
		byte[] data = null;
		FileInputStream fi = null;
		ByteArrayOutputStream bo = null;
		try {
			fi = new FileInputStream(file);
			bo = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = fi.read(buffer)) != -1) {
				bo.write(buffer, 0, len);
			}
			data = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally{
			if(fi != null) {
				fi.close();
			}
			if(bo != null) {
				bo.close();
			}
		}

		return data;
	}

	public static File byteArrayToFile(Byte[] fileData , String filePath) throws Exception {
		FileOutputStream fo = null;
		File file = new File(filePath);
		try{
		    fo = new FileOutputStream(file);
			fo.write(convert(fileData));
		}catch (Exception e){

		}finally{
			if(fo != null) {
				fo.close();
			}
		}
		return file;
	}


	private static byte[] convert(Byte[] fileData){
		byte[] data = new byte[fileData.length];
		for(int i = 0; i < fileData.length ; i++){
			data[i] = fileData[i];
		}
		return data;
	}


	public static void copyFile(File source, File dest) throws IOException{
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			if(input != null) {
				input.close();
			}
			if(output != null) {
				output.close();
			}
		}
	}


	/**
	 * 将文件转成base64 字符串
	 *
	 * @param path 文件路径
	 * @return *
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.getEncoder().encodeToString(buffer);
	}

	/**
	 * 将base64字符解码保存文件
	 *
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void decodeBase64File(String base64Code, String targetPath,String catalogue)
			throws Exception {
		File file = new File(catalogue);
		if(file.exists()==false){
			file.mkdirs();
		}
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] buffer = decoder.decode(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}


}
