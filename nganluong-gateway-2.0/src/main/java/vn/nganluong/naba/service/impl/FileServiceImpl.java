package vn.nganluong.naba.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;
import vn.nganluong.naba.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

	/*
	 * public static void main(String[] args) { FileServiceImpl fileServiceImpl =
	 * new FileServiceImpl(); String regexSeperator = "\\|"; List<List<String>>
	 * listDataText = (List<List<String>>) fileServiceImpl .readFileTextToList(
	 * "E:/DEHA-Project/NL/VIB/VIB_DOITAC_TRANS_20200331_OUT.txt", regexSeperator);
	 * System.out.println(""); }
	 */

	@Override
	public List<?> readFileTextToList(String filePath, String regexSeperator) {

		List<List<String>> listDataText = new ArrayList<List<String>>();

		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {

			String line = StringUtils.EMPTY;

//			int i = 0;
			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
//				if (i == 0) {
//					i++;
//					continue;
//				}
				List<String> data = new ArrayList<String>();
				String[] flatData = line.split(regexSeperator);

				for (int j = 0; j < flatData.length; j++) {
					data.add(flatData[j]);
				}
//				i++;

				listDataText.add(data);
			}

		} catch (Exception e) {

		}

		return listDataText;
	}

	@Override
	public boolean writeListToFile(String filePath, List<ReconciliationDayDataCompareDTO> reconResultList) {

		String seperatorChar = "|";
		try (RandomAccessFile writer = new RandomAccessFile(filePath, "rw");
				FileChannel channel = writer.getChannel()) {

			for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : reconResultList) {
				StringBuilder line = new StringBuilder(reconciliationDayDataCompareDTO.getRecordType());
				line.append(seperatorChar).append(reconciliationDayDataCompareDTO.getReconCode()).append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranType()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getFromAcct()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getToAcct()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranAmtString()))
						.append(seperatorChar).append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getCcy()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getDescription()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranHourString()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranDateString()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranSequence()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getClientTranId()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getTranFeeString()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getNapasResult()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getReconResult()))
						.append(seperatorChar)
						.append(StringUtils.trimToEmpty(reconciliationDayDataCompareDTO.getChecksum())).append("\n");
				byte[] strBytes = line.toString().getBytes(StandardCharsets.UTF_8);
				ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
				buffer.put(strBytes);
				buffer.flip();
				channel.write(buffer);

			}
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(ExceptionUtils.getStackTrace(e));
		}

		return false;
	}

	@Override
	public boolean writeListStringToFile(String filePath, List<String> stringList) {
		try (RandomAccessFile writer = new RandomAccessFile(filePath, "rw");
				FileChannel channel = writer.getChannel()) {

			for (String line : stringList) {
				byte[] strBytes = line.getBytes(StandardCharsets.UTF_8);
				ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
				buffer.put(strBytes);
				buffer.flip();
				channel.write(buffer);
			}
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(ExceptionUtils.getStackTrace(e));
		}

		return false;
	}

}
