package hh.learng.testg.closure;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyClosureTest {

	@Test
	public void test() {
		//assert fail("Not yet implemented");
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

	/**
	 * 1. 闭包中可以包含代码逻辑，闭包中最后一行语句，表示该闭包的返回值，不论该语句是否冠名return关键字。
	 * 2. 如果c是无参数闭包，那么它的标准调用方法是c.call(),它的简洁调用方法是c()。
	 */
	@Test
	public void testClosureRule1() {
		def a = 'coffee'
		def c = {
			def b = 'tea'
			a + ' and ' + b //a refers to the variable a outside the closure,
			//and is remembered by the closure
		}
		assert c() == 'coffee and tea' //short for c.call()
	}

	/**
	 * 1.闭包赋值给一个变量，和变量与变量间的赋值一致。
	 */
	@Test
	public void testClosureRule2() {
		def c
		try {
			def a = 'sugar'
			c = { a } //a closure always returns its only value
		} catch(e) {
			println e.message
		}
		assert c() == 'sugar'
		def d = c //we can also assign the closure to another variable
		assert d() == 'sugar'
	}

	/**
	 * 1.调用闭包的方法等于创建一个闭包实例。对于相同闭包创建出来的不同实例，他们的对象是不同的。
	 */
	@Test
	public void testClosureRule3() {
		def c = { def e = { 'milk' }; e }
		def d = c
		assert c == d
		def v1 = c()
		def v2 = c()
		assert v1 != v2
	}

	/**
	 * 1.闭包的参数声明写在‘->’符号前，调用闭包的的标准写法是：闭包名.call(闭包参数)。
	 */
	@Test
	public void testClosureRule4() {
		def toTriple = {n -> n * 3}
		assert toTriple.call( 5 ) == 15
	}

	/**
	 * 1.对于单一存在的参数it可以不用声明，直接使用it，it在Groovy中有着特殊的意义。
	 */
	@Test
	public void testClosureRule5() {
		def c = { it*3 }
		assert c( 'run' ) == 'runrunrun'
	}

	/**
	 * 1.当且仅当闭包中有且仅有一个参数，且不显示声明，it具有唯一参数引用的作用，
	 * 其他情况下，如果在闭包参数声明中没有it，那么闭包的逻辑代码块中的it降级为普通的变量。
	 */
	@Test
	public void testClosureRule6() {
		//c = { def it = 789 }
		//a compile error when uncommented: 'it' already implicitly defined
		def c = { value1 -> def it = 789; [value1, it]}
		//works OK because no 'it' among parameters
		assert c(456) == [456, 789]
		c = {-> def it = 789; it } //zero parameters, not even 'it', so works OK
		assert c() == 789
	}

	/**
	 * 1.闭包中的参数名不能重复，it除外。
	 */
	@Test
	public void testClosureRule7() {
		def name= 'cup'
		//def c={ name-> println (name) } //a compile error when uncommented:
		//current scope already contains name 'name'
		def c = { def d= { 2 * it }; 3 * d(it) }
		//'it' refers to immediately-surrounding closure's parameter in each case
		assert c(5) == 30
	}

	def it = 2
	/**
	 * 1.如果在脚本范围内Scope已经有it的定义声明，如果闭包中再使用it特性，
	 * 那脚本中的it就近表示闭包中的参数，而owner.it表示脚本范围的it参数。
	 * 这个和java中的this有几分相似。
	 */
	@Test
	public void testClosureRule8() {
		def c = { assert it == 3; assert owner.it == 2 }
		c(3)
	}

	/**
	 * 1. 我们可以将闭包作为参数传入另外一个闭包，同时可以从一个闭包返回一个闭包。
	 */
	@Test
	public void testClosureRule9() {
		def toTriple = {n -> n * 3}
		def runTwice = { a, c -> c( c(a) )}
		assert runTwice( 5, toTriple ) == 45
		def times = { x -> { y -> x * y }}
		assert times(3)(4) == 12
	}

	/**
	 * 1.闭包的一些快捷写法，当闭包作为闭包或方法的最后一个参数。
	 * 可以将闭包从参数圆括号中提取出来接在最后，
	 * 如果闭包是唯一的一个参数，则闭包或方法参数所在的圆括号也可以省略。
	 * 对于有多个闭包参数的，只要是在参数声明最后的，均可以按上述方式省略。
	 */
	@Test
	public void testClosureRule10() {
		def runTwice = { a, c -> c(c(a)) }
		assert runTwice(5, {it * 3} ) == 45 //usual syntax
		assert runTwice(5) {it * 3} == 45 //when closure is last param, can put it after the param list

		def runTwiceAndConcat = { c -> c() + c() }
		assert runTwiceAndConcat({ 'plate' } ) == 'plateplate' //usual syntax
		assert runTwiceAndConcat(){ 'bowl' } == 'bowlbowl' //shortcut form
		assert runTwiceAndConcat{ 'mug' } == 'mugmug'
		//can skip parens altogether if closure is only param

		def runTwoClosures = { a, c1, c2 -> c1(c2(a)) }
		//when more than one closure as last params
		assert runTwoClosures( 5, {it*3}, {it*4} ) == 60 //usual syntax
		assert runTwoClosures( 5 ){it*3}{it*4} == 60 //shortcut form
	}

	/**
	 * 1.闭包接受参数的规则，会将参数列表中所有有键值关系的参数，作为一个map组装，传入闭包作为调用闭包的第一个参数。
	 */
	@Test
	public void testClosureRule11() {
		def f = {m, i, j->
			println "m=$m, i=$i, j=$j, m.x=${m.x}, m.y=${m.y}"
			i + j + m.x + m.y
		}
		assert f(6, x:4, y:3, 7) == 20

		def g = {m, i, j, k, c->
			println "m=$m, i=$i, j=$j, k=$k"
			c(i + j + k, m.x + m.y)
		}
		assert g(y:5, 1, 2, x:6, 3) {a, b-> a * b} == 66
	}

	/**
	 * 1.闭包提供了询问自己参数个数的方法，无论在闭包内或者闭包外。
	 */
	@Test
	public void testClosureRule12() {
		def c = {x,y,z-> getMaximumNumberOfParameters() }
		assert c.getMaximumNumberOfParameters() == 3
		assert c(4,5,6) == 3
	}

	/**
	 * 1.闭包可以将其最后的参数设置其默认的取值。
	 */
	@Test
	public void testClosureRule13() {
		def e = { a, b, c=3, d='a' -> "${a+b+c}$d" }
		assert e( 7, 4 ) == '14a'
		assert e( 9, 8, 7 ) == '24a' //override default value of 'c'
	}

	/**
	 * 1.闭包可以通过定义最后一个参数声明为Object[]，来获取任意多个参数。
	 * 同时，在闭包的逻辑处理中要使用这些参数则需要使用数组的each方法。
	 */
	@Test
	public void testClosureRule14() {
		def c = { arg, Object[] extras ->
			def list= []
			list<< arg
			extras.each{ list<< it }
			list
		}
		assert c( 1 )          == [1]
		assert c( 1, 2 )       == [1, 2]
		assert c( 1, 2, 3 )    == [1, 2, 3]
		assert c( 1, 2, 3, 4 ) == [1, 2, 3, 4]
	}

	/**
	 * 1.如果闭包的参数声明中没有list，那么传入参数可以设置为list，里面的参数将分别传入闭包参数。
	 */
	@Test
	public void testClosureRule15() {
		def c= {a, b, c-> a + b + c}
		def list=[1, 2, 3]
		assert c(list) == 6
	}

	/**
	 * 1.闭包有一个curry方法，该方法的作用是锁定闭包的首个参数。类似于java中的方法重载。
	 */
	@Test
	public void testClosureRule16() {
		def concat = { p1, p2, p3 -> "$p1 $p2 $p3" }
		def concatAfterFly = concat.curry( 'fly' )
		assert concatAfterFly( 'drive', 'cycle' ) == 'fly drive cycle'
		def concatAfterFlySwim = concatAfterFly.curry( 'swim' )
		assert concatAfterFlySwim( 'walk' ) == 'fly swim walk'
	}

	/**
	 * 1.闭包是可嵌套的。
	 */
	@Test
	public void testClosureRule17() {
		def gcd //predefine closure name
		gcd = { m,n-> m%n == 0 ? n: gcd(n,m%n) }
		assert gcd( 28, 35 ) == 7
	}
	
	/**
	 * 1.可以在闭包中用call闭包进行迭代。
	 */
	@Test
	public void testClosureRule18() {
		def results = [];
		{ a, b ->
		  results << a
		  a < 10 && call(b, a + b)
		} (1,1)
		assert results == [1, 1, 2, 3, 5, 8, 13]  // Fibonacci numbers
	}
	
}
