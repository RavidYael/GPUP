package utils;

import GraphManagment.GraphsManager;
import ProcessesManagment.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;
import UserManagement.UserManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static UserManagement.UsersManagementServer.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	public static final String GRAPHS_MANAGER_ATTRIBUTE_NAME = "graphsManager";
	public static final String MISSIONS_MANAGER_ATTRIBUTE_NAME = "missionManager";
	public static final String SUBSCRIBES_MANAGER_ATTRIBUTE_NAME = "subscribesManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object graphsManagerLock = new Object();
	private static final Object missionsManagerLock = new Object();
	private static final Object subscribesManagerLock = new Object();

	public static SubscribesManager getSubscribesManager(ServletContext servletContext) {

		synchronized (subscribesManagerLock) {
			if (servletContext.getAttribute(SUBSCRIBES_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SUBSCRIBES_MANAGER_ATTRIBUTE_NAME, new SubscribesManager());
			}
		}

		return (SubscribesManager) servletContext.getAttribute(SUBSCRIBES_MANAGER_ATTRIBUTE_NAME);
	}


	public static ProcessesManager getProcessesManager(ServletContext servletContext) {

		synchronized (missionsManagerLock) {
			if (servletContext.getAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME, new ProcessesManager());
			}
		}

		return (ProcessesManager) servletContext.getAttribute(MISSIONS_MANAGER_ATTRIBUTE_NAME);
	}


	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}

		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
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
