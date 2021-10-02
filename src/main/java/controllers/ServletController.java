package controllers;

import calculator.CalculatorService;
import calculator.WebCalculatorFactory;
import responseCodes.codes.StatusCode;
import responseCodes.exception.BadRequestException;
import responseCodes.exception.ForbiddenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static constants.ControllerConstants.LOCATION;
import static constants.ControllerConstants.OVER_RANGE;
import static constants.ControllerConstants.WRONG_EXPRESSION;

public class ServletController implements Controller {

    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static CalculatorService calculatorService;

    public void getResult(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        calculatorService = FACTORY.createCalculator();
        String sessionID = getSessionId(httpServletRequest);

        try {
            int result = calculatorService.calculate(sessionID);
            PrintWriter printWriter = response.getWriter();
            printWriter.printf(String.valueOf(result));
            response.setStatus(HttpServletResponse.SC_OK);
            printWriter.close();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        calculatorService = FACTORY.createCalculator();
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);

        calculatorService.deleteData(sessionID, parameterName);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    public void putNewData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        calculatorService = FACTORY.createCalculator();
        InputStreamReader streamReader = new InputStreamReader(request.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String paramValue = bufferedReader.readLine();
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);

        try (streamReader; bufferedReader) {
            StatusCode statusCode = calculatorService.addData(sessionID, parameterName, paramValue);
            setStatusCode(statusCode, response);
            String requestURI = request.getRequestURI();
            response.addHeader(LOCATION, requestURI);
        } catch (Exception e) {
            setErrorStatusCode(e, response);
        }
    }

    private void setErrorStatusCode(Exception e, HttpServletResponse response) throws IOException {
        if (e.getClass().isAssignableFrom(BadRequestException.class)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
        } else if (e.getClass().isAssignableFrom(ForbiddenException.class)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
        }
    }

    private void setStatusCode(StatusCode statusCode, HttpServletResponse response) {
        if (statusCode == StatusCode.CREATED) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else if (statusCode == StatusCode.INSERTED) {
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