package com.example.netflixclone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.netflixclone.config.auth.PrincipalDetailService;


//아래 3개는 시큐리티 설정하는데 세트라고 생각하면 
@Configuration //빈 등록(IoC 관리)
@EnableWebSecurity //시큐리티 필터가 등록이 됨 => 스프링 시큐리티가 활성화가 되어 있는데 어떤 설정을 해당 파일에서 하겠다는 의미
@EnableGlobalMethodSecurity(prePostEnabled=true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 의미
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean // IoC가 됨 
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}

	// 시큐리티가 대신 로그인해주는데 password를 가로채기 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교함
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.ignoringAntMatchers("/h2-console/**")
				.disable() // csrf 토큰 비활성화(테스트 시 걸어두는게 좋음)
			.authorizeRequests()
				.antMatchers("/", "/h2-console/**", "/auth/**", "/js/**", "/css/**", "/image/**") // 해당 페이지는 인증 완료
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.usernameParameter("email") 
				.passwordParameter("password")
				.loginPage("/auth/loginForm") // 인증이 되지 않은 로그인폼은 loginForm으로 
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인 정보를 가로채서 대신 로그인 해줌
				.defaultSuccessUrl("/");
//				.failureUrl("/fail") // 실패하면 해당 url로 이동
	}
}
