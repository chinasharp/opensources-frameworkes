package org.opensourceframework.starter.oss.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.starter.oss.config.OssConfig;
import org.opensourceframework.starter.oss.util.HttpGetInputStreamUtils;
import org.opensourceframework.starter.oss.vo.UploadBody;
import org.opensourceframework.starter.oss.vo.UploadBodyStr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * AliYun Oss 文件服务
 *
 *
 */
public class AliOSSService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(AliOSSService.class);

	public AliOSSService(OssConfig ossConfig) {
		super(ossConfig);
	}

	/**
     * 获取上传路径前缀
     * @param  fileUrl 文件上传路径
     * @return  文件流数组
     */
    public RestResponse<byte[]> getByteArray(String fileUrl) {
    	byte[] b = null;
    	try {
    		InputStream in = getInputStream(fileUrl);
    		
    		if(in != null){
    			 b = IOUtils.toByteArray(in);
    			 return RestResponse.success(b);
    		}
		} catch (Exception e) {
			logger.error("getByteArray失败：{}", e);
			 return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),null);
		}
    	return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() , null);
    }
	  /**
     * 获取上传路径前缀
     * @return  上传路径前缀
     */
    @Override
	public String getAccessUrlPrefix() {
    	return super.getAccessUrlPrefix();
    }
    
    /**
     * 上传单个文件
     * 
     * @param uploadBody
	 *  file 必传字段，文件流
     *  fileName    可选字段，文件名称，如果传了代表上传到指定路径，优先级最高
     *  bucketName  分区路径名称，文件名称，如果传了代表随机生成文件名称
     *  returnPathType  可选字段，返回路径类型，和bucketName组合使用，如果为1代表返回图片完成路径，其他代表返回文件名称
     *  fileType  可选字段，文件类型，和bucketName组合使用，例如jpg
     * @return  
     */
    public RestResponse<String> uploadToOSSFileInputStrem(UploadBody uploadBody) {
    	logger.info("【uploadToOSSFileInputStrem 开始上传图片】");
        try {
        	InputStream sbs = new ByteArrayInputStream(uploadBody.getFile()); 
        	if(uploadBody.getFileName() != null){
            	uploadToOSS(sbs,uploadBody.getFileName());
            	return RestResponse.success(getAccessUrlPrefix()+uploadBody.getFileName());
        	}
        	if(uploadBody.getBucketName() != null){
    			String randomFileName = System.nanoTime() + "_"+UUID.randomUUID().toString().replaceAll("-", "");

				String ossFullPath = "";
				if (uploadBody.getBucketName().endsWith("/")) {
					ossFullPath = uploadBody.getBucketName() + randomFileName+uploadBody.getFileType();
				} else {
					ossFullPath = uploadBody.getBucketName() + "/" + randomFileName+uploadBody.getFileType();
				}

				logger.info("ossFullPath:{}",ossFullPath);
            	uploadToOSS(sbs,ossFullPath);
            	if(uploadBody.getReturnPathType() == 1){
            		return RestResponse.success(getAccessUrlPrefix() + ossFullPath);
            	}else{
            		return RestResponse.success(ossFullPath);
            	}
            	
        	}
            return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                   "上传失败");
        } catch (Exception e) {
        	logger.error("uploadImg失败：{}", e);
			return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					e.getMessage());
        }
    }
   
    /**
     * 上传单个文件
     * 
     * fileStr 必传字段，文件流
     * fileName  可选字段，文件名称，如果传了代表上传到指定路径，优先级最高
     * bucketName  分区路径名称，文件名称，如果传了代表随机生成文件名称
     * returnPathType  可选字段，返回路径类型，和bucketName组合使用，如果为1代表返回图片完成路径，其他代表返回文件名称
     * fileType  可选字段，文件类型，和bucketName组合使用，例如jpg
     * @return  
     */
    public RestResponse<String>  uploadToOSSFileStr(UploadBodyStr uploadBody) {
    	logger.info("【uploadToOSSFileInputStrem 开始上传图片】");
        try {
        	InputStream sbs = new ByteArrayInputStream(Base64.decodeBase64(uploadBody.getFileStr())); 
        	if(uploadBody.getFileName() != null){
            	uploadToOSS(sbs,uploadBody.getFileName());
            	return RestResponse.success(getAccessUrlPrefix()+uploadBody.getFileName());
        	}
        	if(uploadBody.getBucketName() != null){
    			String randomFileName = System.nanoTime() + "_"+UUID.randomUUID().toString().replaceAll("-", "");
    			
    			String ossFullPath = uploadBody.getBucketName() + "/" + randomFileName+uploadBody.getFileType();

				logger.info("ossFullPath:{}",ossFullPath);
            	uploadToOSS(sbs,ossFullPath);
            	if(FILE_FULL_PATH_TYPE == uploadBody.getReturnPathType()){
            		return RestResponse.success(getAccessUrlPrefix() + ossFullPath);
            	}else{
            		return RestResponse.success(ossFullPath);
            	}
            	
        	}

			return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					"上传失败");
		} catch (Exception e) {
			logger.error("uploadImg失败：{}", e);
			return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					e.getMessage());
		}
    }

	/**
	 * 上传网络流
	 *
	 */
	public RestResponse<String>  uploadToOSSNetFileInputStrem(JSONObject uploadBody) throws IOException {
		logger.info("【uploadToOSSNetFileInputStrem 开始上传图片】");
		InputStream inputStream = null ;
		try {
			String returnUrl = "";
			String ossFullPath ="";
			String fUrl =uploadBody.getString("fUrl");
			String fName =uploadBody.getString("fileName");
			String bucketName = uploadBody.getString("bucketName");
			String fileType = uploadBody.getString("fileType");
			if(fName == null){
				fName = System.nanoTime() + "_"+UUID.randomUUID().toString().replaceAll("-", "");
			}
			if(bucketName != null){
				returnUrl = getAccessUrlPrefix()+bucketName + "/" + fName;
				ossFullPath = bucketName + "/" + fName;
			}else{
				returnUrl = getAccessUrlPrefix()+fName;
				ossFullPath = fName;
			}
			//开始上传文件
			inputStream = HttpGetInputStreamUtils.getFileInputStream(fUrl);
			if(inputStream == null){
				return RestResponse.build(-1, "读取网络流异常", null);
			}
			uploadToOSS(inputStream,ossFullPath);
			return RestResponse.success(returnUrl);

		} catch (Exception e) {
			logger.error("uploadImg失败：{}", e);
			return RestResponse.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					e.getMessage());
		}finally {
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
}
