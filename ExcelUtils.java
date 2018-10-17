package com.yqh.shop.utils;

import com.yqh.shop.model.MallActivationCodesForExcel;
import com.yqh.shop.model.MallCouponForExcel;
import com.yqh.shop.model.MallOilCamiDetailsForExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtils {
	
	private static Logger logger = LogManager.getLogger(ExcelUtils.class.getName());
	
	private Sheet sheet;
	private Integer rowNum;

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getCurrentRowNum() {
		return currentRowNum;
	}

	public void setCurrentRowNum(Integer currentRowNum) {
		this.currentRowNum = currentRowNum;
	}

	private Integer currentRowNum;
	private Row currentRow;
	private Integer colNum;
	private Integer currentColNum;

	// 文件中实际的数据，即过滤空行之后的行数
	private int realPhysicalRowCnt;
	
	public ExcelUtils(){
		
	}
	
	/**
	 * 
	 * @param excel 导入的文件
	 * @param startRow 开始读取的行数
	 */
	public ExcelUtils(File excel, Integer startRow) {
		// 声明并初始化一个工作不对象
		Workbook wb = null;
		try {
			// 生成excel工作簿对象
			wb = WorkbookFactory.create(new FileInputStream(excel));
			// 获得工作簿的第一个工作表
			sheet = wb.getSheetAt(0);
			rowNum = sheet.getLastRowNum();
			currentRowNum = startRow;
			realPhysicalRowCnt = 0;
		} catch (InvalidFormatException e) {
			logger.debug("文件格式不合法！");
			e.printStackTrace();
			//ShareLog.LOG.error("文件格式不合法！" + ExceptionUtils.stackTrace(e));
		} catch (Exception e) {
			logger.debug("导入失败，请重新尝试！");
			e.printStackTrace();
			//ShareLog.LOG.error("导入失败，请重新尝试！" + ExceptionUtils.stackTrace(e));
		}
	}
	
	
	/**
	 * 
	 * @param excel 导入的文件
	 * @param startRow 开始读取的行数
	 */
	public ExcelUtils(InputStream excel, Integer startRow) {
		// 声明并初始化一个工作不对象
		Workbook wb = null;
		try {
			// 生成excel工作簿对象
			wb = WorkbookFactory.create(excel);
			// 获得工作簿的第一个工作表
			sheet = wb.getSheetAt(0);
			rowNum = sheet.getLastRowNum();
			currentRowNum = startRow;
			realPhysicalRowCnt = 0;
		} catch (InvalidFormatException e) {
			logger.debug("文件格式不合法！");
			e.printStackTrace();
			//ShareLog.LOG.error("文件格式不合法！" + ExceptionUtils.stackTrace(e));
		} catch (Exception e) {
			logger.debug("导入失败，请重新尝试！");
			e.printStackTrace();
			//ShareLog.LOG.error("导入失败，请重新尝试！" + ExceptionUtils.stackTrace(e));
		}
	}
	

	/**
	 * 
	 * @param excel 导入的文件
	 */
	public ExcelUtils(InputStream excel) {
		this(excel, 1);
	}
	
	/**
	 * 
	 * @param excel 导入的文件
	 */
	public ExcelUtils(File excel) {
		this(excel, 1);
	}
	
	public LinkedList<String> next() {
		LinkedList<String> list = new LinkedList<>();
		while (currentColNum < colNum) {
			Cell cell = currentRow.getCell(currentColNum);
			if (null != cell) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_FORMULA:
					list.add(cell.getCellFormula());
					break;
				case Cell.CELL_TYPE_NUMERIC: {
					// 若该单元格内容为数值类型，则判断是否为日期类型
					if (cell.getCellStyle().getDataFormatString()
							.equals("yyyy\"年\"m\"月\";@")
							|| HSSFDateUtil.isCellDateFormatted(cell)) {
						try {
							// 若转换发生异常，则记录下单元格的位置，并跳过该行的读取
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							list.add(simpleDateFormat.format(cell
									.getDateCellValue()));
						} catch (Exception e) {
							logger.debug("第" + currentRowNum + "行"
									+ (currentColNum + 1) + "列"
									+ "无法转化为日期，直接转化为字符串!");
							cell.setCellType(Cell.CELL_TYPE_STRING);
							list.add(cell.getStringCellValue());
						}

					} else {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						list.add(cell.getStringCellValue());
					}
					break;
				}
				case Cell.CELL_TYPE_STRING:
					list.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BLANK:
					list.add("");
					break;
				default: {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					list.add(cell.getStringCellValue());
				}
				}
			} else {
				list.add("");
			}
			currentColNum++;
		}
		return isEmptyRow(list) ? null : list;
	}

	/**
	 * 判断该行是否为空行
	 * 
	 * @param list
	 * @return
	 */
	private boolean isEmptyRow(List<String> list) {
		if (null == list || list.isEmpty()) {
			return true;
		}
		for (int i = 0; i < list.size(); i++) {
			String val = list.get(i);
			if (null != val && !"".equals(val.trim())) {
				realPhysicalRowCnt++;
				return false;
			}
		}
		return true;
	}

	public boolean hasNext() {
		if (currentRowNum <= rowNum) {
			currentRow = sheet.getRow(currentRowNum);
			currentRowNum++;
			if (null != currentRow) {
				currentColNum = 0;
				colNum = (int) currentRow.getLastCellNum();
				return true;
			} else {
				return hasNext();
			}
		} else {
			return false;
		}
	}

	public int getRealPhysicalRowCnt() {
		return realPhysicalRowCnt;
	}

	public void setRealPhysicalRowCnt(int realPhysicalRowCnt) {
		this.realPhysicalRowCnt = realPhysicalRowCnt;
	}
	
	
	/**
	 * 现有的excel 中写入信息,每次写入一条
	 * @param list
	 * @param url 模版路径
	 * @param newUrl 新路径
	 */
	public static void excelWrite(List<Object> list, List<List<Object>>  annexList, String url, String newUrl){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
        Workbook book = null;
		try {
	        try {
	            book = new XSSFWorkbook(url);
	        } catch (Exception ex) {
	        	InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
	        }
	        
	        CellStyle style = book.createCellStyle();  
	        style.setBorderBottom(CellStyle.BORDER_THIN);  
	        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderLeft(CellStyle.BORDER_THIN);  
	        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderRight(CellStyle.BORDER_THIN);  
	        style.setRightBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderTop(CellStyle.BORDER_THIN);  
	        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		  
	        Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
	        Sheet sheet2 = book.getSheetAt(2); //获取到工作表，因为一个excel可能有多个工作表
	        Row sheet0row0=sheet0.getRow(0);
	        sheet0row0.getCell(0).setCellValue(list.get(1) == null ? "":list.get(1).toString());
	        Row sheet0row1 = sheet0.getRow(1);
	        Row sheet0row2 = sheet0.getRow(2);
	        //第一个sheet
	    	for(int i = 0 ; i< sheet0row1.getLastCellNum(); i++){
	    		String value = list.get(i) == null ? "":list.get(i).toString();
	    		sheet0row2.createCell(i).setCellValue(value);
	    	}
	    	//第三个sheet
	    	Row sheet2row1 = sheet2.getRow(0);
	    	for(int i = 0 ; i < annexList.size(); i++){
	    		System.out.println(i);
	    		List<Object> annex = annexList.get(i);
	    		Row sheet2rowj = sheet2.createRow(i+1);
	    		sheet2rowj.setHeight((short)(34*20));
	    		for(int j = 0 ; j< sheet2row1.getLastCellNum(); j++){
		    		String value = annex.get(j) == null ? "":annex.get(j).toString();
		    		Cell cell = sheet2rowj.createCell(j);
		    		cell.setCellValue(value);
		    		cell.setCellStyle(style);
		    	}
	    	}
	    	
	    	FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel 
	        out.flush();  
	        book.write(out);    
	        out.close(); 
	        logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			 logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 现有的excel 中写入信息,每次写入一条
	 * @param EI 基本信息
	 * @param FL 文件级信息
	 * @param OI 原件索引
	 * @param url 模版路径
	 * @param newUrl 新路径
	 */
	public static void excelWrite(List<Object> EI, List<List<Object>>  FL, List<List<Object>>  OI, String url, String newUrl){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
        Workbook book = null;
		try {
	        try {
	            book = new XSSFWorkbook(url);
	        } catch (Exception ex) {
	        	InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
	        }
	        
	        Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
	        Sheet sheet1 = book.getSheetAt(1); //获取到工作表，因为一个excel可能有多个工作表
	        Sheet sheet2 = book.getSheetAt(2); //获取到工作表，因为一个excel可能有多个工作表
	        
	        Row sheet0row0=sheet0.getRow(0);
	        Row sheet0row1 = sheet0.getRow(1);
	        //第一个sheet
	    	for(int i = 0 ; i < EI.size(); i++){
	    		String value = EI.get(i) == null ? "" : EI.get(i).toString();
	    		sheet0row1.createCell(i).setCellValue(value);
	    	}
	    	//第二个sheet
	    	Row sheet1row1 = sheet1.getRow(0);
	    	for(int i = 0 ; i < FL.size(); i++){
	    		List<Object> annex = FL.get(i);
	    		Row sheet1rowj = sheet1.createRow(i+1);
	    		sheet1rowj.setHeight((short)(57*20));
	    		for(int j = 0 ; j< sheet1row1.getLastCellNum(); j++){
		    		String value = annex.get(j) == null ? "":annex.get(j).toString();
		    		sheet1rowj.createCell(j).setCellValue(value);
		    	}
	    	}
	    	//第三个sheet
	    	Row sheet2row1 = sheet2.getRow(0);
	    	for(int i = 0 ; i < OI.size(); i++){
	    		List<Object> annex = OI.get(i);
	    		Row sheet2rowj = sheet2.createRow(i+1);
	    		sheet2rowj.setHeight((short)(34*20));
	    		for(int j = 0 ; j< sheet2row1.getLastCellNum(); j++){
		    		String value = annex.get(j) == null ? "":annex.get(j).toString();
		    		sheet2rowj.createCell(j).setCellValue(value);
		    	}
	    	}
	    	
	    	FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel 
	        out.flush();  
	        book.write(out);    
	        out.close(); 
	        logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			 logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 现有的excel 中写入信息,每次写入一条
	 * @param list
	 * @param url 模版路径
	 * @param newUrl 新路径
	 */
	public static void excelWrite(List<List<Object>> list, String url, String newUrl){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
        Workbook book = null;
		try {
	        try {
	            book = new XSSFWorkbook(url);
	        } catch (Exception ex) {
	        	InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
	        }
	        
	        CellStyle style = book.createCellStyle();  
	        style.setBorderBottom(CellStyle.BORDER_THIN);  
	        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderLeft(CellStyle.BORDER_THIN);  
	        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderRight(CellStyle.BORDER_THIN);  
	        style.setRightBorderColor(IndexedColors.BLACK.getIndex());  
	        style.setBorderTop(CellStyle.BORDER_THIN);  
	        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		  
	        Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
	        Row sheet0row2=sheet0.getRow(1);
	    
	        int row = 1; //起始行数
	        int hanNum = list.size();
	        if(list.size()%hanNum != 0){
	        	for(int i = 0; i< list.size()%hanNum; i++){
	        		List<Object> tmp = new ArrayList<Object>();
	        		tmp.add("");
	        		tmp.add("");
	        		tmp.add("");
	        		list.add(tmp);
	        	}
	        }
//	        int all = list.size()/hanNum;
//			int row = 1;
//			int hanNum = 2;
	        int pageIndex = 1;
	        for(int i = 0; i < list.size(); i++){
	        	Row sheet0rowj=sheet0.createRow(row);
	        	sheet0rowj.setHeight((short)(25*20));
	        	List<Object> annex = list.get(i);
        		for(int j = 0 ; j< sheet0row2.getLastCellNum(); j++){
		    		Cell cell = sheet0rowj.createCell(j);
		    		if(j == 0){
//		    			cell.setCellValue(i+1);
		    		}else{
		    			if(j-1 > annex.size() - 1){
		    				cell.setCellValue("");
		    			}else{
		    				String value = annex.get(j-1) == null ? "":annex.get(j-1).toString();
			    			cell.setCellValue(value);
		    			}
		    		}
		    		cell.setCellStyle(style);
		    	}
	        	if((i+1)%hanNum==0){
	        		//生成一行
	        		row = row + 1;
	        		Row sheet0rowX=sheet0.createRow(row);
	        		sheet0rowX.setHeight((short)(20*20));
	        		for(int k = 0; k <5; k++){
	        			sheet0rowX.createCell(k);
	        		}
	        		CellRangeAddress cellRangeAddressX = new CellRangeAddress(row, row, 0, 4);
	        		sheet0.addMergedRegion(cellRangeAddressX);
//	        		sheet0rowX.getCell(0).setCellValue("                           共 "+all+" 页                 第 "+pageIndex+" 页                         ");
	        		//生成一行
	        		row = row + 1;
	        		Row sheet0rowY=sheet0.createRow(row);
	        		sheet0rowY.setHeight((short)(20*20));
	        		for(int k = 0; k <5; k++){
	        			sheet0rowY.createCell(k);
	        		}
	        		
	        		if(i < list.size()-1){
	        			//模板
		        		Cell cell0 = sheet0.getRow(0).getCell(0);
		        		
		        		//头文件第一行
		        		row = row + 1;
		        		Row sheet0row0X=sheet0.createRow(row);
		        		sheet0row0X.setHeight((short)(67.5*20));
		        		for(int k = 0; k <5; k++){
		        			sheet0row0X.createCell(k).setCellStyle(cell0.getCellStyle());;
		        		}
		        		Cell cell0X = sheet0row0X.getCell(0);
		        		cell0X.setCellValue(cell0.getRichStringCellValue());
		        		CellRangeAddress cellRangeAddress1 = new CellRangeAddress(row, row, 0, 4);
		        		sheet0.addMergedRegion(cellRangeAddress1);
		        		
		        		//头文件第三行
		           		row = row + 1;
		        		Row sheet0row1X=sheet0.createRow(row);
		        		sheet0row1X.setHeight((short)(35*20));
		        		for(int k = 0; k <5; k++){
		        			Cell cell = sheet0row1X.createCell(k);
		        			cell.setCellStyle(sheet0.getRow(2).getCell(k).getCellStyle());
		        			cell.setCellValue(sheet0.getRow(2).getCell(k).getRichStringCellValue());
		        		}
	        		}
	        		
	        		pageIndex++;
	        		/*CellRangeAddress cellRangeAddress = new CellRangeAddress(row-1, 0, row-1, 4);
	        		sheet0.addMergedRegion(cellRangeAddress);*/
	        	}
	        	row++;
	        }
//	        for(int x = row+2; x < row+9; x++){
//	        	Row sheet0rowX=sheet0.createRow(x);
//	        	for(int z = 0; z < 5; z++){
//	        		Cell cell = sheet0rowX.createCell(z);
//	        		if(x == row +3 && z == 1){
//	        			cell.setCellValue("移交单位:");
//	        		}
//	        		if(x == row +3 && z == 3){
//	        			cell.setCellValue("接受单位:");
//	        		}
//	        		if(x == row +5 && z == 1){
//	        			cell.setCellValue("移交人:");
//	        		}
//	        		if(x == row +5 && z == 3){
//	        			cell.setCellValue("接受人:");
//	        		}
//	        		if(x == row +7 && z == 1){
//	        			cell.setCellValue("移交日期:");
//	        		}
//	        		if(x == row +7 && z == 3){
//	        			cell.setCellValue("接受日期:");
//	        		}
//	        	}
//	        }
	        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(row+3, row+4, 1, 2);
	        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(row+3, row+4, 3, 4);
	        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(row+5, row+6, 1, 2);
	        CellRangeAddress cellRangeAddress4 = new CellRangeAddress(row+5, row+6, 3, 4);
	        CellRangeAddress cellRangeAddress5 = new CellRangeAddress(row+7, row+8, 1, 2);
	        CellRangeAddress cellRangeAddress6 = new CellRangeAddress(row+7, row+8, 3, 4);
	        sheet0.addMergedRegion(cellRangeAddress1);
	        sheet0.addMergedRegion(cellRangeAddress2);
	        sheet0.addMergedRegion(cellRangeAddress3);
	        sheet0.addMergedRegion(cellRangeAddress4);
	        sheet0.addMergedRegion(cellRangeAddress5);
	        sheet0.addMergedRegion(cellRangeAddress6);
	        
			FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel
	        out.flush();  
	        book.write(out);    
	        out.close(); 
	        logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			 logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}

	/**
	 * 现有的excel 中写入信息,每次写入一条
	 * @param
	 * @param url 模版路径
	 * @param newUrl 新路径
	 */
	public static void excelWriteForOilCami(List<MallOilCamiDetailsForExcel> annex, String url, String newUrl, HttpServletRequest request, HttpServletResponse response){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
		Workbook book = null;
		try {
			try {
				book = new XSSFWorkbook(url);
			} catch (Exception ex) {
				InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
			}
			CellStyle style = book.createCellStyle();
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
			Row sheet0row0=sheet0.getRow(1);
			int row = 1; //起始行数
			for(int i = 0; i < annex.size(); i++){
				Row sheet0rowj=sheet0.createRow(row);
				sheet0rowj.setHeight((short)(25*20));
				Cell cell = null;
				for(int j = 0 ; j< sheet0row0.getLastCellNum(); j++){
					cell = sheet0rowj.createCell(j);
					switch (j){
						case 0:
							cell.setCellValue(annex.get(i).getCami_sn() == null ? "":annex.get(i).getCami_sn());
							break;
						case 1:
							cell.setCellValue(annex.get(i).getCami_name() == null ? "":annex.get(i).getCami_name());
							break;
						case 2:
							cell.setCellValue(annex.get(i).getFace_value() == null ? "":annex.get(i).getFace_value());
							break;
						case 3:
							cell.setCellValue(annex.get(i).getCami_num() == null ? "":annex.get(i).getCami_num());
							break;
						case 4:
							cell.setCellValue(annex.get(i).getCami_paw() == null ? "":annex.get(i).getCami_paw());
							break;
						case 5:
							cell.setCellValue(annex.get(i).getUse_status() == null ? "":annex.get(i).getUse_status());
							break;
						case 6:
							cell.setCellValue(annex.get(i).getOil_order_sn() == null ? "":annex.get(i).getOil_order_sn());
							break;
						case 7:
							cell.setCellValue(annex.get(i).getOil_card_num() == null ? "":annex.get(i).getOil_card_num());
							break;
						case 8:
							cell.setCellValue(annex.get(i).getContacts() == null ? "":annex.get(i).getContacts());
							break;
						case 9:
							cell.setCellValue(annex.get(i).getMobile() == null ? "":annex.get(i).getMobile());
							break;
						case 10:
							cell.setCellValue(annex.get(i).getCreate_time() == null ? "":annex.get(i).getCreate_time());
							break;
						case 11:
							cell.setCellValue(annex.get(i).getRecharge_time() == null ? "":annex.get(i).getRecharge_time());
							break;
					}
					cell.setCellStyle(style);
				}
				row++;
			}
			FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel
			book.write(out);
			out.flush();
			out.close();
			response.addHeader("Content-Disposition", "attachment;filename=outOliCard.xls");
			OutputStream output = new BufferedOutputStream(response.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(newUrl));
			int len;
			byte[] buf = new byte[8192];
			while ((len = in.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
			output.flush();
			output.close();
			logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}

	public static void excelWriteForCouponCode(List<MallCouponForExcel> annex, String url, String newUrl, HttpServletRequest request, HttpServletResponse response){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
		Workbook book = null;
		try {
			try {
				book = new XSSFWorkbook(url);
			} catch (Exception ex) {
				InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
			}
			CellStyle style = book.createCellStyle();
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
			Row sheet0row0=sheet0.getRow(0);
			int row = 1; //起始行数
			for(int i = 0; i < annex.size(); i++){
				Row sheet0rowj=sheet0.createRow(row);
				sheet0rowj.setHeight((short)(25*20));
				Cell cell = null;
				for(int j = 0 ; j< sheet0row0.getLastCellNum(); j++){
					cell = sheet0rowj.createCell(j);
					switch (j){
						case 0:
							cell.setCellValue(annex.get(i).getSn() == null ? "":annex.get(i).getSn());
							break;
						case 1:
							cell.setCellValue(annex.get(i).getCouponName() == null ? "":annex.get(i).getCouponName());
							break;
						case 2:
							cell.setCellValue(annex.get(i).getCode() == null ? "":annex.get(i).getCode());
							break;
						case 3:
							cell.setCellValue(annex.get(i).getStatus() == null ? "":annex.get(i).getStatus());
							break;
						case 4:
							cell.setCellValue(annex.get(i).getUse_time() == null ? "":annex.get(i).getUse_time());
							break;
						case 5:
							cell.setCellValue(annex.get(i).getUserId() == null ? "":annex.get(i).getUserId());
							break;
						case 6:
							cell.setCellValue(annex.get(i).getCreate_time() == null ? "":annex.get(i).getCreate_time());
							break;
					}
					cell.setCellStyle(style);
				}
				row++;
			}
			FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel
			book.write(out);
			out.flush();
			out.close();
			response.addHeader("Content-Disposition", "attachment;filename=couponCode.xls");
			OutputStream output = new BufferedOutputStream(response.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(newUrl));
			int len;
			byte[] buf = new byte[8192];
			while ((len = in.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
			output.flush();
			output.close();
			logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}




	public static void excelWriteForCodes(List<MallActivationCodesForExcel> annex, String url, String newUrl, HttpServletRequest request, HttpServletResponse response){
		logger.debug("模板路径:"+url);
		logger.debug("生成路径:"+newUrl);
		Workbook book = null;
		try {
			try {
				book = new XSSFWorkbook(url);
			} catch (Exception ex) {
				InputStream inputStream = new FileInputStream(url);
				book = new HSSFWorkbook(new POIFSFileSystem(inputStream));
			}
			CellStyle style = book.createCellStyle();
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
			Sheet sheet0 = book.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
			Row sheet0row0=sheet0.getRow(1);
			int row = 1; //起始行数
			for(int i = 0; i < annex.size(); i++){
				Row sheet0rowj=sheet0.createRow(row);
				sheet0rowj.setHeight((short)(25*20));
				Cell cell = null;
				for(int j = 0 ; j< sheet0row0.getLastCellNum(); j++){
					cell = sheet0rowj.createCell(j);
					switch (j){
						case 0:
							cell.setCellValue(annex.get(i).getCodes_sn() == null ? "":annex.get(i).getCodes_sn());
							break;
						case 1:
							cell.setCellValue(annex.get(i).getCodes_name() == null ? "":annex.get(i).getCodes_name());
							break;
						case 2:
							cell.setCellValue(annex.get(i).getCodes_num() == null ? "":annex.get(i).getCodes_num());
							break;
						case 3:
							cell.setCellValue(annex.get(i).getUse_status() == null ? "":annex.get(i).getUse_status());
							break;
						case 4:
							cell.setCellValue(annex.get(i).getOrder_sn() == null ? "":annex.get(i).getOrder_sn());
							break;
						case 5:
							cell.setCellValue(annex.get(i).getContacts() == null ? "":annex.get(i).getContacts());
							break;
						case 6:
							cell.setCellValue(annex.get(i).getMobile() == null ? "":annex.get(i).getMobile());
							break;
						case 7:
							cell.setCellValue(annex.get(i).getPay_time() == null ? "":annex.get(i).getPay_time());
							break;
					}
					cell.setCellStyle(style);
				}
				row++;
			}
			FileOutputStream out=new FileOutputStream(newUrl);  //生成新的excel
			book.write(out);
			out.flush();
			out.close();
			response.addHeader("Content-Disposition", "attachment;filename=codes.xls");
			OutputStream output = new BufferedOutputStream(response.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(newUrl));
			int len;
			byte[] buf = new byte[8192];
			while ((len = in.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
			output.flush();
			output.close();
			logger.debug("生成新的excel模板成功");
		} catch (Exception e) {
			logger.debug("生成新的excel内容失败");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
	}
	

}