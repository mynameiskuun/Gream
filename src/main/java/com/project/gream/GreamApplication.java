package com.project.gream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Auditing을 위한 설정값. for BaseEntity
@SpringBootApplication
        (
                exclude = {
                        org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
                        org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
                        org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
                } // spring-cloud-starter-aws 의존성 주입시 로컬환경은 aws환경이 아니기때문에, Failed to connect to service endpoint: 에러 발생.
                // 이를 방지하기 위한 설정값.
        )
public class GreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreamApplication.class, args);
    }

}
