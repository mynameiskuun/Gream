package com.project.gream.common.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@EntityScan(basePackages = {"com.project.gream.domain.item", "com.project.gream.domain.member", "com.project.gream.domain.order", "com.project.gream.domain.post"})
@Configuration
public class JpaConfiguration {

}
