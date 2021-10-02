package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

    void deleteData(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void putData(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException;
}