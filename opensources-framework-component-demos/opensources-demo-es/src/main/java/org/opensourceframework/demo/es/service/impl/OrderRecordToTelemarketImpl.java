package org.opensourceframework.demo.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.es.api.request.OrderTelemarketReqDto;
import org.opensourceframework.demo.es.api.respones.OrderTelemarketRespDto;
import org.opensourceframework.demo.es.dao.OrderRecordToTelemarketDao;
import org.opensourceframework.demo.es.eo.OrderRecordToTelemarketEo;
import org.opensourceframework.demo.es.service.IOrderRecordToTelemarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * * @author yu.ce@foxmail.com
 * 
 */

@Service(value = "orderRecordToTelemarketService")
public class OrderRecordToTelemarketImpl implements IOrderRecordToTelemarketService {
    @Autowired
    private OrderRecordToTelemarketDao orderTelemarketDao;

    @Override
    public RestResponse<List<OrderTelemarketRespDto>> queryList(OrderTelemarketReqDto orderTelemarketReqDto) {
        Map<String , Object> queryParams = objToMap(orderTelemarketReqDto);
        List<OrderRecordToTelemarketEo> eoList = orderTelemarketDao.findList(queryParams);
        List<OrderTelemarketRespDto> dtoList = new ArrayList<>();
        BeanHelper.copyCollection(dtoList , eoList , OrderTelemarketRespDto.class);
        return new RestResponse<>(dtoList);
    }

    @Override
    public RestResponse<PageInfo<OrderTelemarketRespDto>> queryPage(OrderTelemarketReqDto orderTelemarketReqDto, Integer pageNum, Integer pageSize) {
        Map<String , Object> queryParams = objToMap(orderTelemarketReqDto);
        PageInfo<OrderRecordToTelemarketEo> pageInfoEo = orderTelemarketDao.findPage(queryParams , pageNum , pageSize);
        PageInfo<OrderTelemarketRespDto> pageInfoDto = new PageInfo<>();
        BeanHelper.copyPageInfo(pageInfoDto , pageInfoEo , OrderTelemarketRespDto.class);
        return new RestResponse<>(pageInfoDto);
    }

    private Map<String , Object> objToMap(Object obj){
        String json = JSON.toJSONString(obj);
        Map<String , Object> params = JSON.parseObject(json , Map.class);
        return params;
    }
}
