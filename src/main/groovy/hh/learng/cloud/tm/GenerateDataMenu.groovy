package hh.learng.cloud.tm

import groovy.sql.*

class GenerateDataMenu {

	static main(args) {
		def db = [url:'jdbc:postgresql://192.168.9.205:6543/cloudDachser_Dev',
			user:'dev',
			passwd:'dev@1234',
			driver:'org.postgresql.Driver'];
		def query = Sql.newInstance(db.url, db.user, db.passwd, db.driver);
		try {
			println 'DO $$';
			println 'BEGIN';
			query.eachRow("select * from c_pf_p_012 where sys_zh_cn like '%我的团队信息%'"){ row ->
				try {
//					def sip = "(select pf_005_id from c_pf_t_005 where pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1)";
//					println "\tIF NOT EXISTS(select * from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}') THEN"
//					println "\t\tINSERT INTO c_tm_c_000 (sys_sip, sys_start, sys_end, tmconobj_type, tmconobj_code) VALUES ($sip, 1388505600, 253402271999, '${row.tmconobj_type}', '${row.tmconobj_code}');";
//					println "\tEND IF;"
//					query.eachRow("select * from c_tm_c_001 where tmconobj = ${row.tm_000_id}") {row2->
//						def tm_000_id = "(select tm_000_id from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}' order by tm_000_id asc limit 1)";
//						println "\tIF NOT EXISTS(select * from c_tm_c_001 where tmconobj = ${tm_000_id} and com_lang = '${row2.com_lang}') THEN"
//						println "\t\tINSERT INTO c_tm_c_001 (sys_sip, sys_start, sys_end, tmconobj, com_lang, com_langval) VALUES ($sip, 1388505600, 253402271999, $tm_000_id, '${row2.com_lang}', '${row2.com_langval}');"
//						println "\tEND IF;"
//					}
					def nextval = "nextval('c_pf_t_043_pf_043_id_seq'::regclass)"
					def sip = "(SELECT pf_005_id FROM c_pf_t_005 WHERE client_code LIKE 'sp%')"
					println "\tIF NOT EXISTS(SELECT * FROM c_pf_t_043 WHERE resource =(SELECT resource_code from c_pf_p_012 where sys_zh_cn ='${row.sys_zh_cn}' LIMIT 1) AND sys_sip = $sip) THEN"
					def insert = 'INSERT INTO "public"."c_pf_t_043" ("pf_043_id", "sys_sip", "sys_creator", "sys_crtime", "sys_changer", "sys_chtime", "sys_start", "sys_end", "resource")'
					println "\t\t$insert VALUES ($nextval, $sip, '10', (SELECT extract(epoch FROM now())), NULL, NULL, NULL, NULL, (SELECT resource_code FROM c_pf_p_012 WHERE sys_zh_cn = '${row.sys_zh_cn}' limit 1));"
					println "\tEND IF;"
					def sipbycode = "(SELECT pf_005_id FROM c_pf_t_005 WHERE client_code in('LS','Schindler','Qoros','Compass','CHIC','Dachser','AtlasCopco','MDLZ','RL'))"
					println "\tIF NOT EXISTS(SELECT * FROM c_pf_t_043 WHERE resource =(SELECT resource_code from c_pf_p_012 where sys_zh_cn ='${row.sys_zh_cn}' LIMIT 1) AND sys_sip = $sipbycode) THEN"
					def insert2 = 'INSERT INTO "public"."c_pf_t_043" ("pf_043_id", "sys_sip", "sys_creator", "sys_crtime", "sys_changer", "sys_chtime", "sys_start", "sys_end", "resource")'
					println "\t\t$insert2 VALUES ($nextval, $sipbycode,(SELECT pf_008_id FROM c_pf_t_008 WHERE user_name in('sp_admin')), (SELECT extract(epoch FROM now())), NULL, NULL, NULL, NULL, (SELECT resource_code FROM c_pf_p_012 WHERE sys_zh_cn = '${row.sys_zh_cn}' limit 1));"
					println "\tEND IF;"

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
