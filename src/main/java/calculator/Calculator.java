package calculator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Calculator {
    void calculate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    void putNewData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    void deleteData(HttpServletRequest request, HttpServletResponse response);
}