package hh.learng.testgroovy

import groovy.io.FileType

class FileTest {

	/*Path file = Paths.get("D:/tmp/OK.txt");
	 Charset charset = Charset.forName("UTF-8");
	 try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		 String line;
		 while ((line = reader.readLine()) != null) {
			 System.out.println(line);
		 }
	 
	 } catch (IOException e) {
		 e.printStackTrace();
	 }*/
	static main(args) {
		//File Reader
		new File('D:/tm_page_sql.txt').eachLine('UTF-8') { println it }
		new File('D:/tm_page_sql.txt').withReader('UTF-8') { reader ->
			reader.eachLine { println it }
		}
		//File Rename
		def dir = new File('D:/BaiduYunDownload/Objective-C部分');
		dir.eachFileRecurse(FileType.FILES){file->
			def name = file.name;
			//println name;
			def rename = 'D:/BaiduYunDownload/Objective-C部分/' + name.replaceFirst('-陈为', '');
			println rename
			file.renameTo(rename);
		}
	}
}
