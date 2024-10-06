package net.astrostreak.site.configurations;

import io.github.wimdeblauwe.htmx.spring.boot.security.HxRefreshHeaderAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(c -> c.loginPage("/login").permitAll());

        var entryPoint = new HxRefreshHeaderAuthenticationEntryPoint();
        var requestMatcher = new RequestHeaderRequestMatcher("HX-Request");
        http.exceptionHandling(exception ->
                exception.defaultAuthenticationEntryPointFor(entryPoint, requestMatcher));

        http.authorizeHttpRequests(
                customizer -> customizer
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/account").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll()
        );

        http.csrf(customizer -> customizer.ignoringRequestMatchers("/api/**"));

        return http.build();
    }
}
