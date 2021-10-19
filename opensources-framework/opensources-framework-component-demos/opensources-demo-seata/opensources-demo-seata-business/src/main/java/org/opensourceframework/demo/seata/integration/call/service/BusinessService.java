package org.opensourceframework.demo.seata.integration.call.service;

import org.opensourceframework.demo.seata.integration.common.dto.BusinessDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 17:17
 */
public interface BusinessService {

    ObjectResponse handleBusiness(BusinessDTO businessDTO);
}
