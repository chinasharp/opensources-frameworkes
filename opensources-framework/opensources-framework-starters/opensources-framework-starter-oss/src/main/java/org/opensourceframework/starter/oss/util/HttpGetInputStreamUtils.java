package org.opensourceframework.starter.oss.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 */
public class HttpGetInputStreamUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpGetInputStreamUtils.class);

    /**
     * 读取网络文件
     * @param path
     * @return
     */
    public static InputStream getFileInputStream(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(5*1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            return conn.getInputStream();
        } catch (Exception e) {
            logger.error("读取网络文件异常:"+path);
        }
        return null;
    }
}
