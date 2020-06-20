package Controller;

import Persistence.DbConnect;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private DbConnect dbStore = DbConnect.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("Views/login.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean role = dbStore.isAuthorized(request.getParameter("user"),request.getParameter("pwd"));
        if (role) {
            Cookie userName = new Cookie("user", "login");
            userName.setMaxAge(30 * 60);
            response.addCookie(userName);
            response.sendRedirect("service");
        } else {
            request.setAttribute("login","false");
            request.getRequestDispatcher("Views/login.html").forward(request, response);
        }
    }

}