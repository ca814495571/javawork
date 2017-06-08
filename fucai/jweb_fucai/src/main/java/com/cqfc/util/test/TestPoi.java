package com.cqfc.util.test;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class TestPoi {

	
	public String[] getExcelDate(String path){
		
	//	"D:\\map.xls"
		String[] str = null;
		
		try {
			
			
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					path));
			// 创建工作簿
			HSSFWorkbook workBook;
			workBook = new HSSFWorkbook(fs);

			String value = "";
			// 创建工作表
			HSSFSheet sheet = workBook.getSheetAt(0);
			// 获得行数
			int rows = sheet.getPhysicalNumberOfRows();
			//System.out.println(rows);
			str = new String[rows];
			if (rows > 0) {
				sheet.getMargin(HSSFSheet.TopMargin);
				for (int j = 0; j < rows; j++) {
					// 行循环
					HSSFRow row = sheet.getRow(j);
					if (row != null) {
						int cells = row.getLastCellNum();
						// 获得列数
						for (short k = 0; k < cells; k++) {
							// 列循环
							HSSFCell cell = row.getCell(k);
							if (cell != null) {

								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC: // 数值型
									if (HSSFDateUtil.isCellDateFormatted(cell)) {
										// 如果是date类型则，获取该cell的date值
										value += HSSFDateUtil.getJavaDate(
												cell.getNumericCellValue())
												.toString()
												+ ",";
									} else {// 纯数字
										value += String
												.valueOf(new Integer((int) cell
														.getNumericCellValue()))
												+ ",";
									}
									break;

								case HSSFCell.CELL_TYPE_STRING: // 字符串型
									value += cell.getRichStringCellValue()
											.toString() + ",";
									break;
								case HSSFCell.CELL_TYPE_FORMULA:// 公式型
									// 读公式计算值
									value += String.valueOf(cell
											.getNumericCellValue()) + ",";
									if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
										value += cell.getRichStringCellValue()
												.toString() + ",";
									}
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:// 布尔
									value += " " + cell.getBooleanCellValue()
											+ ",";
									break;

								case HSSFCell.CELL_TYPE_BLANK: // 空值
									value += "" + ",";
									break;
								case HSSFCell.CELL_TYPE_ERROR: // 故障
									value += "" + ",";
									break;
								default:
									value += cell.getRichStringCellValue()
											.toString() + ",";
								}
							}
						}
					}

					str[j] = value;
					value = "";

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	
	public static void main(String[] args) {
		
		
		
		TestPoi tp = new TestPoi();
		
		String [] strs = tp.getExcelDate("D:\\map.xls");
		
		for (int i = 0; i < strs.length; i++) {
			
		}
		
		
	}
}
