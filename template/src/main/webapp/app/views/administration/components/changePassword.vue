<template>
  <div class="change-password">
    <el-form :model="form" :rules="formRule" :inline="true">
      <el-form-item prop="password" label="新密码">
        <el-input v-model="form.password" type="password"></el-input>
      </el-form-item>
      <el-form-item prop="passwordAgain" label="重复新密码">
        <el-input v-model="form.passwordAgain" type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="submit">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { changePassword } from '@/api/userManagement'
  export default {
    data() {
      return {
        form: {
          password: '',
          passwordAgain: ''
        },
        formRule: {
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ],
          passwordAgain: [
            { required: true, message: '请重复密码', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      submit() {
        // submit
        if (this.form.password && this.form.passwordAgain && this.form.password === this.form.passwordAgain) {
          changePassword(this.form.password)
            .then(response => {
              this.$message({
                type: 'success',
                message: '修改成功，请重新登录'
              })
              // 重新登录
              this.$store.dispatch('LogOut').then(() => {
                location.reload()// 为了重新实例化vue-router对象 避免bug
              })
            })
            .catch(error => {
              this.$message.error('修改失败' + error)
            })
        } else {
          this.$message({
            type: 'warning',
            message: '请按照要求填写'
          })
        }
      }
    },
    created() {
      // init
    }
  }
</script>

<style lang="scss" scoped>
  .change-password {
    margin: 10px 10px;
  }
</style>
