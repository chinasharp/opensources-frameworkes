package org.opensourceframework.starter.fastdfs.config;

import java.io.Serializable;

/**
 * 文服配置类
 *
 * @author: chenw
 * @date: 2020/3/18 14:35
 */
public class FileServerConfig implements Serializable {

    private static final long serialVersionUID = -7727255869520927831L;
    /** fastdfs文服路径 */
    private String fastdfsTrackerServers;
    /** 老文件服务器地址 */
    private String fileUrl;
    /** 文件存放本地的绝对路径，与服务器做映射的文件路径 */
    private String rootDir;
    /** 文件大小最大值 */
    private Long fileSize;
    /** 文件类型 */
    private String fileType;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFastdfsTrackerServers() {
        return fastdfsTrackerServers;
    }

    public void setFastdfsTrackerServers(String fastdfsTrackerServers) {
        this.fastdfsTrackerServers = fastdfsTrackerServers;
    }
}
