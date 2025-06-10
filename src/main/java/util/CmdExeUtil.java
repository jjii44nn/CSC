package util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.*;
import java.util.Arrays;

public class CmdExeUtil {

    public static String execute(String cmd)  {
        CommandLine cmdline = CommandLine.parse("/bin/sh");
        cmdline.addArgument("-c");
        cmdline.addArgument(cmd, false);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(output);
        DefaultExecutor executor = new DefaultExecutor();

        executor.setStreamHandler(streamHandler);
        try {
            int exitCode = executor.execute(cmdline);
        } catch (IOException e) {
            return "";
        }
        return output.toString().trim();
    }

}
