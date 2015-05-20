package hh.learng.cloud.tm

import groovy.sql.*

/**
 * 根据type生成考勤模块的数据字典脚本
 * @author Hunny.Hu
 */
class GenerateDataDictionary {

	static main(args) {
		def db = [url:'jdbc:postgresql://192.168.9.205:6543/cloudDachser_Dev',
			user:'dev',
			passwd:'dev@1234',
			driver:'org.postgresql.Driver'];
		def query = Sql.newInstance(db.url, db.user, db.passwd, db.driver);
		try {
			println 'DO $$';
			println 'BEGIN';
			query.eachRow("select * from c_tm_c_000 where tmconobj_type = 'C_TM_C_063'"){ row ->
				try {
					def sip = "(select pf_005_id from c_pf_t_005 where pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1)";
					println "\tIF NOT EXISTS(select * from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}') THEN"
					println "\t\tINSERT INTO c_tm_c_000 (sys_sip, sys_start, sys_end, tmconobj_type, tmconobj_code) VALUES ($sip, 1388505600, 253402271999, '${row.tmconobj_type}', '${row.tmconobj_code}');";
					println "\tEND IF;"
					query.eachRow("select * from c_tm_c_001 where tmconobj = ${row.tm_000_id}") {row2->
						def tm_000_id = "(select tm_000_id from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}' order by tm_000_id asc limit 1)";
						println "\tIF NOT EXISTS(select * from c_tm_c_001 where tmconobj = ${tm_000_id} and com_lang = '${row2.com_lang}') THEN"
						println "\t\tINSERT INTO c_tm_c_001 (sys_sip, sys_start, sys_end, tmconobj, com_lang, com_langval) VALUES ($sip, 1388505600, 253402271999, $tm_000_id, '${row2.com_lang}', '${row2.com_langval}');"
						println "\tEND IF;"
					}
				} catch (Exception e) {
					//print 'TableName ' + row.tablename + ' Exception Info:' + e.message
				}
			}
			println 'END$$;'
		} finally {
		  query.close();
		}
	}

}
