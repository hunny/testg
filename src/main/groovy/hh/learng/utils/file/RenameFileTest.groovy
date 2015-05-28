package hh.learng.utils.file

import groovy.io.FileType

class RenameFileTest {

	static main(args) {
		def dir = new File('D:/BaiduYunDownload/KK')
		dir.eachFileRecurse(FileType.FILES){file->
			def name = file.name
			println name
			def rename = 'D:/BaiduYunDownload/KK/' + name.replaceFirst('虎M猫爸', '虎妈猫爸')
			println rename
			file.renameTo(rename)
		}
	}

}
