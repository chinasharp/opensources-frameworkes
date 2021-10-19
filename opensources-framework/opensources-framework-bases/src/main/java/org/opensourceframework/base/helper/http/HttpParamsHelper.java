package org.opensourceframework.base.helper.http;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.collections.MapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * http参数处理工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class HttpParamsHelper {
	public static String convertSortHttpParams(Map<String, Object> params) {
		Map<String, Object> sortedMap = params;

		if (!(sortedMap instanceof SortedMap)) {
			sortedMap = new TreeMap();
			sortedMap.putAll(params);
		}
		Joiner.MapJoiner mapJoiner = Joiner.on("&").withKeyValueSeparator("=").useForNull("");
		return mapJoiner.join(sortedMap);
	}

	public static String convertHttpParams(Map<String, Object> params) {
		String httpParms = "";
		if(MapUtils.isNotEmpty(params)) {
			Map<String , Object> notNullMap = new HashMap<>();
			for(Map.Entry<String , Object> entry : params.entrySet()){
				if(entry.getValue() != null){
					notNullMap.put(entry.getKey() , entry.getValue());
				}
			}
			Joiner.MapJoiner mapJoiner = Joiner.on("&").withKeyValueSeparator("=");
			return mapJoiner.join(notNullMap);
		}
		return httpParms;
	}

	public static Map<String, String> parseQuery(String query) {
		if (query.contains("%")) {
			try {
				query = URLDecoder.decode(query, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return Splitter.onPattern("&").withKeyValueSeparator("=").split(query);
	}
}
