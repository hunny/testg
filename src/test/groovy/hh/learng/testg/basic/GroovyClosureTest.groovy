package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyClosureTest {

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
