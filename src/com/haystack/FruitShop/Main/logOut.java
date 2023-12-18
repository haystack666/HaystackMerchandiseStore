package com.haystack.FruitShop.Main;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logOut")
public class logOut extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取当前会话对象
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 使会话无效
            session.invalidate();
            response.sendRedirect("login.jsp"); // 重定向到登录页面或其他页面
        } else {
            response.sendRedirect("login.jsp"); // 如果会话不存在，也重定向到登录页面或其他页面
        }
    }
}
