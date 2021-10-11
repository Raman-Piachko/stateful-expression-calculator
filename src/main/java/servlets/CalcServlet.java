package servlets;


import controllers.Controller;
import controllers.ServletController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static constants.ControllerConstants.ERROR_MESSAGE;

@WebServlet(urlPatterns = "/calc/*")
public class CalcServlet extends HttpServlet {
    private static final Controller servletController = new ServletController();
    private static final Logger LOGGER = LogManager.getLogger(CalcServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            servletController.getResult(request, response);
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            servletController.deleteData(request, response);
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            servletController.putNewData(request, response);
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}