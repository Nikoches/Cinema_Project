package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter1", urlPatterns = {"/service"})
public class AuthenticationFilter implements Filter {

    private ServletContext context;

    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        Cookie[] cookies = req.getCookies();
        this.context.log("Requested Resource::" + uri);
        String user = "false";
        if (cookies != null) {
            for (Cookie x : cookies) {
                if (x.getName().equals("user")) {
                    user = x.getValue();
                    break;
                }
            }
        }
        if (user.equals("login") || uri.endsWith("login.html") || uri.endsWith("update")) {
            chain.doFilter(request, response);
        } else {
            this.context.log("Unauthorized access request");
            req.getRequestDispatcher("Views/login.html").forward(req, res);
        }
    }

    public void destroy() {
    }

}