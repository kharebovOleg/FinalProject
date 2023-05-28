package kharebov.skill.finalproject.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration

@OpenAPIDefinition(info = @Info(
        title = "Bank service api",
        description = "Bank service", version = "1.0.0",
        contact = @Contact(
                name = "Kharebov Oleg",
                email = "xarebov1993@yandex.ru"
        )
))
public class SwaggerConfiguration {

}
