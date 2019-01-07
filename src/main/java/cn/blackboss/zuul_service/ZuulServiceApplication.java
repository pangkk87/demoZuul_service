package cn.blackboss.zuul_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关服务
 * API getaway，	是系统统一对外的入口，介于客户端和服务器之间的中间层，
 * 处理非业务功能，提供路由，鉴权，监控，缓存，限流，日志埋点等功能
 *
 * 统一接入
 * 	 智能路由 以客户端的角色软负载均衡到各个业务服务
 * 	 ab测试，灰度测试  不同的用户展现不同的主题
 * 	 负载均衡（类似ngnix日志）
 * 流量监控
 *   限流处理
 *   服务降级
 * 安全防护
 *   鉴权处理
 *   监控
 *   机器网络隔离
 *
 *
 * 	一般网关以集群部署，前面还会做一层 Nginx + lvs + keepalive 暴露给外网
 */

@SpringBootApplication
@EnableZuulProxy
public class ZuulServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServiceApplication.class, args);
	}

}

