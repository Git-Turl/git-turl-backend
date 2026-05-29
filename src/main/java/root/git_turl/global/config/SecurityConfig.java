package root.git_turl.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import root.git_turl.global.security.CustomUserDetailsService;
import root.git_turl.global.security.jwt.JwtAuthFilter;
import root.git_turl.global.security.jwt.JwtUtil;
import root.git_turl.global.security.oauth.CustomOAuthService;
import root.git_turl.global.security.oauth.OAuthSuccessHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final String[] allowUris = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/login/oauth2/**",
            "/oauth2/**",
            "/api/v1/members/*/profile",
            "/api/v1/auth/reissue",
            "/api/v1/auth/logout",
            "/login/**",
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuthService oAuth2UserCustomService, OAuthSuccessHandler oAuth2SuccessHandler, ObjectMapper objectMapper) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(allowUris).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                    {
                                      "code": "LOGIN_REQUIRED",
                                      "message": "로그인이 필요한 서비스입니다."
                                    }
                                    """);
                        }))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 주소 허용
        configuration.addAllowedOrigin("https://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:5173");

        // 허용할 헤더와 메서드
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        // 쿠키(Credential)를 서로 주고받을 수 있게 허용
        configuration.setAllowCredentials(true);

        // 헤더에 Authorization(JWT) 같은 게 있어도 읽을 수 있게 허용
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Set-Cookie");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}