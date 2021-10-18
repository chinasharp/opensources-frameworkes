package org.opensourceframework.starter.oss.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.opensourceframework.starter.oss.config.OssConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * aliyun oss
 * 
 * @author yangdx
 *
 */
public class BaseService {
	protected static final String OSS_FILE_PATH_KEY = "ossFullPath";
	protected static final String OSS_FILE_NAME_KEY = "originalFilename";

	protected static final int FILE_FULL_PATH_TYPE = 1;

	private final OssConfig ossConfig;


	private final Object lock = new Object();

	/**
	 * 创建OSSClient实例
	 */
	private OSSClient client;

	public BaseService(OssConfig ossConfig) {
		this.ossConfig = ossConfig;
		initClient();
	}

	private void initClient() {
		synchronized (lock) {
			if (client == null) {
				client = new OSSClient(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
			}
		}
	}

	public String getAccessUrlPrefix() {
		return ossConfig.getAccessUrlPrefix();
	}

	public void uploadToOSS(InputStream fileInputStream, String fileName) throws Exception {
		try {
			if (fileName.startsWith("/")) {
				fileName = fileName.substring(1);
			}
			// 上传文件流
			client.putObject(ossConfig.getBucketName(), fileName, fileInputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}

	/**
	 * 
	 * @param files
	 * @param bucketName
	 * @author yangdx
	 * @return
	 */
	public Map<String, List<Map<String, String>>> uploadToOSS(List<MultipartFile> files, String bucketName) {
		try {
			if (files != null && files.size() > 0) {
				if (!StringUtils.isEmpty(bucketName) && bucketName.startsWith("/")) {
					bucketName = bucketName.substring(1);
				}

				Map<String, List<Map<String, String>>> results = new HashMap<>();
				Map<String, String> fileItemMap = null;
				List<Map<String, String>> inputFileItems = null;
				for (MultipartFile file : files) {
					String inputName = file.getName(); // inputName名
					String originalFilename = file.getOriginalFilename();// 原文件名
					String randomFileName = System.nanoTime()
							+ originalFilename.substring(originalFilename.lastIndexOf("."));

					String ossFullPath = StringUtils.isEmpty(bucketName) ? randomFileName
							: bucketName + (bucketName.endsWith("/") ? "" : "/") + randomFileName;
					uploadToOSS(file.getInputStream(), ossFullPath);

					inputFileItems = results.get(inputName);
					if (inputFileItems == null) {
						inputFileItems = new ArrayList<>();
					}

					fileItemMap = new HashMap<>();
					fileItemMap.put(OSS_FILE_PATH_KEY, ossFullPath);
					fileItemMap.put(OSS_FILE_NAME_KEY, originalFilename);
					inputFileItems.add(fileItemMap);

					results.put(inputName, inputFileItems);
				}

				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param files
	 * @param bucketName
	 * @author yangdx
	 * @return
	 */
	public Map<String, List<Map<String, String>>> uploadToOSS(MultipartFile[] files, String bucketName) {
		if (files != null && files.length > 0) {
			return uploadToOSS(Arrays.asList(files), bucketName);
		}
		return null;
	}

	/**
	 * 根据key获取OSS服务器上的文件输入流
	 * 
	 * @param fileUrl
	 * @return
	 */
	public final InputStream getInputStream(String fileUrl) {
		OSSObject oob = client.getObject(ossConfig.getBucketName(), fileUrl);
		if (oob != null) {
			return oob.getObjectContent();
		}
		return null;
	}

}
