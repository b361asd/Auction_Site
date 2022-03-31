package com.b361asd.auction.filter;

import com.b361asd.auction.gui.UserType;
import com.b361asd.auction.servlet.IConstant;
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
import java.util.Objects;
import java.util.Optional;

/**
 * Used for authorization of an account.
 *
 * <p>Ex: User cannot access admin page. Rep cannot access user page.
 */
public class LoginFilter implements Filter, IConstant {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);

        String loginURL = request.getContextPath() + "/login.jsp";
        String homeURL = request.getContextPath() + "/home";
        String registerURI = request.getContextPath() + "/register.jsp";
        String logoutURL = request.getContextPath() + "/logout";

        boolean isLogoutRequest = request.getRequestURI().equals(logoutURL);
        boolean isCSSRequest = request.getRequestURI().toLowerCase().contains("style");
        if (isLogoutRequest) {
            session.invalidate();
            response.sendRedirect(loginURL);
        } else if (isCSSRequest) {
            chain.doFilter(request, response);
        } else {
            Optional<Object> isLoggedIn =
                    Optional.ofNullable(session.getAttribute(SESSION_ATTRIBUTE_USER));
            if (isLoggedIn.isPresent()) {
                String szUserType =
                        Objects.requireNonNullElse(
                                (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE), "");
                boolean isAdminURL =
                        request.getRequestURI()
                                .startsWith(
                                        request.getContextPath()
                                                + "/"
                                                + UserType.ADMIN.toString().toLowerCase());
                boolean isRepURL =
                        request.getRequestURI()
                                .startsWith(
                                        request.getContextPath()
                                                + "/"
                                                + UserType.REP.toString().toLowerCase());
                boolean isUserURL =
                        request.getRequestURI()
                                .startsWith(
                                        request.getContextPath()
                                                + "/"
                                                + UserType.USER.toString().toLowerCase());
                if (szUserType.equalsIgnoreCase(UserType.ADMIN.getSessionUserType()) && isAdminURL
                        || szUserType.equalsIgnoreCase(UserType.REP.getSessionUserType())
                                && isRepURL
                        || szUserType.equalsIgnoreCase(UserType.USER.getSessionUserType())
                                && isUserURL) {
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
                // Want to log in, want to register
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
