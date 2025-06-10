package information;

import kong.unirest.HttpResponse;
import kong.unirest.json.JSONObject;
import util.CmdExeUtil;
import util.HttpUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class K8SCheck {


    public static boolean CheckK8S() throws IOException {
        String executeResult1 = CmdExeUtil.execute("env | grep KUBERNETES");
        String executeResult2 = CmdExeUtil.execute("mount | grep kube");
        String executeResult3 = CmdExeUtil.execute("cat /proc/1/cgroup");
        return (executeResult1.contains("KUBERNETES_") || (executeResult2.contains("kubernetes") || executeResult2.contains("kubelet")) || executeResult3.contains("/kubepods"));
    }

    public static String getToken() throws IOException {
        File file = new File("/var/run/secrets/kubernetes.io/serviceaccount/token");
        if (!file.exists()){
            Map<String, String[]> mounts = LInuxInfo.getMounts(false);
            for (String dir:mounts.keySet()) {
                if(dir.endsWith("/kubernetes.io/serviceaccount")){
                    file = new File(dir + "/token");
                    break;
                }
            }
            if (!file.exists()){
                return "";
            }
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder token = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            token.append(line).append("\n");
        }
        reader.close();
        return token.toString();
    }

    public static boolean checkK8SAPI(String ip,String token){
        JSONObject json = null;
        try {
            if (Objects.equals(token, "") || token.isEmpty()){
                json = HttpUtil.getJson(ip, null);
                System.out.println("No token was used");
            }else {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer " + token);
                json = HttpUtil.getJson(ip, map);
                System.out.println("Token was used");
            }

        }catch (Exception e){
            return false;
        }
        if (json != null && json.has("paths")) {
            return true;
        }
        return false;
    }

    public static boolean checkKubeletAPI(String ip,String token){
        HttpResponse<String> response = null;
        try {
            if (Objects.equals(token, "") || token.isEmpty()){
                response = HttpUtil.getString("https://" + ip + ":10250/runningpods/", null);
                System.out.println("No token was used");
            }else {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer " + token);
                response = HttpUtil.getString("https://" + ip + ":10250/runningpods/", map);
                System.out.println("Token was used");
            }
        }catch (Exception e){
            return false;
        }
        if (response != null && response.getBody().contains("kind") && response.getBody().contains("PodList") && response.getBody().contains("metadata")) {
            return true;
        }
        return false;
    }
}
