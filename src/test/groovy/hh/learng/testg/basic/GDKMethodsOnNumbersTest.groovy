package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GDKMethodsOnNumbersTest {

	@Test
	public void test() {
		def store = ''
		10.times {
			store += 'x'
		}
		println store
		
		store = ''
		//Walking up with loop variable
		1.upto(5) { number ->
			store += number
		}
		println store
		
		store = ''
		//Walking down
		2.downto(-2) { number ->
			store += number + ' '
		}
		println store
		
		store = ''
		//Walking with step width
		0.step(0.5, 0.1) { number ->
			store += number + ' '
		}
		println store
		
	}

}
