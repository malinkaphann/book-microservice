/**
 * This is the spring security config.
 *
 * @author Phann Malinka
 */
package book.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import book.filters.LogFilter;
import book.utils.RoleEnum;

@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true
  // jsr250Enabled = true
)
public class SecurityConfig {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtAuthEntryPoint unauthorizedHandler;

  @Autowired
  private JwtTokenFilter jwtTokenFilter;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(
      (org.springframework.security.core.userdetails.UserDetailsService) userDetailsService
    );
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler(){
    return new CustomAccessDeniedHandler();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors()
      .and()
      .csrf()
      .disable()
      .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
      .authenticationEntryPoint(unauthorizedHandler)
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      
      // public access
      .authorizeRequests()
      .antMatchers("/actuator/**")
      .permitAll()
      .antMatchers("/api/v1/auth/**")
      .permitAll()
      
      // search for book and get book detail are allowed to any role
      .antMatchers(HttpMethod.GET, "/api/v1/book/**")
      .hasAnyRole(
        RoleEnum.ADMIN.name(),
        RoleEnum.LIBRARIAN.name(),
        RoleEnum.PROFESSOR.name(),
        RoleEnum.STUDENT.name(),
        RoleEnum.API_CLIENT.name()
      )

      // only admin and librarian can add or edit book detail
      .antMatchers(HttpMethod.POST, "/api/v1/book/**")
      .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.LIBRARIAN.name())
      .antMatchers(HttpMethod.PUT, "/api/v1/book/**")
      .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.LIBRARIAN.name())

      // only librarian can hold or unhold
      .antMatchers(HttpMethod.POST, "/api/v1/book/hold/**")
      .hasAnyRole(RoleEnum.LIBRARIAN.name())
      .antMatchers(HttpMethod.DELETE, "/api/v1/book/hold/**")
      .hasAnyRole(RoleEnum.LIBRARIAN.name())
      
      // allow any logged-in user to get his or her profile
      .antMatchers(HttpMethod.GET, "/api/v1/auth/profile")
      .authenticated()
      
      // allow admin to
      .antMatchers("/api/v1/user/**")
      .hasRole(RoleEnum.ADMIN.name())
      
      // turn off some urls
      .antMatchers("/api/v1/role/**")
      .denyAll()
      .anyRequest()
      .authenticated();

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(
      jwtTokenFilter,
      UsernamePasswordAuthenticationFilter.class
    );

    return http.build();
  }
}
