import util.ArgumentTemplate;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {
//        CurrentHost currentHost = new CurrentHost();
//        currentHost.setKubeletAPIUnauth(true);
//        JSONObject jsonObject = new JSONObject("{\"kind\":\"PodList\",\"apiVersion\":\"v1\",\"metadata\":{},\"items\":[{\"metadata\":{\"name\":\"nginx-7cd4f6d4f5-2k4p8\",\"namespace\":\"default\",\"uid\":\"a1b2c3d4-5678-90ef-ghij-klmnopqrstuv\",\"labels\":{\"app\":\"nginx\"}},\"spec\":{\"containers\":[{\"name\":\"nginx\",\"image\":\"nginx:1.19\",\"volumeMounts\":[{\"name\":\"config-volume\",\"mountPath\":\"/etc/nginx\"}]}],\"volumes\":[{\"name\":\"config-volume\",\"configMap\":{\"name\":\"nginx-config\"}}],\"nodeName\":\"node-01\",\"restartPolicy\":\"Always\"},\"status\":{\"phase\":\"Running\",\"conditions\":[{\"type\":\"Ready\",\"status\":\"True\"}],\"hostIP\":\"192.168.1.100\",\"podIP\":\"10.244.0.5\",\"startTime\":\"2023-10-01T12:34:56Z\",\"containerStatuses\":[{\"name\":\"nginx\",\"state\":{\"running\":{\"startedAt\":\"2023-10-01T12:35:00Z\"}},\"lastState\":{},\"ready\":true,\"restartCount\":0,\"image\":\"nginx:1.19\",\"imageID\":\"docker-pullable://nginx@sha256:abc123...\",\"containerID\":\"docker://a1b2c3d4e5f6...\"}]}}]}\n");
//        KubeletGetInfo.getPods(currentHost,false,jsonObject);

        ArgumentTemplate argumentTemplate = new ArgumentTemplate();
        argumentTemplate.getArgument(args);
        boolean empty = argumentTemplate.escape.isEmpty();
        System.out.println(empty);
    }

}
