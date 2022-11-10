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
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("exc")
public class ExcAction {

    @RequestMapping("start")
    public Object start() {

        return "welcome";
    }

    @RequestMapping("importExc")
    public Object importExc(MultipartFile fileUpload,String SCType,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException, InvalidFormatException {


        ExcService excService = new ExcService();
        File file = excService.multopartFileToFile(fileUpload);

        //file 转  workbook  转list  去重
        List<BeforeData> beforeList = excService.getDatByWb(file);
        System.out.println(beforeList.size()+"-----"+beforeList);
//处理数据  生成新excel
        excService.createNewExc(beforeList,SCType);
//压缩zip


        //下载

        String filePath = "";
        String fileName = "";


        ServletOutputStream out = null;
        FileInputStream in = null;

        /*try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            in = new FileInputStream(file);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            out = response.getOutputStream();

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
        }*/
        return "welcome";
    }

}
