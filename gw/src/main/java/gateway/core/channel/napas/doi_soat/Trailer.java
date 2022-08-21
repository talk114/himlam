package gateway.core.channel.napas.doi_soat;

import java.io.Serializable;

import static gateway.core.channel.napas.service.schedule.NapasScheduleService.*;
import static gateway.core.channel.napas.doi_soat.DoiSoatConstants.*;

public class Trailer implements Serializable {
    private static final String recordType = "TR";
    private String numberOfTransactions; // tong so giao dich
    private String fileCreater;// nguoi tao file
    private String timeCreatFile; //thoi gian tao file
    private String dateCreateFile; // ngay tao file
    private String checksumFile; // checksum cho toan ca file

    public String getRecordType() {
        return recordType;
    }

    public String getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(String numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public String getFileCreater() {
        return fileCreater;
    }

    public void setFileCreater(String fileCreater) {
        this.fileCreater = fileCreater;
    }

    public String getTimeCreatFile() {
        return timeCreatFile;
    }

    public void setTimeCreatFile(String timeCreatFile) {
        this.timeCreatFile = timeCreatFile;
    }

    public String getDateCreateFile() {
        return dateCreateFile;
    }

    public void setDateCreateFile(String dateCreateFile) {
        this.dateCreateFile = dateCreateFile;
    }

    public String getChecksumFile() {
        return checksumFile;
    }

    public void setChecksumFile(String checksumFile) {
        this.checksumFile = checksumFile;
    }


    @Override
    public String toString() {
        return "TR[NOT]" + genWhiteSpaceOrZero(this.numberOfTransactions, 9 - this.numberOfTransactions.length(), ZERO)
                + "[CRE]" + genWhiteSpaceOrZero(this.fileCreater, 20 - this.fileCreater.length(), WHITE_SPACE)
                + "[TIME]" + formatDateTimeByPattern("hhmmss", this.timeCreatFile)
                + "[DATE]" + formatDateTimeByPattern("ddMMyyyy", this.dateCreateFile) + "[CSF]";
    }
}
