package org.opensourceframework.starter.fastdfs.service;

import org.opensourceframework.starter.fastdfs.util.FileUploadResp;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    /**
     * 上传文件
     *
     * @param is 文件流
     * @param fileName 文件原名称
     * @param fileDir 上传文件存放路径,相对路径
     * @return
     * @throws IOException
     */
    FileUploadResp uploadFile(InputStream is, String fileName, String fileDir) throws IOException;

    /**
     * 文件下载
     *
     * @param fileUrl 文件相对路径
     * @return
     * @throws IOException
     */
    InputStream downloadFile(String fileUrl) throws IOException;

}
