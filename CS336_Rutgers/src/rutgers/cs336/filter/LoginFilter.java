package rutgers.cs336.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//
		HttpSession session = request.getSession(false);
		String loginURI = request.getContextPath() + "/login.jsp";
		String loginURLServlet = request.getContextPath() + "/login";
		//
		ServletContext context = filterConfig.getServletContext();
		//
		boolean isLoggedIn = session != null && session.getAttribute("user") != null;
		boolean isLoginRequest = request.getRequestURI().equals(loginURI);
		boolean isLoginRequestServlet = request.getRequestURI().equals(loginURLServlet);

		if (isLoggedIn || isLoginRequest || isLoginRequestServlet) {
			chain.doFilter(request, response);
		} else {
			response.sendRedirect(loginURI);
		}
	}

	@Override
	public void destroy() {

	}
}
