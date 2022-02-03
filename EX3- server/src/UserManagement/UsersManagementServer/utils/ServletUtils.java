package UserManagement.UsersManagementServer.utils;

import UserManagement.userManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static UserManagement.UsersManagementServer.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();


	public static userManager getUserManager(ServletContext servletContext) {
		System.out.println("usermanager ENTRY");
		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new userManager());
			}
		}

		System.out.println("usermanager check");
		return (userManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}


	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}
