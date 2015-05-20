package hh.learng.testgroovy

class ClosureTest {
	
	def closureAbc = {int a, int b -> a + b}
	
	static main(args) {
		
		println new ClosureTest().closureAbc(1, 9)
		
	}

}
