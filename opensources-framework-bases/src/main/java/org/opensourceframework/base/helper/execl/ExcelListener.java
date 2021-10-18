package org.opensourceframework.base.helper.execl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取Excel监听器
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private List<T> dataList = new ArrayList<>();

    @Override
    public void invoke(T refundImport, AnalysisContext analysisContext) {
        dataList.add(refundImport);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
