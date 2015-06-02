package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyRegexExpressionTest {

	/**
	 * Groovy三个操作与正则表达式
	 * regex查找操作符：=~
	 * regex匹配操作符：==~
	 * regex模式操作符：~String
	 */
	@Test
	public void test() {
		
		assert "abc" == /abc/
		assert "\\d" == /\d/
		def reference = "hello"
		assert reference == /$reference/
		assert "\$" == /$/
		
		def twister = 'she sells sea shells at the sea shore of seychelles'
		//twister must contain a substring of size 3
		//that starts with s and ends with a
		assert twister =~ /s.a/ //Regex find operator as usable in if
		
		//Find expression evaluates to a matcher object
		def finder = (twister =~ /s.a/)
		assert finder instanceof java.util.regex.Matcher
		
		//twister must contain only words delimited by single spaces
		assert twister ==~ /(\w+ \w+)*/
		
		//Match expression evaluates to a Boolean
		def WORD = /\w+/
		def matches = (twister ==~ /($WORD $WORD)*/)
		assert matches instanceof java.lang.Boolean
		
		//Match is full, not partial like find.
		assert (twister ==~ /s.a/) == false
		
		def wordsByX = twister.replaceAll(WORD, 'x')
		assert wordsByX == 'x x x x x x x x x x'
		
		def words = twister.split(/ /)
		assert words.size() == 10
		assert words[0] == 'she'
		
	}

}
