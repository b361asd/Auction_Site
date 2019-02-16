package rutgers.cs336.listener;

import rutgers.cs336.db.DBBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
		//
		String url      = ctx.getInitParameter("DBURL");
		String username = ctx.getInitParameter("DBUSER");
		String password = ctx.getInitParameter("DBPWD");
		//
		DBBase.init(url, username, password);
		//
		System.out.println("Database connection initialized for Application.");
	}


	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
		//
		System.out.println("Database connection closed for Application.");
	}
}
