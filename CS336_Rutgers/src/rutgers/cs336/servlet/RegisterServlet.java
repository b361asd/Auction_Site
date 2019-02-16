package rutgers.cs336.servlet;

import rutgers.cs336.db.AddUser;
import rutgers.cs336.db.IDaoConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RegisterServlet extends HttpServlet implements IDaoConstant {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String street = request.getParameter("street");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zipCode = request.getParameter("zipCode");
		String phone = request.getParameter("phoneNumber");
		//
		Map map = AddUser.doAddUser(username, password, email, firstName, lastName, street, city, state, zipCode, phone,
				  3);
		//
		boolean isOK = (Boolean) map.get(DATA_NAME_STATUS);
		if (isOK) {
			request.getSession().setAttribute("user", username);
			//
			request.getSession().setAttribute("message", map.get(DATA_NAME_MESSAGE).toString());
			request.getSession().setAttribute("userType", "3");
			//
			if ((request.getSession().getAttribute("userType")).toString().equals("1")) {
				response.sendRedirect(request.getContextPath() + "/homeAdmin.jsp");
			} else if ((request.getSession().getAttribute("userType")).toString().equals("2")) {
				response.sendRedirect(request.getContextPath() + "/homeRep.jsp");
			} else {
				response.sendRedirect(request.getContextPath() + "/home.jsp");
			}
			//
			//request.setAttribute("JSP_DATA", map);
			//request.getRequestDispatcher("/home.jsp").forward(request, response);
		} else {
			request.getSession().setAttribute("message", map.get(DATA_NAME_MESSAGE).toString());
			//
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			//
			//request.setAttribute("JSP_DATA", map);
			//request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
