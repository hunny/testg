package hh.learng.life

class CheckChineseField {

	static main(args) {
		def query = GMdbc.source([host:'192.168.9.243', name:'cubi-life'])
		def table = [:]
		def column = [:]
		def total = 0;
		query.eachRow("select table_name, column_name, column_comment from information_schema.columns where table_schema = 'cubi-life' and table_name like 'shopnc_%' and data_type in ('varchar', 'text', 'longtext', 'char', 'mediumtext') order by table_name asc, column_name asc") {
			def sql = 'select count(1) as count from `' + it.table_name + '` where length(`' + it.column_name + '`) <> character_length(`' + it.column_name + '`)'
			def result = query.firstRow(sql);
			total++
			if (result['count'] != 0) {
				println '[+]TABLE<' + it.table_name + '>[-]COLUMN<' + it.column_name + '>[-][' + it.column_comment + ']'
				table[it.table_name] = it.table_name
				column[it.table_name + '.' + it.column_name] = it.column_name
			}
		}
		println 'Table Count:' + table.size() + ', Column Count:' + column.size() + ', total:' + total
	}

}
