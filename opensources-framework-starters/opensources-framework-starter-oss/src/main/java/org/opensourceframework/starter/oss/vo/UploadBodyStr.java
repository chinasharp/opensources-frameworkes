package org.opensourceframework.starter.oss.vo;

/**
 * 文件上传对象
 */
public class UploadBodyStr extends UploadBodyBase {
	/**
	 * 必传字段，文件流
	 */
	private String fileStr;

	public UploadBodyStr(String fileStr) {
		this.fileStr = fileStr;
	}

	public UploadBodyStr(String fileName, String bucketName, Integer returnPathType, String fileType, String fileStr) {
		super(fileName, bucketName, returnPathType, fileType);
		this.fileStr = fileStr;
	}

	public String getFileStr() {
		return fileStr;
	}

	public void setFileStr(String fileStr) {
		this.fileStr = fileStr;
	}
}
