package vn.nganluong.naba.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp.LsEntry;

import vn.nganluong.naba.entities.PgSftp;
import vn.nganluong.naba.service.SFTPService;
import org.apache.logging.log4j.Logger;

@Service
public class SFTPServiceImpl implements SFTPService {
    private static final Logger logger = LogManager.getLogger(SFTPServiceImpl.class);

    @Override
    public ChannelSftp SetupChannelSftp(PgSftp pgSftp) throws JSchException {
        JSch jsch = new JSch();
        
        Session jschSession = jsch.getSession(pgSftp.getUserName(), pgSftp.getHost(), pgSftp.getPort());

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        jschSession.setConfig(config);
        jschSession.setPassword(pgSftp.getPassword());
        jschSession.connect();

        ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
        channelSftp.connect();
        return channelSftp;
    }

    @Override
    public void RemoveChannelSftp(ChannelSftp channelSftp) throws JSchException {
        channelSftp.disconnect();
        channelSftp.exit();

        Session jschSession = channelSftp.getSession();
        if (jschSession != null) {
            jschSession.disconnect();
        }
    }

    @Override
    public List<String> DownloadFileTSubtract1(PgSftp pgSftp, String dateInFileName) throws JSchException, SftpException {
        List<String> filesDownloaded = new ArrayList<String>();
        if (pgSftp == null) {
            return filesDownloaded;
        }
        ChannelSftp channelSftp = SetupChannelSftp(pgSftp);
        // Get all file in remote dir:
        channelSftp.cd(pgSftp.getRemoteDirIn());
        Vector<?> fileList = channelSftp.ls(pgSftp.getRemoteDirIn());
        for (int i = 0; i < fileList.size(); i++) {
            LsEntry entry = (LsEntry) fileList.get(i);
            // System.out.println(entry.getFilename());
            if (StringUtils.contains(entry.getFilename(), dateInFileName)) {
                channelSftp.get(pgSftp.getRemoteDirIn() + entry.getFilename(), pgSftp.getLocalDirIn() + entry.getFilename());
                filesDownloaded.add(pgSftp.getLocalDirIn() + entry.getFilename());
            }
        }
        RemoveChannelSftp(channelSftp);
        return filesDownloaded;
    }

    @Override
    public boolean DownloadFileTSubtract1And2(PgSftp pgSftp, String dateInFileName1, String dateInFileName2) {
        boolean isCopied = false;
        try {
            if (pgSftp == null) {
                return false;
            }
            ChannelSftp channelSftp = SetupChannelSftp(pgSftp);
            // Get all file in remote dir:
            channelSftp.cd(pgSftp.getRemoteDirIn());
            Vector<?> fileList = channelSftp.ls(pgSftp.getRemoteDirIn());
            for (int i = 0; i < fileList.size(); i++) {
                LsEntry entry = (LsEntry) fileList.get(i);
                // System.out.println(entry.getFilename());
                if (StringUtils.contains(entry.getFilename(), dateInFileName1) || StringUtils.contains(entry.getFilename(), dateInFileName2)) {
                    channelSftp.get(pgSftp.getRemoteDirIn() + entry.getFilename(), pgSftp.getLocalDirIn() + entry.getFilename());
                    isCopied = true;
                }
            }
            RemoveChannelSftp(channelSftp);
        } catch (com.jcraft.jsch.JSchException e) {
            // System.out.println(e.getMessage());
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return isCopied;
    }

    @Override
    public boolean UploadFile(PgSftp pgSftp, String fileSrc, String fileDst) {
        boolean isUploaded = false;
        try {
            if (pgSftp == null) {
                return false;
            }
            ChannelSftp channelSftp = SetupChannelSftp(pgSftp);
            channelSftp.put(fileSrc, fileDst);
            isUploaded = true;

            RemoveChannelSftp(channelSftp);
        } catch (com.jcraft.jsch.JSchException e) {
            // System.out.println(e.getMessage());
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return isUploaded;
    }

    @Override
    public boolean DownloadFileSpecificFileName(PgSftp pgSftp, String fileName) {
        boolean isCopied = false;
        try {
            if (pgSftp == null) {
                return false;
            }
            ChannelSftp channelSftp = SetupChannelSftp(pgSftp);
            // Get all file in remote dir:
            channelSftp.cd(pgSftp.getRemoteDirIn());
            Vector<?> fileList = channelSftp.ls(pgSftp.getRemoteDirIn());
            for (int i = 0; i < fileList.size(); i++) {
                LsEntry entry = (LsEntry) fileList.get(i);
                // System.out.println(entry.getFilename());
                if (StringUtils.contains(entry.getFilename(), fileName)) {
                    channelSftp.get(pgSftp.getRemoteDirIn() + entry.getFilename(), pgSftp.getLocalDirIn() + entry.getFilename());
                    isCopied = true;
                    break;
                }
            }
            RemoveChannelSftp(channelSftp);
        } catch (com.jcraft.jsch.JSchException e) {
            // System.out.println(e.getMessage());
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return isCopied;
    }
}
