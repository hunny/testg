package hh.learng.cloud.tm

import groovy.sql.*
import hh.learng.apache.poi.ExcelBuilder

class GenerateDictionaryByExcel {
	
	def db = [url:'jdbc:postgresql://192.168.9.231:6543/cloudHR',
		user:'dev',
		passwd:'dev@1234',
		driver:'org.postgresql.Driver'];
	def file = new File('D:/myfile.txt')
	
	def build() {
		def query = Sql.newInstance(db.url, db.user, db.passwd, db.driver);
		try {
			println 'DO $$ \n BEGIN'
			file.append('DO $$ \nBEGIN\n', 'UTF-8')
			new ExcelBuilder("D:/中英文对照表.xls").eachLine([labels:true]) {
				query.eachRow("select c001.* from c_tm_c_001 c001 left join c_tm_c_000 c000 on c001.tmconobj = c000.tm_000_id where c001.com_langval = '" + cell(1) + "' and tmconobj_code = '" + (cell(0) + '').replaceAll('\\..*$', '') + "'"){ row ->
					try {
						//println row.sys_sip + '|' + row.sys_creator + '|' + row.tmconobj + '|' + row.com_lang + '|' + row.com_langval
						def tmp = '';
						tmp += "IF NOT EXISTS(select * from c_tm_c_001 where sys_sip = 100 and tmconobj = ${row.tmconobj} and sys_start = ${row.sys_start} and sys_end = ${row.sys_end} and com_lang = 'en_us'"
						tmp += " and com_langval = '"
						tmp += (cell(2) + '').replaceAll("'", "''")
						tmp += "'"
						tmp += ") THEN\n";
						tmp += "\tINSERT INTO c_tm_c_001 (sys_sip, sys_creator, sys_crtime, sys_changer, sys_chtime, sys_start, sys_end, tmconobj, com_lang, com_langval) VALUES (100, ";
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
