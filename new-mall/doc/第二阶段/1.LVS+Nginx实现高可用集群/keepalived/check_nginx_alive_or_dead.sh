#!/bin/bash

A=`ps -C nginx --no-header |wc -l`
# 判断nginx是否宕机
if [ $A -eq 0 ];then
    # 这里写自己安装的nginx的目录
    /usr/local/myapp/program/nginx/sbin/nginx
    # 等待3秒再次检查nginx，如果没有启动成功，则停止keepalived，使其启动备用机
    sleep 3
    if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
        # 杀死keepaived进程
        killall keepalived
        # 正常退出
        exit 0
    else
        # 异常退出
        exit 1
    fi
fi