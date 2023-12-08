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
@WebServlet("/deleteCart")
public class deleteCart extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String fruitId = request.getParameter("fruitId");
        int IntFruitId = Integer.parseInt(fruitId);
        String fruitName = request.getParameter("fruitName");
        String fruitQuantity = request.getParameter("fruitQuantity");
        int IntFruitQuantity = Integer.parseInt(fruitQuantity);

        try {
            Connection conn = DBConnect.getConn();
            String sql = "DELETE FROM myshopcart WHERE GoodsId=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, IntFruitId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // 删除成功
                    response.getWriter().println("删除成功");

                    // 更新库存
                    String sql2 = "UPDATE fruitinfo SET FruitQuantity = FruitQuantity + ? WHERE FruitName = ?";
                    try (PreparedStatement recoverData = conn.prepareStatement(sql2)) {
                        recoverData.setInt(1, IntFruitQuantity);
                        recoverData.setString(2, fruitName);
                        int rowsAffected2 = recoverData.executeUpdate();

                        if (rowsAffected2 > 0) {
                            System.out.println("sql语句为" + sql2);
                            System.out.println("sql语句为" + recoverData);
                            System.out.println("库存恢复成功，受影响的行数：" + rowsAffected2);
                            response.getWriter().write("库存恢复成功");
                        } else {
                            response.getWriter().write("库存恢复失败");
                        }
                    }
                } else {
                    // 删除失败
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("删除失败");
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

