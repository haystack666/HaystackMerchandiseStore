<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录/注册 - 欢迎光临Haystack商店</title>
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/element-ui/2.15.14/theme-chalk/index.css">
    <style>
        body {
            background: url("https://api.vvhan.com/api/bing?rand=sj");
            background-attachment: fixed;
            background-repeat: no-repeat;
            background-size: cover;
        }

        .box-card {
            width: 30%;
            margin: 0 auto;
            margin-top: 10%;
        }

        .loginButton {
            width: 100%;
        }
    </style>
</head>
<body>
<div class="mainContent" id="app">
    <el-card class="box-card">
        <h1>登录</h1>
        <p style="color: rgb(128,128,128); margin-top: -15px; font-size: 12px">若没有账号将自动创建账号</p>
        <br>
        <br>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
            <el-form-item prop="username">
                <el-input placeholder="请输入用户名" v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input placeholder="请输入密码" v-model="ruleForm.password" show-password></el-input>
            </el-form-item>
            <br>
            <br>
            <el-form-item>
                <el-button class="loginButton" @click="loginVerify" type="primary">登录</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</div>

<script src="https://cdn.bootcdn.net/ajax/libs/vue/2.7.9/vue.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/element-ui/2.15.14/index.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
    new Vue({
        el: '#app',
        data: {
            ruleForm: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'}
                ],
            },
        },
        methods: {
            loginVerify() {
                // 使用 Axios 发送 POST 请求
                axios.post('loginVerify', {
                    username: this.ruleForm.username,
                    password: this.ruleForm.password
                }, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then(response => {
                        // 处理后端返回的响应
                        console.log(response.data);

                        // 根据后端返回的信息执行相应的操作
                        if (response.data === '登录成功') {
                            // 用户已存在，执行相应的操作
                            this.$message({
                                message: '欢迎回来！',
                                duration: 2000,
                                type: 'success',
                                // 在消息关闭后再执行刷新操作
                                onClose: () => {
                                    // 延迟执行刷新操作
                                    setTimeout(() => {
                                        window.location.href="showGoods";
                                    }, 500);
                                }
                            });
                        } else if (response.data === '用户注册成功') {
                            // 用户注册成功，执行相应的操作
                            this.$message({
                                message: '注册成功！',
                                duration: 2000,
                                type: 'success',
                                // 在消息关闭后再执行刷新操作
                                onClose: () => {
                                    // 延迟执行刷新操作
                                    setTimeout(() => {
                                        window.location.href="showGoods";
                                    }, 500);
                                }
                            });
                        } else if (response.data === '密码错误，请重新输入') {
                            // 密码错误
                            this.$message({
                                message: '密码错误，请重新输入！',
                                duration: 2000,
                                type: 'error',
                            });
                        } else {
                            // 其他情况
                        }
                    })
                    .catch(error => {
                        // 处理请求错误
                        console.error(error);
                    });

            }
        }
    })
</script>
</body>
</html>
