package hh.learng.utils.file.usecase

import groovy.io.FileType

/**
 * 在指定的目录下，搜索指定类型文件中包含特定的字符串
 * @author Hunny.Hu
 */
class FindStringInFiles {

	static main(args) {
		println 'Searching...'
		println '--------------'
		def result = 0;
		def dir = new File('D:/环境发布/')
		dir.eachFileRecurse(FileType.FILES){file->
			def name = file.name
			if (name =~ /\.sql$/) {
				def lineNumber = 0
				file.eachLine { line ->
					lineNumber ++
					if (line.contains('c_tm_t_116') || line.contains('C_TM_T_116')) {
						result ++
						println "=============> File[${file.absolutePath}] found at line number :${lineNumber}"
						println line
					}
				}
			}
		}
		println '--------------'
		println "Over. Result:${result}"
	}

}
