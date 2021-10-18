package org.opensourceframework.demo.es.dao;

import org.opensourceframework.demo.es.eo.OrderRecordToTelemarketEo;
import org.opensourceframework.component.es.base.BaseEsDao;
import org.opensourceframework.component.es.base.ESTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * * @author yu.ce@foxmail.com
 * 
 */
@Repository
public class OrderRecordToTelemarketDao extends BaseEsDao<OrderRecordToTelemarketEo, String> {
	@Autowired
	private ESTemplate esTemplate;

	@Override
	protected ESTemplate getESTemplate() {
		return esTemplate;
	}
}
