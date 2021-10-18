package org.opensourceframework.starter.oss.vo;

/**
 * 文件上传对象
 */
public class UploadBody extends UploadBodyBase{
	/**
	 * 必传字段，文件流
	 */
	protected byte[] file;

	public UploadBody() {
	}

	public UploadBody(byte[] file) {
		this.file = file;
	}

	public UploadBody(String fileName, String bucketName, Integer returnPathType, String fileType, byte[] file) {
		super(fileName, bucketName, returnPathType, fileType);
		this.file = file;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
}
