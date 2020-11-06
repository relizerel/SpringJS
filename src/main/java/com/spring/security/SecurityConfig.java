package com.spring.security;


import com.spring.handler.LoginSuccessHandler;
import com.spring.model.User;
import com.spring.service.interfaces.RoleService;
import com.spring.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProvider authProvider;
    private final RoleService roleService;

    @Autowired
    public SecurityConfig(AuthProvider authProvider, RoleService roleService) {
        this.authProvider = authProvider;
        this.roleService = roleService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .antMatchers("/reg/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .and()
                .addFilterAfter(oAuth2ClientAuthenticationProcessingFilter(),
                        BasicAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserService userService) {
        return map -> {
            String email = (String) map.get("email");
            User user = userService.findUserByEmail(email);
            if (user == null) {
                User newUser = new User();
                newUser.setName((String) map.get("name"));
                newUser.setPassword("12345");
                newUser.setEmail((String) map.get("email"));
                newUser.getRoleSet().add(roleService.getDefaultRole());
                userService.addUser(newUser);
                return newUser;
            } else {
                return user;
            }
        };
    }

    private OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter() {
        OAuth2SsoProperties sso = this.getApplicationContext().getBean(OAuth2SsoProperties.class);
        OAuth2RestOperations restTemplate = (this.getApplicationContext().getBean(UserInfoRestTemplateFactory.class)).getUserInfoRestTemplate();
        ResourceServerTokenServices tokenServices = this.getApplicationContext().getBean(ResourceServerTokenServices.class);
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(sso.getLoginPath());
        filter.setRestTemplate(restTemplate);
        filter.setTokenServices(tokenServices);
        filter.setApplicationEventPublisher(this.getApplicationContext());
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
