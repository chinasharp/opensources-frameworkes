package org.opensourceframework.starter.ratelimiter.helper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 限流工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/2 下午6:00
 */
public class RateLimiterHelper {
	private static final Map<String , String> luaMap = new HashMap<>();
	private static final String LUA_SUFFIX = ".lua";

	private static void putScript(String key , String script){
		luaMap.put(key , script);
	}

	public String getScript(String key){
		return luaMap.get(key);
	}

	public static void loadLuaScript(){
		URL url = RateLimiterHelper.class.getClassLoader().getResource("/");
		File file = new File(url.getFile());
		readLuaFile(file);
	}

	private static void readLuaFile(File file) {
		if (file.exists() && file != null) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					readLuaFile(f);
				}
			} else {
				if(file.getName().endsWith(LUA_SUFFIX)){
					try {
						String key = file.getName();
						int endIndex = key.indexOf(LUA_SUFFIX);
						key = key.substring(0 , endIndex);
						String val = FileUtils.readFileToString(file);
						luaMap.put(key , val);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
