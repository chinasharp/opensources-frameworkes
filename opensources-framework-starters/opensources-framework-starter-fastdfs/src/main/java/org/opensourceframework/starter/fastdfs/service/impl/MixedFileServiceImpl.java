package org.opensourceframework.starter.fastdfs.service.impl;

import org.opensourceframework.starter.fastdfs.util.FileUploadResp;
import org.opensourceframework.starter.fastdfs.util.FastDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: yeshuai
 * @date: 2020/3/9 15:38
 */
public class MixedFileServiceImpl extends DefaultFileServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(MixedFileServiceImpl.class);

    private static final String FASTDFS_FILE_FLAG = "/M00/";

    private final FastDFSClient fastDFSClient;

    public MixedFileServiceImpl(String trackerServers) throws Exception {
        fastDFSClient = new FastDFSClient(trackerServers.split(","));
    }

    @Override
    public FileUploadResp uploadFile(InputStream is, String fileName, String fileDir) {
        logger.info("开始上传文件到fastdfs服务器，文件名：{}", fileName);
        return uploadFileToFastdfs(is, fileName);
    }

    /**
     * 上传文件到fastdfs服务器
     *
     * @param is
     * @param fileName
     * @return
     */
    private FileUploadResp uploadFileToFastdfs(InputStream is, String fileName) {
        FileUploadResp resp = new FileUploadResp();
        try {
            // 文件校验
            boolean check = super.check(resp, is, fileName);
            if (!check) {
                return resp;
            }
            byte[] fileByteArray = inputStream2ByteArray(is);
            String fileExtName = getSuffix(fileName);
            fileExtName = fileExtName.substring(1);
            String relativePath = fastDFSClient.uploadFile(fileByteArray, fileExtName);
            resp.setSuccess(true);
            resp.setUrl(relativePath);
            resp.setMsg("文件上传成功");
            logger.debug("[fastdfs] 文件上传成功，存储相对路径：{}", relativePath);
            return resp;
        } catch (Exception e) {
            logger.error("[fastdfs] 文件上传异常", e);
            resp.setMsg("文件上传失败，原因：" + e.getMessage());
            resp.setSuccess(false);
            return resp;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("文件流关闭异常", e);
            }
        }
    }

    @Override
    public InputStream downloadFile(String fileUrl) {
        if(isFastdfsFilePath(fileUrl)) {
            logger.info("开始从fastdfs服务器下载文件，文件相对路径：{}", fileUrl);
            return downloadFileFromFastdfs(fileUrl);
        }else {
            logger.info("开始从远程URL下载文件，远程URL：{}，文件相对路径：{}", super.getUrl(), fileUrl);
            return super.downloadFile(fileUrl);
        }
    }

    /**
     * 从fastdfs服务器下载文件
     *
     * @param fileUrl
     * @return
     */
    private InputStream downloadFileFromFastdfs(String fileUrl) {
        try {
            byte[] fileByteArray = fastDFSClient.downloadFile(fileUrl);
            return new ByteArrayInputStream(fileByteArray);
        } catch (Exception e) {
            logger.error("[fastdfs] 文件下载失败", e);
            return null;
        }
    }

    /**
     * 获取文件扩展名（带.）
     *
     * @param fileName
     * @return
     */
    private String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 文件流转字节数组
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    private byte[] inputStream2ByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[100 * 1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            byteArrayOutputStream.write(buf, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 判断是否是fastdfs文件
     *
     * @param fileDir
     * @return
     */
    private boolean isFastdfsFilePath(String fileDir) {
        return fileDir.contains(FASTDFS_FILE_FLAG);
    }
}
