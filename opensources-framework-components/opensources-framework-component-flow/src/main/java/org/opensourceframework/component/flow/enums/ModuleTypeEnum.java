package org.opensourceframework.component.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * * @author 
 * @version 1.0
 * 
 */
@Getter
@AllArgsConstructor
public enum ModuleTypeEnum {


    JUDGE(1, "判断组件"),
    EXECUTE(2, "执行组件"),
    TRIGGER(3, "触发组件"),
    ;

    private final Integer type;
    private final String name;
}
