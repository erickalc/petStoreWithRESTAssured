package io.swagger.utils;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	private static String pathNameExcel = "./massaDeTestes/dataUsersPetsOrders.xlsx";
	private static String sheetNameExcelUsers = "Users";
	private static String sheetNameExcelPets = "Pets";
	private static String sheetNameExcelOrders = "Orders";

	protected static XSSFWorkbook workbook;
	protected static XSSFSheet sheet;

	public ExcelUtils(String pathname, String sheetName) throws IOException {
		workbook = new XSSFWorkbook(pathname);
		sheet = workbook.getSheet(sheetName);
	}
	
//	public static void main(String[] args) throws IOException {
//		ExcelUtils dataExcel = new ExcelUtils("./massaDeTestes/dataUsersPetsOrders.xlsx", "Users");
//		System.out.println(dataExcel.getColCount());
//	}

	public static Object getCellData(int rowNum, int colNum) {
		DataFormatter data = new DataFormatter();
		
		Object value = data.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
		return value;
	}

	public static int getRowCount() {
		return sheet.getPhysicalNumberOfRows();
	}
	
	public static int getColCount() {
		return sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells();
	}
	
	public static ExcelUtils lerArquivoExcelUsers() throws IOException {
		ExcelUtils dataUsersExcel = new ExcelUtils(pathNameExcel, sheetNameExcelUsers);
		return dataUsersExcel;
	}
	
	public static ExcelUtils lerArquivoExcelPets() throws IOException {
		ExcelUtils dataPetsExcel = new ExcelUtils(pathNameExcel, sheetNameExcelPets);
		return dataPetsExcel;
	}
	
	public static ExcelUtils lerArquivoExcelOrders() throws IOException {
		ExcelUtils dataOrdersExcel = new ExcelUtils(pathNameExcel, sheetNameExcelOrders);
		return dataOrdersExcel;
	}

}
