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

@WebServlet("/orderSettlement")
public class orderSettlement extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        // 检查session是否存在
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("password") == null) {
            response.getWriter().write("尚未登录，前往<a href='login.jsp' >登录</a>");
            return;
        }

        String[] goodsIDs = request.getParameterValues("GoodsID");

        if (goodsIDs != null && goodsIDs.length > 0) {
            Connection conn = null;

            try {
                conn = DBConnect.getConn();
                String sql = "DELETE FROM myshopcart WHERE id = ?";

                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    for (String goodsID : goodsIDs) {
                        int intGoodsID = Integer.parseInt(goodsID);

                        preparedStatement.setInt(1, intGoodsID);
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected <= 0) {
                            // 更新失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().println("系统错误");
                            return;
                        }
                    }
                }

                // 所有商品删除成功
                response.getWriter().println("购物车结算成功");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                // 返回错误信息给前端
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error updating fruit information: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 前端未传递GoodsID
            response.getWriter().println("未选择要删除的商品");
        }
    }
}
