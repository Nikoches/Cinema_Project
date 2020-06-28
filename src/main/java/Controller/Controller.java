package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("Views/index.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String place = req.getParameter("place");
        req.setAttribute("x",place.charAt(0));
        req.setAttribute("y",place.charAt(1));
        req.setAttribute("summ","500");
        Cookie[] cookies = req.getCookies();
        String user = "false";
        if (cookies != null) {
            for (Cookie x : cookies) {
                if (x.getName().equals("user")) {
                    user = x.getValue();
                    break;
                }
            }
        }
        req.setAttribute("userId",user);
        req.getRequestDispatcher("Views/paypage.jsp").forward(req,resp);
    }
}
