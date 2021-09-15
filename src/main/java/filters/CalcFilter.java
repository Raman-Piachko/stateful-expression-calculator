package filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static calculator.CalculatorConstants.EXPRESSION;
import static filters.FilterConstants.FILTER_EXPRESSION;
import static filters.FilterConstants.REMOTE_HOST;
import static filters.FilterConstants.REMOTE_PORT;

public class CalcFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteHost = request.getRemoteHost();
        int remotePort = request.getRemotePort();
        String expression = request.getParameter(EXPRESSION);

        ServletContext servletContext = filterConfig.getServletContext();

        servletContext.log(REMOTE_HOST + remoteHost);
        servletContext.log(REMOTE_PORT + remotePort);
        servletContext.log(FILTER_EXPRESSION + expression);

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        filterConfig = null;
    }
}