package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyGStringToDoTest {

	/**
	 * What to do with strings
	 */
	@Test
	public void test() {
		def g = 'Hello Groovy!'
		assert g.startsWith('Hello')
		assert g.getAt(0) == 'H'
		assert g[0] == 'H'
		assert g.indexOf('Groovy') >= 0
		assert g.contains('Groovy')
		assert g[6..11] == 'Groovy'
		assert 'Hi' + g - 'Hello' == 'Hi Groovy!'
		assert g.count('o') == 3
		assert 'x'.padLeft(3) == '  x'
		assert 'x'.padRight(3, '_') == 'x__'
		assert 'x'.center(3) == ' x '
		assert 'x' * 3 == 'xxx'
		
		//1.追加文本和赋值一起完成
		def k = 'Hello'
		k <<= ' Groovy'
		assert k instanceof java.lang.StringBuffer
		//2.在StringBuffer上追加文本
		k << '!'
		assert k.toString() == 'Hello Groovy!'
		//3.将字符串"ello"替换为"i"
		k[1..4] = 'i'
		assert k.toString() == 'Hi, Groovy!'
		
	}

}
