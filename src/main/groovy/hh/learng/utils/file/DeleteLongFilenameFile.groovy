package hh.learng.utils.file

import groovy.io.FileType

class DeleteLongFilenameFile {

	static main(args) {
		mydelete('D:/code/angular-app/client/node_modules/');
		
	}
	
	static mydelete(filename) {
		def dir = new File(filename)
		try {
			dir.eachFileRecurse(){file->
				try {
					if (file.directory) {
						println '[+][-]DIRECTORY[-]' + file.absolutePath
						file.deleteDir()
					} else {
						println '[+][-]FILE[-]' + file.absolutePath
						file.delete()
					}
				} catch(e) {
					println '[+][-]ERROR[-]' + e.message
				}
			}
		} catch (e) {
			println '[+][-]ERROR[-]' + e.message 
			mydelete(filename)
		}
	}

}
