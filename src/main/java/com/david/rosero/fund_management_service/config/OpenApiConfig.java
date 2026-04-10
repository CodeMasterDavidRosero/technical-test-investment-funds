package com.david.rosero.fund_management_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fundManagementApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fund Management API")
                        .description("Technical test API for fund subscriptions, cancellations, and transaction history.")
                        .version("v1")
                        .contact(new Contact().name("Technical Test").email("noreply@example.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
