package org.securityrat.requirementmanagement.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.securityrat.requirementmanagement")
public class FeignConfiguration {

}
