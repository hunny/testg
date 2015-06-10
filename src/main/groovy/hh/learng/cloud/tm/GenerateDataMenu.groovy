package hh.learng.cloud.tm

class GenerateDataMenu {

	static main(args) {
		def query = Gdbc.source(['host': '192.168.9.205', 'name': 'cloudDachser_Dev'])
		try {
			println 'DO $$';
			println 'BEGIN';
			query.eachRow("select * from c_pf_p_012 where sys_zh_cn like '%我的团队信息%'"){ row ->
				try {
					def resource_code = "(SELECT resource_code from c_pf_p_012 where sys_zh_cn ='${row.sys_zh_cn}' limit 1)"
					def nextval = "nextval('c_pf_t_043_pf_043_id_seq'::regclass)"
					def now = '(SELECT extract(epoch FROM now()))'
					def sip = "(SELECT pf_005_id FROM c_pf_t_005 WHERE client_code LIKE 'sp%')"
					println "\tIF NOT EXISTS(SELECT * FROM c_pf_t_043 WHERE resource = $resource_code AND sys_sip = $sip) THEN"
					def insert = 'INSERT INTO "public"."c_pf_t_043" ("pf_043_id", "sys_sip", "sys_creator", "sys_crtime", "sys_changer", "sys_chtime", "sys_start", "sys_end", "resource")'
					println "\t\t$insert VALUES ($nextval, $sip, '10', $now, NULL, NULL, NULL, NULL, $resource_code);"
					println "\tEND IF;"
					def sipbycode = "(SELECT pf_005_id FROM c_pf_t_005 WHERE client_code in('LS','Schindler','Qoros','Compass','CHIC','Dachser','AtlasCopco','MDLZ','RL'))"
					println "\tIF NOT EXISTS(SELECT * FROM c_pf_t_043 WHERE resource = $resource_code AND sys_sip = $sipbycode) THEN"
					def sp_admin = "(SELECT pf_008_id FROM c_pf_t_008 WHERE user_name in('sp_admin'))"
					println "\t\t$insert VALUES ($nextval, $sipbycode, $sp_admin, $now, NULL, NULL, NULL, NULL, $resource_code);"
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
