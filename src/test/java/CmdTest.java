import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CmdTest {

    public static String execute(String cmd) {
//        String[] cmds = cmd.split(" ");
//        Process process = Runtime.getRuntime().exec(cmds);
//        InputStream inputStream = process.getInputStream();
//        byte[] buffer = new byte[1024];
//        StringBuffer s = new StringBuffer();
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            s.append(new String(buffer));
//        }
//        try {
//            process.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return s.toString();
        CommandLine cmdline = CommandLine.parse("cmd");
        cmdline.addArgument("/c");
        cmdline.addArgument(cmd, false);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(output);
        DefaultExecutor executor = new DefaultExecutor();

        executor.setStreamHandler(streamHandler);
        try {
            int exitCode = executor.execute(cmdline);
        } catch (IOException e) {
            System.out.println(e);
        }
        return output.toString().trim();
    }
}
