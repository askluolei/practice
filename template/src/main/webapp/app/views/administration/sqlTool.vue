<template>
  <div class="sql-editor">
    <el-row>
      <el-col :span="12">
        <h3>最近执行的 <strong>sql</strong></h3>
        <el-table :data="latestExecuteSqls" @row-dblclick="handleClick">
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="sql" label="sql语句"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="12">
        <h3>最近保存的 <strong>sql</strong></h3>
        <el-table :data="latestSavedSqls" @row-dblclick="handleClick">
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="sql" label="sql语句"></el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-row class="form-wrapper">
      <el-form ref="form" :model="form" :rules="formRules" label-position="left" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="保存sql的标题，保存必填"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="explanation">
          <el-input v-model="form.explanation" placeholder="保存sql的描述"></el-input>
        </el-form-item>
      </el-form>
      <span>SQL</span>
      <textarea ref="textarea"></textarea>
      <el-button-group class="button-group">
        <el-button type="primary" @click="handleExecute">执行</el-button>
        <el-button type="primary" @click="handleExecuteAndSave">保存&执行</el-button>
        <el-button type="primary">下载 CSV</el-button>
      </el-button-group>
    </el-row>
    <el-row>
      <h3 class="result">执行结果</h3>
      <el-table v-if="resultData && resultData.length > 0" :data="resultData">
        <el-table-column v-for="column of resultColumnNames" :key="column" :prop="column" :label="column"></el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/addon/lint/lint.css'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/rubyblue.css'
import 'codemirror/theme/base16-dark.css'
require('script-loader!jsonlint')
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/sql/sql'
import 'codemirror/addon/lint/lint'

import { executeSql, latestExecuteSql, latestSavedSql } from '@/api/administrator'
export default {
  name: 'sqlEditor',
  data() {
    return {
      sqlEditor: false,
      form: {
        title: '',
        explanation: ''
      },
      formRules: {
        title: [
          { required: true, message: '请输入sql标题', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ]
      },
      resultData: [],
      resultColumnNames: [],
      latestExecuteSqls: [],
      latestSavedSqls: []
    }
  },
  props: ['value'],
  watch: {
    value(value) {
      const editor_value = this.sqlEditor.getValue()
      if (value !== editor_value) {
        if (this.value) {
          this.sqlEditor.setValue(JSON.stringify(value, null, 2))
        } else {
          this.sqlEditor.setValue('')
        }
      }
    }
  },
  mounted() {
    this.sqlEditor = CodeMirror.fromTextArea(this.$refs.textarea, {
      lineNumbers: true,
      mode: 'text/x-mysql',
      gutters: ['CodeMirror-lint-markers'],
      theme: 'base16-dark',
      lint: true
    })
    if (this.value) {
      this.sqlEditor.setValue(JSON.stringify(this.value, null, 2))
    }
    this.sqlEditor.setSize('auto', '300px')
    this.sqlEditor.on('change', cm => {
      this.$emit('changed', cm.getValue())
      this.$emit('input', cm.getValue())
    })
  },
  methods: {
    handleClick(row) {
      this.sqlEditor.setValue(row.originSql || row.sql)
    },
    downloadResult() {
      // 下载结果
    },
    handleExecuteAndSave() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 执行并保存
          this.executeSql(true)
        } else {
          return false
        }
      })
    },
    handleExecute() {
      // 执行sql
      this.executeSql(false)
    },
    executeSql(save) {
      executeSql({
        sql: this.getValue(),
        title: this.form.title,
        explanation: this.form.explanation,
        save
      })
        .then(response => {
          const data = response.data
          if (data) {
            if (data instanceof Array) {
              this.resultData = data
              if (data.length > 0) {
                this.resultColumnNames = Object.keys(data[0])
                this.$notify({
                  title: 'sql 查询成功',
                  message: '查询行数：' + data.length,
                  type: 'success'
                })
              }
            } else {
              this.$notify({
                title: 'sql 执行成功',
                message: '影响行数：' + data,
                type: 'success'
              })
            }
            this.getLatestExecuteSqlDefault()
            this.getLatestSavedSqlDefault()
          }
        })
        .catch(error => {
          this.$notify({
            title: 'sql 执行失败',
            message: '失败原因：' + error,
            type: 'error'
          })
        })
    },
    getLatestExecuteSqlDefault() {
      this.getLatestExecuteSql({ page: 0, size: 5, sort: 'createdDate,desc' })
    },
    getLatestSavedSqlDefault() {
      this.getLatestSavedSql({ page: 0, size: 5, sort: 'createdDate,desc' })
    },
    getLatestExecuteSql(page) {
      latestExecuteSql(page)
        .then(response => {
          this.latestExecuteSqls = response.data || []
        })
    },
    getLatestSavedSql(page) {
      latestSavedSql(page)
        .then(response => {
          this.latestSavedSqls = response.data || []
        })
    },
    init() {
      this.getLatestExecuteSqlDefault()
      this.getLatestSavedSqlDefault()
    },
    getValue() {
      return this.sqlEditor.getValue()
    }
  },
  created() {
    this.init()
  }
}
</script>

<style>
.sql-editor {
  margin: 10px 10px;
}
.CodeMirror {
  height: 100%;
}
.json-editor .cm-s-rubyblue span.cm-string {
  color: #F08047;
}
.button-group {
  margin-top: 20px;
}
.form-wrapper {
  margin-top: 10px;
}
</style>
