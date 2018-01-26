<template>
  <div class="schedule-wrapper">
    <h3>调度任务</h3>
    <el-table :data="shceduleTasks" stripe>
      <el-table-column prop="beanName" label="任务处理类"></el-table-column>
      <el-table-column prop="cronExpression" label="调度配置"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 'NORMAL'">{{scope.row.status}}</el-tag>
          <el-tag type="warning" v-else>{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column label="执行日志">
        <template slot-scope="scope">
          <span @click="showLogs(scope.row)"><i class="el-icon-view"></i></span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="logDialogShow" title="调度执行日志">
      <el-table :data="selectedTaskLogs">
        <el-table-column prop="result" label="执行结果">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.result === 'SUCCESS'">{{scope.row.result}}</el-tag>
            <el-tag type="danger" v-else>{{scope.row.result}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="times" label="耗时（ms）"></el-table-column>
        <el-table-column prop="error" label="失败信息"></el-table-column>
      </el-table>
    </el-dialog>
  </div>  
</template>

<script>
  import { getScheduleTasks } from '@/api/administrator'
  export default {
    data() {
      return {
        queryParams: {
          beanName: '',
          status: 'NORMAL'
        },
        queryPage: {
          page: 1,
          total: 0
        },
        shceduleTasks: [],
        selectedTask: undefined,
        logDialogShow: false
      }
    },
    computed: {
      selectedTaskLogs() {
        let result = []
        if (this.selectedTask && this.selectedTask.logs) {
          result = this.selectedTask.logs
        }
        return result
      }
    },
    methods: {
      showLogs(row) {
        this.selectedTask = row
        this.logDialogShow = true
      },
      init() {
        const params = {
          pageNumber: 0,
          pageSize: 10
        }
        getScheduleTasks(params)
          .then(response => {
            this.shceduleTasks = response.data
          })
      }
    },
    mounted() {
      this.init()
    }
  }
</script>

<style lang="scss" scoped>
  .schedule-wrapper {
    margin: 10px 20px;
  }
</style>
