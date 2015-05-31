package hh.learng.testgroovy.closure

class GroovyClosureGuide {

	static main(args) {
		codeBlockFeature()
	}
	
	/**
	 * Groovy代码块说明
	 * @return
	 */
	static codeBlockFeature() {
		println 'Groovy中代码块的一些特性:'
		println '1.groovy的变量作用域和java相似，代码块内部声明的变量不能被外部访问调用。'
		println '2.对于Groovy Script， 用def定义的变量对binding.variables不可见。'
		println '''
	def c = 5  
	assert c == 5  
	d = 6  
	assert d == 6 //def keyword optional because we're within a script context  
	assert binding.variables.c == null  
	assert binding.variables.d == 6  
	           //when def not used, variable becomes part of binding.variables  
'''
		println '3.没有def等任何定义的可被binding.variable.参数名所访问。'
		println '4.对于第一条规则，有个例外，当变量没有def等任何定义时，该变量全局有效。'
		println '''
	try{  
	  h = 9  
	  assert binding.variables.h == 9  
	}  
	assert h == 9  
	assert binding.variables.h == 9  
'''
		println '5.代码块可以嵌套，比如try代码块，这和Java是一样的。'
	}
	

}
