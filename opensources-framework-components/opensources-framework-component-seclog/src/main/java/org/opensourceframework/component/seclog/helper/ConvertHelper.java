package org.opensourceframework.component.seclog.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.opensourceframework.component.seclog.vo.SecLogConvertVo;
import org.opensourceframework.component.seclog.vo.SecLogVo;

/**
 * 获取及转换SecLogVo帮助类
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class ConvertHelper {


    public static SecLogVo convertSecLogVo(SecLogConvertVo convertVo) {
        String appName = convertVo.getSecLog().appName();
        String functionName = convertVo.getSecLog().functionName();
        String operateName = convertVo.getSecLog().operateName();
        // 参数列表
        Object[] args = convertVo.getJoinPoint().getArgs();
        String params = argsToJson(args);
        // SecLogVo设值
        SecLogVo secLogVo = new SecLogVo();
        secLogVo.setAppName(appName);
        secLogVo.setFunctionName(functionName);
        secLogVo.setOperateName(operateName);
        secLogVo.setParams(params);
        secLogVo.setIp(convertVo.getIp());
        secLogVo.setPath(convertVo.getPath());
        secLogVo.setOperateDate(new Date());
        secLogVo.setOperator(convertVo.getOperator());
        return secLogVo;
    }

    /**
     * 入参转json字符串
     * @param args 入参
     * @return json字符串
     */
    private static String argsToJson(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        // json过滤空字符串
        PropertyFilter profilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                return !(value instanceof String) || !StringUtils.isEmpty(value.toString());
            }
        };
        // 过滤复杂的参数
        List<Object> requiredArgs = filterComplexArgs(args);
        return JSON.toJSONString(requiredArgs, profilter);
    }

    /**
     * 过滤复杂的参数
     * @param args 入参
     * @return 过滤后的参数列表
     */
    private static List<Object> filterComplexArgs(Object[] args) {
        List<Object> requiredArgs = new ArrayList<>();
        for (Object arg : args) {
            // 过滤SpringMVC的ModelMap、ModelAndView、HttpServletRequest、HttpServletResponse，不然json转换报错
            if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof ModelMap
                || arg instanceof ModelAndView) {
                continue;
            }
            // MultipartFile 打印原始名称
            if (arg instanceof MultipartFile) {
                arg = ((MultipartFile) arg).getOriginalFilename();
            }
            requiredArgs.add(arg);
        }
        return requiredArgs;
    }

}
