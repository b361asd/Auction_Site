package rutgers.cs336.filter;

import rutgers.cs336.servlet.IConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter, IConstant {

	public static final String USER_PATH  = "user";
	public static final String ADMIN_PATH = "admin";
	public static final String REP_PATH   = "rep";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			  throws ServletException, IOException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//
		request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "filter!!!");
		//
		HttpSession session = request.getSession(true);
		session.setAttribute(SESSION_ATTRIBUTE_MESSAGE, null);
		//
		String loginURL = request.getContextPath() + "/login.jsp";
		//
		String  logoutURLServlet = request.getContextPath() + "/logout";
		boolean isLogoutRequest  = request.getRequestURI().equals(logoutURLServlet);
		if (isLogoutRequest) {
			session.invalidate();
			//
			request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "redirect to login 0");
			response.sendRedirect(loginURL);
		}
		else {
			boolean isLoggedIn = session.getAttribute(SESSION_ATTRIBUTE_USER) != null;
			//
			String homeURL = request.getContextPath() + "/home";
			//
			if (isLoggedIn) {                     // Already Login
				String szUserType = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
				szUserType = szUserType == null ? "" : szUserType;
				//
				boolean isAdminURL = request.getRequestURI().startsWith(request.getContextPath() + "/" + ADMIN_PATH);
				boolean isRepURL   = request.getRequestURI().startsWith(request.getContextPath() + "/" + REP_PATH);
				boolean isUserURL  = request.getRequestURI().startsWith(request.getContextPath() + "/" + USER_PATH);
				//
				if (szUserType.equalsIgnoreCase("1") && isAdminURL) {
					chain.doFilter(request, response);
				}
				else if (szUserType.equalsIgnoreCase("2") && isRepURL) {
					chain.doFilter(request, response);
				}
				else if (szUserType.equalsIgnoreCase("3") && isUserURL) {
					chain.doFilter(request, response);
				}
				else {                           // Including login.jsp
					boolean isHomeRequest = request.getRequestURI().equals(homeURL);
					boolean isCSSRequest  = request.getRequestURI().toLowerCase().endsWith(".css");
					if (isHomeRequest || isCSSRequest) {
						chain.doFilter(request, response);
					}
					else {
						request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "home 0. " + "HomeURL: " + homeURL + "-" + request.getRequestURI());
						response.sendRedirect(homeURL);
					}
				}
			}
			else {
				String registerURI        = request.getContextPath() + "/register.jsp";
				String registerURLServlet = request.getContextPath() + "/register";
				//
				boolean isLoginRequest           = request.getRequestURI().equals(loginURL);
				boolean isHomeRequest            = request.getRequestURI().equals(homeURL);
				boolean isRegisterRequest        = request.getRequestURI().equals(registerURI);
				boolean isRegisterRequestServlet = request.getRequestURI().equals(registerURLServlet);
				//
				if (isLoginRequest || isHomeRequest || isRegisterRequest || isRegisterRequestServlet) {        //want to login, want to register
					chain.doFilter(request, response);
				}
				else {
					request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "redirect to login 1");
					response.sendRedirect(loginURL);
				}
			}
		}
	}

	@Override
	public void destroy() {
	}
}
