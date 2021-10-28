#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.mq.handler;


import ${package}.api.dto.request.DemoReqDto;
import ${package}.biz.dao.eo.DemoEo;
import ${package}.biz.service.IDemoService;
import ${groupId}.base.helper.BeanHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * MQ消息处理类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoHandler {
	@Resource
	private IDemoService demoService;

	@Transactional(rollbackFor = Exception.class)
	public DemoEo handler(DemoReqDto reqDto){
		DemoEo demoUserEo = new DemoEo();
		BeanHelper.copyProperties(demoUserEo , reqDto);
		demoService.saveOrUpdate(demoUserEo);
		return demoUserEo;
	}

	public void rollBackHandler(Long pkId){
		demoService.deleteById(pkId);
	}
}
