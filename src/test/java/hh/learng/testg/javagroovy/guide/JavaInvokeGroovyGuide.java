package hh.learng.testg.javagroovy.guide;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;

import java.io.File;
import java.lang.reflect.Method;

public class JavaInvokeGroovyGuide {

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		exeGroovyClassWithParameters();
		exeGroovyFragment();
		exeGroovyFile();
		System.out.print(System.currentTimeMillis() - time);
		System.out.println(" ms");
	}

	/**
	 * 直接调用Groovy文件并执行，需要Groovy文件里有main启动方法
	 */
	protected static void exeGroovyFile() {
		System.out.println("===================> Executing exeGroovyFile");
		try {
			GroovyScriptEngine engine = new GroovyScriptEngine("");
			Binding binding = new Binding();
			binding.setVariable("language", "Groovy");
			System.out.println("===================> "
					+ new File(JavaInvokeGroovyGuide.class.getResource(
							"/hh/learng/testgroovy/TestGroovy.class").toURI()));
			Object value = engine.run(
					"src/main/groovy/hh/learng/testgroovy/TestGroovy.groovy",
					binding);
			System.out.println("===================> value = " + value);
			assert value.equals("The End");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("===================> Executed exeGroovyFile");
	}

	/**
	 * 直接调用Groovy脚本片断
	 */
	protected static void exeGroovyFragment() {
		System.out.println("===================> Executing exeGroovyFragment");
		Binding binding = new Binding();
		binding.setVariable("x", 10);
		binding.setVariable("language", "Groovy");
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell
				.evaluate("println \"Welcome to $language\"; y = x * 2; z = x * 3; println x ;return x;");
		System.out.println("===================> value = " + value);
		assert value.equals(10);
		assert binding.getVariable("y").equals(20);
		assert binding.getVariable("z").equals(30);
		System.out.println("===================> Executed exeGroovyFragment");
	}

	/**
	 * 直接调用Groovy脚本类，并从外面直接传递参数
	 * 通过看groovy的创建类的地方，就能发现，每次执行的时候，都会新生成一个class文件，这样就会导致JVM的perm区持续增长
	 * ，进而导致FullGCc问题，解决办法很简单，就是脚本文件变化了之后才去创建文件，之前从缓存中获取即可。
	 */
	protected static void exeGroovyClassWithParameters() {
		System.out
				.println("===================> Executing exeGroovyClassWithParameters");
		/**
		 * 为啥要每次new一个GroovyClassLoader，而不是所有的脚本持有一个？
		 * 因为如果脚本重新加载了，这时候就会有新老两个class文件， 如果通过一个classloader持有的话，这样在GC扫描的时候，
		 * 会认为老的类还在存活，导致回收不掉，所以每次new一个就能解决这个问题了。
		 */
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader(
				JavaInvokeGroovyGuide.class.getClassLoader());
		GroovyScriptClassCache groovyScriptClassCache = GroovyScriptClassCache
				.newInstance();
		try {
			String scriptText = "class Foo {\n"
					+ "  int add(int x, int y) { x + y }}";
			Class<?> newClazz = null;
			System.out
					.println("script text hash code:" + scriptText.hashCode());
			String classKey = String.valueOf(scriptText.hashCode());
			// 先从缓存里面去Class文件
			if (groovyScriptClassCache.containsKey(classKey)) {
				newClazz = groovyScriptClassCache.getClassByKey(classKey);
			} else {
				newClazz = groovyClassLoader.parseClass(scriptText);
				groovyScriptClassCache.putClass(classKey, newClazz);
			}
			GroovyObject go = (GroovyObject) newClazz.newInstance();
			System.out.println("===================> GroovyObject HashCode:"
					+ go.hashCode());
			for (Method method : go.getClass().getDeclaredMethods()) {
				System.out.println("===================> GroovyObject method:"
						+ method.getName());
			}
			Object obj = newClazz.newInstance();
			System.out.println("===================> JavaObject HashCode:"
					+ obj.hashCode());
			for (Method method : obj.getClass().getDeclaredMethods()) {
				System.out.println("===================> JavaObject method:"
						+ method.getName());
			}
			Object i = obj.getClass().getMethod("add", int.class, int.class)
					.invoke(obj, 23, 3);
			System.out.println(i);
		} catch (Exception e) {
		}
		System.out
				.println("===================> Executed exeGroovyClassWithParameters");
	}

}
