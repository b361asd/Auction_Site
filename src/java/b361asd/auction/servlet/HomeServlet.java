package java.b361asd.auction.servlet;

import java.b361asd.auction.db.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Dispatch Servlet based on userType:
 * <ol>
 *    <li>admin</li>
 *    <li>rep</li>
 *    <li>user</li>
 * </ol>
 */
public class HomeServlet extends HttpServlet implements IConstant {


   /**
    * Serial Version UID
    */
   private static final long serialVersionUID = -8617808045057289714L;


   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doPost(request, response);
   }


   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      //request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "home!!!");
      Map map;
      //
      boolean isRegister = (request.getParameter("register")) != null && (request.getParameter("register")).equalsIgnoreCase("YES");
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
         map = User.doAddUser(username, password, email, firstName, lastName, street, city, state, zipCode, phone, 3);
         //
         if ((Boolean) map.get(DATA_NAME_STATUS)) {      // Register successful also means logged in.
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
            map = User.doVerifyLogin(username, password);
            //
            if ((Boolean) map.get(DATA_NAME_STATUS)) {
               request.getSession().setAttribute(SESSION_ATTRIBUTE_USER, username);
               request.getSession().setAttribute(SESSION_ATTRIBUTE_USERTYPE, map.get(DATA_NAME_USER_TYPE).toString());
               request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_FNAME, map.get(DATA_NAME_FIRST_NAME).toString());
               request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_LNAME, map.get(DATA_NAME_LAST_NAME).toString());
               //
               //request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "no not OK");
            }
         }
         else {
            map = new HashMap();
            map.put(DATA_NAME_STATUS, true);
            map.put(DATA_NAME_MESSAGE, "Already logon.");
            //
            //Session attributes already set
         }
      }
      //
      //
      //
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, map);
      //
      if ((Boolean) map.get(DATA_NAME_STATUS)) {
         String szUserType = request.getSession() == null ? "3" : (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_USERTYPE);
         //
         if (szUserType.equals("1")) {
            map.put(DATA_NAME_MESSAGE, map.get(DATA_NAME_MESSAGE) + " Admin Login!");
            //
            request.getRequestDispatcher("/admin/homeAdmin.jsp").forward(request, response);
         }
         else if (szUserType.equals("2")) {
            map.put(DATA_NAME_MESSAGE, map.get(DATA_NAME_MESSAGE) + " Rep Login!");
            //
            request.getRequestDispatcher("/rep/homeRep.jsp").forward(request, response);
         }
         else {
            map.put(DATA_NAME_MESSAGE, map.get(DATA_NAME_MESSAGE) + " User Login!");
            //
            request.getRequestDispatcher("/user/home.jsp").forward(request, response);
         }
      }
      else {
         //request.getSession().setAttribute(SESSION_ATTRIBUTE_MESSAGE, "not OK");
         //
         map.put(DATA_NAME_MESSAGE, map.get(DATA_NAME_MESSAGE) + " Login or register failed. Redirect back to login");
         //
         response.sendRedirect(request.getContextPath() + "/login.jsp");
      }
   }
}
