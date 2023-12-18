package com.haystack.FruitShop.Main;

import com.haystack.util.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteFruit")
public class deletaInfo extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        // 检查session是否存在
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("password") == null) {
            response.getWriter().write("尚未登录，前往<a href='login.jsp' >登录</a>");
            return;
        }

        String fruitId = request.getParameter("fruitId");
        int IntFruitId = Integer.parseInt(fruitId);

        try {
            Connection conn = DBConnect.getConn();
            String sql = "UPDATE fruitinfo SET isApear=0 WHERE FruitId=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, IntFruitId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // 更新成功
                    response.getWriter().println("删除成功");
                } else {
                    // 更新失败
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("系统错误");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // 返回错误信息给前端
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error updating fruit information: " + e.getMessage());
        }

    }
}
