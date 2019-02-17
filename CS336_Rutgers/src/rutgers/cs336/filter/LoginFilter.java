package rutgers.cs336.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			  throws ServletException, IOException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//
		String loginURI        = request.getContextPath() + "/login.jsp";
		String loginURLServlet = request.getContextPath() + "/login";
		//
		String registerURI        = request.getContextPath() + "/register.jsp";
		String registerURLServlet = request.getContextPath() + "/register";
		//
		String logoutURLServlet = request.getContextPath() + "/logout";
		//
		boolean isLogoutRequest = request.getRequestURI().equals(logoutURLServlet);
		//
		if (isLogoutRequest) {
			HttpSession session = request.getSession(true);
			session.invalidate();
			//
			response.sendRedirect(loginURI);
		}
		else {
			HttpSession session = request.getSession(true);
			//
			boolean isLoggedIn = session != null && session.getAttribute("user") != null;
			//
			boolean isLoginRequest        = request.getRequestURI().equals(loginURI);
			boolean isLoginRequestServlet = request.getRequestURI().equals(loginURLServlet);
			//
			boolean isRegisterRequest        = request.getRequestURI().equals(registerURI);
			boolean isRegisterRequestServlet = request.getRequestURI().equals(registerURLServlet);
			//
			if (isLoggedIn || isLoginRequest || isLoginRequestServlet || isRegisterRequest || isRegisterRequestServlet) {
				chain.doFilter(request, response);
			}
			else {
				response.sendRedirect(loginURI);
			}
		}
	}

	@Override
	public void destroy() {

	}
}
