package auction.filter;

import auction.servlet.IConstant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
// import javax.servlet.Filter;
// import javax.servlet.FilterChain;
// import javax.servlet.FilterConfig;
// import javax.servlet.ServletException;
// import javax.servlet.ServletRequest;
// import javax.servlet.ServletResponse;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

/**
 * Used for authorization of an account.
 *
 * <p>Ex: User cannot access admin page. Rep cannot access user page.
 */
public class LoginFilter implements Filter, IConstant {

    public static final String USER_PATH = "user";
    public static final String ADMIN_PATH = "admin";
    public static final String REP_PATH = "rep";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);
        //
        String loginURL = request.getContextPath() + "/login.jsp";
        String homeURL = request.getContextPath() + "/home";
        String registerURI = request.getContextPath() + "/register.jsp";
        String logoutURL = request.getContextPath() + "/logout";
        //
        boolean isLogoutRequest = request.getRequestURI().equals(logoutURL);
        boolean isCSSRequest = request.getRequestURI().toLowerCase().contains("style");
        if (isLogoutRequest) {
            session.invalidate();
            //
            response.sendRedirect(loginURL);
        } else if (isCSSRequest) {
            chain.doFilter(request, response);
        } else {
            boolean isLoggedIn = session.getAttribute(SESSION_ATTRIBUTE_USER) != null;
            // Already Login
            if (isLoggedIn) {
                String szUserType = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
                szUserType = szUserType == null ? "" : szUserType;
                //
                boolean isAdminURL =
                        request.getRequestURI()
                                .startsWith(request.getContextPath() + "/" + ADMIN_PATH);
                boolean isRepURL =
                        request.getRequestURI()
                                .startsWith(request.getContextPath() + "/" + REP_PATH);
                boolean isUserURL =
                        request.getRequestURI()
                                .startsWith(request.getContextPath() + "/" + USER_PATH);
                //
                if (szUserType.equalsIgnoreCase("1") && isAdminURL) {
                    chain.doFilter(request, response);
                } else if (szUserType.equalsIgnoreCase("2") && isRepURL) {
                    chain.doFilter(request, response);
                } else if (szUserType.equalsIgnoreCase("3") && isUserURL) {
                    chain.doFilter(request, response);
                } else {
                    // Including login.jsp
                    boolean isHomeRequest = request.getRequestURI().equals(homeURL);
                    if (isHomeRequest) {
                        chain.doFilter(request, response);
                    } else {
                        response.sendRedirect(homeURL);
                    }
                }
            } else {
                boolean isLoginRequest = request.getRequestURI().equals(loginURL);
                boolean isHomeRequest = request.getRequestURI().equals(homeURL);
                boolean isRegisterRequest = request.getRequestURI().equals(registerURI);
                //
                // Want to login, want to register
                if (isLoginRequest || isHomeRequest || isRegisterRequest) {
                    chain.doFilter(request, response);
                } else {
                    response.sendRedirect(loginURL);
                }
            }
        }
    }

    @Override
    public void destroy() {}
}
