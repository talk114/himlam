package vn.nganluong.naba.service;

import java.util.List;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;

import com.jcraft.jsch.SftpException;
import vn.nganluong.naba.entities.PgSftp;

public interface SFTPService {

	public ChannelSftp SetupChannelSftp(PgSftp pgSftp) throws JSchException;
	
	public void RemoveChannelSftp(ChannelSftp channelSftp) throws JSchException;
	
	public List<String> DownloadFileTSubtract1(PgSftp pgSftp, String dateInFileName) throws JSchException, SftpException;
	
	public boolean DownloadFileTSubtract1And2(PgSftp pgSftp, String dateInFileName1, String dateInFileName2);
	
	public boolean DownloadFileSpecificFileName(PgSftp pgSftp, String fileName);
	
	public boolean UploadFile(PgSftp pgSftp, String fileSrc, String fileDst);
}
