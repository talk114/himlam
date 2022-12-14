package vn.nganluong.naba.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {
	private static final Logger logger = LogManager.getLogger(ExcelServiceImpl.class);

	@Override
	public List<?> readFileExcelToList(String filePath) {

		List<List<String>> listDataExel = new ArrayList<List<String>>();

		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(filePath));

			Workbook workbook = null;
			if (filePath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(file);
			} else if (filePath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(file);
			}

			Sheet sheet = workbook.getSheetAt(0);

			// Map<Integer, List<String>> data = new HashMap<>();
			int i = 0;
			for (Row row : sheet) {
				if (i == 0) {
					i++;
					continue;
				}

				List<String> data = new ArrayList<String>();

				// data.put(i, new ArrayList<String>());
				for (Cell cell : row) {

					switch (cell.getCellType()) {
					case STRING:
						// data.get(i).add(cell.getStringCellValue());
						data.add(cell.getStringCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							// data.get(i).add(cell.getDateCellValue() + "");
							data.add(cell.getDateCellValue().toString());
						} else {
							// data.get(i).add(cell.getNumericCellValue() + "");
							data.add(cell.getNumericCellValue() + "");
						}
						break;
					default:
						// data.get(i).add(cell.getRichStringCellValue().getString());
						data.add(cell.getRichStringCellValue().getString());
						break;
					}
				}
				i++;

				listDataExel.add(data);
			}

			workbook.close();

		} catch (Exception e) {
			logger.info(ExceptionUtils.getStackTrace(e));
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					logger.info(ExceptionUtils.getStackTrace(e));
				}
			}
		}

		return listDataExel;
	}

	@Override
	public boolean writeFileExcelFromList(String[] columns, List<String> dataList, String filePath) {
		try {

			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

			/*
			 * CreationHelper helps us create instances of various things like DataFormat,
			 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
			 */
			CreationHelper createHelper = workbook.getCreationHelper();

			// Create a Sheet
			Sheet sheet = workbook.createSheet("Danh sach");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Create a Row
			Row headerRow = sheet.createRow(0);

			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			// Create Cell Style for formatting Date
//			CellStyle dateCellStyle = workbook.createCellStyle();
//			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			// Create Other rows and cells with employees data
			int rowNum = 1;
			for (String line : dataList) {
				String[] lineArray = line.split(VIBConst.ERP_RECONCILIATION_SEPERATOR_CHAR_IN_FILE);

				Row row = sheet.createRow(rowNum++);

				for (int i = 0; i < lineArray.length; i++) {
					row.createCell(i).setCellValue(lineArray[i]);
				}

//	            row.createCell(1)
//	                    .setCellValue(lineArray[1]);
//
//	            Cell dateOfBirthCell = row.createCell(2);
//	            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
//	            dateOfBirthCell.setCellStyle(dateCellStyle);
//
//	            row.createCell(3)
//	                    .setCellValue(employee.getSalary());
			}

			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();
			return true;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	@Override
	public boolean writeFileExcelFromNestedList(String[] columns, List<List<String>> dataList, String filePath) {
		try {

			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

			// Create a Sheet
			Sheet sheet = workbook.createSheet("Danh sach");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Create a Row
			Row headerRow = sheet.createRow(0);

			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			// Create Other rows and cells with employees data
			int rowNum = 1;
			for (List<String> line : dataList) {

				Row row = sheet.createRow(rowNum++);

				for (int i = 0; i < line.size(); i++) {
					row.createCell(i).setCellValue(line.get(i));
				}

			}

			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();
			return true;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

}
