package hh.learng.testgroovy.jdbc.guide.sqlite

import groovy.sql.*

class SqliteGuide {

	static main(args) {
		
		def query = sqlInstance(config([path:'/Users/hunnyhu/Desktop/sqlite.db']))
		
		query.eachRow('select * from sqlite_master') {
			println it
		}
		
		query.eachRow('select * from sign') {
			println it
		}
		
	}
	
	def static config(map) {
		
		def myconfig = [
			driver: 'org.sqlite.JDBC',
			path: 'sqlite.db'
		]
		
		if (map && map.size() > 1) {
			myconfig.putAll(map)
		}
		return myconfig
	}
	
	def static sqlInstance(config) {
		
		return Sql.newInstance("jdbc:sqlite:${config.path}", config.driver)
		
	}

}
