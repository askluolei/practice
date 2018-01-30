<template>
  <div class="schedule-wrapper">
    <h3>调度任务</h3>
    <el-button @click="handleAddClick">新增调度任务</el-button>
    <el-table :data="shceduleTasks" stripe>
      <el-table-column prop="beanName" label="任务处理类"></el-table-column>
      <el-table-column prop="cronExpression" label="调度配置"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 'NORMAL'">{{scope.row.status}}</el-tag>
          <el-tag type="warning" v-else>{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nextFireTime" label="下次触发时间"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column label="执行日志">
        <template slot-scope="scope">
          <span @click="showLogs(scope.row)"><i class="el-icon-view"></i></span>
        </template>
      </el-table-column>
      <el-table-column label="">
        <template slot-scope="scope">
          <el-button @click="handleEditClick(scope.row)">编辑</el-button>
          <el-button @click="handleDelete(scope.row.id)">删除</el-button>
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

    <el-dialog title="调度配置" :visible.sync="editDialogShow">
      <el-form :model="editForm" label-position="top" :rules="editFormRule">
        <el-form-item prop="beanName" label="调度类">
          <el-autocomplete class="bean-input" v-model="editForm.beanName" :fetch-suggestions="querySearchBeanName" valueKey="beanName">
            <template slot-scope="props">
              <div class="name">类名:{{ props.item.beanName }}</div>
              <span class="addr">调度说明:{{ props.item.explain }}</span>
            </template>
          </el-autocomplete>
          <!-- <el-select v-model="editForm.beanName">
            <el-option v-for="item of scheduleBeans" :key="item.beanName" :label="item.explain + ' (' + item.beanName + ')'" :value="item.beanName"></el-option>
          </el-select> -->
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-select v-model="editForm.status">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="暂停" value="PAUSE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="cronExpression" label="定时策略">
          <el-popover v-model="cronPopover">
            <cron @change="changeCron" @close="cronPopover=false" :data="editForm.cronExpression"></cron>
            <el-input slot="reference" @click="cronPopover=true" v-model="editForm.cronExpression" placeholder="请输入定时策略"></el-input>
          </el-popover>
        </el-form-item>
        <el-form-item prop="params" label="参数">
          <el-input v-model="editForm.params" type="textarea" :rows="5" placeholder="请输入参数"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>  
</template>

<script>
  import { getScheduleTasks, getScheduleTaskLogs, getScheduleBeans, addScheduleTask, updateScheduleTask, deleteScheduleTask } from '@/api/administrator'
  import Cron from '@/components/Cron'
  export default {
    components: {
      cron: Cron
    },
    data() {
      return {
        scheduleBeans: [],
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
        },
        editForm: {
          id: '',
          beanName: '',
          status: 'NORMAL',
          cronExpression: '',
          params: ''
        },
        editFormRule: {
          beanName: [
            { required: true, message: '请选择调度类', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '请选择状态', trigger: 'blur' }
          ],
          cronExpression: [
            { required: true, message: '请输入调度表达式', trigger: 'blur' }
          ]
        },
        editFormType: 'edit',
        cronPopover: false,
        editDialogShow: false
      }
    },
    computed: {
    },
    methods: {
      handleDelete(id) {
        this.$confirm('此操作将删除该调度配置,不可回滚, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteScheduleTask(id)
            .then(response => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.getScheduleTasks()
            })
            .catch(error => {
              this.$message({
                type: 'success',
                message: '删除失败:' + error
              })
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      handleAddClick() {
        this.editForm.beanName = ''
        this.editForm.status = ''
        this.editForm.cronExpression = ''
        this.editForm.id = ''
        this.editFormType = 'add'
        this.editDialogShow = true
      },
      querySearchBeanName(queryString, cb) {
        const matchedBeans = this.scheduleBeans.filter(this.createBeanFilter(queryString))
        cb(matchedBeans)
      },
      createBeanFilter(queryString) {
        return (bean) => {
          let match = false
          match = match || bean.beanName.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
          match = match || bean.explain.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
          match = match || bean.author.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
          return match
        }
      },
      changeCron(cron) {
        this.editForm.cronExpression = cron
      },
      handleEditClick(row) {
        this.editForm.beanName = row.beanName
        this.editForm.status = row.status
        this.editForm.cronExpression = row.cronExpression
        this.editForm.id = row.id
        this.editFormType = 'edit'
        this.editDialogShow = true
      },
      handleSave() {
        if (this.editFormType === 'edit') {
          this.updateTask()
        } else {
          this.addTask()
        }
      },
      addTask() {
        addScheduleTask(this.editForm)
          .then(response => {
            this.$message({
              type: 'success',
              message: '添加调度任务成功'
            })
            this.getScheduleTasks()
            this.editDialogShow = false
          })
      },
      updateTask() {
        updateScheduleTask(this.editForm)
          .then(response => {
            this.$message({
              type: 'success',
              message: '修改调度任务成功'
            })
            this.getScheduleTasks()
            this.editDialogShow = false
          })
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
      getScheduleBeans() {
        getScheduleBeans()
          .then(response => {
            this.scheduleBeans = response.data
          })
      },
      init() {
        this.getScheduleTasks(this.taskPagination)
        this.getScheduleBeans()
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
    
    .bean-input {
      width: 100%;

      .name {
        text-overflow: ellipsis;
        overflow: hidden;
      }
      .addr {
        font-size: 12px;
        color: #b4b4b4;
      }
    }
  }

  .json-editor {
    font-size: 14px;
  }
</style>
