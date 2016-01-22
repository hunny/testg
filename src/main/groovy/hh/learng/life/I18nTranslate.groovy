package hh.learng.life

import groovyx.net.http.*

import java.util.regex.Matcher
import java.util.regex.Pattern

class I18nTranslate {

	static main(args) {
//		def http = new HTTPBuilder('https://google.com')
//		def html = http.get(path : '/search', query : [q:'waffles'])
//		collectData()
		splitData()
	}
	
	static collectData() {
		def query = GMdbc.source([host:'192.168.9.243', name:'cubi-life'])
		def file = new File('C:/Users/Hunny.hu/Desktop/goods_class.src')
		query.eachRow("select gc_id, gc_name from shopnc_goods_class order by gc_id desc limit 2000") {
			def tmp = '[' + it.gc_id + ' ' + it.gc_name + ']\r\n'
			print tmp
			file.append(tmp);
		}
	}
	
	static splitData() {
		def file = new File('C:/Users/Hunny.hu/Desktop/goods_class.src')
		def sql = new File('C:/Users/Hunny.hu/Desktop/goods_class.sql')
		Pattern u = Pattern.compile("(\\d+)")
		file.eachLine { line ->
			line = line.replaceFirst('^\\[', '').replaceFirst('\\]$', '').replaceFirst('\\s+', '')
			Matcher um = u.matcher(line);
			while (um.find()) {
				def id = um.group(1)
				def name = line.replaceFirst(id, '')
				name = name.replaceAll("'", "''")
				def s = "INSERT INTO shopnc_i18n (table_name, column_name, locale, ref_id, content, last_updated) VALUES ('goods_class', 'gc_name', 'en-US', '${id}', '${name}', 1452591560);\r\n"
				println s
				sql.append(s)
			}
		}
	}

}
