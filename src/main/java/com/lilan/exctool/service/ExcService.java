package com.lilan.exctool.service;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExcService {

    //file 转wb  取出data 转pojo准备处理
    public void getDatByWb(File file) throws IOException, InvalidFormatException {
      /*  XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("firstRowNum-"+firstRowNum+"..lastRowNum-"+lastRowNum);*/

        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("firstRowNum-"+firstRowNum+"/lastRowNum-"+lastRowNum);
 //线程池处理
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

        HashSet<String> bigKXSet = new HashSet<>();
        for (int i = 2; i < lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell cell = row.getCell(4);
            String bigKX2 = cell.getStringCellValue();
            if(bigKXSet.add(bigKX2)){
                System.out.println(bigKX2);
            }
        }
        System.out.println(bigKXSet);
        // threadPool.execute();


    }














    public File multopartFileToFile(MultipartFile fileUpload) throws IOException {

        //MultipartFile转File
        File file = null;
        file = File.createTempFile("prefix", "_" + fileUpload.getOriginalFilename());
        fileUpload.transferTo(file);

        return file;
    }
}
