package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import repository.Repository;
import repository.RepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static calculator.CalculatorConstants.ABS_RANGE;
import static calculator.CalculatorConstants.DIVIDE;
import static calculator.CalculatorConstants.EMPTY_SYMBOL;
import static calculator.CalculatorConstants.EXPRESSION;
import static calculator.CalculatorConstants.MINUS;
import static calculator.CalculatorConstants.MULTIPLY;
import static calculator.CalculatorConstants.OVER_RANGE;
import static calculator.CalculatorConstants.PLUS;
import static calculator.CalculatorConstants.WRONG_EXPRESSION;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorImpl implements Calculator {
    private Repository repositoryImpl = RepositoryImpl.getInstance();

    @Override
    public void calculate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MathParser mathParser = MathParserFactory.create();
        String expression = getFinalExpression(request);
        int result = mathParser.calculate(expression)
                .doubleValue()
                .intValue();

        PrintWriter printWriter = response.getWriter();
        printWriter.printf(String.valueOf(result));
    }


    @Override
    public void putNewData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(request.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String paramValue = bufferedReader.readLine();

        addParametersToRepository(request, response, paramValue, repositoryImpl);
    }

    @Override
    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);
        ConcurrentHashMap<String, String> data = getDataBySessionID(sessionID);
        data.remove(parameterName);
    }

    private ConcurrentHashMap<String, String> getDataBySessionID(String sessionID) {
        return repositoryImpl.getRepositoryData()
                .get(sessionID);
    }

    private void addParametersToRepository(HttpServletRequest request, HttpServletResponse response, String paramValue, Repository repositoryImpl) throws IOException {
        String sessionID = getSessionId(request);
        if (!repositoryImpl.getRepositoryData().containsKey(sessionID)) {
            repositoryImpl.updateRepositoryData(sessionID, new ConcurrentHashMap<>());
        }
        setStatusCode(request, response, paramValue);
        if (!isBadFormatExpression(paramValue, request) && !isParameterHasOverLimitValue(paramValue)) {
            getDataBySessionID(sessionID).put(getParameterName(request), paramValue);
        }

    }

    private String getParameterName(HttpServletRequest request) {
        return request.getPathInfo()
                .substring(1);
    }

    private String getFinalExpression(HttpServletRequest request) {
        String sessionID = getSessionId(request);
        List<String> expression = getExpression(sessionID);
        ConcurrentHashMap<String, String> repositoryData = getDataBySessionID(sessionID);

        while (isExpressionWithVariables(repositoryData, expression)) {
            convertExpressionWithValue(expression, getSessionId(request));
        }

        return deleteSpacesAndConvertListToString(expression);
    }

    private String getSessionId(HttpServletRequest request) {
        return request.getSession()
                .getId();
    }

    private List<String> getExpression(String sessionID) {
        ConcurrentHashMap<String, String> data = getDataBySessionID(sessionID);

        return Arrays.asList(data
                .get(EXPRESSION)
                .split(EMPTY_SYMBOL));
    }

    private void convertExpressionWithValue(List<String> expression, String sessionID) {
        ConcurrentHashMap<String, String> data = getDataBySessionID(sessionID);

        for (int i = 0; i < expression.size(); i++) {
            String key = expression.get(i);
            if (data.containsKey(key)) {
                expression.set(i, data.get(key));
            }
        }
    }

    private boolean isExpressionWithVariables(ConcurrentHashMap map, List<String> expression) {
        return expression.stream().anyMatch(map::containsKey);
    }

    private void setStatusCode(HttpServletRequest request, HttpServletResponse response, String paramValue) throws IOException {
        if (getValueFromRepository(request) == null) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else if (isParameterHasOverLimitValue(paramValue)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, OVER_RANGE);
        } else if (isBadFormatExpression(paramValue, request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_EXPRESSION);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private String getValueFromRepository(HttpServletRequest request) {
        String sessionID = getSessionId(request);
        String parameterName = getParameterName(request);
        ConcurrentHashMap<String, String> data = getDataBySessionID(sessionID);

        return data.get(parameterName);
    }

    private boolean isBadFormatExpression(String paramValue, HttpServletRequest request) {
        String parameterName = getParameterName(request);
        return parameterName.equals(EXPRESSION) &&
                !(paramValue.contains(PLUS) || paramValue.contains(MINUS) ||
                        paramValue.contains(DIVIDE) || paramValue.contains(MULTIPLY));
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