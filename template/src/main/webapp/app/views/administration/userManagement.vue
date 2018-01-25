<template>
  <div class="user-manage-wrapper">
    <h2>
      <span>用户</span>
      <el-button type="primary" class="float-right" icon="el-icon-plus" @click="showAddDialog">新增用户</el-button>
    </h2>
    <el-table :data="data" stripe>
      <el-table-column prop="id" label="主键" sortable>
        <template slot-scope="prop">
          <a @click.prevent="showViewDialog(prop.row)">{{prop.row.id}}</a>
        </template>
      </el-table-column>
      <el-table-column prop="login" label="用户名" sortable></el-table-column>
      <el-table-column prop="email" label="邮箱" sortable></el-table-column>
      <el-table-column prop="activated" label="激活状态">
        <template slot-scope="prop">
          <el-switch v-model="prop.row.activated" @change="activatedChange(prop.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="roleNames" label="角色">
        <template slot-scope="prop">
          <el-tag size="small" class="role-tag" v-for="role of prop.row.roleNames" :key="role">{{role}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdDate" label="创建时间" sortable>
        <template slot-scope="prop">
          {{prop.row.createdDate | formatDateTimeStr}}
        </template>
      </el-table-column>
      <el-table-column prop="lastModifiedDate" label="上次修改时间" sortable>
        <template slot-scope="prop">
          {{prop.row.lastModifiedDate | formatDateTimeStr}}
        </template>
      </el-table-column>
      <el-table-column prop="lastModifiedBy" label="修改人" sortable></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="prop">
          <el-button-group>
            <el-button type="info" icon="el-icon-view" @click="showViewDialog(prop.row)"></el-button>
            <el-button type="warning" icon="el-icon-edit" @click="showEditDialog(prop.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="showDeleteConform(prop.row)"></el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="page.currentPage" :page-size="page.pageSize" :total="page.total" @current-change="currentPageChange"></el-pagination>
    <el-dialog :visible.sync="viewDialog">
      <user-detail :user="selectedUser"></user-detail>
    </el-dialog>
    <el-dialog :visible.sync="addDialog">
      <user-add @submit="addUser" :visible.sync="addDialog"></user-add>
    </el-dialog>
    <el-dialog :visible.sync="editDialog">
      <user-add @submit="updateUser" :visible.sync="editDialog" :editUser="selectedUser"></user-add>
    </el-dialog>
  </div>
</template>

<script>
import userDetail from './components/userDetail'
import userAdd from './components/userAdd'
import { userList, addUser, updateUser, getUserByLogin, deleteUserByLogin } from '@/api/userManagement'

export default {
  components: {
    'user-detail': userDetail,
    'user-add': userAdd
  },
  data() {
    return {
      data: [],
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1
      },
      selectedUser: {},
      viewDialog: false,
      addDialog: false,
      editDialog: false
    }
  },
  methods: {
    queryUser(params) {
      userList(params)
        .then(response => {
          if (response.data) {
            this.data = response.data
          }
        })
    },
    addUser(data) {
      addUser(data)
        .then(response => {
          this.init()
        })
    },
    updateUser(data) {
      updateUser(data)
        .then(response => {
          this.init()
        })
    },
    showDeleteConform(user) {
      getUserByLogin(user.login)
        .then(response => {
          const toDeletedUser = response.data
          this.$confirm('是否要删除用户：' + toDeletedUser.login, '删除操作确认',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(() => {
              deleteUserByLogin(toDeletedUser.login)
                .then(response => {
                  this.$message({
                    type: 'success',
                    message: '删除成功!'
                  })
                  this.init()
                })
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              })
            })
        })
    },
    showEditDialog(user) {
      this.selectedUser = user
      this.editDialog = true
    },
    showAddDialog() {
      this.addDialog = true
    },
    showViewDialog(user) {
      this.selectedUser = user
      this.viewDialog = true
    },
    activatedChange(user) {
      this.updateUser(user)
    },
    // 当前页改变，从后台获取数据
    currentPageChange(currentPage) {
      if (this.page.currentPage === currentPage) {
        return
      }
      this.page.currentPage = currentPage
      this.queryUser({
        page: this.page.currentPage - 1,
        size: this.page.pageSize,
        sort: 'id,asc'
      })
    },
    init() {
      this.queryUser({
        page: 0,
        size: this.page.pageSize,
        sort: 'id,asc'
      })
      this.page.total = this.data.length
    }
  },
  created() {
    this.init()
  }
}
</script>

<style lang="scss">
@import "src/main/webapp/app/styles/index.scss";
.user-manage-wrapper {
  margin: 10px 20px;
  h2 {
    display: block;
    font-size: 1.5em;
    -webkit-margin-before: 0.83em;
    -webkit-margin-after: 0.83em;
    -webkit-margin-start: 0px;
    -webkit-margin-end: 0px;
    font-weight: bold;
  }

  .role-tag {
    margin-top: 5px;
  }
}
</style>
