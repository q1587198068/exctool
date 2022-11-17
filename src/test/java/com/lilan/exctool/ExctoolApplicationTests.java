package com.lilan.exctool;

import com.lilan.exctool.utils.ZipUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExctoolApplicationTests {

	@Test
	void contextLoads() {


	}


	@Test
	public void t2343() throws FileNotFoundException {

		ZipUtils zipUtils = new ZipUtils();
		File file = new File("C:\\workspace\\exctool\\target\\classes\\downFile\\2022-11-17\\1668647084311");

		File file1  = new File("C:\\zzzzzzzz"+File.separator+"11.zip");

		if(!file1.exists()){
			//先得到文件的上级目录，并创建上级目录，在创建文件
			file1.getParentFile().mkdir();
			try {
				//创建文件
				file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fileOutputStream = new FileOutputStream(file1);
		zipUtils.toZip("C:\\workspace\\exctool\\target\\classes\\downFile\\2022-11-17\\1668647084311",
				fileOutputStream,true);


		}




}
