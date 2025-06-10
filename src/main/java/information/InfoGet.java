package information;

import JOBO.CurrentHost;
import kong.unirest.HttpResponse;
import util.CmdExeUtil;
import util.HttpUtil;

import java.io.IOException;
import java.util.*;

public class InfoGet {

    public static void isDockerOrK8S(CurrentHost currentHost) throws IOException {
        if (DockerCheck.checkDocker()) {
            currentHost.setDocker(true);
            System.out.println("Docker!!");
            if (K8SCheck.CheckK8S()) {
                currentHost.setK8s(true);
                System.out.println("K8S!!");
            }
        }else {
            currentHost.setDocker(false);
            currentHost.setK8s(false);
            System.out.println("NO Docker");
        }
    }

    public static void getSyetemInfo(CurrentHost currentHost) throws IOException {
        String user = CmdExeUtil.execute("whoami");
        String id = CmdExeUtil.execute("id");
        String kernel = CmdExeUtil.execute("uname -a");
        String[] ip = CmdExeUtil.execute("hostname -I").split(" ");
        String pwd = CmdExeUtil.execute("pwd");
        System.out.println("-------------------Systeminfo-------------------");
        System.out.println("current user: " + user);
        System.out.println("current id " + id);
        System.out.println("current dir: " + pwd);
        System.out.println("kernel: " + kernel);
        System.out.println("ip: " + Arrays.toString(ip));
        currentHost.setIp(ip);
    }

    public static void getMounts(String sensitiveFileName) throws IOException {
        Map<String, String[]> mounts = LInuxInfo.getMounts(false);
        ArrayList<String> dirArrRm = new ArrayList<>();
        for (String dir:mounts.keySet()) {
            for (String dir1:mounts.keySet()) {
                if (Objects.equals(dir, dir1)){
                    continue;
                }
                if (dir1.contains(dir)){
                    dirArrRm.add(dir1);
                }
            }
        }
        for (String dirRm:dirArrRm) {
            mounts.remove(dirRm);
        }
        System.out.println("-------------------Mountinfo-------------------");
        for (String dir:mounts.keySet()) {
            String execute = CmdExeUtil.execute("mount | grep '" + dir + " '");
            String flag = execute.substring(execute.indexOf("("));
            System.out.println("DIR: " + dir + " DEVICE: " + mounts.get(dir)[0] + " TYPE: " + mounts.get(dir)[1] + " " + flag);
        }
        List<String> sensitiveFile = LInuxInfo.getSensitiveFile(mounts.keySet(), sensitiveFileName);
        if (!sensitiveFile.isEmpty()){
            for (String s : sensitiveFile) {
                System.out.println("Found SensitiveFile: " + s);
            }
        }
    }


    public static void getCap(CurrentHost currentHost){
        String cap = CmdExeUtil.execute("grep CapEff /proc/self/status");
        String capHex = cap.split(":")[1].trim();
        long capDecimal = Long.parseLong(capHex.replaceFirst("0+", ""), 16);
        String capBinary = Long.toBinaryString(capDecimal);
        char[] charArr = new StringBuilder(capBinary).reverse().toString().toCharArray();
        System.out.println("-------------------Capabilitiesinfo-------------------");
        System.out.println(cap);
        System.out.println("Brnary: " + capBinary);
        if (capHex.equals("000003fffffffff") || capHex.equals("000001fffffffff")){
            currentHost.setPrivilege(true);
            System.out.println("cap: Privileged Container!!!");
        }
        List<String> caplist = new ArrayList<>();
        if (charArr[21] == '1'){
            caplist.add("cap_sys_admin");
            System.out.println("cap: cap_sys_admin");
        }
        if (charArr[2] == '1'){
            caplist.add("cap_dac_read_search");
            System.out.println("cap: cap_dac_read_search");
        }
        if (charArr[16] == '1'){
            caplist.add("cap_sys_module");
            System.out.println("cap: cap_sys_module");
        }
        if (charArr[19] == '1'){
            caplist.add("cap_sys_ptrace");
            System.out.println("cap: cap_sys_ptrace");
        }
        currentHost.setCap(caplist);

    }


    public static void getNetInfo(CurrentHost currentHost){
        System.out.println("-------------------Netinfo-------------------");
        String gatewayIP = LInuxInfo.getGatewayIP();
        currentHost.setNodeIP(gatewayIP);
        System.out.println("Gateway IP: " + gatewayIP);
        if (currentHost.isK8s()){
            String executeK8sAPI = CmdExeUtil.execute("echo $KUBERNETES_PORT").replaceAll("tcp","https");
            System.out.println("K8S API SERVER: " + executeK8sAPI);
            currentHost.setK8SAPIIP(executeK8sAPI);
            boolean isK8SAPI = K8SCheck.checkK8SAPI(executeK8sAPI,currentHost.getSAToken());
            if (isK8SAPI){
                currentHost.setK8SAPIUnauth(true);
                System.out.println("K8S API server is Unauthorized!!!");
            }else{
                currentHost.setK8SAPIUnauth(false);
                System.out.println("K8S API cannot be connected");
            }

            boolean isKubeletAPI = K8SCheck.checkKubeletAPI(gatewayIP, currentHost.getSAToken());
            if (isKubeletAPI){
                currentHost.setKubeletAPIUnauth(true);
                System.out.println("Kubelet is Unauthorized!!!");
            }else {
                currentHost.setKubeletAPIUnauth(false);
                System.out.println("Kubelet API cannot be connected");
            }
        }
        boolean isDockerAPI = DockerCheck.checkDockerAPI(gatewayIP);
        if (isDockerAPI){
            currentHost.setDockerAPIUnauth(true);
            System.out.println("Docker API is Unauthorized!!!");
        }else {
            currentHost.setDockerAPIUnauth(false);
            System.out.println("Docker API cannot be connected");
        }
        getNetNamespace(currentHost);

    }

    public static void getNetNamespace(CurrentHost currentHost){
        String execute = CmdExeUtil.execute("cat /proc/net/unix | grep 'containerd-shim' | grep '@'");
        if (!execute.isEmpty()){
            currentHost.setContainerdSock(true);
            System.out.println("Containerd Socket Found!!!");
            System.out.println(execute);
        }else {
            currentHost.setContainerdSock(false);
            System.out.println("No Share Net Namespace");
        }
    }

    public static void getSAToken(CurrentHost currentHost) throws IOException {
        String token = K8SCheck.getToken();
        currentHost.setSAToken(token);
        System.out.println(token);
    }

    public static void getCloudMetadata(){
        System.out.println("-------------------Metadatainfo-------------------");
        HttpResponse<String> AliResponse = null;
        HttpResponse<String> TencentResponse = null;
        HttpResponse<String> HCSResponse = null;
        try {
            AliResponse = HttpUtil.getString("http://100.100.100.200/latest/meta-data/", null);
            if (AliResponse.getBody() != null){
                System.out.println("Found AliCloud Metadata!!! : http://100.100.100.200/latest/meta-data/");
            }
        }catch (Exception e){
            System.out.println("no AliCloud");
        }
        try {
            TencentResponse = HttpUtil.getString("http://metadata.tencentyun.com/latest/meta-data/", null);
            if (TencentResponse.getBody() != null){
                System.out.println("Found TencentCloud Metadata!!! : http://metadata.tencentyun.com/latest/meta-data/");
            }
        }catch (Exception e){
            System.out.println("no TencentCloud");
        }
        try {
            HCSResponse = HttpUtil.getString("http://169.254.169.254/openstack/latest/meta_data.json", null);
            if (HCSResponse.getBody() != null){
                System.out.println("Found HuaweiCloud Metadata!!! : http://169.254.169.254/openstack/latest/meta_data.json");
            }
        }catch (Exception e){
            System.out.println("no HuaweiCloud");
        }
    }

}
