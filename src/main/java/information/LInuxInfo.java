package information;

import util.CmdExeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LInuxInfo {

    public static Map<String,String[]> getMounts(boolean ALL){
        String mount = CmdExeUtil.execute("df -aT 2>/dev/null");
        HashMap<String,String[]> mounts = new HashMap<>();
        String[] mountArray = mount.split("\n");
        for (String item:mountArray) {
            String str = item.replaceAll("\\s+", " ");
            String[] arr = str.split(" ");
            if (arr.length >= 8 ){
                int i = arr.length - 7;
                for (int j = i+6; j > 6 ; j--) {
                    arr[j-1] = arr[j-1] + arr[j];
                }

            }
            if (Objects.equals(arr[0], "proc") && !arr[6].startsWith("/proc")){
                mounts.put(arr[6],arr);
                continue;
            }
            if (ALL){
                if (!Objects.equals(arr[3], "0") && !Objects.equals(arr[3], "Used") && !Objects.equals(arr[6], "/") && !arr[6].endsWith("挂载点")){
                    mounts.put(arr[6],arr);
                }
            }else {
                if (!Objects.equals(arr[3], "0") && !Objects.equals(arr[3], "Used") && !Objects.equals(arr[6], "/") && !arr[6].endsWith("/hosts") && !arr[6].endsWith("/resolv.conf") && !arr[6].endsWith("/hostname") && !arr[6].endsWith("/termination-log") && !arr[6].endsWith("挂载点")){
                    mounts.put(arr[6],arr);
                }
            }

        }
        return mounts;
    }

    public static String getGatewayIP(){
        try (BufferedReader br = new BufferedReader(new FileReader("/proc/self/net/route"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length > 2 && "00000000".equals(parts[1])) {
                    String gatewayHex = parts[2];
                    StringBuilder ip = new StringBuilder();
                    for (int i = gatewayHex.length(); i >= 2; i -= 2) {
                        String octet = gatewayHex.substring(i - 2, i);
                        ip.append(Integer.parseInt(octet, 16));
                        if (i > 2) ip.append(".");
                    }
                    String gateway = ip.toString();
                    return gateway;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<String> getSensitiveFile(Set<String> mounts, String sensitiveFile){
        if (Objects.equals(sensitiveFile, "")){
            sensitiveFile = "/var/run/docker.sock";
        }
        String[] files = sensitiveFile.split(",");
        ArrayList<String> filePath = new ArrayList<>();
        for (String mount : mounts) {
            for (String file : files) {
                while (file.contains("/")){
                    file = file.substring(file.indexOf("/")+1);
                    if (new File(mount + "/" + file).exists()) {
                        filePath.add(mount + "/" + file);
                        break;
                    }
                }
            }
        }
        return filePath;

    }

}
