package hh.learng.cloud.tm

import groovy.sql.*
import hh.learng.apache.poi.ExcelBuilder

class GenerateDictionaryByExcel {
	
	def file = new File('D:/myfile.txt')
	
	def config(map) {
		
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
	
	def sqlInstance(config) {
		return Sql.newInstance("jdbc:postgresql://${config.host}:${config.port}/${config.name}", config.user, config.passwd, config.driver)
	}
	
	def build() {
		def query = sqlInstance(config([host:'192.168.9.245', name:'cloudHR']));
		try {
			println 'DO $$ \n BEGIN'
			file.append('DO $$ \nBEGIN\n', 'UTF-8')
			def company = query.firstRow('SELECT pf_005_id as sip FROM c_pf_t_005 WHERE pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1')
			new ExcelBuilder("C:/Users/Hunny.hu/Desktop/中英文对照表.xls").eachLine([labels:true]) {
				query.eachRow("SELECT c001.* FROM c_tm_c_001 c001 LEFT JOIN c_tm_c_000 c000 ON c001.tmconobj = c000.tm_000_id where c001.sys_sip = ${company.sip} and c001.com_langval = '" + cell(1) + "' and tmconobj_code = '" + (cell(0) + '').replaceAll('\\..*$', '') + "'"){ row ->
					try {
						//println row.sys_sip + '|' + row.sys_creator + '|' + row.tmconobj + '|' + row.com_lang + '|' + row.com_langval
						def tmp = '';
						tmp += "IF NOT EXISTS(SELECT * from c_tm_c_001 where sys_sip = ${company.sip} and tmconobj = ${row.tmconobj} and sys_start = ${row.sys_start} and sys_end = ${row.sys_end} and com_lang = 'en_us'"
						tmp += " and com_langval = '"
						tmp += (cell(2) + '').replaceAll("'", "''")
						tmp += "'"
						tmp += ") THEN\n";
						tmp += "\tINSERT INTO c_tm_c_001 (sys_sip, sys_creator, sys_crtime, sys_changer, sys_chtime, sys_start, sys_end, tmconobj, com_lang, com_langval) VALUES (${company.sip}, ";
						tmp += row.sys_creator + ", "
						tmp += row.sys_crtime + ", "
						tmp += row.sys_changer + ", "
						tmp += row.sys_chtime + ", "
						tmp += row.sys_start + ", "
						tmp += row.sys_end + ", "
						tmp += row.tmconobj + ", 'en_us', '"
						tmp += (cell(2) + '').replaceAll("'", "''")
						tmp += "');";
						tmp += '\nEND IF;'
						println tmp
						file.append(tmp + '\n', 'UTF-8')
					} catch (Exception e) {
						println e.message
					}
				}
			}
			println 'END$$;';
			file.append('END$$;\n', 'UTF-8');
		} finally {
		  query.close();
		}
	}

	static main(args) {
		new GenerateDictionaryByExcel().build();
	}

}
