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
@WebServlet("/addCart")
public class addCart extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        // 检查session是否存在
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("password") == null) {
            response.getWriter().write("尚未登录，前往<a href='login.jsp' >登录</a>");
            return;
        }

        String usernameInSession = String.valueOf(session.getAttribute("username"));

        // 获取前端传递的商品信息
        String fruitId = request.getParameter("fruitId");
        int IntFruitId = Integer.parseInt(fruitId);
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String intro = request.getParameter("intro");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        int intQuantity = Integer.parseInt(quantity);

        try {
            // 获取数据库连接
            Connection conn = DBConnect.getConn();

            // 插入或更新购物车信息
            String cartSql = "INSERT INTO myshopcart (GoodsName, GoodsType, GoodsIntroduce, GoodsPrice, GoodsQuantity, whoAdd) VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE GoodsQuantity = GoodsQuantity + VALUES(GoodsQuantity)";
            try (PreparedStatement cartStatement = conn.prepareStatement(cartSql)) {
                cartStatement.setString(1, name);
                cartStatement.setString(2, type);
                cartStatement.setString(3, intro);
                cartStatement.setString(4, price);
                cartStatement.setInt(5, intQuantity);
                cartStatement.setString(6, usernameInSession);
//                cartStatement.setInt(7, intQuantity);

                int cartRowsAffected = cartStatement.executeUpdate();

                if (cartRowsAffected > 0) {
                    // 插入或更新购物车成功
                    response.getWriter().println("添加购物车成功");

                    // 减少库存
                    String stockSql = "UPDATE fruitinfo SET FruitQuantity = FruitQuantity - ? WHERE FruitId = ?";
                    try (PreparedStatement stockStatement = conn.prepareStatement(stockSql)) {
                        stockStatement.setInt(1, intQuantity);
                        stockStatement.setInt(2, IntFruitId);

                        int stockRowsAffected = stockStatement.executeUpdate();

                        if (stockRowsAffected > 0) {
                            // 减少库存成功
                            response.getWriter().println("库存减少成功");
                        } else {
                            // 减少库存失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().println("库存减少失败");
                        }



                    }
                } else {
                    // 插入或更新购物车失败
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("添加或更新购物车失败");
                }


            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // 返回错误信息给前端
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding or updating cart information: " + e.getMessage());
        }
    }
}

