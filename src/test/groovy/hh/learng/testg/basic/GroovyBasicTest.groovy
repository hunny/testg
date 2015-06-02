package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyBasicTest {

	/**
	 * GroovyBeans
	 * groovy使用beans工作便利表现在三种途径: 
	 *  自动生成访问者方法
	 *  JavaBeans(包括GroovyBeans)的简化访问方式 
	 *  事情处理器的简化注册
	 */
	@Test
	public void testGroovyBeans() {
		def groovyBook = new GroovyBeansBook()
		//通过显示的方法调用来使用属性
		groovyBook.setTitle('Groovy conquers the world')
		assert groovyBook.getTitle() == 'Groovy conquers the world'
		//通过groovy的快捷方式来使用属性
		groovyBook.title = 'Groovy in Action'
		assert groovyBook.title == 'Groovy in Action'
	}

	/**
	 * 在groovy中,字符串能出现在单引号或者双引号中,在双引号的字符串中允许使用占位符,
	 * 占位符在需要的时候将自动解析,这是一个GString类型
	 */
	@Test
	public void testGString() {
		def nick = 'Gina'
		def book = 'Groovy in Action'
		assert "$nick is $book" == 'Gina is Groovy in Action'
	}

	@Test
	public void testText() {
		//正则表达式
		assert '12345' =~ /\d+/
		//正则表达式
		println '12345'.replaceAll(/\d{0,3}/, 'Q')

		//数字也是对象
		def x = 1
		def y = 2
		assert x + y == 3
		assert x.plus(y) == 3
		assert x instanceof Integer
		if ((x + y) != 3) {
			fail("Not correct");
		}
		def code = '1 + '
		code += System.getProperty('os.version')
		//prints “1 + 5.1”
		def x1 = '1'
		def y1 = '3.2'
		def z1 = '2'
		println code
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate("($x1 + $y1) * $z1")
		println value
		assert 'ABCDE'.indexOf(67) == 2
	}

	@Test
	public void testListMapAndRanges() {
		//罗马数字列表
		def roman = [
			'',
			'I',
			'II',
			'III',
			'IV',
			'V',
			'VI',
			'VII'
		]
		//访问列表
		assert roman[4] == 'IV'
		//扩张列表
		roman[8] = 'VIII'
		assert roman.size() == 9

		//map操作
		def http = [
			100 : 'CONTINUE',
			200 : 'OK',
			400 : 'BAD REQUEST'
		]
		assert http[200] == 'OK'
		http[500] = 'INTERNAL SERVER ERROR'
		assert http.size() == 4

		//范围(Ranges),一个有效的开始点和一个结束点,
		//那么range是如何从开始点移动到结束点,
		//groovy又一次在语法层面对这个概念提供了支持,
		//range就像for语句一样容易理解。
		def xr = 1..10
		assert xr.contains(5)
		assert xr.contains(15) == false
		assert xr.size() == 10
		assert xr.from == 1
		assert xr.to == 10
		assert xr.reverse() == 10..1
	}

	/**
	 * 闭包
	 */
	@Test
	public void testClosure() {
		//遍历列表
		[1, 2, 3].each {entry-> println 'closure:' + entry }
		//使用闭包计算所有在会议室的客人之间的碰杯数
		def totalClinks = 0
		def partyPeople = 100
		1.upto(partyPeople) { guestNumber ->
			totalClinks += (guestNumber - 1)
		}
		assert totalClinks == (partyPeople*(partyPeople-1))/2
	}

	/**
	 * 控制结构
	 */
	@Test
	public void testGroovyControl() {
		//在一行的if语句
		if (false) assert false
		//null表示false
		if (null) {
			assert false
		} else {
			assert true
		}
		//典型的while
		def i = 0
		while (i < 10) {
			i++
		}
		assert i == 10
		//迭代一个range
		def clinks = 0
		for (remainingGuests in 0..9) {
			clinks += remainingGuests
		}
		assert clinks == (10 * 9) / 2
		//迭代一个列表
		def list = [
			0,
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9
		]
		for (j in list) {
			assert j == list[j]
		}
		//以闭包为参数的each方法
		list.each() { item ->
			assert item == list[item]
		}
		//典型的switch
		switch(3) {
			case 1 : assert false;
				break
			case 3 : assert true;
				break
			default: assert false
		}
	}
	
	/**
	 * 测试Java调用Groovy
	 */
	@Test
	public void testJavaInvokeGroovy() {
		// 从Java代码中调用Groovy语句
		Binding binding = new Binding();
		binding.setVariable("foo", new Integer(2));
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate("println 'Hello World!'; x = 123; return foo * 10");
		assert value.equals(new Integer(20));
		assert binding.getVariable("x").equals(new Integer(123));
	}

}

/**
 * GroovyBeans测试用例Bean
 * @author hunnyhu
 */
class GroovyBeansBook {
	String title //声明一个属性
}
