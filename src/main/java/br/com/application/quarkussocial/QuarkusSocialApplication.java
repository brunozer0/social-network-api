package br.com.application.quarkussocial;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title = "API QUARKUS SOCIAL",
                version= "1.0.1",
                contact = @Contact(
                        name = "Bruno",
                        url = "https://github.com/brunozer0/",
                        email = ""
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
