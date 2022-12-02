package com.lilan.exctool.controller;

import com.lilan.exctool.pojo.BeforeData;
import com.lilan.exctool.service.ExcService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("exc")
public class ExcController {

    @RequestMapping("start")
    public Object start() {

        return "welcome";
    }

    @RequestMapping("importExc")
    public Object importExc(MultipartFile fileUpload,String SCType,String jgName,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException, InvalidFormatException {


        ExcService excService = new ExcService();
        File file = excService.multopartFileToFile(fileUpload);
        //获取源文件名
        String fileName1 = file.getName();
        System.out.println("name"+fileName1);
        String fileTyle=fileName1.substring(fileName1.lastIndexOf("."),fileName1.length());
        System.out.println(fileTyle);
        List<BeforeData> beforeList=new ArrayList<>();
        //不同类型file 转  workbook  转list  去重
        if(".xlsx".equals(fileTyle)){
            beforeList= excService.getDatByWbXLSX(file);
        }
        if(".xls".equals(fileTyle)){
            beforeList= excService.getDatByWbXLS(file);
        }


       // System.out.println(beforeList.size()+"-----"+beforeList);
//处理数据  生成新excel
        String zipPath = excService.createNewExc(beforeList, SCType,jgName);
//压缩zip
        String zipFileDown = excService.zipDataByPath(zipPath, jgName,jgName);
        System.out.println("zipFileDown--"+zipFileDown);
        //下载
        String filePath = "";
        String fileName = "".equals(jgName)? "文件.zip":  jgName+".zip";


        ServletOutputStream out = null;
        FileInputStream in = null;

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            in = new FileInputStream(new File(zipFileDown));
            out = response.getOutputStream();
            response.reset();

            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);


            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();;
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return "welcome";
    }



    @RequestMapping("importExcSplitType")
    public Object importExcSplitType(MultipartFile fileUpload,String SCType,String jgName,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException, InvalidFormatException {


        ExcService excService = new ExcService();
        File file = excService.multopartFileToFile(fileUpload);
        //获取源文件名
        String fileName1 = file.getName();
        System.out.println("name"+fileName1);
        String fileTyle=fileName1.substring(fileName1.lastIndexOf("."),fileName1.length());
        System.out.println(fileTyle);
        List<BeforeData> beforeList=new ArrayList<>();
        //不同类型file 转  workbook  转list  去重
        if(".xlsx".equals(fileTyle)){
            beforeList= excService.getDatByWbXLSX(file);
        }
        if(".xls".equals(fileTyle)){
            beforeList= excService.getDatByWbXLS(file);
        }


        // System.out.println(beforeList.size()+"-----"+beforeList);
//处理数据  生成新excel
        String zipPath = excService.createNewExcSplitType(beforeList, SCType,jgName);
//压缩zip
        String zipFileDown = excService.zipDataByPath(zipPath, jgName,jgName);
        System.out.println("zipFileDown--"+zipFileDown);
        //下载
        String filePath = "";
        String fileName = "".equals(jgName)? "文件.zip":  jgName+".zip";


        ServletOutputStream out = null;
        FileInputStream in = null;

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            in = new FileInputStream(new File(zipFileDown));
            out = response.getOutputStream();
            response.reset();

            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);


            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();;
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        return "welcome";
    }

}
