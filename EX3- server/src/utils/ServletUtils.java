package utils;

import GraphManagment.GraphsManager;
import ProcessesManagment.ProcessesManager;
import UserManagement.userManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static UserManagement.UsersManagementServer.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	public static final String GRAPHS_MANAGER_ATTRIBUTE_NAME = "graphsManager";
	public static final String MISSIONS_MANAGER_ATTRIBUTE_NAME = "missionManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object graphsManagerLock = new Object();
	private static final Object missionsManagerLock = new Object();

	public static ProcessesManager getMissionsManager(ServletContext servletContext) {

		synchronized (missionsManagerLock) {
			if (servletContext.getAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME, new ProcessesManager());
			}
		}

		return (ProcessesManager) servletContext.getAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME);
	}


	public static userManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new userManager());
			}
		}

		return (userManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static GraphsManager getGraphsManager(ServletContext servletContext) {

		synchronized (graphsManagerLock) {
			if (servletContext.getAttribute(GRAPHS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(GRAPHS_MANAGER_ATTRIBUTE_NAME, new GraphsManager());
			}
		}

		return (GraphsManager) servletContext.getAttribute(GRAPHS_MANAGER_ATTRIBUTE_NAME);
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
