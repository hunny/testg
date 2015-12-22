package hh.learng.life

import groovy.sql.*

class GMdbc {
	
	def static source(map) {
		
		return sqlInstance(config(map))
		
	}

	def static config(map) {
		
		def myconfig = [
			user:'root',
			passwd:'123456',
			driver:'com.mysql.jdbc.Driver',
			host:'192.168.9.243',
			port:'3306',
			name:'cubi-life'
		]
		
		if (map && map.size() > 1) {
			myconfig.putAll(map)
		}
		println myconfig
		return myconfig
	}
	
	def static sqlInstance(config) {
		return Sql.newInstance("jdbc:mysql://${config.host}:${config.port}/${config.name}", config.user, config.passwd, config.driver)
	}
}
