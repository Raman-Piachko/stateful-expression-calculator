package filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static constants.FilterConstants.REMOTE_HOST;
import static constants.FilterConstants.REMOTE_PORT;

public class CalcFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig config) {
        this.filterConfig = config;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteHost = request.getRemoteHost();
        int remotePort = request.getRemotePort();
        ServletContext servletContext = filterConfig.getServletContext();

        servletContext.log(REMOTE_HOST + remoteHost);
        servletContext.log(REMOTE_PORT + remotePort);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}