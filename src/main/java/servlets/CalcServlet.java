package servlets;


import calculator.WebCalculatorFactory;
import controllers.Controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/calc/*")
public class CalcServlet extends HttpServlet {
    private static final WebCalculatorFactory factory = new WebCalculatorFactory();
    private static final Controller servletController = factory.createServletController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        servletController.getResult(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        servletController.deleteData(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        servletController.putData(request, response);
    }
}