package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyRangeTest {

	/**
	 * 范围用两个点“..”表示范围操作符,用来指定从左边到右边的边界,
	 * 这个操作符具 有较低的优先级,因此你常常需要使用圆括号把它们括起来,
	 * 范围也可以通过相应的构造方 法进行声明。
	 * “..<”范围操作符号指定了一个半排除范围——也就是说,右边界值不是range的一部分:
	 */
	@Test
	public void test() {
		
		assert (0..10).contains(0)
		assert (0..10).contains(5)
		assert (0..10).contains(10)
		
		assert (0..10).contains(-1) == false
		assert (0..10).contains(11) == false
		
		assert (0..<10).contains(9)
		assert (0..<10).contains(10) == false
		
		def a = 0..10
		assert a instanceof Range
		assert a.contains(5)
		
		a = new IntRange(0, 10)
		assert a.contains(5)
		
		(0.0..2.0).each {
			println '((0.0)..(2.0)):' + it
		}
		assert  ((0.0)..(2.0)).contains(1.0)
		
		def today = new Date()
		def yesterday = today - 1
		assert (yesterday..today).size() == 2
		
		assert ('a'..'c').contains('b')
		def log = ''
		for (element in 5..9) {
			log += element
		}
		assert log == '56789'
		log = ''
		(9..<5).each { element ->
			log += element
		}
		assert log == '9876'
		
		assert (0..10).isCase(5)
		
		def rate = 0.0
		def age = 36
		switch(age) {
			case 16..20: rate = 0.05; break
			case 21..50: rate = 0.06; break
			case 51..65: rate = 0.07; brea;
			default: throw new IllegalArgumentException()
		}
		assert rate == 0.06
		
		def ages = [20, 36, 42, 56]
		def midage = 21..50
		assert ages.grep(midage) == [36, 42]
		
		/**
		 * range可以使用任何类型, 只要这个类型满足以下两个条件:
		 *  该类型实现next和previous方法,也就是说,重写++和--操作符;
		 *  该类型实现java.lang.Comparable接口;也就是说实现compareTo方法, 实际上是重写<=>操作符。
		 */
		def mon = new CustomRangesOfWeekdays('Mon')
		def fri = new CustomRangesOfWeekdays('Fri')
		def worklog = ''
		for (day in mon..fri) {
			worklog += day.toString() + ' '
		}
		println worklog
		
	}

}
