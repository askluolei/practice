<template>
  <div class="configuration-wrapper">
    <h2 v-text="$t('configuration.title')">Configuration</h2>

    <span v-text="$t('configuration.filter')">Filter (by prefix)</span>
    <el-input v-model="filter"></el-input>
    <label>Spring configuration</label>

    <el-table :data="dataView" stripe v-loading="loading" :element-loading-text="$t('common.loading')" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
      <el-table-column prop="prefix" :label="$t('configuration.table.prefix')"></el-table-column>
      <el-table-column :label="$t('configuration.table.properties')">
        <template slot-scope="prop">
          <el-row v-for="key of Object.keys(prop.row.properties)" :key="key">
            <el-col :span="8">{{key}}</el-col>
            <el-col :span="16">
              <span class="float-right">{{prop.row.properties[key] | morph-json(2) }}</span>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <div v-for="key of Object.keys(env)" :key="key" class="env">
      <el-tag>{{key}}</el-tag>
      <el-table :data="env[key]" stripe>
        <el-table-column prop="key" label="Property"></el-table-column>
        <el-table-column prop="value" label="Value">
          <template slot-scope="prop">
            <span class="float-right">{{prop.row.value}}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  import { configprops, env } from '@/api/administrator'
  export default {
    data() {
      return {
        loading: false,
        configprops: {},
        env: {},
        data: [],
        filter: ''
      }
    },
    computed: {
      dataView() {
        if (this.filter) {
          const result = []
          this.data.forEach(entry => {
            if (entry.prefix.startsWith(this.filter)) {
              result.push(entry)
            }
          })
          return result
        } else {
          return this.data
        }
      }
    },
    methods: {
      init() {
        this.loading = true
        configprops()
          .then(response => {
            const properties = []
            const data = response.data

            for (const key in data) {
              if (data.hasOwnProperty(key)) {
                properties.push(data[key])
              }
            }
            this.data = properties.sort((pA, pB) => {
              return (pA.prefix === pB.prefix) ? 0 : (pA.prefix < pB.prefix) ? -1 : 1
            })
            this.loading = false
          })
          .catch(error => {
            console.log('error', error)
            this.loading = false
          })
        this.loading = true
        env()
          .then(response => {
            const data = response.data
            const properties = {}

            for (const key in data) {
              if (data.hasOwnProperty(key)) {
                const valsObject = data[key]
                const vals = []

                for (const valKey in valsObject) {
                  if (valsObject.hasOwnProperty(valKey)) {
                    vals.push({
                      key: valKey,
                      value: valsObject[valKey]
                    })
                  }
                }
                properties[key] = vals
              }
            }
            this.env = properties
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
  .configuration-wrapper {
    margin: 10px 20px;

    .env {
      margin-top: 10px;
    }
  }
</style>