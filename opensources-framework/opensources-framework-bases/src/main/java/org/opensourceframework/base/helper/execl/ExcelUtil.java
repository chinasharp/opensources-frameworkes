package org.opensourceframework.base.helper.execl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Excel导出工具类
 *
 * @author opensourceframework dever
 */
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";
    private static final int DEFAULT_COLOUMN_WIDTH = 17;
    /**
     * 导出Excel 97(.xls)格式 ，少量数据
     * @param title 标题行
     * @param headMap 属性-列名
     * @param jsonArray 数据集
     * @param datePattern 日期格式，null则用默认日期格式
     * @param colWidth 列宽 默认 至少17个字节
     * @param out 输出流
     */
    public static void exportExcel(String title,Map<String, String> headMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
        if(datePattern==null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        try (
                // 声明一个工作薄
                HSSFWorkbook workbook = new HSSFWorkbook()){
                workbook.createInformationProperties();
                workbook.getDocumentSummaryInformation().setCompany("*****公司");
                SummaryInformation si = workbook.getSummaryInformation();
                //填加xls文件作者信息
                si.setAuthor("JACK");
                //填加xls文件创建程序信息
                si.setApplicationName("导出程序");
                //填加xls文件最后保存者信息
                si.setLastAuthor("最后保存者信息");
                //填加xls文件作者信息
                si.setComments("JACK is a programmer!");
                //填加xls文件标题信息
                si.setTitle("POI导出Excel");
                //填加文件主题信息
                si.setSubject("POI导出Excel");
                si.setCreateDateTime(new Date());
                //表头样式
                HSSFCellStyle titleStyle = workbook.createCellStyle();
                titleStyle.setAlignment(HorizontalAlignment.CENTER);
                HSSFFont titleFont = workbook.createFont();
                titleFont.setFontHeightInPoints((short) 20);
                titleFont.setBold(true);
                titleStyle.setFont(titleFont);
                // 列头样式
                HSSFCellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                HSSFFont headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                // 单元格样式
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                HSSFFont cellFont = workbook.createFont();
                cellFont.setBold(true);
                cellStyle.setFont(cellFont);
                // 生成一个(带标题)表格
                HSSFSheet sheet = workbook.createSheet();
                // 声明一个画图的顶级管理器
                HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                // 定义注释的大小和位置,详见文档
                HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                        0, 0, 0, (short) 4, 2, (short) 6, 5));
                // 设置注释内容
                comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
                // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
                comment.setAuthor("JACK");
                //设置列宽 至少字节数
                int minBytes = Math.max(colWidth, DEFAULT_COLOUMN_WIDTH);
                int[] arrColWidth = new int[headMap.size()];
                // 产生表格标题行,以及设置列宽
                String[] properties = new String[headMap.size()];
                String[] headers = new String[headMap.size()];
                int ii = 0;
                for (String fieldName : headMap.keySet()) {
                    properties[ii] = fieldName;
                    headers[ii] = fieldName;

                    int bytes = fieldName.getBytes().length;
                    arrColWidth[ii] = Math.max(bytes, minBytes);
                    sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
                    ii++;
                }
                // 遍历集合数据，产生数据行
                int rowIndex = 0;
                for (Object obj : jsonArray) {
                    if(rowIndex == 65535 || rowIndex == 0){
                        //如果数据超过了，则在第二页显示
                        if ( rowIndex != 0 ) {
                            sheet = workbook.createSheet();
                        }
                        //表头 rowIndex=0
                        HSSFRow titleRow = sheet.createRow(0);
                        titleRow.createCell(0).setCellValue(title);
                        titleRow.getCell(0).setCellStyle(titleStyle);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
                       //列头 rowIndex =1
                        HSSFRow headerRow = sheet.createRow(1);
                        for(int i=0;i<headers.length;i++)
                        {
                            headerRow.createCell(i).setCellValue(headers[i]);
                            headerRow.getCell(i).setCellStyle(headerStyle);

                        }
                        /**
                         *   数据内容从 rowIndex=2开始
                         */
                        rowIndex = 2;
                    }
                    JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                    HSSFRow dataRow = sheet.createRow(rowIndex);
                    for (int i = 0; i < properties.length; i++)
                    {
                        HSSFCell newCell = dataRow.createCell(i);
                        Object o =  jo.get(properties[i]);
                        String cellValue;
                        if(o==null) {
                            cellValue = "";
                        } else if(o instanceof Date) {
                            cellValue = new SimpleDateFormat(datePattern).format(o);
                        } else {
                            cellValue = o.toString();
                        }

                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(cellStyle);
                    }
                    rowIndex++;
                }
                workbook.write(out);
        }catch (Exception e){
            log.error("导出Excel异常",e);
        }
    }
    /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     * @param title 标题行
     * @param headMap 属性-列头
     * @param jsonArray 数据集
     * @param datePattern 日期格式，传null值则默认 年月日
     * @param colWidth 列宽 默认 至少17个字节
     * @param out 输出流
     */
    public static void exportExcelX(String title,Map<String, String> headMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
        if (headMap.isEmpty()) {
            return;
        }
        if(datePattern==null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        // 声明一个工作薄
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000)){
            workbook.setCompressTempFiles(true);
            //表头样式
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short) 20);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            // 列头样式
            CellStyle headerStyle = setHeatherCellStyle(workbook);
            // 单元格样式
            CellStyle cellStyle = setCellStyle(workbook);

            // 生成一个(带标题)表格
            SXSSFSheet sheet = workbook.createSheet();
            //设置列宽 少字节数
            int minBytes = Math.max(colWidth, DEFAULT_COLOUMN_WIDTH);
            int[] arrColWidth = new int[headMap.size()];
            // 产生表格标题行,以及设置列宽
            String[] properties = new String[headMap.size()];
            String[] headers = new String[headMap.size()];
            int index = 0;
//            for (String fieldName : headMap.keySet()) {
//                properties[index] = fieldName;
//                headers[index] = headMap.get(fieldName);
//
//                int bytes = headMap.get(fieldName).getBytes().length;
//                arrColWidth[index] = Math.max(bytes, minBytes);
//                sheet.setColumnWidth(index, arrColWidth[index] * 256);
//                index++;
//            }
            for(Map.Entry<String, String> entry : headMap.entrySet()){
                properties[index] = entry.getKey();
                headers[index] = entry.getValue();

                int bytes = entry.getValue().getBytes().length;
                arrColWidth[index] = Math.max(bytes, minBytes);
                sheet.setColumnWidth(index, arrColWidth[index] * 256);
                index++;
            }

            // 遍历集合数据，产生数据行
            int rowIndex = 0;
            for (Object obj : jsonArray) {
                if(rowIndex == 65535 || rowIndex == 0){
                    if ( rowIndex != 0 ) {
                        sheet = workbook.createSheet();
                        index = 0;
//                        for (String fieldName : headMap.keySet()) {
//                            properties[index] = fieldName;
//                            headers[index] = headMap.get(fieldName);
//
//                            int bytes = fieldName.getBytes().length;
//                            arrColWidth[index] = Math.max(bytes, minBytes);
//                            sheet.setColumnWidth(index, arrColWidth[index] * 256);
//                            index++;
//                        }

                        for(Map.Entry<String , String> entry : headMap.entrySet()){
                            properties[index] = entry.getKey();
                            headers[index] = entry.getValue();

                            int bytes = entry.getKey().getBytes().length;
                            arrColWidth[index] = Math.max(bytes, minBytes);
                            sheet.setColumnWidth(index, arrColWidth[index] * 256);
                            index++;
                        }

                    }
                    //如果数据超过了，则在第二页显示
                    //表头 rowIndex=0
                    SXSSFRow titleRow = sheet.createRow(0);
                    titleRow.createCell(0).setCellValue(title);
                    titleRow.getCell(0).setCellStyle(titleStyle);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
                    //列头 rowIndex =1
                    SXSSFRow headerRow = sheet.createRow(1);
                    for(int i=0;i<headers.length;i++)
                    {
                        headerRow.createCell(i).setCellValue(headers[i]);
                        headerRow.getCell(i).setCellStyle(headerStyle);

                    }
                    //数据内容从 rowIndex=2开始
                    rowIndex = 2;
                }

                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                SXSSFRow dataRow = sheet.createRow(rowIndex);
                for (int i = 0; i < properties.length; i++)
                {
                    SXSSFCell newCell = dataRow.createCell(i);

                    Object o =  jo.get(properties[i]);
                    String cellValue;
                    if(o==null) {
                        cellValue = "";
                    } else if(o instanceof Date) {
                        cellValue = new SimpleDateFormat(datePattern).format(o);
                    } else if(o instanceof Float || o instanceof Double) {
                        cellValue= new BigDecimal(o.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                    } else {
                        cellValue = o.toString();
                    }

                    newCell.setCellValue(cellValue);
                    newCell.setCellStyle(cellStyle);
                }
                rowIndex++;
            }
            workbook.write(out);
            workbook.dispose();
        }catch (Exception e){
            log.error("导出Excel异常",e);
        }
    }
    //Web 导出excel
    public static void downloadExcelFile(String title,Map<String,String> headMap,JSONArray ja,HttpServletResponse response){
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            ExcelUtil.exportExcelX(title,headMap,ja,null,0,os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((title + ".xlsx").getBytes(), StandardCharsets.ISO_8859_1));
            response.setContentLength(content.length);
            try (ServletOutputStream outputStream = response.getOutputStream();
                 BufferedInputStream bis = new BufferedInputStream(is);
                 BufferedOutputStream bos = new BufferedOutputStream(outputStream)){
                byte[] buff = new byte[8192];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            }
        }catch (Exception e) {
            log.error("导出文件异常",e);
        }
    }
    /**
     * 设置单元格样式
     * @param workbook workbook
     * @return 单元格样式对象
     */
    public static CellStyle setCellStyle(SXSSFWorkbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        // cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBold(true);
        cellStyle.setFont(cellFont);
        return cellStyle;

    }
    /**
     * 设置头部单元格样式
     * @param workbook workbook
     * @return 单元格样式对象
     */
    public static CellStyle setHeatherCellStyle(SXSSFWorkbook workbook){
        CellStyle headerStyle = workbook.createCellStyle();
        // headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 字体样式
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;

    }

}