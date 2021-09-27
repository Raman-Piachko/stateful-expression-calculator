package controllers;

import calculator.Calculator;
import calculator.WebCalculatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static controllers.ControllerConstants.ABS_RANGE;
import static controllers.ControllerConstants.DIVIDE;
import static controllers.ControllerConstants.EXPRESSION;
import static controllers.ControllerConstants.LOCATION;
import static controllers.ControllerConstants.MINUS;
import static controllers.ControllerConstants.MISSING_EXPRESSION;
import static controllers.ControllerConstants.MULTIPLY;
import static controllers.ControllerConstants.OVER_RANGE;
import static controllers.ControllerConstants.PLUS;
import static controllers.ControllerConstants.WRONG_EXPRESSION;

public class ServletController implements Controller {

    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static final Calculator CALCULATOR_SERVICE = FACTORY.createCalculator();

    public void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        PrintWriter writer = response.getWriter();

        try {
            int result = CALCULATOR_SERVICE.calculate(session);
            writer.print(result);
            response.setStatus(HttpServletResponse.SC_OK);
            writer.close();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, MISSING_EXPRESSION);
        }
    }


    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String attributeName = request.getRequestURI().substring(6);
        session.removeAttribute(attributeName);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    public void putData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String attribute = request.getRequestURI().substring(6);
        String value = request.getReader().readLine();

        if (attribute.equalsIgnoreCase(EXPRESSION)) {
            if (isGoodFormatExpression(value)) {
                addData(response, session, value, attribute);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
            }
        } else {
            if (!isParameterHasOverLimitValue(value)) {
                addData(response, session, value, attribute);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
            }
        }

        String requestURI = request.getRequestURI();
        response.addHeader(LOCATION, requestURI);
    }

    private void addData(HttpServletResponse response, HttpSession session, String value, String attributeName) {
        if (session.getAttribute(attributeName) == null) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        session.setAttribute(attributeName, value);
    }

    private boolean isGoodFormatExpression(String expression) {
        return (expression.contains(PLUS) || expression.contains(MINUS) ||
                expression.contains(DIVIDE) || expression.contains(MULTIPLY));
    }

    private boolean isParameterHasOverLimitValue(String paramValue) {
        try {
            int i = Integer.parseInt(paramValue);
            return Math.abs(i) > ABS_RANGE;
        } catch (Exception e) {
            return false;
        }
    }
}