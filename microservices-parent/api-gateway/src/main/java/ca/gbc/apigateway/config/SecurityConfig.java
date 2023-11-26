package ca.gbc.apigateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer_uri;

    @Bean
    public ReactiveJwtDecoder jwtDecoder(){
        return ReactiveJwtDecoders.fromIssuerLocation(issuer_uri);
    }

    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){

        serverHttpSecurity.csrf(csrf -> csrf.disable()
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(configure -> configure.jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))));

        return serverHttpSecurity.build();

    }


}
