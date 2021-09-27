package controllers;

import calculator.Calculator;
import calculator.WebCalculatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static controllers.ControllerConstants.LOCATION;
import static controllers.ControllerConstants.OVER_RANGE;
import static controllers.ControllerConstants.STATUS_BAD_REQUEST;
import static controllers.ControllerConstants.STATUS_CREATED;
import static controllers.ControllerConstants.STATUS_FORBIDDEN;
import static controllers.ControllerConstants.STATUS_OK;
import static controllers.ControllerConstants.WRONG_EXPRESSION;

public class ServletController implements Controller {

    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static final Calculator CALCULATOR_SERVICE = FACTORY.createCalculator();

    public void getResult(HttpServletRequest httpServletRequest, HttpServletResponse response) {

        String sessionID = getSessionId(httpServletRequest);
        try {
            int result = CALCULATOR_SERVICE.calculate(sessionID);
            PrintWriter printWriter = response.getWriter();
            printWriter.printf(String.valueOf(result));
            response.setStatus(HttpServletResponse.SC_OK);
            printWriter.close();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }


    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);
        CALCULATOR_SERVICE.deleteData(sessionID, parameterName);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    public void putNewData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(request.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String paramValue = bufferedReader.readLine();
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);

        int statusCode = CALCULATOR_SERVICE.addData(sessionID, parameterName, paramValue);
        setStatusCode(statusCode, response);
        String requestURI = request.getRequestURI();
        response.addHeader(LOCATION, requestURI);

        bufferedReader.close();
    }

    private void setStatusCode(int statusCode, HttpServletResponse response) throws IOException {
        if (statusCode == STATUS_CREATED) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else if (statusCode == STATUS_FORBIDDEN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
        } else if (statusCode == STATUS_BAD_REQUEST) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
        } else if (statusCode == STATUS_OK) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private String getParameterName(HttpServletRequest request) {
        return request.getPathInfo()
                .substring(1);
    }

    private String getSessionId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getSession()
                .getId();
    }
}