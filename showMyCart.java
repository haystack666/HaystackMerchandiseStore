package com.haystack.FruitShop.Main;

import com.haystack.FruitShop.info.FruitInfo;
import com.haystack.util.DBConnect;
import jakarta.servlet.RequestDispatcher;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/showMyCart")
public class showMyCart extends HttpServlet {
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

        try {
            Connection conn = DBConnect.getConn();
            // 创建 PreparedStatement
            String sql = "SELECT * FROM myshopcart WHERE whoAdd = ? ORDER BY CAST(id AS SIGNED)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, usernameInSession);

                // 执行查询并获取结果集
                ResultSet resultSet = preparedStatement.executeQuery();

                // 将查询结果存储到List中
                List<FruitInfo> FruitInfo = new ArrayList<>();
                while (resultSet.next()) {
                    FruitInfo fruitInfo = new FruitInfo();
                    fruitInfo.setFruitId(resultSet.getString("id"));
                    fruitInfo.setFruitName(resultSet.getString("GoodsName"));
                    fruitInfo.setFruitType(resultSet.getString("GoodsType"));
                    fruitInfo.setFruitIntroduce(resultSet.getString("GoodsIntroduce"));
                    fruitInfo.setFruitPrice(resultSet.getString("GoodsPrice"));
                    fruitInfo.setFruitQuantity(resultSet.getString("GoodsQuantity"));

                    // 计算并设置 FruitAllPrice
                    double price = Double.parseDouble(fruitInfo.getFruitPrice());
                    int quantity = Integer.parseInt(fruitInfo.getFruitQuantity());
                    double GoodsAllPriceBefore = price * quantity;
                    String saveTwo = String.format("%.2f", GoodsAllPriceBefore);
                    double GoodsAllPrice = Double.parseDouble(saveTwo);
                    fruitInfo.setFruitAllPrice(String.valueOf(GoodsAllPrice));

                    FruitInfo.add(fruitInfo);
                }

                // 将结果存入请求中，以便在JSP中使用
                request.setAttribute("GoodsInfo", FruitInfo);

                // 转发到JSP页面
                RequestDispatcher dispatcher = request.getRequestDispatcher("myShopCart.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
