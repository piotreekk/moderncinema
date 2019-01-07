package pl.piotrek.cinemabackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .jdbcAuthentication()
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .dataSource(dataSource)
                    .usersByUsernameQuery("SELECT email, password, enabled FROM users WHERE email=?")
                    .authoritiesByUsernameQuery("SELECT email, role FROM users WHERE email=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                    .antMatchers("/user/adduser/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}