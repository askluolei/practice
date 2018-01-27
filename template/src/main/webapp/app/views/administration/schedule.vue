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
      <el-table-column label="">
        <template slot-scope="scope">
          <el-button @click="handleEditClick(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="taskPagination.page + 1" :page-size="taskPagination.size" :total="taskPagination.total" @current-change="tasksPageChange"></el-pagination>

    <el-dialog :visible.sync="logDialogShow" title="调度执行日志">
      <el-table :data="taskLogs">
        <el-table-column prop="result" label="执行结果">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.result === 'SUCCESS'">{{scope.row.result}}</el-tag>
            <el-tag type="danger" v-else>{{scope.row.result}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="times" label="耗时（ms）"></el-table-column>
        <el-table-column prop="error" label="失败信息"></el-table-column>
      </el-table>
      <el-pagination :current-page="logsPagination.page + 1" :page-size="logsPagination.size" :total="logsPagination.total" @current-change="logsPageChange"></el-pagination>
    </el-dialog>
  </div>  
</template>

<script>
  import { getScheduleTasks, getScheduleTaskLogs } from '@/api/administrator'
  import Cron from '@/components/Cron'
  export default {
    components: {
      cron: Cron
    },
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
        taskPagination: {
          page: 0,
          size: 10,
          total: 0
        },
        selectedRow: undefined,
        logDialogShow: false,
        taskLogs: [],
        logsPagination: {
          page: 0,
          size: 10,
          total: 0
        }
      }
    },
    computed: {
    },
    methods: {
      handleEditClick(row) {
        console.log(row)
      },
      logsPageChange(currentPage) {
        this.logsPagination.page = currentPage - 1
        const id = this.selectedRow.id
        this.getScheduleTaskLogs(id, this.logsPagination)
      },
      showLogs(row) {
        this.selectedRow = row
        const id = row.id
        this.getScheduleTaskLogs(id, this.logsPagination)
      },
      getScheduleTaskLogs(id, params) {
        getScheduleTaskLogs(id, params)
          .then(response => {
            this.taskLogs = response.data || []
            this.logDialogShow = true
            const total = response.headers['x-total-count']
            this.logsPagination.total = total ? Number(total) : response.data.length
          })
      },
      getScheduleTasks(params) {
        getScheduleTasks(params)
          .then(response => {
            this.shceduleTasks = response.data
            const total = response.headers['x-total-count']
            this.taskPagination.total = total ? Number(total) : response.data.length
          })
      },
      tasksPageChange(currentPage) {
        this.taskPagination.page = currentPage - 1
        this.getScheduleTasks(this.taskPagination)
      },
      init() {
        this.getScheduleTasks(this.taskPagination)
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
