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

@WebServlet("/showGoods")
public class showGoods extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        // 检查session是否存在
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null || session.getAttribute("password") == null) {
            response.getWriter().write("尚未登录，前往<a href='login.jsp' >登录</a>");
            return;
        }

        try {
            Connection conn = DBConnect.getConn();
            // 创建 PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM fruitinfo WHERE isApear=1 ORDER BY CAST(FruitId AS SIGNED)");
            // 执行查询并获取结果集
            ResultSet resultSet = preparedStatement.executeQuery();
            // 将查询结果存储到List中
            List<FruitInfo> FruitInfo = new ArrayList<>();
            while (resultSet.next()) {
                FruitInfo fruitInfo = new FruitInfo();
                fruitInfo.setFruitId(resultSet.getString("FruitId"));
                fruitInfo.setFruitName(resultSet.getString("FruitName"));
                fruitInfo.setFruitType(resultSet.getString("FruitType"));
                fruitInfo.setFruitIntroduce(resultSet.getString("FruitIntroduce"));
                fruitInfo.setFruitPrice(resultSet.getString("FruitPrice"));
                fruitInfo.setFruitQuantity(resultSet.getString("FruitQuantity"));

                FruitInfo.add(fruitInfo);
            }

            // 将结果存入请求中，以便在JSP中使用
            request.setAttribute("FruitInfo", FruitInfo);

            // 转发到JSP页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("homepage.jsp");
            dispatcher.forward(request, response);


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
