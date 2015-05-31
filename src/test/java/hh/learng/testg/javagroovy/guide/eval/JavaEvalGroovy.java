package hh.learng.testg.javagroovy.guide.eval;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Java Eval Groovy Script
 * @author hunnyhu
 */
public class JavaEvalGroovy {
	
	public Object doIt() {
		ScriptEngineManager factory = new ScriptEngineManager(
				JavaEvalGroovy.class.getClassLoader());
		ScriptEngine scriptEngine = factory.getEngineByName("groovy");// 或者"Groovy"
																		// groovy版本要1.6以上的
		try {
			scriptEngine.put("test", "hello world!");
			scriptEngine.put("outer", this);
			scriptEngine.eval("println test; outer.java_out()");
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		Binding bb = new Binding();
		bb.setVariable("test", "hello world!");
		bb.setProperty("outer", this);
		GroovyShell gs = new GroovyShell(bb);

		return gs.evaluate("println test; outer.java_out()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JavaEvalGroovy te = new JavaEvalGroovy();
		te.doIt();

	}

	public void java_out() {
		System.out.println("out from java");
	}

}
