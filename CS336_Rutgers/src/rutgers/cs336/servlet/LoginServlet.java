package rutgers.cs336.servlet;

import rutgers.cs336.db.IDaoConstant;
import rutgers.cs336.db.VerifyLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet implements IDaoConstant {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//
		Map map = VerifyLogin.doVerifyLogin(username, password);
		//
		boolean isOK = (Boolean) map.get(DATA_NAME_STATUS);
		if (isOK) {
			request.getSession().setAttribute("user", username);
			//
			request.getSession().setAttribute("message", map.get(DATA_NAME_MESSAGE).toString());
			request.getSession().setAttribute("userType", map.get(DATA_NAME_USER_TYPE).toString());
			//
			if ((request.getSession().getAttribute("userType")).toString().equals("1")) {
				response.sendRedirect(request.getContextPath() + "/homeAdmin.jsp");
			}
			else if ((request.getSession().getAttribute("userType")).toString().equals("2")) {
				response.sendRedirect(request.getContextPath() + "/homeRep.jsp");
			}
			else {
				response.sendRedirect(request.getContextPath() + "/home.jsp");
			}
			//
			//request.setAttribute("JSP_DATA", map);
			//request.getRequestDispatcher("/home.jsp").forward(request, response);
		}
		else {
			request.getSession().setAttribute("message", map.get(DATA_NAME_MESSAGE).toString());
			//
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			//
			//request.setAttribute("JSP_DATA", map);
			//request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
