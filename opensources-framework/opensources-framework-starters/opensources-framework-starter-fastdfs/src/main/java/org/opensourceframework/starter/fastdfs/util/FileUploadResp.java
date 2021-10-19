package org.opensourceframework.starter.fastdfs.util;

import java.io.Serializable;

public class FileUploadResp implements Serializable {

    private static final long serialVersionUID = 3530311542648652763L;
    /** 文件存放相对路径 */
    private String url;
    /** 文件上传成功与否 */
    private boolean success;
    /** 文件上传完毕提示消息 */
    private String msg;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
