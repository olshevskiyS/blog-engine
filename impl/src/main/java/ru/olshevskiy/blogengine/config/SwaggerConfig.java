package ru.olshevskiy.blogengine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig.
 *
 * @author Sergey Olshevskiy
 */
@Configuration
public class SwaggerConfig {

  @Value("${swagger.path}")
  private String swaggerPath;

  @Value("${swagger.schema-title}")
  private String swaggerSchemaTitle;

  @Value("${swagger.schema-version}")
  private String swaggerSchemaVersion;

  @Value("${swagger.schema-description}")
  private String swaggerSchemaDescription;

  /**
   * SwaggerConfig. API schema properties.
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI().info(new Info()
                        .title(swaggerSchemaTitle)
                        .version(swaggerSchemaVersion)
                        .description(swaggerSchemaDescription)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                        .servers(List.of(new Server().url(swaggerPath)));
  }
}
