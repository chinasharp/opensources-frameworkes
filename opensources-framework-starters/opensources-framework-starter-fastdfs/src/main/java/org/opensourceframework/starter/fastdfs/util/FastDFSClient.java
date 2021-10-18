package org.opensourceframework.starter.fastdfs.util;

import com.roncoo.fastdfs.StorageClient;
import com.roncoo.fastdfs.TrackerClient;
import com.roncoo.fastdfs.TrackerGroup;
import com.roncoo.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author luziliandemo
 * @version Id: FastDFSClient.java, v 0.1 2020/3/11 15:25 luziliandemo Exp $$
 */
public class FastDFSClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    private static final String SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR = "/";

    private final TrackerGroup trackerGroup;

    public FastDFSClient(String[] trackerServers) throws Exception {
        InetSocketAddress[] trackerSocketAdds = new InetSocketAddress[trackerServers.length];
        for (int i = 0; i < trackerServers.length; i++) {
            String[] parts = trackerServers[i].split(":", 2);
            if (parts.length != 2) {
                throw new Exception();
            }
            trackerSocketAdds[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        this.trackerGroup = new TrackerGroup(trackerSocketAdds);
    }

    /**
     * upload file to storage server (by file buff)
     *
     * @param fileBuff file content/buff
     * @param fileExtName file ext name, do not include dot(.)
     * @return file id(including group name and filename) if success, <br>
     * return null if fail
     */
    public String uploadFile(byte[] fileBuff, String fileExtName) {

        TrackerServer trackerServer = null;
        try {
            TrackerClient tracker = new TrackerClient(trackerGroup);
            trackerServer = tracker.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] fileIds = storageClient.upload_file(fileBuff, fileExtName, null);
            return fileIds != null ? fileIds[0] + "/" + fileIds[1] : null;
        } catch (Exception e) {
            logger.error("上传文件到FastDFS发生异常：", e);
            return null;
        }
    }

    /**
     * download file from storage server
     *
     * @param trackId the file id(including group name and filename)
     * @return file content/buffer, return null if fail
     */
    public byte[] downloadFile(String trackId) {
        TrackerServer trackerServer = null;

        try {
            TrackerClient tracker = new TrackerClient(trackerGroup);
            trackerServer = tracker.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            int pos = trackId.indexOf(SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR);
            //group name
            String groupName = trackId.substring(0, pos);
            //file name
            String filePath = trackId.substring(pos + 1);
            return storageClient.download_file(groupName, filePath);

        } catch (Exception e) {
            logger.error("从FastDFS下载文件发生异常：", e);
            return null;
        }

    }

}
