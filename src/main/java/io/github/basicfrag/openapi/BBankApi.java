package io.github.basicfrag.openapi;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(info = @Info(description = "API que simula operações bancárias através de alguns endpoints e seus repectivos verbos HTTP", title = "BBank API",version = "1.0"))
public class BBankApi extends Application {
}
