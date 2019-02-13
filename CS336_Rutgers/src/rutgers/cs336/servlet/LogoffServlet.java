package rutgers.cs336.servlet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LogoffServlet")
public class LogoffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext ctx = request.getServletContext();
		ctx.setAttribute("User", "Pankaj");
		String user = (String) ctx.getAttribute("User");
		ctx.removeAttribute("User");

		HttpSession session = request.getSession();
		session.invalidate();

		PrintWriter out = response.getWriter();
		out.write("Hi " + user);
	}

}
