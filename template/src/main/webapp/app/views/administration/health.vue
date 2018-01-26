<template>
  <div class="health-wrapper">
    <h2>
      <span>服务状态</span>
      <el-button @click="init" icon="el-icon-refresh" type="primary" class="float-right">刷新</el-button>
    </h2>
    <el-table :data="data">
      <el-table-column prop="key" label="服务名称">
        <template slot-scope="prop">
          {{$t('health.indicator.' + prop.row.key)}}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="prop">
          <el-tag :type="statusInfo(prop.row.status)">{{$t('health.status.' + prop.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="详细情况">
        <template slot-scope="prop">
          <span @click="showHealth(prop.row)"><i class="el-icon-view"></i></span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="show" @close="dialogClose">
      <h4>
        {{currentHealth.key ? $t('health.indicator.' + currentHealth.key) : ''}}
      </h4>
      <el-table :data="currentHealthData" :title="$t('health.details.properties')">
        <el-table-column prop="key" label="名称"></el-table-column>
        <el-table-column prop="value" label="值">
          <template slot-scope="prop">
            {{readableValue(prop.row.value)}}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { health } from '@/api/administrator'
  export default {
    data() {
      return {
        health: {},
        data: [],
        show: false,
        currentHealth: {},
        currentHealthData: []
      }
    },
    methods: {
      readableValue(value) {
        if (this.currentHealth.key !== 'diskSpace') {
          return value
        }

        const val = value / 1073741824
        if (val > 1) { // Value
          return val.toFixed(2) + ' GB'
        } else {
          return (value / 1048576).toFixed(2) + ' MB'
        }
      },
      dialogClose() {
        this.currentHealth = {}
        this.currentHealthData = []
      },
      showHealth(health) {
        this.currentHealth = health
        this.show = true
        Object.keys(this.currentHealth).forEach(k => {
          if (k !== 'key' && k !== 'status') {
            this.currentHealthData.push({
              key: k,
              value: this.currentHealth[k]
            })
          }
        })
      },
      statusInfo(status) {
        if (status === 'UP') {
          return 'success'
        } else if (status === 'DOWN') {
          return 'danger'
        }
      },
      init() {
        health()
          .then(response => {
            if (response.data) {
              this.health = response.data
              this.data = []
              Object.keys(this.health).forEach(key => {
                if (key !== 'status') {
                  this.data.push({
                    key,
                    ...this.health[key]
                  })
                }
              })
            }
          })
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style lang="scss">
  .health-wrapper {
    margin: 10px 20px;
  }
</style>