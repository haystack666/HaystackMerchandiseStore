<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>欢迎光临Haystack商店</title>
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/element-ui/2.15.14/theme-chalk/index.css">
</head>
<body>

<div id="app">
    <h1>Haystack百货商店</h1>
    <el-button type="primary" @click="addGoodsDialogVisible = true" plain>新增商品</el-button>
    <el-button type="danger" @click="clearFilter">重置筛选</el-button>
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
                        @click="editMenu(scope.row)">编辑
                </el-button>
                <el-button
                        size="mini"
                        type="danger"
                        @click="deleteMenu(scope.row)">删除
                </el-button>
                <el-button size="mini" type="primary" @click="addCart(scope.row)" plain>加入购物车</el-button>
            </template>
        </el-table-column>
    </el-table>
    </el-table>
    <el-link href="showMyCart" target="_blank" type="primary">查看我的购物车</el-link>

    <div class="addGoods">
        <el-dialog
                title="新增商品"
                :visible.sync="addGoodsDialogVisible"
                width="50%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item :rules="[
      { required: true, message: '请输入商品名称', trigger: 'blur' }]" label="商品名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item :rules="[
      { required: true, message: '请选择商品类型', trigger: 'blur' }]" label="商品类型">
                    <el-select v-model="form.type" placeholder="请选择商品类型">
                        <el-option label="水果" value="水果"></el-option>
                        <el-option label="饮品" value="饮品"></el-option>
                        <el-option label="调味品" value="调味品"></el-option>
                        <el-option label="冰淇淋" value="冰淇淋"></el-option>
                        <el-option label="糕点" value="糕点"></el-option>
                        <el-option label="糖果" value="糖果"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item :rules="[
      { required: true, message: '请输入商品介绍', trigger: 'blur' }]" label="商品介绍">
                    <el-input v-model="form.intro"></el-input>
                </el-form-item>
                <el-form-item :rules="[
      { required: true, message: '请输入商品价格', trigger: 'blur' }]" label="商品价格">
                    <el-input v-model="form.price"></el-input>
                </el-form-item>
                <el-form-item :rules="[
      { required: true, message: '请输入商品数量', trigger: 'blur' }]" label="商品数量">
                    <el-input v-model="form.quantity"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="addGoodsDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addGoodsPost()">确 定</el-button>
  </span>
        </el-dialog>
    </div>

    <div class="editInfo">
        <el-dialog
                title="修改信息"
                :visible.sync="editDialogVisible"
                width="50%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="id">
                    <el-input :disabled="true" v-model="form.id"></el-input>
                </el-form-item>
                <el-form-item label="水果名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="水果类型">
                    <el-select v-model="form.type" placeholder="请选择水果类型">
                        <el-option label="水果" value="水果"></el-option>
                        <el-option label="饮品" value="饮品"></el-option>
                        <el-option label="调味品" value="调味品"></el-option>
                        <el-option label="冰淇淋" value="冰淇淋"></el-option>
                        <el-option label="糕点" value="糕点"></el-option>
                        <el-option label="糖果" value="糖果"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="水果介绍">
                    <el-input v-model="form.intro"></el-input>
                </el-form-item>
                <el-form-item label="水果价格">
                    <el-input v-model="form.price"></el-input>
                </el-form-item>
                <el-form-item label="水果数量">
                    <el-input v-model="form.quantity"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editMenuPost()">确 定</el-button>
  </span>
        </el-dialog>
    </div>

    <div class="deleteInfo">
        <el-dialog
                title="删除内容"
                :visible.sync="deleteDialogVisible"
                width="50%">
            <span>确定要删除吗？确定后将永久删除该消息，且无法恢复！</span>
            <span slot="footer" class="dialog-footer">
    <el-button @click="deleteDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="deleteMenuPost()">确 定</el-button>
  </span>
        </el-dialog>
    </div>

    <div class="addCart">
        <el-dialog
                title="加入购物车"
                :visible.sync="addCartDialogVisible"
                width="50%">
            <span>确定要将其加入购物车吗？</span>
            <br>
            <br>
            <el-form>
                <el-form-item>
                    <el-input-number v-model="num" @change="handleChange" :min="1" :max="9999" label="数量"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="addCartDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addCartPost()">确 定</el-button>
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
                <c:forEach var="fruitInfo" items="${FruitInfo}">
                {
                    FruitId: ${fruitInfo.fruitId},
                    FruitName: '${fruitInfo.fruitName}',
                    FruitType: '${fruitInfo.fruitType}',
                    FruitIntroduce: '${fruitInfo.fruitIntroduce}',
                    FruitPrice: ${fruitInfo.fruitPrice},
                    FruitQuantity: ${fruitInfo.fruitQuantity}
                },
                </c:forEach>
            ],
            addGoodsDialogVisible: false,
            editDialogVisible: false,
            deleteDialogVisible: false,
            addCartDialogVisible: false,
            num: 1,
            form: {
                id: '',
                name: '',
                type: '水果',
                intro: '',
                price: '',
                quantity: ''
            }
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
            addGoodsPost() {
                // 校验表单数据，如果有错误则不发送请求
                this.$refs['form'].validate(valid => {
                    if (!valid) {
                        return;
                    }

                    // 发送POST请求
                    axios.post('addGoods', {
                        name: this.form.name,
                        type: this.form.type,
                        intro: this.form.intro,
                        price: this.form.price,
                        quantity: this.form.quantity
                    }, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    })
                        .then(response => {
                            // 请求成功的处理逻辑
                            // 根据后端返回的信息显示不同的提示
                            const responseData = response.data.trim(); // 去除两端空格
                            if (responseData === '添加成功') {
                                this.$message({
                                    message: '添加成功，页面即将刷新',
                                    type: 'success',
                                    // 在消息关闭后再执行刷新操作
                                    onClose: () => {
                                        // 延迟执行刷新操作
                                        setTimeout(() => {
                                            window.location.reload();
                                        }, 500);
                                    }
                                });
                                this.addGoodsDialogVisible = false;
                            } else if (responseData === '添加失败，商品已存在'){
                                this.$message.error('添加失败，商品已存在');
                            }
                        })
                        .catch(error => {
                            // 请求失败的处理逻辑
                            this.$message.error('添加出错，商品已存在');
                        });
                });
            },
            editMenu(row) {
                this.form = {
                    id: row.FruitId,
                    name: row.FruitName,
                    type: row.FruitType,
                    intro: row.FruitIntroduce,
                    price: row.FruitPrice,
                    quantity: row.FruitQuantity
                };
                this.editDialogVisible = true;
            },
            editMenuPost() {
                // 发送POST请求
                axios.post('updataFruit', {
                    fruitId: this.form.id,
                    name: this.form.name,
                    type: this.form.type,
                    intro: this.form.intro,
                    price: this.form.price,
                    quantity: this.form.quantity
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
                        this.editDialogVisible = false;
                    })

                    .catch(error => {
                        // 请求失败的处理逻辑
                        this.$message.error('更新出错');
                    });
            },
            deleteMenu(row) {
                this.form = {
                    id: row.FruitId
                };
                this.deleteDialogVisible = true;
            },
            deleteMenuPost() {
                // 发送POST请求
                axios.post('deleteFruit', {
                    fruitId: this.form.id
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
                        this.deleteDialogVisible = false;
                    })

                    .catch(error => {
                        // 请求失败的处理逻辑
                        this.$message.error('删除失败');
                    });
            },
            handleChange(value) {
                console.log(value);
            },
            addCart(row) {
                this.form = {
                    id: row.FruitId,
                    name: row.FruitName,
                    type: row.FruitType,
                    intro: row.FruitIntroduce,
                    price: row.FruitPrice,
                    quantity: row.FruitQuantity
                };
                this.addCartDialogVisible = true;
            },
            addCartPost(row) {
                // 发送POST请求
                axios.post('addCart', {
                    fruitId: this.form.id,
                    name: this.form.name,
                    type: this.form.type,
                    intro: this.form.intro,
                    price: this.form.price,
                    quantity: this.num
                }, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then(response => {
                        // 请求成功的处理逻辑
                        this.$message({
                            message: '加入购物车成功',
                            type: 'success',
                            // 在消息关闭后再执行刷新操作
                            onClose: () => {
                                // 延迟执行刷新操作
                                setTimeout(() => {
                                    window.location.reload();
                                }, 500);
                            }
                        });
                        this.addCartDialogVisible = false;
                    })

                    .catch(error => {
                        // 请求失败的处理逻辑
                        this.$message.error('加入购物车失败');
                    });
            }
        }
    });
</script>

</body>
</html>
