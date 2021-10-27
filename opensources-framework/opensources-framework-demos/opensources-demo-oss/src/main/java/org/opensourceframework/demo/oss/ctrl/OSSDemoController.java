package org.opensourceframework.demo.oss.ctrl;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.starter.oss.service.AliOSSService;
import org.opensourceframework.starter.oss.vo.UploadBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Api(tags = "阿里云oss操作demo ")
@RestController
@RequestMapping("/v1/demo/oss")
public class OSSDemoController{
    private static final Logger logger = LoggerFactory.getLogger(OSSDemoController.class);

    @Resource
    private AliOSSService aliOSSService;

    @PostMapping(value = "/up_load")
    @ApiOperation(value = "文件上传",notes = "文件上传")
    public RestResponse<String> upLoad(MultipartFile file)  {
        String fileName = file.getOriginalFilename();
        logger.info("获取到文件，文件名：{}, fileType:{}, logo:{}", fileName);
        try {
            UploadBody uploadBody = new UploadBody(file.getBytes());
            uploadBody.setFileName(fileName);
            return aliOSSService.uploadToOSSFileInputStrem(uploadBody);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.error("网络错误,稍后再试");
        }
    }
}
