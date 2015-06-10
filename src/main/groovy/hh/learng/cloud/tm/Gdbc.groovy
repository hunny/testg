package hh.learng.cloud.tm

import groovy.sql.*

class Gdbc {
	
	def static source(map) {
		
		return sqlInstance(config(map))
		
	}

	def static config(map) {
		
		def myconfig = [
			user:'dev',
			passwd:'dev@1234',
			driver:'org.postgresql.Driver',
			host:'192.168.9.245',
			port:'6543',
			name:'cloudAC'
		]
		
		if (map && map.size() > 1) {
			myconfig.putAll(map)
		}
		return myconfig
	}
	
	def static sqlInstance(config) {
		return Sql.newInstance("jdbc:postgresql://${config.host}:${config.port}/${config.name}", config.user, config.passwd, config.driver)
	}
	
}
