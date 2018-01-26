<template>
  <div class="user-tracker-wrapper">
    <h2>用户实时状态</h2>
    <el-table :data="data">
      <el-table-column prop="userLogin" label="用户"></el-table-column>
      <el-table-column prop="ipAddress" label="IP"></el-table-column>
      <el-table-column prop="page" label="当前页"></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        data: []
      }
    },
    methods: {
      init() {

      }
    },
    created() {
      this.init()
    },
    socket: {
      events: {
        '/topic/tracker': function(response) {
          console.log('接收到数据', response)
          this.data.push(JSON.parse(response.body))
        }
      }
    }
  }
</script>

<style lang="scss">
  .user-tracker-wrapper {
    margin: 10px 20px;
  }
</style>