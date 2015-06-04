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
		
		aList = ['a', 'b', 'c', 'd', 'e', 'f']
		//getAt(Range)
		assert aList[0..2] == ['a', 'b', 'c']
		//getAt(collection of indexes)
		assert aList[0, 2, 4] == ['a', 'c', 'e']
		//putAt(Range)
		aList[0..2] = ['x', 'y', 'z']
		assert aList == ['x', 'y', 'z', 'd', 'e', 'f']
		//Removing elements
		aList[3..5] = []
		assert aList == ['x', 'y', 'z']
		//Adding elements
		aList[1..1] = ['y', '1', '2']
		assert aList == ['x', 'y', '1', '2', 'z']
		
		aList = []
		//plus(Object)
		aList += 'a'
		assert aList == ['a']
		//plus(Collection)
		aList += ['b', 'c']
		assert aList == ['a', 'b', 'c']
		aList = []
		//leftShift is like append
		aList << 'a' << 'b'
		assert aList == ['a', 'b']
		//minus(Collection)
		assert aList - ['b'] == ['a']
		//Multipy
		assert aList * 2 == ['a', 'b', 'a', 'b']
		
		//Lists taking part in control structures
		aList = ['a', 'b', 'c']
		assert aList.isCase('a')
		def candicate = 'a'
		//Classify by containment
		switch(candicate) {
			case aList: assert true; break
			default: assert false
		}
		//Intersection filter
		assert ['x', 'a', 'z'].grep(aList) == ['a']
		aList = []
		//Empty lists are false
		if (aList) assert false
		//Lists can be interated with a 'for' loop
		def log = ''
		for (i in [1, 'x', 5]) {
			log += i
		}
		assert log == '1x5'
		
		//Methods to manipulate list content
		assert [1, [2, 3]].flatten() == [1, 2, 3]
		assert [1, 2, 3].intersect([4, 3, 1]) == [3, 1]
		assert [1, 2, 3].disjoint([4, 5, 6])
		
		list = [1, 2, 3]
		def popped = list.pop()
		//Treating a list like a stack
		assert popped == 3
		assert list == [1, 2]
		assert [1, 2].reverse() == [2, 1]
		assert [3, 1, 2].sort() == [1, 2, 3]
		list = [[1, 0], [0, 1, 2]]
		list = list.sort {a, b ->
			a[0] <=> b[0]
		}
		//Comparing lists by first element
		assert list == [[0, 1, 2], [1, 0]]
		list = list.sort {item -> item.size()}
		assert list == [[1, 0], [0, 1, 2]]
		list = ['a', 'b', 'c']
		list.remove(2)
		assert list == ['a', 'b']
		list.remove('b')
		assert list == ['a']
		
		list = ['a', 'b', 'c']
		list.removeAll(['b', 'c'])
		assert list == ['a']
		
		//Transforming one list into another
		def doubled = [1, 2, 3].collect {
			it * 2
		}
		assert doubled == [2, 4, 6]
		
		//Finding every element matching the closure
		def odd = [1, 2, 3].findAll {
			it % 2 == 1
		}
		assert odd == [1, 3]
		
		def x = [1, 1, 1]
		assert [1] == new HashSet(x).toList()
		assert [1] == x.unique()
		
		x = [1, null, 1]
		assert [1, 1] == x.findAll { it != null}
		assert [1, 1] == x.grep{it}
		
		//List query iteration and accumulation
		//Query
		list = [1, 2, 3]
		assert list.count(2) == 1
		assert list.max() == 3
		assert list.min() == 1
		def even = list.find {item ->
			item % 2 == 0
		}
		assert even == 2
		assert list.every {item -> item < 5}
		assert list.any {item -> item < 2}
		//Iteration
		def store = ''
		list.each{item -> store += item}
		assert store == '123'
		store = ''
		list.reverseEach {item ->
			store += item
		}
		assert store == '321'
		//Accumulation
		assert list.join('-') == '1-2-3'
		def result = list.inject(0) {clinks, guests ->
			clinks += guests
		}
		assert result == 0 + 1 + 2 + 3
		assert list.sum() == 6
		def factorial = list.inject(1) {fac, item ->
			fac *= item
		}
		assert factorial == 1 * 1 * 2 * 3
		
	}

}
