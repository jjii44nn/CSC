package util;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class ArgumentTemplate {

    @Option(name = "-C",aliases = "--checkInfo",usage = "get host info")
    public boolean CheckENV;

    @Option(name = "-sf",aliases = "--sensitivefile",usage = "Sensitive files that may be mounted")
    public String sensitiveFile = "";

    @Option(name = "-E",aliases = "--escape",usage = "Specify escape type")
    public String escape = "";

    @Option(name = "-dip",aliases = "--dockerip",usage = "Specify Docker API IP")
    public String dockerIP = "";

    @Option(name = "-ds",aliases = "--dockersock",usage = "Specify Docker API Sock")
    public String dockerSock = "";

    @Option(name = "-ip",aliases = "-shellip",usage = "attack vps IP and port")
    public String ShellIP = "";

    @Option(name = "-pp",aliases = "--procpath",usage = "Proc mounting path")
    public String procPath = "";

    @Option(name = "-nu",aliases = "--nginxurl",usage = "Specify ingress-nginx api url")
    public String nginxUrl = "";

    @Option(name = "-au",aliases = "--admissionurl",usage = "Specify ingress-nginx admission-webhook url")
    public String admissionUrl = "";

    @Option(name = "-SA",aliases = "--serverapi",usage = "Operation k8s server API")
    public String OptionServerAPI = "";

    @Option(name = "-KA",aliases = "--kubeletapi",usage = "Operation Kubelet API")
    public String OptionKubeletAPI = "";

    @Option(name = "-t",aliases = "--token",usage = "Specify K8S Server Token")
    public String token = "";

    @Option(name = "-ns",aliases = "--namespace",usage = "Specify namespace")
    public String nameSpace = "";

    @Option(name = "-pn",aliases = "--podname",usage = "Specify podName")
    public String podName = "";

    @Option(name = "-cn",aliases = "--containername",usage = "Specify containerName")
    public String containerName = "";

    @Option(name = "-c",aliases = "--cmd",usage = "Command to be executed")
    public String cmd = "";

    @Option(name = "-nn",aliases = "--nodename",usage = "Specify nodename")
    public String nodeName = "";

    @Argument
    public List<String> arguments = new ArrayList<String>();


    public void getArgument(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            if (args.length == 0){
                throw new CmdLineException("no args");
            }
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("CloudSecurityCheck [options...] arguments...");
            parser.printUsage(System.err);
        }
    }

}
