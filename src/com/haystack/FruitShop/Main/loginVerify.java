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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/loginVerify")
public class loginVerify extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // 获取数据库连接
            Connection conn = DBConnect.getConn();

            // 检查用户是否已存在
            String querySql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement queryStatement = conn.prepareStatement(querySql)) {
                queryStatement.setString(1, username);
                ResultSet resultSet = queryStatement.executeQuery();

                if (resultSet.next()) {
                    // 用户已存在，检查密码
                    String storedPassword = resultSet.getString("password");
                    if (password.equals(storedPassword)) {
                        // 密码匹配，登录成功
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("password", password);

                        response.getWriter().write("登录成功");
                    } else {
                        // 密码不匹配，返回错误信息
                        response.getWriter().write("密码错误，请重新输入");
                    }
                } else {
                    // 用户不存在，进行注册
                    String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = conn.prepareStatement(insertSql)) {
                        insertStatement.setString(1, username);
                        insertStatement.setString(2, password);
                        insertStatement.executeUpdate();

                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("password", password);

                        response.getWriter().write("用户注册成功");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // 返回错误信息给前端
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing login: " + e.getMessage());
        }
    }
}
