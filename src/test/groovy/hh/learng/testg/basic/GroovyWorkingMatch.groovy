package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyWorkingMatch {

	/**
	 * 匹配不是一个简单的字符串,而是一个字符串列表,
	 * 在位置0包含了整个匹配,如果模式包括分组,
	 * 那么可以通过match[n]进行访问,n是分组号,
	 * 分组的编号通过它们在圆括号中的顺序决定的。
	 */
	@Test
	public void test() {
		def tmp = "The rain in Spain stays mainly in the plain!"
		//words that end with 'ain': \b\w*ain\b
		def BOUNDS = /\b/
		def rhyme = /$BOUNDS\w*ain$BOUNDS/
		def found = ''
		//string.eachMatch(pattern_string)
		tmp.eachMatch(rhyme) { match ->
			match.each {
				found += it
			}
			found += ' '
		}
		assert found == 'rain Spain plain '
		
		//matcher.each(closure)
		found = ''
		(tmp =~ rhyme).each { match ->
			found += (match + ' ')
		}
		assert found == 'rain Spain plain '
		
		//string.replaceAll(pattern_string, closure)
		def cloze = tmp.replaceAll(rhyme) { it - 'ain' + '___'}
		println cloze
		assert cloze == 'The r___ in Sp___ stays mainly in the pl___!'
	}

}
