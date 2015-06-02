package hh.learng.utils.file.usecase

import groovy.io.FileType

class FindStringInFiles {

	static main(args) {
		def dir = new File('D:/环境发布/')
		dir.eachFileRecurse(FileType.FILES){file->
			def name = file.name
			if (name =~ /\.sql$/) {
				def fileName = file.absolutePath
				new File(fileName).eachLine { line ->
					if (line.contains('c_tm_t_116') || line.contains('C_TM_T_116')) {
						println fileName
						println line
					}
				}
			}
		}
	}

}
