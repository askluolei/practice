<template>
  <div class="test-wrapper">
    <el-row>
      <h3>代码编辑器</h3>
      <span>选择语言:</span>
      <el-select v-model="language">
        <el-option v-for="item of supportedLanguage" :key="item" :value="item" :label="item"></el-option>
      </el-select>
      <code-editor class="code-editor" height="600" :language="language" :code.sync="code" @codeChange="codeChange"></code-editor>
    </el-row>
    
  </div>
</template>

<script>
  import CodeEditor from '@/components/CodeEditor'
  export default {
    components: {
      'code-editor': CodeEditor
    },
    data() {
      return {
        code: '',
        language: 'javascript',
        supportedLanguage: [
          'bat',
          'c',
          'cpp',
          'css',
          'dockerfile',
          'go',
          'html',
          'ini',
          'java',
          'javascript',
          'json',
          'less',
          'markdown',
          'objective-c',
          'php',
          'plaintext',
          'powershell',
          'python',
          'r',
          'ruby',
          'scss',
          'sql',
          'swift',
          'typescript',
          'vb',
          'xml',
          'yaml'
        ]
      }
    },
    methods: {
      codeChange(code) {
        console.log(code)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .test-wrapper {
    margin: 10px 10px;
    
    .code-editor {
      margin-top: 10px;
    }
  }
</style>
