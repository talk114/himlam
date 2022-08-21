package gateway.core.channel.napas.doi_soat;

import com.jcraft.jsch.*;
import gateway.core.channel.PaymentGate;
import org.apache.logging.log4j.LogManager;

import java.io.*;

public class SFTPServer extends PaymentGate {

    protected static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(SFTPServer.class);

    public static void uploadSFTPNapas(File file, String SFTPWORKINGDIR) throws JSchException, IOException, SftpException, Exception {
        final String SFTP_HOST = DoiSoatConstants.SFTP_HOST;
        final int SFTP_PORT = DoiSoatConstants.SFTP_PORT;
        final String SFTP_USER = DoiSoatConstants.SFTP_USERNAME;
        final String SFTP_PASS = DoiSoatConstants.SFTP_PASSWORD;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASS);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
//        channelSftp.cd(SFTPWORKINGDIR);
        LOG.info("###### FILE PATH: " + file.getAbsolutePath() + "--" + SFTPWORKINGDIR);
        channelSftp.put(file.getAbsolutePath(), SFTPWORKINGDIR);
        channelSftp.exit();
        session.disconnect();
    }

    public static void downloadSFTPNapas(File file, String SFTPWORKINGDIR, String remoteFileName)
            throws IOException, JSchException, SftpException {
        final String SFTP_HOST = DoiSoatConstants.SFTP_HOST;
        final int SFTP_PORT = DoiSoatConstants.SFTP_PORT;
        final String SFTP_USER = DoiSoatConstants.SFTP_USERNAME;
        final String SFTP_PASS = DoiSoatConstants.SFTP_PASSWORD;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASS);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        channelSftp.cd(SFTPWORKINGDIR);

        String path = channelSftp.ls(remoteFileName).toString();
        if (!path.contains(remoteFileName)) {
            WriteInfoLog("DOI SOAT NAPAS " + remoteFileName + " File doesn't exist in SFTP");
        } else {
            if (!file.exists()) {
                file.createNewFile();
            }
            WriteInfoLog("DOI SOAT NAPAS " + remoteFileName + " File exist in SFTP");
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(channelSftp.get(remoteFileName));
        File newFile = new File(String.valueOf(file.getAbsoluteFile()));
        OutputStream os = new FileOutputStream(newFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int readCount;
        //System.out.println("Getting: " + theLine);
        while ((readCount = bis.read(buffer)) > 0) {
            bos.write(buffer, 0, readCount);
        }
        bis.close();
        bos.close();
        channelSftp.exit();
        session.disconnect();
    }

    public static void downloadSFTPNapasWL(File file, String SFTPWORKINGDIR, String remoteFileName)
            throws IOException, JSchException, SftpException {
        final String SFTP_HOST = DoiSoatConstants.SFTP_HOST;
        final int SFTP_PORT = DoiSoatConstants.SFTP_PORT;
        final String SFTP_USER = DoiSoatConstants.SFTP_USERNAME;
        final String SFTP_PASS = DoiSoatConstants.SFTP_PASSWORD;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASS);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        channelSftp.cd(SFTPWORKINGDIR);

        String path = channelSftp.ls(remoteFileName).toString();
        if (!path.contains(remoteFileName)) {
            WriteInfoLog("DOI SOAT NAPAS WL " + remoteFileName + " File doesn't exist in SFTP");
        } else {
            if (!file.exists()) {
                file.createNewFile();
            }
            WriteInfoLog("DOI SOAT NAPAS WL " + remoteFileName + " File exist in SFTP");
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(channelSftp.get(remoteFileName));
        File newFile = new File(String.valueOf(file.getAbsoluteFile()));
        OutputStream os = new FileOutputStream(newFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int readCount;
        //System.out.println("Getting: " + theLine);
        while ((readCount = bis.read(buffer)) > 0) {
            bos.write(buffer, 0, readCount);
        }
        bis.close();
        bos.close();
        channelSftp.exit();
        session.disconnect();
    }

    public static void uploadSFTPNapasWL(File file, String SFTPWORKINGDIR) throws JSchException, IOException, SftpException, Exception {
        final String SFTP_HOST = DoiSoatConstants.SFTP_HOST;
        final int SFTP_PORT = DoiSoatConstants.SFTP_PORT;
        final String SFTP_USER = DoiSoatConstants.SFTP_USERNAME;
        final String SFTP_PASS = DoiSoatConstants.SFTP_PASSWORD;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASS);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
//        channelSftp.cd(SFTPWORKINGDIR);
        LOG.info("###### FILE PATH: " + file.getAbsolutePath() + "--" + SFTPWORKINGDIR);
        channelSftp.put(file.getAbsolutePath(), SFTPWORKINGDIR);
        channelSftp.exit();
        session.disconnect();
    }
}