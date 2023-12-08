<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>购物车 - 欢迎光临Haystack商店</title>
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/element-ui/2.15.14/theme-chalk/index.css">
</head>
<body>

<div id="app">
    <h1>我的购物车</h1>
    <br>
    <br>
    <el-table :default-sort = "{prop: 'FruitId', order: 'ascending'}" height="500" ref="fruitInfoList" :data="fruitInfoList" border>
        <el-table-column sortable width="70" label="id" prop="FruitId"></el-table-column>
        <el-table-column label="商品名称" prop="FruitName"></el-table-column>
        <el-table-column :filters="[{text: '水果', value: '水果'}, {text: '饮品', value: '饮品'}, {text: '调味品', value: '调味品'}, {text: '冰淇淋', value: '冰淇淋'}, {text: '糕点', value: '糕点'}, {text: '糖果', value: '糖果'}]"
                         :filter-method="filterHandler" label="商品类型" prop="FruitType"></el-table-column>
        <el-table-column label="商品介绍" prop="FruitIntroduce"></el-table-column>
        <el-table-column label="商品价格" prop="FruitPrice"></el-table-column>
        <el-table-column label="商品数量" prop="FruitQuantity"></el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
            <template slot-scope="scope">
                <el-button
                        size="mini"
                        @click="editCart(scope.row)">编辑
                </el-button>
                <el-button
                        size="mini"
                        type="danger"
                        @click="deleteCart(scope.row)">删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    </el-table>
    <el-link href="showGoods" target="_blank" type="primary">返回商城</el-link>

    <div class="editCart">
        <el-dialog
                title="编辑数量"
                :visible.sync="editCartDialogVisible"
                width="50%">
            <el-form>
                <el-form-item>
                    <el-input-number v-model="num" :min="1" :max="9999" label="数量"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="editCartDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editCartPost()">确 定</el-button>
  </span>
        </el-dialog>
    </div>

    <div class="deleteCart">
        <el-dialog
                title="删除内容"
                :visible.sync="deleteCartDialogVisible"
                width="50%">
            <span>确定要删除吗？确定后将永久删除该消息，且无法恢复！</span>
            <span slot="footer" class="dialog-footer">
    <el-button @click="deleteCartDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="deleteCartPost()">确 定</el-button>
  </span>
        </el-dialog>
    </div>

</div>

<script src="https://cdn.bootcdn.net/ajax/libs/vue/2.7.9/vue.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/element-ui/2.15.14/index.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script>
    // 使用 Vue.js 实例化一个 Vue 对象
    new Vue({
        el: '#app',
        data: {
            // 在 Vue 实例中定义数据
            fruitInfoList: [
                // 在这里引用 Java 对象的属性
                <c:forEach var="GoodsInfo" items="${GoodsInfo}">
                {
                    FruitId: ${GoodsInfo.fruitId},
                    FruitName: '${GoodsInfo.fruitName}',
                    FruitType: '${GoodsInfo.fruitType}',
                    FruitIntroduce: '${GoodsInfo.fruitIntroduce}',
                    FruitPrice: ${GoodsInfo.fruitPrice},
                    FruitQuantity: ${GoodsInfo.fruitQuantity}
                },
                </c:forEach>
            ],
            editCartDialogVisible: false,
            deleteCartDialogVisible: false,
            num: 1,
        },
        methods: {
            clearFilter() {
                this.$refs.fruitInfoList.clearFilter();
                this.$message({
                    message: '重置完成',
                    type: 'success',
                });
            },
            filterHandler(value, row, column) {
                const property = column['property'];
                return row[property] === value;
            },
            editCart(row) {
                this.num = row.FruitQuantity;
                this.form = {
                    id: row.FruitId,
                    name: row.FruitName,
                    quantity: row.FruitQuantity
                };
                this.editCartDialogVisible = true;
            },
            editCartPost() {
                // 发送POST请求
                axios.post('editCart', {
                    fruitId: this.form.id,
                    fruitName: this.form.name,
                    fruitQuantity: this.form.quantity,
                    newFruitQuantity: this.num,
                }, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then(response => {
                        // 请求成功的处理逻辑
                        this.$message({
                            message: '更新成功，页面即将刷新',
                            type: 'success',
                            // 在消息关闭后再执行刷新操作
                            onClose: () => {
                                // 延迟执行刷新操作
                                setTimeout(() => {
                                    window.location.reload();
                                }, 500);
                            }
                        });
                        this.deleteCartDialogVisible = false;
                    })

                    .catch(error => {
                        // 请求失败的处理逻辑
                        this.$message.error('更新失败');
                    });
            },
            deleteCart(row) {
                this.form = {
                    id: row.FruitId,
                    name: row.FruitName,
                    quantity: row.FruitQuantity
                };
                this.deleteCartDialogVisible = true;
            },
            deleteCartPost() {
                // 发送POST请求
                axios.post('deleteCart', {
                    fruitId: this.form.id,
                    fruitName: this.form.name,
                    fruitQuantity: this.form.quantity
                }, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then(response => {
                        // 请求成功的处理逻辑
                        this.$message({
                            message: '删除成功，页面即将刷新',
                            type: 'success',
                            // 在消息关闭后再执行刷新操作
                            onClose: () => {
                                // 延迟执行刷新操作
                                setTimeout(() => {
                                    window.location.reload();
                                }, 500);
                            }
                        });
                        this.deleteCartDialogVisible = false;
                    })

                    .catch(error => {
                        // 请求失败的处理逻辑
                        this.$message.error('删除失败');
                    });
            },
        }
    });
</script>

</body>
</html>
