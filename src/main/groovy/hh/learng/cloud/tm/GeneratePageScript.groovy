package hh.learng.cloud.tm

class GeneratePageScript {

	def query
	def file

	GeneratePageScript(def query, def file) {
		this.query = query
		this.file = file
	}
	boolean wirteToFile(row) {
		if ('TM_PUBLIC_GROUP_Head' != row.block_code) {
			//println pf052.block_name + '|' + pf052.block_code;
			def s1 = "delete from c_pf_t_055 WHERE ( element_block = (select pf_052_id from c_pf_p_052 where block_code='${row.block_code}'));\r\n";
			def sip = '(select pf_005_id from c_pf_t_005 where pf_005_id >= 100 and pf_005_id <= 300 order by pf_005_id desc limit 1)';
			def s2 = "select c_syn_page_elements_byblock($sip, '${row.block_code}');\r\n";
			println s1
			println s2
			file.append(s1)
			file.append(s2)
			return true
		}
		return false
	}

	def calc(Integer pf052Id) {
		def subBlock = [];
		//		println 'parent id:' + pf052Id
		query.eachRow('select * from c_pf_p_048 where parent_block = ' + pf052Id) {row->
			//			println 'sub_block:' + row.sub_block
			def pf052 = query.firstRow('select * from c_pf_p_052 where pf_052_id = ' + row.sub_block)
			if (wirteToFile(pf052)) {
				subBlock << row.sub_block
			}
		}
		if (subBlock.size() > 0) {
			for (def id : subBlock) {
				calc(id)
			}
		}
	}

	def build() {
		try {
			query.eachRow("select * from C_PF_P_052 a where 1=1 and a.block_code ilike 'TM_%' and a.block_type = 'M'"){ row ->
				try {
					def title = '\n\n--=======================================' + row.block_name + '对应的页面元素与P表同步脚本\r\n'
					println title
					file.append(title, 'UTF-8')
					wirteToFile(row)
					calc(row.pf_052_id)
				} catch (e) {
					println e.message
					//print 'TableName ' + row.tablename + ' Exception Info:' + e.message
				}
			}
		} finally {
			query.close()
		}
	}

	static main(args) {
		def query = Gdbc.source([host:'192.168.9.205', name:'cloudAC_Dev'])
		def file = new File('d:/tm_page_sql.txt')
		new GeneratePageScript(query, file).build()
	}
}
