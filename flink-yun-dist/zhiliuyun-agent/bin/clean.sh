#!/bin/bash

######################
# 清理日志脚本
######################

# hdfs 78% -> 48%
rm -rf /tmp/hadoop-zhiliuyun/nm-local-dir/usercache/zhiliuyun/filecache

# k8s 34% -> 34%
kubectl delete --all pods --namespace=zhiliuyun-space

# docker 34% -> 34%
docker ps -a | grep 'k8s_POD_zhiliuyun-job-*' | awk '{print $1}' | xargs docker rm