package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import repository.Repository;
import repository.RepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static calculator.CalculatorConstants.EMPTY_SYMBOL;
import static calculator.CalculatorConstants.EXPRESSION;
import static utils.СonversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorImpl implements Calculator {
    private Repository repositoryImpl = RepositoryImpl.getInstance();

    @Override
    public void calculate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void addParametersToRepository(HttpServletRequest request, HttpServletResponse resp, String paramValue, Repository repositoryImpl) {
        String sessionID = getSessionId(request);
        if (!repositoryImpl.getRepositoryData().containsKey(sessionID)) {
            repositoryImpl.updateRepositoryData(sessionID, new ConcurrentHashMap<>());
        }
        repositoryImpl.getRepositoryData().get(sessionID).put(getNameParameter(request), paramValue);

    }

    private String getNameParameter(HttpServletRequest request) {
        return request.getPathInfo().substring(1);
    }

    private String getFinalExpression(HttpServletRequest request) {
        String sessionID = getSessionId(request);
        List<String> expression = getExpression(sessionID);
        ConcurrentHashMap<String, String> repositoryData = repositoryImpl.getRepositoryData().get(sessionID);

        while (isExpressionWithVariables(repositoryData, expression)) {
            convertExpressionWithValue(expression, getSessionId(request));
        }

        return deleteSpacesAndConvertListToString(expression);
    }

    private String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    private List<String> getExpression(String sessionID) {
        return Arrays.asList(repositoryImpl.getRepositoryData()
                .get(sessionID)
                .get(EXPRESSION)
                .split(EMPTY_SYMBOL));
    }

    private void convertExpressionWithValue(List<String> expression, String sessionID) {
        int bound = expression.size();
        IntStream.range(0, bound)
                .filter(i -> repositoryImpl.getRepositoryData()
                        .get(sessionID)
                        .containsKey(expression.get(i)))
                .forEach(i -> expression.set(i, repositoryImpl.getRepositoryData()
                        .get(sessionID)
                        .get(expression.get(i))));
    }

    private boolean isExpressionWithVariables(ConcurrentHashMap map, List<String> expression) {
        return expression.stream()
                .anyMatch(map::containsKey);
    }

}