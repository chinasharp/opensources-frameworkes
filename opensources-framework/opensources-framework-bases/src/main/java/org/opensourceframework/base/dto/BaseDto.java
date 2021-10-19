package org.opensourceframework.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto基类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ApiModel(value = "BaseDto", description = "请求基类Dto")
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 3033333352492375L;

    @ApiModelProperty(name = "id", value = "实体主键id")
    protected Long id;

    @ApiModelProperty(name = "applicationId", value = "所属应用id")
    protected Long applicationId;

    @ApiModelProperty(name = "createPerson", value = "数据创建人")
    protected String createPerson;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    protected Date createTime;

    @ApiModelProperty(name = "updatePerson", value = "最后更新人")
    protected String updatePerson;

    @ApiModelProperty(name = "updatePerson", value = "最后更新时间")
    protected Date updateTime;
    /**
     * 0 正常数据
     * 1 已删除数据
     */
    @ApiModelProperty(name = "dr", value = "删除标志")
    protected Integer dr = 0;

    @ApiModelProperty(name = "updatePersonId;", value = "创建人ID")
    protected Long createPersonId;


    @ApiModelProperty(name = "updatePersonId;", value = "更新人ID")
    protected Long updatePersonId;

    @ApiModelProperty(name = "createTimeStamp;", value = "创建时间戳")
    protected Long createTimeStamp;

    @ApiModelProperty(name = "updateTimeStamp", value = "更新时间戳")
    protected Long updateTimeStamp;

    public BaseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Long getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(Long createPersonId) {
        this.createPersonId = createPersonId;
    }

    public Long getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Long updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public Long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Long getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
}
