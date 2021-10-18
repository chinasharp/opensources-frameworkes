package org.opensourceframework.component.seclog.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.helper.ReflectHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义日志内容帮助类
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Slf4j
public class ContextHelper {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{.+?\\}");

    /**
     * 处理自定义文本
     *
     * @param joinPoint
     * @param context
     * @param args
     * @return
     */
    public static String handleContext(ProceedingJoinPoint joinPoint, String context, Object[] args) {
        // 获取参数名称和值map
        Map<String, Object> nameFieldsMap = getNameFieldsMap(joinPoint, args);
        // 替换${}符号中的参数值
        context = replaceContext(context, nameFieldsMap);
        log.info(context);
        return context;
    }

    /**
     * 获取方法入参名称及属性值
     *
     * @param joinPoint
     * @param args
     * @return
     */
    private static Map<String, Object> getNameFieldsMap(ProceedingJoinPoint joinPoint, Object[] args) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        // 通过map封装参数和参数值
        HashMap<String, Object> nameFields = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            nameFields.put(parameterNames[i], args[i]);
        }
        return nameFields;
    }

    // /**
    //  * 获取方法入参名称及属性值
    //  * @param cls
    //  * @param clazzName
    //  * @param methodName
    //  * @param args
    //  * @return
    //  * @throws NotFoundException
    //  */
    // private static Map<String, Object> getNameFieldsMap(Class cls, String clazzName,
    //     String methodName, Object[] args) throws ClassNotFoundException, NoSuchMethodException {
    //     Class<?>[] classes = new Class[args.length];
    //     for (int k = 0; k < args.length; k++) {
    //         if (!args[k].getClass().isPrimitive()) {
    //             // 获取的是封装类型而不是基础类型
    //             String result = args[k].getClass().getName();
    //             Class s = map.get(result);
    //             classes[k] = s == null ? args[k].getClass() : s;
    //         }
    //     }
    //     ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
    //     // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
    //     Class<?> aClass = Class.forName(clazzName);
    //     Method method = aClass.getMethod(methodName, classes);
    //     // 参数名
    //     String[] parameterNames = pnd.getParameterNames(method);
    //     // 通过map封装参数和参数值
    //     HashMap<String, Object> nameFields = new HashMap();
    //     for (int i = 0; i < parameterNames.length; i++) {
    //         nameFields.put(parameterNames[i], args[i]);
    //     }
    //     return nameFields;
    // }

    // private static HashMap<String, Class> map = new HashMap<String, Class>() {
    //     {
    //         put("java.lang.Integer", int.class);
    //         put("java.lang.Double", double.class);
    //         put("java.lang.Float", float.class);
    //         put("java.lang.Long", long.class);
    //         put("java.lang.Short", short.class);
    //         put("java.lang.Boolean", boolean.class);
    //         put("java.lang.Char", char.class);
    //     }
    // };

    /**
     * 文本替换${}为具体的值
     *
     * @param context
     * @param nameFieldsMap
     * @return
     */
    private static String replaceContext(String context, Map<String, Object> nameFieldsMap) {
        Matcher matcher = PATTERN.matcher(context);
        while (matcher.find()) {
            String group = matcher.group(0);
            String keyAndField = group.substring(2, group.length() - 1);
            String keyName = StringUtils.split(keyAndField, ".")[0];
            String fieldName = StringUtils.split(keyAndField, ".")[1];
            for (String key : nameFieldsMap.keySet()) {
                if (keyName.equals(key)) {
                    Object arg = nameFieldsMap.get(key);
                    Object fieldValue = ReflectHelper.getFieldValue(arg, fieldName);
                    if (fieldValue != null) {
                        context = StringUtils.replaceAll(context, escapeExprSpecialWord(group),
                            JSON.toJSONString(fieldValue));
                    }
                }
            }
        }
        return context;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public String testContext(Map<String, String> params) {
        System.out.println(params);
        return "";
    }
}
