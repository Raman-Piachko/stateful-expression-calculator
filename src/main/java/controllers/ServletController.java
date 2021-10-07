package controllers;

import calculator.CalculatorService;
import calculator.WebCalculatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import response.codes.codes.StatusCode;
import response.codes.exception.BadRequestException;
import response.codes.exception.ForbiddenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static constants.ControllerConstants.LOCATION;
import static constants.ControllerConstants.OVER_RANGE;
import static constants.ControllerConstants.WRONG_EXPRESSION;

public class ServletController implements Controller {

    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static final Logger LOGGER = LogManager.getLogger(ServletController.class);

    public void getResult(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        CalculatorService calculatorService = FACTORY.createCalculator();
        String sessionID = getSessionId(httpServletRequest);

        try {
            int result = calculatorService.calculate(sessionID);
            try (PrintWriter printWriter = response.getWriter()) {
                printWriter.printf(String.valueOf(result));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            LOGGER.info(e);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        CalculatorService calculatorService = FACTORY.createCalculator();
        try {
            String sessionID = getSessionId(request);
            String parameterName = getParameterName(request);
            calculatorService.deleteData(sessionID, parameterName);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            LOGGER.info(e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    public void putNewData(HttpServletRequest request, HttpServletResponse response) {
        CalculatorService calculatorService = FACTORY.createCalculator();
        try (InputStreamReader streamReader = new InputStreamReader(request.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {

            String paramValue = bufferedReader.readLine();
            String sessionID = getSessionId(request);
            String parameterName = getParameterName(request);

            try (streamReader; bufferedReader) {
                StatusCode statusCode = calculatorService.addData(sessionID, parameterName, paramValue);
                int code = getStatusCode(statusCode);
                response.setStatus(code);
                String requestURI = request.getRequestURI();
                response.addHeader(LOCATION, requestURI);
            } catch (BadRequestException e) {
                LOGGER.error(WRONG_EXPRESSION, e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
            } catch (ForbiddenException e) {
                LOGGER.error(OVER_RANGE, e);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
            }
        } catch (Exception e) {
            LOGGER.info(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int getStatusCode(StatusCode statusCode) {
        if (statusCode == StatusCode.CREATED) {
            return HttpServletResponse.SC_CREATED;
        } else if (statusCode == StatusCode.INSERTED) {
            return HttpServletResponse.SC_OK;
        } else return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
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