package vn.nganluong.naba.service;

import java.util.List;

public interface ExcelService {

	public List<?> readFileExcelToList(String filePath);

	public boolean writeFileExcelFromList(String[] columns, List<String> dataList, String filePath);
	
	public boolean writeFileExcelFromNestedList(String[] columns, List<List<String>> dataList, String filePath);

}