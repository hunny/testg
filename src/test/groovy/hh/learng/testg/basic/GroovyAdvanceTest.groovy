package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class GroovyAdvanceTest {

	@Test
	public void testExpando() {
		def abc = new Expando()
		abc.abc = 'ABC'
		println abc
	}
	
	@Test
	public void testThisVariable() {
		println this
		println this.class
		println 'Methods:'
		this.class.methods.each {
			println it
		}
		println 'Method Names:'
		this.class.methods.name.each {
			println it
		}
	}
	
	@Test
	public void testMethodProperty() {
		println 'this.class.methods.name.grep(~/get.*/).sort()'
		this.class.methods.name.grep(~/get.*/).sort()
		println 'this.class.methods.name.class.methods.name'
		this.class.methods.name.class.methods.name
	} 
	
	@Test
	public void testXml() {
		def builder = new groovy.xml.MarkupBuilder()
		builder.numbers {
			description 'my test'
			for (i in 10..15) {
				number(value:i, square: i * i) {
					for (j in 2..<i) {
						if (i % j == 0) {
							factor(value:j)
						}
					}
				}
			}
		}
		println builder
	}

}
