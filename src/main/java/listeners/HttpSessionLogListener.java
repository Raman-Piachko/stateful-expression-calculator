package listeners;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static listeners.ListenerConstants.SESSION_ID_FINISHED;
import static listeners.ListenerConstants.SESSION_ID_START;

public class HttpSessionLogListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        ServletContext servletContext = session.getServletContext();
        servletContext.log(String.format(SESSION_ID_START, session.getId()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        ServletContext servletContext = session.getServletContext();
        servletContext.log(String.format(SESSION_ID_FINISHED, session.getId()));

    }
}