package hh.learng.life

class GenerateSql {

	static main(args) {
		def query = GMdbc.source([host:'192.168.9.243', name:'cubi-life'])
		def table = [:]
		def column = [:]
		def total = 0;
		query.eachRow("select distinct(cust_id) as cust_id from life_company") {
			println "INSERT INTO shopnc_cust_relation (table_id, cust_id, relation) VALUES (1, ${it.cust_id}, 'homepage');"
			println "INSERT INTO shopnc_cust_relation (table_id, cust_id, relation) VALUES (2, ${it.cust_id}, 'homepage');"
		}
	}

}
