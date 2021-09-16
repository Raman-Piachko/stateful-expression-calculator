package servlets;


import calculator.Calculator;
import calculator.WebCalculatorFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/calc/*")
public class CalcServlet extends HttpServlet {
    private static final WebCalculatorFactory factory = new WebCalculatorFactory();
    private static final Calculator calculator = factory.createCalculator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            calculator.calculate(request, response);
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(409);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        calculator.deleteData(request, response);
        response.setStatus(204);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        calculator.putNewData(request, response);
        response.addHeader("Location", requestURI);
    }
}