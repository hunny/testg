package hh.learng.testg.basic;

import static org.junit.Assert.*;

import org.junit.Test;

class TestClosureConsole {

	@Test
	public void test() {
	
		def configurator = {format, filter, line ->
			filter(line) ? format(line) : null
		}
		
		def appender = {config, append, line ->
			def out = config(line)
			if (out) append(out)
		}
		
		def dateFormatter = {line ->
			"${new Date()} : $line"
		}
		
		def debugFilter = {line ->
			line.contains('debug')
		}
		
		def consoleAppender = {line ->
			println line
		}
		
		def myConfig = configurator.curry(dateFormatter, debugFilter)
		def myLog = appender.curry(myConfig, consoleAppender)
		
		myLog('Here is some debug message.')
		myLog('This is will not be printed')
	}

}
