package hh.learng.cloud.tm

import groovy.sql.*

class GjdbcTemplate {
	
	def config = [url:'jdbc:postgresql://192.168.9.205:6543/cloudDachser_Dev',
		user:'dev',
		passwd:'dev@1234',
		driver:'org.postgresql.Driver',
		host:'192.168.9.205',
		port:'6543',
		name:'cloudDachser_Dev']
	
	def getTemplate() {
		Sql.newInstance("jdbc:postgresql://${config.host}:${config.port}/${config.name}", config.user, config.passwd, config.driver)
	}
	
	static main(args) {
		def tmp = new GjdbcTemplate();
		println tmp.config
		tmp.config.name = 'cloudLS_Dev'
		println tmp.config
		def query = tmp.getTemplate()
		query.eachRow('select * from c_tm_c_000 limit 10') {
			println it
		}
		
	}
	
}
