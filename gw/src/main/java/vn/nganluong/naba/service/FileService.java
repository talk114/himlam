package vn.nganluong.naba.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;

public interface FileService {

	public List<?> readFileTextToList(String filePath, String regexSeperator);

	public boolean writeListToFile(String filePath, List<ReconciliationDayDataCompareDTO> reconResultList);
	
	public boolean writeListStringToFile(String filePath, List<String> stringList);

}