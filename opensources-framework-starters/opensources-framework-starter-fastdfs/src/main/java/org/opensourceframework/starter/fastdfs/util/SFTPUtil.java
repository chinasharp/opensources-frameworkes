package org.opensourceframework.starter.fastdfs.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * sftp工具类
 */
public class SFTPUtil {
    private static final Logger logger = LoggerFactory.getLogger(SFTPUtil.class);

    /**
     * 上传文件到ftp
     *
     * @param server 服务地址
     * @param port 端口号
     * @param user sftp用户名
     * @param pwd sftp密码
     * @param path ftp文件保存路径
     * @param fileName ftp保存文件名
     * @param is 文件输入流
     * @throws Exception
     */
    public static void upload(String server, int port, String user, String pwd, String path, String fileName,
        InputStream is) throws Exception {
        SFTP ftp = null;
        try {
            ftp = new SFTP();
            ftp.connect(server, port, user, pwd);
            ftp.upload(fileName, path, is);
        } catch (Exception e) {
            logger.error("上传文件失败！", e);
            throw e;
        } finally {
            if (null != ftp) {
                ftp.close();
            }
        }
    }

    /**
     * 上传文件到ftp
     *
     * @param server 服务地址
     * @param port 端口号
     * @param user sftp用户名
     * @param priKeyPath sftp密钥路径
     * @param path ftp文件保存路径
     * @param fileName ftp保存文件名
     * @param is 文件输入流
     * @throws Exception
     */
    public static void uploadByPriKey(String server, int port, String user, String priKeyPath, String path,
        String fileName, InputStream is) throws Exception {
        SFTP ftp = null;
        try {
            ftp = new SFTP();
            ftp.connectByPriKey(server, port, user, priKeyPath);
            ftp.upload(fileName, path, is);
        } catch (Exception e) {
            logger.error("上传文件失败！", e);
            throw e;
        } finally {
            if (null != ftp) {
                ftp.close();
            }
        }
    }

    /**
     * 下载ftp文件到本地
     *
     * @param ip
     * @param user
     * @param psw
     * @param port
     * @param fromFullPath
     * @param localFullPath
     * @throws Exception
     */
    public static void download(String ip, String user, String psw, int port, String fromFullPath, String localFullPath)
    throws Exception {
        Session session;
        Channel channel;
        JSch jsch = new JSch();

        if (port <= 0) {
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        } else {
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip, port);
        }
        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;
            //以下代码实现从服务器读一个文件
            sftp.get(fromFullPath, localFullPath);
        } catch (Exception e) {
            logger.error("读取FTP服务器文件异常", e);
            throw e;
        } finally {
            session.disconnect();
        }
        logger.info("读取 {} 成功", fromFullPath);
    }

    /**
     * 创建本地路径
     *
     * @param destDirName
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            logger.info("{}路径已经存在，创建路径成功！", destDirName);
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            logger.info("创建目录{}成功！", destDirName);
            return true;
        } else {
            logger.error("创建目录{}失败！", destDirName);
            return false;
        }
    }

}
