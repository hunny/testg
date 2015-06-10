package hh.learng.testgroovy.jdbc.guide.sqlite

import java.text.SimpleDateFormat

class EOTCalc {

	static main(args) {
		
		def query = Sqlite.source([path:'/Users/hunnyhu/Desktop/sqlite.db'])
		def count = 0//工作总天数
		def eot = 0 //平时加班
		def weot = 0//周末加班
		def map = [:]
		(1..3).each { map["2015-01-0$it"] = true}//元旦
		(18..24).each { map["2015-02-$it"] = true}//春节
		(4..6).each { map["2015-04-0$it"] = true}//清明节
		(1..3).each { map["2015-05-0$it"] = true}//劳动节
		(20..22).each { map["2015-06-$it"] = true}//端午节
		(3..5).each { map["2015-09-0$it"] = true}//胜利日
		(26..27).each { map["2015-09-$it"] = true}//中秋节
		(1..7).each { map["2015-10-0$it"] = true}//国庆节
		query.eachRow("select * from sign where end_date < '2015-06-05 23:59:59' ") {
			def format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			def mDate = format.parse(it.end_date)
			def date = String.format('%tF', mDate)
			def week = String.format('%tA', mDate)
			def hour = String.format('%tH', mDate)
			def tmp = it.end_date + '|' + date + '|' + week + '|' + hour
			count ++
			if (('星期六' == week || '星期日' == week) && !map[date]) {
				println '==> 周末加班：|' + tmp
				weot ++
			}
			if (hour.toInteger() >= 19) {
				eot ++
				println '平时加班：|' + tmp
			}
		}
		println 'total: ' + count + ', eot:' + eot + ', week eot:' + weot
		
	}

}
