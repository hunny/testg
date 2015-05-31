package hh.learng.testg.javagroovy.guide;

import java.util.HashMap;
import java.util.Map;

public class GroovyScriptClassCache {

	private static final Map<String /* class文件的描述 */, Class<?>> GROOVY_SCRIPT_CLASS_CACHE = new HashMap<String, Class<?>>();

	private GroovyScriptClassCache() {

	}

	private static GroovyScriptClassCache instance = new GroovyScriptClassCache();

	public static GroovyScriptClassCache newInstance() {
		return instance;
	}

	public Class<?> getClassByKey(String key) {
		return GROOVY_SCRIPT_CLASS_CACHE.get(key);
	}

	public void putClass(String key, Class<?> clazz) {
		GROOVY_SCRIPT_CLASS_CACHE.put(key, clazz);
	}

	public boolean containsKey(String key) {
		return GROOVY_SCRIPT_CLASS_CACHE.containsKey(key);
	}

}
