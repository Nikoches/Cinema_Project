package Controller;

import Service.CinService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pay")
public class PayController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        CinService cinService = CinService.getInstance();
        int[] ar = new int[]{Integer.parseInt(request.getParameter("xplc")),Integer.parseInt(request.getParameter("yplc"))};
        cinService.makeTransaction(ar,Integer.parseInt(request.getParameter("userId")),500);
        request.getRequestDispatcher("Views/index.html").forward(request, response);
    }
}
