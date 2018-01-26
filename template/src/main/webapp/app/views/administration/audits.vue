<template>
  <div class="audits-wrapper">
    <h2 v-text="$t('audits.title')">Audits</h2>
    <el-row>
        <h4 v-text="$t('audits.filter.title')">Filter by date</h4>
        <p>
          <span v-text="$t('audits.filter.from')">from</span>
          <el-date-picker @change="dateChange" v-model="fromDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
          <span v-text="$t('audits.filter.to')">to</span>
          <el-date-picker @change="dateChange" v-model="toDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </p>
    </el-row>
    <el-table :data="data">
      <el-table-column prop="timestamp" :label="$t('audits.table.header.date')"></el-table-column>
      <el-table-column prop="principal" :label="$t('audits.table.header.principal')"></el-table-column>
      <el-table-column prop="type" :label="$t('audits.table.header.status')"></el-table-column>
      <el-table-column prop="data.message" :label="$t('audits.table.header.data')"></el-table-column>
    </el-table>
    <el-pagination :current-page.sync="page.currentPage" :total="page.total" @current-change="pageChange"></el-pagination>
  </div>
</template>

<script>
  import { audits } from '@/api/administrator'
  import { formatDate } from '@/utils/date'
  const FORMAT = 'yyyy-MM-dd'
  export default {
    data() {
      return {
        data: [],
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        fromDate: '',
        toDate: ''
      }
    },
    methods: {
      pageChange() {
        this.query()
      },
      dateChange() {
        this.query()
      },
      query(para) {
        const currentPara = {
          fromDate: this.fromDate,
          toDate: this.toDate,
          page: this.page.currentPage - 1,
          size: this.page.pageSize
        }
        const params = para || currentPara
        audits(params)
          .then(response => {
            const data = response.data
            this.page.total = Number(response.headers['x-total-count'])
            this.data = data
          })
      },
      init() {
        this.toDate = formatDate(new Date(), FORMAT)
        let previousMonth = new Date()
        if (previousMonth.getMonth === 0) {
          previousMonth = new Date(previousMonth.getFullYear() - 1, 11, previousMonth.getDate())
        } else {
          previousMonth = new Date(previousMonth.getFullYear(), previousMonth.getMonth() - 1, previousMonth.getDate())
        }
        this.fromDate = formatDate(previousMonth, FORMAT)
        this.query({
          fromDate: this.fromDate,
          toDate: this.toDate,
          page: 0,
          size: this.page.pageSize
        })
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style lang="scss">
  .audits-wrapper {
    margin: 10px 20px;
  }
</style>