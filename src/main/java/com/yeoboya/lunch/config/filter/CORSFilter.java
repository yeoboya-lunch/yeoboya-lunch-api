package com.yeoboya.lunch.config.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		// "Access-Control-Allow-Origin" 헤더로 CORS 요청이 허용될 오리진을 설정합니다. 이 예에서는 "http://localhost:3000"으로 설정되어 있습니다.
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		// "Access-Control-Allow-Methods" 헤더로 허용될 메소드 종류를 설정합니다. 이 예제에서는 "POST, GET, OPTIONS, PATCH, DELETE" 메소드가 허용되어 있습니다.
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PATCH, DELETE");
		// "Access-Control-Max-Age" 헤더로 preflight 요청의 결과를 캐시할 시간을 초 단위로 설정합니다. 이 예에서는 3600초 (1시간)로 설정되어 있습니다.
		response.setHeader("Access-Control-Max-Age", "3600");
		// "Access-Control-Allow-Credentials" 헤더는 자격 증명을 포함한 요청이 허용되는지를 설정합니다. 이 예에서는 "true"로 설정되어 있습니다.
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// "Access-Control-Allow-Headers" 헤더는 실제 요청에서 사용할 수 있는 HTTP 헤더를 설정합니다. 이 예에서는 "x-requested-with, Content-Type, Authorization" 헤더가 허용되어 있습니다.
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Authorization");
		// chain.doFilter(req, res)는 다음 필터로 요청과 응답을 전달합니다. 이 작업은 여러 필터를 체인으로 연결할 수 있게 해줍니다.
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

}
