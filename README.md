# CSC
用于云环境安全检查的小工具
## 免责声明

本程序应仅用于学习与研究使用。

## 介绍

CloudSecurityScanner是一款基于Java开发的专用于云环境检测的安全工具，可以在容器中完成逃逸，横向移动等操作，用于云环境（Docker/K8s）的安全配置审计与渗透测试

## 使用方法

```
 -C (--checkInfo)          : get host info
 -E (--escape) VAL         : Specify escape type(dockerapi/privilege/cap_sysadmin/procfs/ingress)
 -KA (--kubeletapi) VAL    : Operation Kubelet API(目前未支持)
 -SA (--serverapi) VAL     : Operation k8s server API(getpod/getpod/cmd/shell/ )
 -au (--admissionurl) VAL  : Specify ingress-nginx admission-webhook url(指定ingress-nginx的admission-webhook地址，用于发送配置)
 -c (--cmd) VAL            : Command to be executed (指定需要执行的命令)
 -cn (--containername) VAL : Specify containerName(指定容器名称)
 -dip (--dockerip) VAL     : Specify Docker API IP(指定Docker API的IP，默认端口为2375)
 -ds (--dockersock) VAL    : Specify Docker API Sock(指定Docker sock挂载的绝对路径)
 -ip (-shellip) VAL        : attack vps IP and port(指定反弹shell的地址，eg：ip:port)
 -nn (--nodename) VAL      : Specify nodename(指定节点名称)
 -ns (--namespace) VAL     : Specify namespace(指定命名空间)
 -nu (--nginxurl) VAL      : Specify ingress-nginx api url(指定ingress-nginx api的地址，用于发送恶意动态链接库)
 -pn (--podname) VAL       : Specify podName(指定pod)
 -pp (--procpath) VAL      : Proc mounting path(指定挂载的proc目录)
 -sf (--sensitivefile) VAL : Sensitive files that may be mounted(需要探测的挂载文件路径，默认只有Docker sock路径)
 -t (--token) VAL          : Specify K8S Server Token(指定K8sAPI的token，用于调用server API)
```

## 功能

|   功能   | 具体描述                                                     | 参数使用            |
| :------: | ------------------------------------------------------------ | ------------------- |
| 信息收集 | 判断当前运行环境，获取环境基本信息，挂载信息，Cap信息，网络信息，公有云元数据信息 | -C -sf ...          |
| 容器逃逸 | 使用docker api或者docker.sock挂载实现逃逸，通过构建高权限容器并获取宿主机shell | -E dockerapi ...    |
| 容器逃逸 | 针对特权容器的逃逸，挂载宿主机高危路径，获取宿主机shell      | -E privilege ...    |
| 容器逃逸 | 针对sysadmin能力容器进行逃逸，获取宿主机shell                | -E cap_sysadmin ... |
| 容器逃逸 | 针对proc目录挂载容器的逃逸，获取宿主机shell                  | -E procfs ...       |
| 容器逃逸 | 对Ingress Nginx（CVE-2025-1974）的横向移动，获取Ingress Nginx容器shell | -E ingress ...      |
| api控制  | 针对K8sAPI执行操作，获取pod信息                              | -SA getpod ...      |
| api控制  | 针对K8sAPI执行操作，获取node信息                             | -SA getnode ...     |
| api控制  | 针对K8sAPI执行操作，在特定容器中执行命令                     | -SA cmd ...         |
| api控制  | 针对K8sAPI执行操作，获取特定节点shell                        | -SA shell ...       |

## 后续计划

- 优化bug
- 新增kubelet API操作
- 脱离JDK依赖
