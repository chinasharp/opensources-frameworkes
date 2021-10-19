package org.opensourceframework.starter.fastdfs.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author cherish
 *
 */
public class SFTP {

    private static final Logger logger = LoggerFactory.getLogger(SFTP.class);
    private final String SPLIT = "/";
    private final int retryTimes = 3;
    private final int sleepTime = 10000;
    private Session session = null;
    private Channel channel = null;
    private JSch jsch = null;
    private int TIMEOUT = 15000;
    private ChannelSftp sftp = null;
    private int currRetryTimes = 1;

    public SFTP() {
        jsch = new JSch();
    }

    public void setTimeout(int time) {
        this.TIMEOUT = time;
    }

    public void connect(String ip, int port, String user, String pwd) {
        while (currRetryTimes <= retryTimes) {
            logger.info("第{}次尝试sftp连接{}:{}", currRetryTimes, ip, port);
            try {
                if (port <= 0) {
                    // 连接服务器，采用默认端口
                    session = jsch.getSession(user, ip);
                } else {
                    // 采用指定的端口连接服务器
                    session = jsch.getSession(user, ip, port);
                }

                // 如果服务器连接不上，则抛出异常
                if (session == null) {
                    throw new Exception("fail to connect server:." + ip);
                }

                // 设置登陆主机的密码
                session.setPassword(pwd);// 设置密码
                // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
                session.setConfig("StrictHostKeyChecking", "no");

                // 设置登陆超时时间
                session.connect(TIMEOUT);
                // 创建sftp通信通道
                channel = session.openChannel("sftp");
                channel.connect(TIMEOUT);
                sftp = (ChannelSftp) channel;
                if (null != sftp) {
                    logger.info("sftp连接成功！");
                    break;
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            logger.info("第{}次sftp连接失败,{}秒后尝试重连。", currRetryTimes, sleepTime / 1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            currRetryTimes++;
        }
    }

    /**
     * 根据密钥连接sftp
     *
     * @param ip
     * @param port
     * @param user
     * @param priKeyPath
     */
    public void connectByPriKey(String ip, int port, String user, String priKeyPath) {
        while (currRetryTimes <= retryTimes) {
            logger.info("第{}次尝试sftp连接{}:{}", currRetryTimes, ip, port);
            try {
                jsch.addIdentity(priKeyPath);
                if (port <= 0) {
                    // 连接服务器，采用默认端口
                    session = jsch.getSession(user, ip);
                } else {
                    // 采用指定的端口连接服务器
                    session = jsch.getSession(user, ip, port);
                }

                // 如果服务器连接不上，则抛出异常
                if (session == null) {
                    throw new Exception("fail to connect server:." + ip);
                }
                // 设置登陆主机的密码
                // session.setPassword(pwd);// 设置密码
                // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
                session.setConfig("StrictHostKeyChecking", "no");

                // 设置登陆超时时间
                session.connect(TIMEOUT);
                // 创建sftp通信通道
                channel = session.openChannel("sftp");
                channel.connect(TIMEOUT);
                sftp = (ChannelSftp) channel;
                if (null != sftp) {
                    logger.info("sftp连接成功！");
                    break;
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            logger.info("第{}次sftp连接失败,{}秒后尝试重连。", currRetryTimes, sleepTime / 1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            currRetryTimes++;
        }
    }

    private boolean isDirExist(String path) {
        boolean rtn = true;
        try {
            sftp.cd(path);
        } catch (SftpException e) {
            rtn = false;
            logger.warn("path {} not exist", path);
        }
        return rtn;
    }

    private void mkDir(String path) throws SftpException {
        path = path.replace("\\", "/");
        String[] folders = path.split("/");
        StringBuilder sb = new StringBuilder("/");
        for (String folder : folders) {
            if (folder.length() > 0) {
                sb.append(folder)
                    .append("/");
                try {
                    sftp.cd(sb.toString());
                } catch (SftpException e) {
                    sftp.mkdir(sb.toString());
                    sftp.cd(sb.toString());
                }
            }
        }
    }

    public void close() {
        sftp.disconnect();
        channel.disconnect();
        session.disconnect();
    }

    public void upload(String directory, String uploadFile) {
        try {
            if (!isDirExist(directory)) {
                mkDir(directory);
            }
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
            logger.info("{},{}上传成功！", directory, uploadFile);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void upload(String fileName, String path, InputStream is) throws SftpException {
        if (!isDirExist(path)) {
            mkDir(path);
        }
        sftp.put(is, path + SPLIT + fileName);
    }

    //    public void getFile(String remoteFilePath, String remoteFileName, HttpServletResponse response)
    //            throws SftpException, IOException {
    //        isDirExist(remoteFilePath);
    //
    //        InputStream istream = null;
    //        OutputStream out = null;
    //        try {
    //            istream = sftp.get(remoteFilePath + SPLIT + remoteFileName);
    //            out = response.getOutputStream();
    //            response.reset();
    //            response.setContentType("application/octet-stream");
    //            response.setHeader("Content-Disposition",
    //                    "attachment; filename=\"" + new String(remoteFileName.getBytes("utf-8"), "iso8859-1") + "\"");
    //
    //            byte[] temp = new byte[1024];
    //            int size = 0;
    //            while ((size = istream.read(temp)) != -1) {
    //                out.write(temp, 0, size);
    //            }
    //
    //        } catch (IOException e) {
    //            LOGGER.error("", e);
    //        } finally {
    //            if (null != istream) {
    //                try {
    //                    istream.close();
    //                } catch (IOException e) {
    //                    LOGGER.error("", e);
    //                }
    //            }
    //
    //            if (null != out) {
    //                try {
    //                    out.close();
    //                } catch (IOException e) {
    //                    LOGGER.error("", e);
    //                }
    //            }
    //        }
    //
    //    }
}
