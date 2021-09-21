package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    String getFinalExpression(HttpServletRequest httpServletRequest);

    void deleteData(HttpServletRequest request);

    void putNewData(HttpServletRequest request, HttpServletResponse response) throws IOException;
}