package information;

import kong.unirest.HttpResponse;
import util.CmdExeUtil;
import util.HttpUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DockerCheck {

    private static final String filePath = "/.dockerenv";

    public static boolean checkDocker() throws IOException {
        File dockerfile = new File(filePath);
        String executeResult1 = CmdExeUtil.execute("mount | grep docker");
        String executeResult2 = CmdExeUtil.execute("cat /proc/self/cgroup");
        String executeResult3 = CmdExeUtil.execute("cat /proc/1/cgroup");
        return (dockerfile.exists() || executeResult1.contains("/var/lib/docker/") || executeResult2.contains("/docker") || executeResult3.contains("/docker"));
    }

    public static boolean checkDockerAPI(String ip){
        HttpResponse<String> response = null;
        try {
            response = HttpUtil.getString("http://" + ip + ":2375/version", null);
        }catch (Exception e){
            return false;
        }
        if (response != null && response.getBody().contains("Platform") && response.getBody().contains("Components") && response.getBody().contains("Version")) {
            return true;
        }
        return false;
    }


}
