package hh.learng.cloud.tm

import groovy.sql.*

class CompareTableAndViews {

	static main(args) {
		
		def srcConfig = [
			user:'dev',
			passwd:'dev@1234',
			driver:'org.postgresql.Driver',
			host:'192.168.9.205',
			port:'6543',
			name:'cloudAC_Dev'
		]
		
		def srcSql = sqlInstance(srcConfig)
		
		def sql = "SELECT table_schema,table_name FROM information_schema.tables WHERE table_name like 'act%' ORDER BY table_schema,table_name;"
		def vsql = "select table_name from INFORMATION_SCHEMA.views "//WHERE table_schema = ANY (current_schemas(false))
		def table = [:]
		def view = [:]
		
		srcSql.eachRow('SELECT datname FROM pg_database WHERE datistemplate = false') {
			
		}
		
		srcSql.eachRow(sql) {
			table[it.table_name] = 'YES'
		}
		
		srcSql.eachRow(vsql) {
			view[it.table_name] = 'YES'
		}
		
		def destConfig = [
			user:'dev',
			passwd:'dev@1234',
			driver:'org.postgresql.Driver',
			host:'192.168.9.245',
			port:'6543',
			name:'cloudAC'
		]
		
		def destSql = sqlInstance(destConfig)
		
		destSql.eachRow(sql) {
			table[it.table_name] = 'OK'
		}
		
		destSql.eachRow(vsql) {
			view[it.table_name] = 'OK'
		}
		
		def i = 0
		table.each() {
			if (it.value == 'YES') {
				println it.key
				i ++
			}
		}
		println '====================> Table Different Number:' + i
		
		def n = 0
		view.each() {
			if (it.value == 'YES') {
				println it.key
				n ++
			}
		}
		println '====================> Views Different Number:' + n
	}
	
	def static sqlInstance(config) {
		return Sql.newInstance("jdbc:postgresql://${config.host}:${config.port}/${config.name}", config.user, config.passwd, config.driver)
	}

}
