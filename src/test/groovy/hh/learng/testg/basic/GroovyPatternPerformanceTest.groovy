package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyPatternPerformanceTest {

	@Test
	public void test() {
		def twister = 'she sells sea shells at the sea shore of seychelles'
		//some more complicated regex:
		//word that starts and ends with same letter
		def regex = /\b(\w)\w*\1\b/
		def start = System.currentTimeMillis()
		//Find operator with implicit pattern construction
		10000.times {
			twister =~ regex
		}
		def first = System.currentTimeMillis() - start
		start = System.currentTimeMillis()
		//Explicit pattern construction
		def pattern = ~regex
		//def pattern = ~/\b(\w)\w*\1\b/
		//Apply the pattern on a String
		//注意: 不是a =~ b,而是a = ~b,小心这个地方!
		10000.times {
			pattern.matcher(twister)
		}
		def second = System.currentTimeMillis() - start
		println 'first:' + first
		println 'second:' + second
		assert first > second * 1.20
		
		assert (~/..../).isCase('bear')
		switch('bear') {
			case ~/..../ : assert true; break
			default: assert false
		}
		def beasts = ['bear', 'wolf', 'tiger', 'regex']
		assert beasts.grep(~/..../) == ['bear', 'wolf']
		assert beasts.grep(~/...../) == ['tiger', 'regex']
		
	}

}
