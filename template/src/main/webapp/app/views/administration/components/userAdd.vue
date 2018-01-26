<template>
  <div class="user-add">
    <h2>{{title}}</h2>
    <el-row>
      <el-form :model="user" :rules="userRule" ref="form">
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="login" label="用户名" label-width="30%">
            <el-input v-model="user.login"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="firstName" label="名" label-width="30%">
            <el-input v-model="user.firstName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="lastName" label="姓" label-width="30%">
            <el-input v-model="user.lastName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="email" label="邮箱" label-width="30%">
            <el-input v-model="user.email"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="imageUrl" label="头像" label-width="30%">
            <el-upload :multiple="false" :action="actionUrl" @before-upload="preUpload" @on-success="imageUploadSuccess" @on-error="imageUploadFail" list-type="picture">
              <el-button size="small" type="primary">头像上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="activated" label="激活状态" label-width="30%">
            <el-switch v-model="user.activated"></el-switch>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item prop="roleNames" label="角色" label-width="30%">
            <el-checkbox-group v-model="user.roleNames" :min="1">
              <el-checkbox v-for="role of roleList" :key="role" :label="role"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24" :push="4">
          <el-form-item>
            <el-button type="primary" @click="onSubmit">{{submitText}}</el-button>
            <el-button @click="closeDialog">取消</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import { roleList } from '@/api/userManagement'
  import { validateEmail } from '@/utils/validate'
  export default {
    props: {
      editUser: {
        type: Object,
        required: false
      },
      // 允许内部关闭dialog
      visible: {
        type: Boolean,
        required: false
      }
    },
    data() {
      const emailRule = (rule, value, callback) => {
        if (!validateEmail(value)) {
          callback(new Error('请输入正确的邮箱'))
        } else {
          callback()
        }
      }
      return {
        roleList: [],
        user: {
          login: '',
          firstName: '',
          lastName: '',
          email: '',
          imageUrl: '',
          activated: '',
          roleNames: []
        },
        userRule: {
          login: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
          ],
          firstName: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur' }
          ],
          lastName: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur' }
          ],
          email: [
            { required: true, trigger: 'blur', validator: emailRule }
          ]
        },
        actionUrl: 'upload',
        title: '新增用户',
        submitText: '立即创建'
      }
    },
    methods: {
      closeDialog() {
        this.$emit('update:visible', false)
        this.reset()
      },
      preUpload(file) {
        // 上传之前，检查一下
      },
      imageUploadSuccess(response, file, fileList) {
        // 头像上传成功 设计到文件上传的，先通过上传接口上传，成功后返回可访问的链接
      },
      imageUploadFail(err, file, fileList) {
        // 头像上传失败
        console.log('上传失败', err)
      },
      onSubmit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            // 表单提交，触发事件，外面处理
            this.$emit('submit', this.user)
            this.closeDialog()
            console.log('表单提交', this.user)
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      resetForm() {
        this.$refs.form.resetFields()
      },
      init() {
        if (this.$props.editUser) {
          // 这里使用深复制 防止这里修改，导致外面的数据变化
          this.user = this._.cloneDeep(this.$props.editUser)
          this.title = '编辑用户'
          this.submitText = '提交修改'
        }
        roleList()
          .then(response => {
            if (response.data) {
              this.roleList = response.data
            }
          })
      },
      reset() {
        this.user = {}
        this.title = '新增用户'
        this.submitText = '立即创建'
      }
    },
    watch: {
      editUser: function(val, oldVal) {
        this.init()
      }
    },
    mounted() {
      // init
      console.log('mounted')
      this.init()
    },
    created() {
      console.log('created')
    }
  }
</script>

<style lang="scss">
  .user-add {

  }
</style>
