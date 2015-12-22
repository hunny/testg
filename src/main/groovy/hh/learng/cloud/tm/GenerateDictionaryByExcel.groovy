package hh.learng.cloud.tm

import groovy.sql.*
import hh.learng.apache.poi.ExcelBuilder

class GenerateDictionaryByExcel {
	
	def file = new File('D:/myfile.txt')
	
	def build() {
		def query = Gdbc.source([host:'192.168.9.245', name:'cloudHR'])
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
	
	def buildData() {
		def query = Gdbc.source([host:'192.168.9.205', name:'cloud'])
		file.append('it.ee_id\tit.iden_type\tit.iden_number\tit.object\tit.com_langval\tit.user_name\r\n', 'UTF-8')
		query.eachRow("""
select t025.ee_id, t025.iden_type, t025.iden_number, t001.object, t001.com_langval, t008.user_name
from c_pa_t_045 t025 
left join c_om_t_001 t001 on t025.ee_id = t001.object 
left join c_pf_t_008 t008 on t008.ee_id = t001.object
order by t001.object asc, t001.com_lang asc
""") {
		def s = it.ee_id + '\t' + it.iden_type + '\t' + it.iden_number + '\t' + it.object + '\t' + it.com_langval + '\t' + it.user_name + '\r\n'
			file.append(s, 'UTF-8')
		}
	}

	static main(args) {
//		new GenerateDictionaryByExcel().build();
		new GenerateDictionaryByExcel().buildData();
	}

}
