package hh.learng.testgroovy.jdbc.guide.sqlite

import groovy.sql.*

class Sqlite {
	
	def static source(map) {
		
		return sqlInstance(config(map))
		
	}

	def static config(map) {
		
		def myconfig = [
			driver:'org.sqlite.JDBC',
			path:'sqlite.db'
		]
		
		if (map && !map.isEmpty()) {
			myconfig.putAll(map)
		}
		
		return myconfig
	}
	
	def static sqlInstance(config) {
		
		return Sql.newInstance("jdbc:sqlite:${config.path}", config.driver)
		
	}
	
}
