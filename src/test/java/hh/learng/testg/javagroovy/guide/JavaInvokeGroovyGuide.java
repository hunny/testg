package hh.learng.testg.javagroovy.guide;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;

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
			Object value = engine.run("src/main/groovy/hh/learng/testgroovy/TestGroovy.groovy", binding);
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
	 */
	protected static void exeGroovyClassWithParameters() {
		System.out.println("===================> Executing exeGroovyClassWithParameters");
		GroovyClassLoader loader = new GroovyClassLoader();
		try {
			String scriptText = "class Foo {\n"
					+ "  int add(int x, int y) { x + y }}";
			Class<?> newClazz = loader.parseClass(scriptText);
			Object obj = newClazz.newInstance();
			Object i = obj.getClass().getMethod("add", int.class, int.class)
					.invoke(obj, 23, 3);
			System.out.println(i);
		} catch (Exception e) {
		}
		System.out.println("===================> Executed exeGroovyClassWithParameters");
	}

}
