package rutgers.cs336.servlet;

import rutgers.cs336.db.AddUser;
import rutgers.cs336.db.VerifyLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet implements IConstant {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws IOException, ServletException {
		String  register   = request.getParameter("register");
		boolean isRegister = (register != null) && register.equalsIgnoreCase("YES");
		//
		Map map;
		//
		if (isRegister) {
			String username  = request.getParameter("username");
			String password  = request.getParameter("password");
			String email     = request.getParameter("email");
			String firstName = request.getParameter("firstName");
			String lastName  = request.getParameter("lastName");
			String street    = request.getParameter("street");
			String city      = request.getParameter("city");
			String state     = request.getParameter("state");
			String zipCode   = request.getParameter("zipCode");
			String phone     = request.getParameter("phoneNumber");
			//
			map = AddUser.doAddUser(username, password, email, firstName, lastName, street, city, state, zipCode, phone, 3);
			//
			boolean isOK = (Boolean) map.get(DATA_NAME_STATUS);
			if (isOK) {
				request.getSession().setAttribute(SESSION_ATTRIBUTE_USER, username);
				request.getSession().setAttribute(SESSION_ATTRIBUTE_USERTYPE, "3");
				request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_FNAME, firstName);
				request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_LNAME, lastName);
			}
		}
		else {
			boolean isLoggedIn = request.getSession() != null && request.getSession().getAttribute(SESSION_ATTRIBUTE_USER) != null;
			if (!isLoggedIn) {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				map = VerifyLogin.doVerifyLogin(username, password);
				//
				if ((Boolean) map.get(DATA_NAME_STATUS)) {
					request.getSession().setAttribute(SESSION_ATTRIBUTE_USER, username);
					request.getSession().setAttribute(SESSION_ATTRIBUTE_USERTYPE, map.get(DATA_NAME_USER_TYPE).toString());
					request.getSession()
							  .setAttribute(SESSION_ATTRIBUTE_USER_FNAME, map.get(DATA_NAME_FIRST_NAME).toString());
					request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_LNAME, map.get(DATA_NAME_LAST_NAME).toString());
					//
					request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, map.get(DATA_NAME_MESSAGE).toString());
				}
			}
			else {
				map = new HashMap();
				map.put(DATA_NAME_STATUS, true);
				map.put(DATA_NAME_MESSAGE, "Already logon.");
			}
		}
		//
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, map);
		//
		String szUserType = request.getSession() == null ? "3" : (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_USERTYPE);
		//
		boolean isOK = (Boolean) map.get(DATA_NAME_STATUS);
		if (isOK) {
			//
			if (szUserType.equals("1")) {
				//response.sendRedirect(request.getContextPath() + "/homeAdmin.jsp");
				//
				//request.setAttribute("JSP_DATA", map);
				request.getRequestDispatcher("/admin/homeAdmin.jsp").forward(request, response);
			}
			else if (szUserType.equals("2")) {
				//response.sendRedirect(request.getContextPath() + "/homeRep.jsp");
				//
				//request.setAttribute("JSP_DATA", map);
				request.getRequestDispatcher("/rep/homeRep.jsp").forward(request, response);
			}
			else {
				//response.sendRedirect(request.getContextPath() + "/home.jsp");
				//
				//request.setAttribute("JSP_DATA", map);
				request.getRequestDispatcher("/user/home.jsp").forward(request, response);
			}
		}
		else {
			request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, map.get(DATA_NAME_MESSAGE).toString());
			//
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			//
			//request.setAttribute("JSP_DATA", map);
			//request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
