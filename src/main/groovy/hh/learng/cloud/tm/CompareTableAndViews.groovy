package hh.learng.cloud.tm

class CompareTableAndViews {

	static main(args) {
		
		def srcSql = Gdbc.source([host:'192.168.9.205', name:'cloudAC_Dev'])
		
		def tsql = "SELECT table_schema,table_name FROM information_schema.tables WHERE table_name like 'act%' ORDER BY table_schema, table_name;"
		def vsql = "SELECT table_name FROM information_schema.views "//WHERE table_schema = ANY (current_schemas(false))
		def table = [:]
		def view = [:]
		
		srcSql.eachRow('SELECT datname FROM pg_database WHERE datistemplate = false') {
			
		}
		
		srcSql.eachRow(tsql) {
			table[it.table_name] = 'YES'
		}
		
		srcSql.eachRow(vsql) {
			view[it.table_name] = 'YES'
		}
		
		def destSql = sqlInstance(config([host:'192.168.9.245', name:'cloudAC']))
		
		destSql.eachRow(tsql) {
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

}
