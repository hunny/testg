package hh.learng.cloud.tm

class TmXmModeAmPmScript {
	
	def file = new File('D:/myfile.txt')

	static main(args) {
		def config = ['host': '192.168.9.234', 'name': 'cloudmdlz']
		//Query Tmconobj
		generateTm000AndTm001(config)
		println '--注意，不能合并这两个脚本'
		//
		generateTmT107(config)
	}
	
	static generateTm000AndTm001(db = [:]) {
		def query = Gdbc.source(db)
		try {
			println 'DO $$';
			println 'BEGIN';
			query.eachRow("select * from c_tm_c_000 where tmconobj_type = 'C_TM_T_107' "){ row ->
				try {
					def sip = "(select pf_005_id from c_pf_t_005 where pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1)"
					println "\tIF NOT EXISTS(select * from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}') THEN"
					println "\t\tINSERT INTO c_tm_c_000 (sys_sip, sys_start, sys_end, tmconobj_type, tmconobj_code) VALUES ($sip, 1388505600, 253402271999, '${row.tmconobj_type}', '${row.tmconobj_code}');"
					println "\tEND IF;"
					query.eachRow("select * from c_tm_c_001 where tmconobj = ${row.tm_000_id}") {row2->
						def tm_000_id = "(select tm_000_id from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}' order by tm_000_id asc limit 1)"
						println "\tIF NOT EXISTS(select * from c_tm_c_001 where tmconobj = ${tm_000_id} and com_lang = '${row2.com_lang}') THEN"
						println "\t\tINSERT INTO c_tm_c_001 (sys_sip, sys_start, sys_end, tmconobj, com_lang, com_langval) VALUES ($sip, 1388505600, 253402271999, $tm_000_id, '${row2.com_lang}', '${row2.com_langval}');"
						println "\tEND IF;"
					}
				} catch (e) {
				}
			}
			println 'END$$;'
		} finally {
		  query.close();
		}
	}
	
	static generateTmT107(db = [:]) {
		def query = Gdbc.source(db)
		println 'DO $$';
		println 'BEGIN';
		try {
			def code = 'D001'//工作日的code
			def sip = "(select pf_005_id from c_pf_t_005 where pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1)"
			def working_schedule = "(select tm_000_id from c_tm_c_000 where tmconobj_type = 'C_TM_C_008' and tmconobj_code = '$code' order by tm_000_id limit 1)"
			def insert = "INSERT INTO C_TM_T_107(sys_sip,sys_start,sys_end,tmconobj,working_schedule,ti_start_time,ti_end_time,work_hours,pay_hours,work_days,pay_days,ess_app_quickly)"
			def map = [
				'TP001': ['s': '9:00', 'e': '14:00'],
				'TP002': ['s': '14:00', 'e': '18:00']
			]
			query.eachRow("select * from c_tm_c_000 where tmconobj_type = 'C_TM_T_107' ") {row->
				def tmconobj = "(select tm_000_id from c_tm_c_000 where tmconobj_type = '${row.tmconobj_type}' and tmconobj_code = '${row.tmconobj_code}' order by tm_000_id desc limit 1)"
				def ti_start_time = map[row.tmconobj_code]['s']
				def ti_end_time = map[row.tmconobj_code]['e']
				def values = "VALUES ($sip, 1388505600, 253402271999, $tmconobj, $working_schedule, '${ti_start_time}', '${ti_end_time}', 4, 4, 0.5, 0.5, true)"
				println "\tIF NOT EXISTS(select * from C_TM_T_107 where sys_sip = $sip and tmconobj = $tmconobj ) THEN"
				println "\t\t$insert $values"
				println "\tEND IF;"
			}
		} finally {
			query.close()
		}
		println 'END$$;'
	}

}
