package JOBO;

import java.io.Serializable;
import java.util.List;

public class CurrentHost implements Serializable {

    public CurrentHost() {
    }

    public CurrentHost(boolean docker, boolean k8s, String[] ip, boolean privilege, List<String> cap, String nodeIP, String k8SAPIIP, boolean k8SAPIUnauth, boolean dockerAPIUnauth, boolean kubeletAPIUnauth, boolean containerdSock, String SAToken, String procPath, String dockerSockPath) {
        Docker = docker;
        K8s = k8s;
        this.ip = ip;
        this.privilege = privilege;
        this.cap = cap;
        this.nodeIP = nodeIP;
        K8SAPIIP = k8SAPIIP;
        K8SAPIUnauth = k8SAPIUnauth;
        DockerAPIUnauth = dockerAPIUnauth;
        KubeletAPIUnauth = kubeletAPIUnauth;
        ContainerdSock = containerdSock;
        this.SAToken = SAToken;
        this.procPath = procPath;
        this.dockerSockPath = dockerSockPath;
    }

    private boolean Docker;
    private boolean K8s;
    private String[] ip;

    private boolean privilege;

    private List<String> cap;

    private String nodeIP;
    private String K8SAPIIP;

    private boolean K8SAPIUnauth;

    private boolean DockerAPIUnauth;

    private boolean KubeletAPIUnauth;

    private boolean ContainerdSock;

    private String SAToken;

    private String procPath;

    private String dockerSockPath;




    public String[] getIp() {
        return ip;
    }

    public void setIp(String[] ip) {
        this.ip = ip;
    }

    public boolean isDocker() {
        return Docker;
    }

    public void setDocker(boolean docker) {
        Docker = docker;
    }

    public boolean isK8s() {
        return K8s;
    }

    public void setK8s(boolean k8s) {
        K8s = k8s;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public String getK8SAPIIP() {
        return K8SAPIIP;
    }

    public void setK8SAPIIP(String k8SAPIIP) {
        K8SAPIIP = k8SAPIIP;
    }

    public boolean isK8SAPIUnauth() {
        return K8SAPIUnauth;
    }

    public void setK8SAPIUnauth(boolean k8SAPIUnauth) {
        K8SAPIUnauth = k8SAPIUnauth;
    }

    public boolean isDockerAPIUnauth() {
        return DockerAPIUnauth;
    }

    public void setDockerAPIUnauth(boolean dockerAPIUnauth) {
        DockerAPIUnauth = dockerAPIUnauth;
    }

    public boolean isKubeletAPIUnauth() {
        return KubeletAPIUnauth;
    }

    public void setKubeletAPIUnauth(boolean kubeletAPIUnauth) {
        KubeletAPIUnauth = kubeletAPIUnauth;
    }

    public boolean isContainerdSock() {
        return ContainerdSock;
    }

    public void setContainerdSock(boolean containerdSock) {
        ContainerdSock = containerdSock;
    }

    public String getSAToken() {
        return SAToken;
    }

    public void setSAToken(String SAToken) {
        this.SAToken = SAToken;
    }

    public String getProcPath() {
        return procPath;
    }

    public void setProcPath(String procPath) {
        this.procPath = procPath;
    }

    public String getDockerSockPath() {
        return dockerSockPath;
    }

    public void setDockerSockPath(String dockerSockPath) {
        this.dockerSockPath = dockerSockPath;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public List<String> getCap() {
        return cap;
    }

    public void setCap(List<String> cap) {
        this.cap = cap;
    }
}
