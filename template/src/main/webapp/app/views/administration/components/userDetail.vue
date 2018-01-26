<template>
  <div class="user-detail-wrapper">
    <h2>User [{{user.login}}]
      <el-tag v-if="user.activated" type="success">已激活</el-tag>
      <el-tag v-else type="danger">未激活</el-tag>
    </h2>
    <el-row>
      <el-form v-model="user">
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="用户名" label-width="30%">
            <el-input v-model="user.login" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="名" label-width="30%">
            <el-input v-model="user.firstName" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="姓" label-width="30%">
            <el-input v-model="user.lastName" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="邮箱" label-width="30%">
            <el-input v-model="user.email" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="创建人" label-width="30%">
            <el-input v-model="user.createdBy" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="创建日期" label-width="30%">
            <el-input v-model="user.createdDate" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="最近修改人" label-width="30%">
            <el-input v-model="user.lastModifiedBy" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="最近修改日期" label-width="30%">
            <el-input v-model="user.lastModifiedDate" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="角色" label-width="30%">
            <el-checkbox-group v-model="user.roleNames">
              <el-checkbox disabled v-for="role of user.roleNames" :key="role" :label="role"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  export default {
    props: {
      user: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style lang="scss">
  .user-detail-wrapper {
    dt {
      font-weight: 700;
    }
  }
</style>
