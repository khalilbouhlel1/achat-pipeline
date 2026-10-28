package tn.esprit.rh.achat.util;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI achatOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("My STOCK PROJECT")
						.description("Micro-Service Documentation")
						.version("1.0.0"))
				.components(new Components()
						.addSecuritySchemes("Bearer", new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")))
				.addSecurityItem(new SecurityRequirement().addList("Bearer"));
	}

	@Bean
	public GroupedOpenApi achatApi() {
		return GroupedOpenApi.builder()
				.group("achat")
				.packagesToScan("tn.esprit.rh.achat.controllers")
				.build();
	}
}