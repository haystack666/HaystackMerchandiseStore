package com.haystack.FruitShop.Main;

import com.haystack.util.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@WebServlet("/addGoods")
public class addGoods extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        // 获取前端传递的商品信息
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String intro = request.getParameter("intro");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        int intQuantity = Integer.parseInt(quantity);

        try {
            // 获取数据库连接
            Connection conn = DBConnect.getConn();

            // 插入商品信息，如果已存在相同的 FruitName 则忽略
            String sql = "INSERT IGNORE INTO fruitinfo (FruitName, FruitType, FruitIntroduce, FruitPrice, FruitQuantity, isApear) VALUES (?, ?, ?, ?, ?, 1)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, type);
                preparedStatement.setString(3, intro);
                preparedStatement.setString(4, price);
                preparedStatement.setInt(5, intQuantity);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // 插入成功
                    response.getWriter().println("添加成功");
                } else {
                    // 插入失败，商品已存在
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("添加失败，商品已存在");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // 返回错误信息给前端
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding fruit information: " + e.getMessage());
        }
    }
}
