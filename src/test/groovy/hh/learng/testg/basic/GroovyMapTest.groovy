package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyMapTest {

	@Test
	public void test() {
		//Specifying maps
		def map = [a:1, b:2, c:3]
		assert map instanceof HashMap
		assert map.size() == 3
		assert map['a'] == 1
		
		def emptyMap = [:]
		assert emptyMap.size() == 0
		
		def explicitMap = new TreeMap()
		explicitMap.putAll(map)
		assert explicitMap['a'] == 1
		
		//Retrieve existing elements
		assert map['a'] == 1
		assert map.a == 1
		assert map.get('a') == 1
		assert map.get('a', 0) == 1
		
		//Attempt to retrieve missing elements
		assert map['d'] == null
		assert map.d == null
		assert map.get('d') == null
		
		//Supply a default
		assert map.get('d', 0) == 0
		assert map.d == 0
		
		//Simple assignments in the map
		map['d'] = 1
		assert map.d == 1
		map.d = 2
		assert map.d == 2
		
		map = ['a.b': 1]
		assert map.'a.b' == 1
		
		//Query methods on maps
		map = [a:1, b:2, c:3]
		def other = [b:2, c:3, a:1]
		//Call to equals
		assert map == other
		
		//Normal JDK methods
		assert map.isEmpty() == false
		assert map.size() == 3
		assert map.containsKey('a')
		assert map.containsValue(1)
		assert map.keySet() == toSet(['a', 'b', 'c'])
		assert map.entrySet() instanceof Collection
		
		//Methods added by GDK
		assert map.any {entry -> entry.value > 2}
		assert map.every {entry -> entry.key < 'd'}
		
		//Iterating over maps(GDK)
		//Iterate over entries
		map = [a:1, b:2, c:3]
		def store = ''
		map.each {entry ->
			store += entry.key
			store += entry.value
		}
		assert store.contains('a1')
		assert store.contains('b2')
		assert store.contains('c3')
		
		//Iterate over keys/values
		store = ''
		map.each {key, value ->
			store += key
			store += value
		}
		assert store.contains('a1')
		assert store.contains('b2')
		assert store.contains('c3')
		
		//Iterate over just the keys
		store = ''
		for (key in map.keySet()) {
			store += key
		}
		assert store.contains('a')
		assert store.contains('b')
		assert store.contains('c')
		
		//Iterate over just the values
		store = ''
		for (value in map.values()) {
			store += value
		}
		assert store.contains('1')
		assert store.contains('2')
		assert store.contains('3')
		
		//Changing map content and building new objects from it
		map = [a:1, b:2, c:3]
		map.clear()
		assert map.isEmpty()
		
		map = [a:1, b:2, c:3]
		map.remove('a')
		assert map.size() == 2
		
		map = [a:1, b:2, c:3]
		def abMap = map.subMap(['a', 'b'])
		assert abMap.size() == 2
		
		abMap = map.findAll {entry -> entry.value < 3}
		assert abMap.size() == 2
		assert abMap.a == 1
		
		def found = map.find {entry -> entry.value < 2}
		assert found.key == 'a'
		assert found.value == 1
		
		def doubled = map.collect {entry -> entry.value *= 2}
		assert doubled instanceof List
		assert doubled.every {item -> item % 2 == 0}
		
		def addTo = []
		map.collect(addTo) {entry -> entry.value *= 2}
		assert addTo instanceof List
		assert addTo.every {item -> item % 2 == 0}
		
	}
	
	//Utility method used for assertions
	def toSet(list) {
		new java.util.HashSet(list)
	}

}
