package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyClosureTest {
	
	//sudo wash -i en0
	//sudo reaver -d 5 -N -S -i en0 -b 78:A1:06:53:DE:FA -c 11 -vv -A
	//sudo reaver -i en0 -b 78:A1:06:53:DE:FA -N -S -a -T 0.5 -x 360 -c 11 -vv
	//sudo reaver -i en0 -b 78:A1:06:53:DE:FA -N -S -a -T 0.5 -x 360 -c 11 -vv

	@Test
	public void test() {
		//Normal construct calls
		MethodClosureSample first = new MethodClosureSample(5)
		def words = ['long string', 'medium', 'short']
		//Passinga method closure directly
		assert 'short' == words.find(first.&validate)
		
		MultiMethodSample instance = new MultiMethodSample();
		//Only a single closure is created
		Closure multi = instance.&mysteryMethod
		//Different implementations are called based on arguments types
		assert 10 == multi('string arg')
		assert 3 == multi(['lis', 'of', 'value'])
		assert 14 == multi(6, 8)
		
		//Parameter sequence with commas
		def map = ['a': 1, 'b': 2]
		//(1),我们直接把闭包作为参数传递,这是最常用的一种方式。
		map.each {key, value-> map[key] = value * 2}
		assert map == ['a': 2, 'b': 4]
		//Assign and then call a closure reference
		//(2)声明闭包的方式是与后面的用法不连贯的,花括号是 groovy 声明闭包的方式, 因此我们将闭包对象赋值给变量 doubler
		def doubler = {key, value-> map[key] = value * 2}
		map.each(doubler)
		assert map == ['a':4, 'b':8]
		//Reference and call a method as a closure
		//(4),referenc e.&操作符用来引用方法名称为一个闭包,这一次,方法也没有立即被 调用;执行的代码在接下来的一行,
		//这就像(2)一样,闭包被传递给 each 方法,这个方法 为 map 中的每一个实体进行回调。
		doubler = this.&doubleMethod
		map.each(doubler)
		assert map == ['a':8, 'b':16]
		
		//调用闭包
		//Calling closures
		def adder = {x, y -> return x + y}
		assert adder(4, 3) == 7
		assert adder.call(2, 6) == 8
		
		//Pass different closures for analysis
		def slow = benchmark(1000) {(int) it / 2}
		def fast = benchmark(1000) {it.intdiv(2)}
		assert fast < slow * 15
		
		//Closure default value
		adder = {x, y = 5 -> return x + y}
		assert adder(4, 3) == 7
		assert adder.call(7) == 12
		
		//p.130
		//闭包参数
		assert caller { one -> } == 1 
		assert caller { one, two -> } == 2
		
		def myAdder = {x, y -> return x + y}
		def myAddOne = myAdder.curry(1)
		assert myAddOne(5) == 6
		
		//通过 isCase 方法进行分类
		assert [1, 2, 3].grep {it < 3} == [1, 2]
		switch(10) {
			case {it % 2 == 1} : assert false
		}
	
		Mother julia = new Mother()
		def closure = julia.birth(4)
		def context = closure.call(this)
		println context[0].class.name
		
		assert context[1..4] == [1, 2, 3, 4]
		println context[5]
		
		def rvalue = [1, 2, 3].collect {
			if (it % 2 == 0) {
				return it * 2
			}
			return it
		}
		println rvalue
		
	}
	
	//A usual method declaration
	//(3)的方法声明是一个普通的方法,这里没有发现使用闭包的痕迹。
	def doubleMethod (entry) {
		entry.value = entry.value * 2
	}
	
	//参数的数量
	def caller (Closure closure) {
		closure.getParameterTypes().size()
	}
	
	def benchmark(repeat, Closure worker) {//1. Put closure last
		//2 Some pre-work
		def start = System.currentTimeMillis()
		//3 Call closure the given number of times
		repeat.times {worker(it)}
		//4 Some post-work
		def stop = System.currentTimeMillis()
		return stop - start
	}
	
}

class MethodClosureSample {
	int limit
	MethodClosureSample(int limit) {
		this.limit = limit
	}
	boolean validate(String value) {
		return value.length() <= limit
	}
}

class MultiMethodSample {
	int mysteryMethod (String value) {
		return value.length()
	}
	int mysteryMethod (List list) {
		return list.size()
	}
	int mysteryMethod (int x, int y) {
		return x + y
	}
}

class Mother {
	
	int field = 1
	
	int foo() {
		return 2
	}
	
	//This method creates and returns the closure
	Closure birth(param) {
		def local = 3
		def closure = {caller ->
			[this, field, foo(), local, param, caller]
		}
		return closure
	}
}

