package hh.learng.testg

import groovy.transform.NotYetImplemented

/**
 * Reference Url:
 * http://docs.groovy-lang.org/latest/html/documentation/core-testing-guide.html#_unit_tests_with_junit_3_and_4
 * @author Hunny.Hu
 *
 */
class AppGroovyTest extends GroovyTestCase {

	/**
	 * Assertion Methods
	 */
	void testAssertions() {
        assertTrue(1 == 1)
        assertEquals("test", "test")

        def x = "42"
        assertNotNull "x must not be null", x
        assertNull null

        assertSame x, x
    }
	
	/**
	 * An interesting assertion method that is added by GroovyTestCase is assertScript. 
	 * It ensures that the given Groovy code string succeeds without any exception:
	 */
	void testScriptAssertions() {
		assertScript '''
        def x = 1
        def y = 2

        assert x + y == 3
    '''
	}
	
	/**
	 * shouldFail Methods
	 */
	void testInvalidIndexAccess1() {
		def numbers = [1,2,3,4]
		shouldFail {
			numbers.get(4)
		}
	}
	
	/**
	 * notYetImplemented Method
	 */
	void testNotYetImplemented() {
		if (notYetImplemented()) return
	
		assert 1 == 2
	}
	
	@NotYetImplemented
	void testNotYetImplemented2() {
		assert 1 == 2
	}
	
	
}
