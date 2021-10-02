package controllers;

import calculator.Calculator;
import calculator.WebCalculatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static controllers.ControllerConstants.ABS_RANGE;
import static controllers.ControllerConstants.CALCULATE_EXCEPTION;
import static controllers.ControllerConstants.DIVIDE;
import static controllers.ControllerConstants.EXPRESSION;
import static controllers.ControllerConstants.LOCATION;
import static controllers.ControllerConstants.MINUS;
import static controllers.ControllerConstants.MISSING_EXPRESSION;
import static controllers.ControllerConstants.MULTIPLY;
import static controllers.ControllerConstants.OVER_RANGE;
import static controllers.ControllerConstants.PARSING_INFO;
import static controllers.ControllerConstants.PLUS;
import static controllers.ControllerConstants.URL_SUBSTRING;
import static controllers.ControllerConstants.WRONG_EXPRESSION;

public class ServletController implements Controller {

    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static final Calculator CALCULATOR_SERVICE = FACTORY.createCalculator();
    private static final Logger logger = LogManager.getLogger(ServletController.class);

    public void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String expression = (String) session.getAttribute(EXPRESSION);
        Map<String, String> attributeValueMap = getAttributeValueMap(session);
        PrintWriter writer = response.getWriter();
        try {
            int result = CALCULATOR_SERVICE.calculate(expression, attributeValueMap);
            writer.print(result);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.info(String.format(CALCULATE_EXCEPTION, expression));
            response.sendError(HttpServletResponse.SC_CONFLICT, MISSING_EXPRESSION);
        } finally {
            writer.close();
        }
    }

    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String attributeName = getItemNameFromURI(request);
        session.removeAttribute(attributeName);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public void putData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String attribute = getItemNameFromURI(request);
        String value = request.getReader().readLine();

        if (attribute.equalsIgnoreCase(EXPRESSION)) {
            if (isGoodFormatExpression(value)) {
                addData(response, session, value, attribute);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
            }
        } else {
            if (!isParameterHasOverLimitValue(value)) {
                addData(response, session, value, attribute);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
            }
        }

        String requestURI = request.getRequestURI();
        response.addHeader(LOCATION, requestURI);
    }

    private Map<String, String> getAttributeValueMap(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributeValueMap = new ConcurrentHashMap<>();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributeValueMap.put(attributeName, (String) session.getAttribute(attributeName));
        }

        return attributeValueMap;
    }

    private String getItemNameFromURI(HttpServletRequest request) {
        return request.getRequestURI().substring(URL_SUBSTRING);
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
            logger.info(String.format(PARSING_INFO, paramValue));
            return false;
        }
    }
}