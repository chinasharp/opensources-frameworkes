package org.opensourceframework.starter.oss.vo;

import java.io.Serializable;

/**
 * 文件上传对象
 */
public class UploadBodyBase implements Serializable {
	/**
	 * 可选字段，文件名称，如果传了代表上传到指定路径，优先级最高
	 */
	protected String fileName;
	/**
	 * 分区路径名称，文件名称，如果传了代表随机生成文件名称
	 */
	protected String bucketName;
	/**
	 * 可选字段，返回路径类型，和bucketName组合使用，如果为1代表返回图片完成路径，其他代表返回文件名称
	 */
	protected Integer returnPathType;
	/**
	 * 可选字段，文件类型，和bucketName组合使用，例如jpg
	 */
	protected String fileType;

	public UploadBodyBase() {
	}

	public UploadBodyBase(String fileName, String bucketName, Integer returnPathType, String fileType) {
		this.fileName = fileName;
		this.bucketName = bucketName;
		this.returnPathType = returnPathType;
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Integer getReturnPathType() {
		return returnPathType;
	}

	public void setReturnPathType(Integer returnPathType) {
		this.returnPathType = returnPathType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
