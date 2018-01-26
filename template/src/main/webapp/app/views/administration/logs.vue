<template>
  <div class="log-wrapper">
    <h2 v-text="$t('logs.title')">Logs</h2>
    <span v-text="$t('logs.filter')">Filter</span>
    <el-input v-model="filter"></el-input>
    <el-table :data="loggersView" stripe v-loading="loading" :element-loading-text="$t('common.loading')" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
      <el-table-column prop="name" :label="$t('logs.table.name')"></el-table-column>
      <el-table-column prop="level" :label="$t('logs.table.level')">
        <template slot-scope="prop">
          <el-button @click="changeLevel(prop.row.name, 'TRACE')" :type="prop.row.level === 'TRACE' ? 'danger': ''">TRACE</el-button>
          <el-button @click="changeLevel(prop.row.name, 'DEBUG')" :type="prop.row.level === 'DEBUG' ? 'warning': ''">DEBUG</el-button>
          <el-button @click="changeLevel(prop.row.name, 'INFO')" :type="prop.row.level === 'INFO' ? 'info': ''">INFO</el-button>
          <el-button @click="changeLevel(prop.row.name, 'WARN')" :type="prop.row.level === 'WARN' ? 'success': ''">WARN</el-button>
          <el-button @click="changeLevel(prop.row.name, 'ERROR')" :type="prop.row.level === 'ERROR' ? 'primary': ''">ERROR</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { logs, modifyLogs } from '@/api/administrator'
  export default {
    data() {
      return {
        loading: false,
        loggers: [],
        filter: ''
      }
    },
    computed: {
      loggersView() {
        if (this.filter) {
          const result = []
          this.loggers.forEach(log => {
            if (log.name.startsWith(this.filter)) {
              result.push(log)
            }
          })
          return result
        } else {
          return this.loggers
        }
      }
    },
    methods: {
      changeLevel(name, level) {
        modifyLogs({ name, level })
          .then(response => {
            this.init()
          })
      },
      init() {
        // init
        this.loading = true
        logs()
          .then(response => {
            const data = response.data
            this.loggers = data
            this.loading = false
          })
          .catch(error => {
            console.log('error', error)
            this.loading = false
          })
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style lang="scss">
  .log-wrapper {
    margin: 10px 20px;
  }
</style>