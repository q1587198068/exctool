package com.lilan.exctool.service;

import com.lilan.exctool.pojo.BeforeData;
import com.lilan.exctool.pojo.ExcMessage;
import com.lilan.exctool.pojo.Message2;
import com.lilan.exctool.utils.MyFileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExcService {

    public String zipDataByPath(String path, String zipName,String JG) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("path" + path);
     /*   File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())//如果是文件
            {
                System.out.println("文件名" + files[i].getName()); //文件名
                System.out.println("完整路径" + files[i]);  //完整路径
            }
        }*/
        String format = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long l = System.currentTimeMillis();
        String jarPath = getJarPath();
        String zipPath = jarPath + File.separator + "zipFiles" + File.separator + format;
        String endZipName = zipName + l + ".zip";
        MyFileUtils fileUtils = new MyFileUtils();
        fileUtils.compressToZip(path, zipPath, endZipName,JG);

        return zipPath +File.separator+ endZipName;
    }


    public String createNewExc(List<BeforeData> beforeList, String SCType,String JG) {
        HashSet<String> disSet = new HashSet();
        for (int i = 0; i < beforeList.size(); i++) {
            BeforeData beforeData = beforeList.get(i);
            String partOrder = beforeData.getPartOrder();
            disSet.add(partOrder);
        }

        //线程池处理
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        long savaPathTime = System.currentTimeMillis();
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {
            String jarPath = getJarPath();
            System.out.println("jarPath--" + jarPath);
            String savePath = jarPath + File.separator + "downFile" + File.separator + format + File.separator + savaPathTime;

            //获取所有的去重的分单原则
            List<String> partOrderList = new ArrayList(disSet);
            //要生成的文件数
            for (int i = 0; i < partOrderList.size(); i++) {
                String partOrder = partOrderList.get(i);
                //保存文件地址+机构  解压缩方便
                runn runn = new runn(beforeList, partOrder, SCType, jarPath, savePath+File.separator+JG);
                //提交任务到线程池
                threadPool.execute(runn);
            }
            //等待任务执行完毕，关闭线程池
            threadPool.shutdown();
            while (true) {
                //判断线程池线程执行完毕
                if (threadPool.isTerminated()) {
                    break;
                }
                Thread.sleep(1000);
            }
            return savePath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class runn implements Runnable {
        List<BeforeData> beforeList;
        String partOrder;
        String SCType;
        String jarPath;
        String savePath;


        @Override
        public void run() {
            //生成的excel的所有数据
            ExcMessage excMessage = new ExcMessage();
            int SCA = 0;
            int SCB = 0;
            int SCC = 0;
            for (int i = 0; i < beforeList.size(); i++) {
                //每一条原始数据
                BeforeData beforeData = beforeList.get(i);
                if (partOrder.equals(beforeData.getPartOrder())) {
                    //-----是需要的  beforeData
                    //收件人信息
                    String clientName = beforeData.getClientName();
                    if (!"".equals(clientName)) {
                        excMessage.setClientName(clientName);
                    }
                    //订单号
                    String orderNum = beforeData.getOrderNum();
                    if (!"".equals(orderNum)) {
                        excMessage.setOrderNum(orderNum);
                    }
                    //ERP
                    String orderNumERP = beforeData.getOrderNumERP();
                    if (!"".equals(orderNumERP)) {
                        excMessage.setOrderNumERP(orderNumERP);
                    }
                    //SC类型
                    String type = beforeData.getType();
                    if ("A".equals(type) || "a".equals(type)) {
                        SCA++;
                    }
                    if ("B".equals(type) || "b".equals(type)) {
                        SCB++;
                    }
                    if ("C".equals(type) || "c".equals(type)) {
                        SCC++;
                    }
                    //id
                    String id = beforeData.getId();
                    Message2 message2 = new Message2();
                    message2.setId(id);
                    message2.setType(type);
                    excMessage.getMessageList().add(message2);
                }
            }
            //分单原则
            excMessage.setPartOrder(partOrder);
            excMessage.setScA(SCA);
            excMessage.setScB(SCB);
            excMessage.setScC(SCC);
            excMessage.setScAll(SCA + SCB + SCC);

            // System.out.println("excMessage--"+partOrder+"--"+excMessage);
            //数据整合over，生成excel
            //下载模板
            String tempPath = jarPath + File.separator + "template_down.xlsx";
            File file = new File(tempPath);
            //处理下载模板数据
            XSSFWorkbook wb = doWBData(file, excMessage, SCType);
            //存储生成文件
            File file1 = new File(savePath);
            if (!file1.exists()) {
                //不存在，创建目录
                file1.mkdirs();
            }

            String filename = partOrder + "-";
            if (0 != excMessage.getScA()) {
                if (!filename.endsWith("-")) {
                    filename += "+";
                }
                filename += "A型" + excMessage.getScA() + "个";
            }
            if (0 != excMessage.getScB()) {
                if (!filename.endsWith("-")) {
                    filename += "+";
                }
                filename += "B型" + excMessage.getScB() + "个";
            }
            if (0 != excMessage.getScC()) {
                if (!filename.endsWith("-")) {
                    filename += "+";
                }
                filename += "C型" + excMessage.getScC() + "个";
            }

            //  wb.文件保存
            File savefile = new File(savePath, filename + ".xlsx");
            if (!savefile.exists()) {
                try {
                    savefile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            OutputStream os = null;
            try {
                os = new FileOutputStream(savefile);
                wb.write(os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        public runn(List<BeforeData> beforeList, String partOrder, String SCType, String jarPath,
                    String savePath) {
            this.beforeList = beforeList;
            this.partOrder = partOrder;
            this.SCType = SCType;
            this.jarPath = jarPath;
            this.savePath = savePath;
        }
    }


    public XSSFWorkbook doWBData(File file, ExcMessage excMessage, String SCType) {
        XSSFWorkbook tempwb = new XSSFWorkbook();
        try {
            //获取wb  填入数据
            tempwb = new XSSFWorkbook(file);
            XSSFSheet sheetAt = tempwb.getSheetAt(0);
            XSSFRow row1 = sheetAt.getRow(1);
            //订单编号
            XSSFCell ordercell = row1.getCell(1);
            ordercell.setCellValue(excMessage.getOrderNum());
            //分单原则（变电站/馈线等）
            XSSFCell partordercell = row1.getCell(4);
            partordercell.setCellValue(excMessage.getPartOrder());

            XSSFRow row2 = sheetAt.getRow(2);
            XSSFCell r2c1 = row2.getCell(1);
            r2c1.setCellValue(excMessage.getOrderNumERP());

            XSSFRow row3 = sheetAt.getRow(3);
            XSSFCell r3c1 = row3.getCell(1);
            r3c1.setCellValue(excMessage.getClientName());

            XSSFRow row4 = sheetAt.getRow(4);
            XSSFCell r4c1 = row4.getCell(1);
            r4c1.setCellValue(excMessage.getScAll());

            XSSFRow row5 = sheetAt.getRow(5);
            row5.getCell(0).setCellValue(SCType + "A数量");
            XSSFCell r5c1 = row5.getCell(1);
            r5c1.setCellValue(excMessage.getScA());

            XSSFRow row6 = sheetAt.getRow(6);
            row6.getCell(0).setCellValue(SCType + "B数量");
            XSSFCell r6c1 = row6.getCell(1);
            r6c1.setCellValue(excMessage.getScB());

            XSSFRow row7 = sheetAt.getRow(7);
            row7.getCell(0).setCellValue(SCType + "C数量");
            XSSFCell r7c1 = row7.getCell(1);
            r7c1.setCellValue(excMessage.getScC());

            ArrayList<Message2> messageList = excMessage.getMessageList();

            for (int i = 0; i < messageList.size(); i++) {
                Message2 message2 = messageList.get(i);
                XSSFRow row = sheetAt.getRow(9 + i);
                XSSFCell cell0 = row.getCell(0);
                cell0.setCellValue(i + 1);

                XSSFCell cell1 = row.getCell(1);
                cell1.setCellValue(message2.getType());

                XSSFCell cell2 = row.getCell(2);
                cell2.setCellValue(message2.getId());

                XSSFCell cell3 = row.getCell(3);
                cell3.setCellValue(1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempwb;
    }

    //file xlsx 转wb  取出data 转pojo准备处理
    public List<BeforeData> getDatByWbXLSX(File file) throws IOException, InvalidFormatException {
      /*  XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("firstRowNum-"+firstRowNum+"..lastRowNum-"+lastRowNum);*/

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("firstRowNum-" + firstRowNum + "/lastRowNum-" + lastRowNum);

        List<BeforeData> resultList = new ArrayList<>();
        HashSet disSet = new HashSet();
        ArrayList isdisList = new ArrayList();
        //遍历原始表数据
        for (int i = 2; i <= lastRowNum; i++) {
            BeforeData beforeData = new BeforeData();
            XSSFRow row = sheet.getRow(i);
            //分单原则  大馈线
            XSSFCell bigKXcell = row.getCell(4);
            String bigKX = "";
            if (null != bigKXcell) {
                bigKXcell.setCellType(CellType.STRING);
                bigKX = bigKXcell.getStringCellValue();
            }
            //实物id
            XSSFCell idcell = row.getCell(8);
            String id = "";
            if (null != idcell) {
                idcell.setCellType(CellType.STRING);
                id = idcell.getStringCellValue();
            }
            //类型
            XSSFCell typecell = row.getCell(12);
            String type = "";
            if (null != typecell) {
                typecell.setCellType(CellType.STRING);
                type = typecell.getStringCellValue();
            }
            //收货地址
            XSSFCell clientNamecell = row.getCell(13);
            String clientName = "";
            if (null != clientNamecell) {
                clientNamecell.setCellType(CellType.STRING);
                clientName = clientNamecell.getStringCellValue();
            }
            //订单编号
            XSSFCell ordercell = row.getCell(14);
            String order = "";
            if (null != ordercell) {
                ordercell.setCellType(CellType.STRING);
                order = ordercell.getStringCellValue();
            }
            //ERP编号
            XSSFCell ERPcell = row.getCell(15);
            String erp = "";
            if (null != ERPcell) {
                ERPcell.setCellType(CellType.STRING);
                erp = ERPcell.getStringCellValue();
            }
            //设置属性
            beforeData.setClientName(clientName);
            beforeData.setId(id);
            beforeData.setOrderNum(order);
            beforeData.setOrderNumERP(erp);
            beforeData.setType(type);
            beforeData.setPartOrder(bigKX);
            //System.out.println(beforeData);
            if (disSet.add(id)) {
                resultList.add(beforeData);
            } else {
                isdisList.add(id);
            }

        }
        System.out.println("源数据重复id：" + isdisList);

        return resultList;
    }

    //file xls 转wb  取出data 转pojo准备处理
    public List<BeforeData> getDatByWbXLS(File file) throws IOException, InvalidFormatException {
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("firstRowNum-" + firstRowNum + "/lastRowNum-" + lastRowNum);

        List<BeforeData> resultList = new ArrayList<>();
        HashSet disSet = new HashSet();
        ArrayList isdisList = new ArrayList();
        //遍历原始表数据
        for (int i = 2; i <= lastRowNum; i++) {
            BeforeData beforeData = new BeforeData();
            HSSFRow row = sheet.getRow(i);
            //分单原则  大馈线
            HSSFCell bigKXcell = row.getCell(4);
            String bigKX = "";
            if (null != bigKXcell) {
                bigKXcell.setCellType(CellType.STRING);
                bigKX = bigKXcell.getStringCellValue();
            }
            //实物id
            HSSFCell idcell = row.getCell(8);
            String id = "";
            if (null != idcell) {
                idcell.setCellType(CellType.STRING);
                id = idcell.getStringCellValue();
            }
            //类型
            HSSFCell typecell = row.getCell(12);
            String type = "";
            if (null != typecell) {
                typecell.setCellType(CellType.STRING);
                type = typecell.getStringCellValue();
            }
            //收货地址
            HSSFCell clientNamecell = row.getCell(13);
            String clientName = "";
            if (null != clientNamecell) {
                clientNamecell.setCellType(CellType.STRING);
                clientName = clientNamecell.getStringCellValue();
            }
            //订单编号
            HSSFCell ordercell = row.getCell(14);
            String order = "";
            if (null != ordercell) {
                ordercell.setCellType(CellType.STRING);
                order = ordercell.getStringCellValue();
            }
            //ERP编号
            HSSFCell ERPcell = row.getCell(15);
            String erp = "";
            if (null != ERPcell) {
                ERPcell.setCellType(CellType.STRING);
                erp = ERPcell.getStringCellValue();
            }
            //设置属性
            beforeData.setClientName(clientName);
            beforeData.setId(id);
            beforeData.setOrderNum(order);
            beforeData.setOrderNumERP(erp);
            beforeData.setType(type);
            beforeData.setPartOrder(bigKX);
            //System.out.println(beforeData);
            if (disSet.add(id)) {
                resultList.add(beforeData);
            } else {
                isdisList.add(id);
            }

        }
        System.out.println("源数据重复id：" + isdisList);

        return resultList;
    }



    public File multopartFileToFile(MultipartFile fileUpload) throws IOException {

        //MultipartFile转File
        File file = null;
        file = File.createTempFile("prefix", "_" + fileUpload.getOriginalFilename());
        fileUpload.transferTo(file);

        return file;
    }


    public String getJarPath() throws FileNotFoundException, UnsupportedEncodingException {
        //第一种
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");
        /*System.out.println(path.getAbsolutePath());
        //第二种
        System.out.println(System.getProperty("user.dir"));
        //第三种
        String path1 = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(URLDecoder.decode(path1, "utf-8"));
        //第四种
        String path2 = ResourceUtils.getURL("classpath:").getPath();
        System.out.println(path2);
        //第五种
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        System.out.println(jarF.getParentFile().toString());*/

        return path.getAbsolutePath();
    }


    //获取一个文件夹下所有的文件
    private static void getFile(String path) {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile())//如果是文件
            {
                System.out.println(array[i].getName()); //文件名
                System.out.println(array[i]);  //完整路径
            } else if (array[i].isDirectory())//如果是文件夹
            {
                System.out.println(array[i].getName());
                getFile(array[i].getPath());
            }
        }
    }


}
