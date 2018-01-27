<template>
  <div class="schedule-from-wrapper">
    <h3>新增调度</h3>
    <el-row>
      <el-form :model="scheduleForm">
        <el-form-item prop="beanName" label="调度实现类">
          <el-autocomplete v-model="scheduleForm.beanName" :fetch-suggestions="queryBeanList"
            placeholder="请输入 beanName" ></el-autocomplete>
        </el-form-item>
        <el-form-item prop="status" lang="状态">
          <el-switch v-model="scheduleForm.status" active-text="正常" inactive-text="暂停"></el-switch>
        </el-form-item>
        <el-form-item prop="cronExpression" label="cron 表达式">
          <el-input v-model="scheduleForm.cronExpression"></el-input>
        </el-form-item>
        <el-form-item prop="remark" label="备注">
          <el-input v-model="scheduleForm.remark"></el-input>
        </el-form-item>
      </el-form>
    </el-row>
  </div>  
</template>

<script>
  export default {
    props: {
      scheduleTask: {
        type: Object,
        require: false
      }
    },
    data() {
      return {
        scheduleForm: {
          beanName: '',
          status: 'NORMAL',
          cronExpression: '',
          remark: ''
        }
      }
    },
    methods: {
      queryBeanList() {
        // bean list
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
