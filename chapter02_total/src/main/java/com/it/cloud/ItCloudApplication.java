package com.it.cloud;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ItCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItCloudApplication.class, args);
    }


    /**
     * 负载均衡的RestTemplate
     *
     * @return
     */
    /*@Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
*/
}
