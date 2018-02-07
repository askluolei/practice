<template>
  <div class="json-editor">
    <el-form :model="form" :rules="formRules" label-position="left" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description"></el-input>
      </el-form-item>
    </el-form>
    <span>SQL</span>
    <textarea ref="textarea"></textarea>
    <el-button-group class="button-group">
      <el-button type="primary">保存&执行</el-button>
      <el-button type="primary">下载 CSV</el-button>
    </el-button-group>
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

export default {
  name: 'sqlEditor',
  data() {
    return {
      sqlEditor: false,
      form: {
        title: '',
        description: ''
      },
      formRules: {
        title: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ]
      }
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
    getValue() {
      return this.sqlEditor.getValue()
    }
  }
}
</script>

<style>
.CodeMirror {
  height: 100%;
}
.json-editor .cm-s-rubyblue span.cm-string {
  color: #F08047;
}
.button-group {
  margin-top: 20px;
}
</style>
