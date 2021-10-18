package org.opensourceframework.starter.fastdfs;

import com.google.gson.Gson;
import org.opensourceframework.starter.fastdfs.config.FileServerConfig;
import org.opensourceframework.starter.fastdfs.config.FileServerProperties;
import org.opensourceframework.starter.fastdfs.service.FileService;
import org.opensourceframework.starter.fastdfs.service.impl.MixedFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * file server自动auto registryvo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@EnableConfigurationProperties({FileServerProperties.class})
public class FileServerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FileServerAutoConfiguration.class);
    @Autowired
    private FileServerConfig fileServerConfig;

    public FileServerConfig getFileServerConfig() {
        return fileServerConfig;
    }

    public void setFileServerConfig(FileServerConfig fileServerConfig) {
        this.fileServerConfig = fileServerConfig;
    }
    @Bean
    public FileService initFileService() throws Exception {
        logger.info("文服初始化信息:{}", new Gson().toJson(fileServerConfig));
        MixedFileServiceImpl mixedFileService = new MixedFileServiceImpl(fileServerConfig.getFastdfsTrackerServers());
        mixedFileService.setUrl(fileServerConfig.getFileUrl());
        mixedFileService.setFileType(fileServerConfig.getFileType());
        mixedFileService.setFileSize(fileServerConfig.getFileSize());
        mixedFileService.setRootDir(fileServerConfig.getRootDir());
        return mixedFileService;
    }
}
