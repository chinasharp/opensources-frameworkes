package org.opensourceframework.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: sjy
 * @Date: 2020/2/21 16:07
 * @Description:
 */
@ApiModel(value = "PageBaseDto", description = "分页请求基类Dto")
public class PageQueryDto implements Serializable {

    @ApiModelProperty(value = "每页条数", required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "当前页", required = true)
    private Integer pageNum = 1;

    public PageQueryDto() {
    }

    public PageQueryDto(Integer pageSize, Integer pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
