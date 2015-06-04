package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyListTest {

	@Test
	public void test() {
		
		def list = [1, 2, 3]
		assert list.size() == 3
		assert list[0] == 1
		
		assert list instanceof ArrayList
		
		def emptyList = []
		assert emptyList.size() == 0
		
		def longList = (0..1000).toList()
		assert longList[555] == 555
		
		def aList = new ArrayList()
		aList.addAll(list)
		assert aList.size() == 3
		
		aList[0] = 10
		assert aList[0] == 10
		
		aList = new LinkedList(list)
		assert aList.size() == 3
		aList[0] = 10
		assert aList[0] == 10
		
	}

}
