/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.util;

import com.jcraft.jsch.*;
import gateway.core.channel.tcb.dto.TCBConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * @author taind
 */
public class SFTPUploader {
    private static final Logger logger = LogManager.getLogger(SFTPUploader.class);

    public static boolean updateSFTP(File file, String partPrivateKey, String SFTPWORKINGDIR, String fileNameDelete) {
        String SFTPHOST = "10.0.14.10";
        int SFTPPORT = 4122;
        String SFTPUSER = "mb_sftp";
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setConfig(
                    "PreferredAuthentications",
                    "publickey,gssapi-with-mic,keyboard-interactive,password");
            jsch.addIdentity(partPrivateKey);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            // UPLOAD FILE TO SFTP
            channelSftp.put(file.getAbsolutePath(), SFTPWORKINGDIR);
            System.out.println("MB UPLOAD FILE DS - SUCCESS : " + file.getName() + "    |  TIME UPLOAD: " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));

//            // DELETE FILE TO SFTP
//            try {
//                channelSftp.rm(fileNameDelete);
//            } catch (Exception e){
//                channel.disconnect();
//                session.disconnect();
//                return true;
//            }
            channel.disconnect();
            session.disconnect();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String downloadSFTPVNA(File file, String SFTPWORKINGDIR, String remoteFileName, String SFPT_VNA_IP_HOST, String SFPT_VNA_USER, String SFPT_VNA_PASSWORD) {
        String SFTPHOST = SFPT_VNA_IP_HOST;
        int SFTPPORT = 22;
        String SFTPUSER = SFPT_VNA_USER;
        String SFTPPASS = SFPT_VNA_PASSWORD;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
//            session.setConfig(
//                    "PreferredAuthentications",
//                    "publickey,gssapi-with-mic,keyboard-interactive,password");
//            jsch.addIdentity(partPrivateKey);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(remoteFileName));
            File newFile = new File(String.valueOf(file.getAbsoluteFile()));
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            //System.out.println("Getting: " + theLine);
            while ((readCount = bis.read(buffer)) > 0) {
                System.out.println("Writing: ");
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public static String downloadTCB(String remoteFileName, String directory) {
        String SFTPHOST = TCBConstants.SFTP_HOST;
        int SFTPPORT = TCBConstants.SFTP_POST;
        String SFTPUSER = TCBConstants.SFTP_USER;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        ClassLoader classLoader = SFTPUploader.class.getClassLoader();
        String fileAuth = Objects.requireNonNull(classLoader.getResource("tcb_key/transfer/id_rsa")).getFile();
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setConfig("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            jsch.addIdentity(fileAuth);
            session.connect();
            System.out.println("DOI SOAT TCB connected sftp......");

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd("/data/");
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(remoteFileName));
            File newFile = new File(directory + remoteFileName);
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                System.out.println("Writing: ");
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
            System.out.println("DOI SOAT TCB download file done : " + remoteFileName);
            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("DOI SOAT TCB download file error" + ex.getMessage());
            return "error";
        }
    }

    public static String downloadVIBIBFT(String remoteFileName, String directory) {
        String SFTPHOST = "10.0.14.10";
        int SFTPPORT = 4122;
        String SFTPUSER = "vib_ibft";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        ClassLoader classLoader = SFTPUploader.class.getClassLoader();
        String fileAuth = Objects.requireNonNull(classLoader.getResource("vib-key/id_rsa")).getFile();
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setConfig("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            jsch.addIdentity(fileAuth);
            session.connect();
            System.out.println("connected......");

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd("/in/");
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(remoteFileName));
            File newFile = new File(directory + remoteFileName);
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                System.out.println("Writing: ");
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public static boolean downloadSftp(String user,
                                       String host,
                                       int port,
                                       String keyPathOrPassword,
                                       SFTPFunction sftpFunction){
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp;
        try{
            JSch jSch = new JSch();
            session = jSch.getSession(user,host, port);
            if (keyPathOrPassword.contains("/")){
                session.setConfig("PreferredAuthentications",
                        "publickey,gssapi-with-mic,keyboard-interactive,password");
                jSch.addIdentity(keyPathOrPassword);
            }else {
                session.setPassword(keyPathOrPassword);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            sftpFunction.execute(channelSftp);
            return true;
        }catch (JSchException | SftpException | IOException e){
            logger.error(e.getMessage());
            return false;
        }finally {
            if(channel!=null) channel.disconnect();
            if(session!=null) session.disconnect();
        }
    }
}
