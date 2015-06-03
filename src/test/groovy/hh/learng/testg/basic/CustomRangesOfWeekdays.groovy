package hh.learng.testg.basic

/**
 * range可以使用任何类型, 只要这个类型满足以下两个条件:
 *  该类型实现next和previous方法,也就是说,重写++和--操作符;
 *  该类型实现java.lang.Comparable接口;也就是说实现compareTo方法, 实际上是重写<=>操作符。
 * @author hunnyhu
 *
 */
class CustomRangesOfWeekdays implements Comparable {
	
	static final DAYS = [
		'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'
	]
	private int index = 0
	CustomRangesOfWeekdays(String day) {
		index = DAYS.indexOf(day)
	}
	
	CustomRangesOfWeekdays next() {
		return new CustomRangesOfWeekdays(DAYS[(index + 1) % DAYS.size()])
	}
	
	CustomRangesOfWeekdays previous() {
		return new CustomRangesOfWeekdays(DAYS[index - 1])
	}
	
	int compareTo(Object other) {
		return this.index <=> other.index
	}
	
	String toString() {
		return DAYS[index]
	}

}
