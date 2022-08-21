package gateway.core.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

@FunctionalInterface
public interface SFTPFunction {
    void execute(ChannelSftp channelSftp) throws SftpException, IOException;
}
