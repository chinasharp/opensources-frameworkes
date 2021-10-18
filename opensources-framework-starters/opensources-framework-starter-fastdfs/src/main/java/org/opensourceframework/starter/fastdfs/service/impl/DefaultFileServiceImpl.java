package org.opensourceframework.starter.fastdfs.service.impl;

import org.opensourceframework.starter.fastdfs.service.FileService;
import org.opensourceframework.starter.fastdfs.util.FileUploadResp;
import org.opensourceframework.starter.fastdfs.util.FileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultFileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFileServiceImpl.class);
    /** 文件服务器地址 */
    private String url;
    /** 文件存放本地的绝对路径，与服务器做映射的文件路径 */
    private String rootDir;
    /** 文件大小最大值 */
    private Long fileSize;
    /** 文件类型 */
    private String fileType;
    private final Map<String, String> fileTypeMap = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (isNotBlank(url) && !url.endsWith("/")) {
            url = url + "/";
        }
        this.url = url;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        if (isNotBlank(rootDir) && !rootDir.endsWith("/")) {
            rootDir = rootDir + "/";
        }
        this.rootDir = rootDir;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        if (fileSize == null) {
            fileSize = new Long(0);
        }
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
        if (isNotBlank(fileType)) {
            String[] strs = fileType.split(",");
            for (String str : strs) {
                fileTypeMap.put("." + str, str);
                fileTypeMap.put(str, str);
                fileTypeMap.put(str.toUpperCase(), str);
                fileTypeMap.put(str.toLowerCase(), str);
            }
        }
    }

    @Override
    public FileUploadResp uploadFile(InputStream is, String fileName, String fileDir) {
        logger.info("开始上传文件到本地服务器，文件名：{}，文件相对路径：{}", fileName, fileDir);
        return uploadFileToLocal(is, fileName, fileDir);
    }

    /**
     * 上传文件到本地服务器
     *
     * @param is
     * @param fileName
     * @param fileDir
     * @return
     */
    private FileUploadResp uploadFileToLocal(InputStream is, String fileName, String fileDir) {
        FileUploadResp resp = new FileUploadResp();
        FileOutputStream fos = null;
        try {
            // 文件校验
            boolean check = check(resp, is, fileName);
            if (!check) {
                return resp;
            }
            // 创建本地目录
            fileDir = mkdir(fileDir);
            // 重命名
            String newName = rename(fileName);

            fos = new FileOutputStream(rootDir + fileDir + newName);
            byte[] suffixBuf = new byte[3];
            int len = -1;
            is.read(suffixBuf);
            fos.write(suffixBuf, 0, suffixBuf.length);
            byte[] buf = new byte[100 * 1024];
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            resp.setSuccess(true);
            resp.setUrl(fileDir + newName);
            resp.setMsg("文件上传成功");
            logger.debug("文件上传成功，文件相对路径：{}", fileDir + newName);
            return resp;
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            resp.setMsg("文件上传失败，错误原因:" + e.getMessage());
            resp.setSuccess(false);
            return resp;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("文件流关闭异常", e);
            }
        }
    }

    @Override
    public InputStream downloadFile(String fileUrl) {
        logger.info("开始从远程URL下载文件，远程URL：{}，文件相对路径：{}", url, fileUrl);
        return downloadFileFromUrl(fileUrl);
    }

    /**
     * 从远程URL下载文件
     *
     * @param fileUrl
     * @return
     */
    private InputStream downloadFileFromUrl(String fileUrl) {
        String completeUrl = url + fileUrl;
        logger.debug("下载路径：{}", completeUrl);
        try {
            // 初始化连接
            URL localURL = new URL(completeUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) localURL.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                FileInputStream in = new FileInputStream(httpURLConnection.getInputStream(), httpURLConnection);
                logger.debug("文件：{}下载成功", completeUrl);
                return in;
            } else {
                logger.error("文件服务器：{}连接失败", url);
                return null;
            }
        } catch (Exception e) {
            logger.error("文件:{}下载失败", completeUrl);
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
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    private boolean isNotBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件名重命名
     *
     * @param fileName
     * @return
     */
    private String rename(String fileName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date());
        // 重命名文件
        String suffix = getSuffix(fileName);
        String uuid = UUID.randomUUID()
            .toString()
            .trim()
            .replaceAll("-", "")
            .toUpperCase();
        String newName = date + "_" + uuid + suffix;
        logger.debug("文件存放名称：{}", newName);
        return newName;
    }

    /**
     * 生成文件目录
     *
     * @param fileDir
     * @return
     */
    private String mkdir(String fileDir) {
        if (isNotBlank(fileDir)) {
            fileDir = assembleDir(fileDir);
        }
        File dir = new File(rootDir + fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logger.debug("文件本地存放路径：{}", rootDir + fileDir);
        logger.debug("文件服务器存放路径：{}", fileDir);
        return fileDir;
    }

    /**
     * 检查文件流、文件名等信息
     *
     * @param resp
     * @param is
     * @param fileName
     * @return
     * @throws IOException
     */
    protected boolean check(FileUploadResp resp, InputStream is, String fileName) throws IOException {
        boolean check = true;
        if (is == null) {
            resp.setMsg("文件流为空");
            resp.setSuccess(false);
            check = false;
        }
        if (fileSize > 0 && is.available() > fileSize) {
            resp.setMsg("文件大小超过限制");
            resp.setSuccess(false);
            check = false;
        }
        if (StringUtils.isEmpty(fileName)) {
            resp.setMsg("文件名为空");
            resp.setSuccess(false);
            check = false;
        } else {
            String fileExtName = getSuffix(fileName);
            fileExtName = fileExtName.substring(1);
            if (!fileTypeMap.containsKey(fileExtName)) {
                resp.setMsg("文件类型不匹配");
                resp.setSuccess(false);
                check = false;
            }
        }
        return check;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 处理文件路径
     *
     * @param fileDir
     * @return
     */
    private String assembleDir(String fileDir) {
        if (fileDir.startsWith("/")) {
            fileDir = fileDir.substring(1);
        }
        if (!fileDir.endsWith("/")) {
            fileDir = fileDir + "/";
        }
        return fileDir;
    }
}
