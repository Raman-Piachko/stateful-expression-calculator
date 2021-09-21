package servlets;


import calculator.Calculator;
import calculator.WebCalculatorFactory;
import controllers.Controller;
import controllers.ServletController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/calc/*")
public class CalcServlet extends HttpServlet {
    private static final WebCalculatorFactory factory = new WebCalculatorFactory();
    private static final Calculator calculator = factory.createCalculator();
    private static final Controller servletController = new ServletController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String expression = servletController.getFinalExpression(request);
            int result = calculator.calculate(expression);
            PrintWriter printWriter = response.getWriter();
            printWriter.printf(String.valueOf(result));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        servletController.deleteData(request);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        servletController.putNewData(request, response);
    }
}