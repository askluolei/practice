<template>
  <div class="dump-wrapper">
    <span @click="changeState('ALL')"><el-tag >ALL&nbsp;{{threadDumpAll}}</el-tag></span>
    <span @click="changeState('RUNNABLE')"><el-tag type="success">Runnable&nbsp;{{threadDumpRunnable}}</el-tag></span>
    <span @click="changeState('WAITING')"><el-tag type="info">Waiting &nbsp;{{threadDumpWaiting}}</el-tag></span>
    <span @click="changeState('TIMED_WAITING')"><el-tag type="warning">Timed Waiting&nbsp;{{threadDumpTimedWaiting}}</el-tag></span>
    <span @click="changeState('BLOCKED')"><el-tag type="danger">Blocked &nbsp;{{threadDumpBlocked}}</el-tag></span>

    <el-row v-for="thread of threadViews" :key="thread.threadId">
      <h6>
        <span>
          <el-tag :type="getType(thread.threadState)">{{thread.threadState}}</el-tag>
          {{thread.threadName}}(ID {{thread.threadId}})
        </span>
        <a>
          <span v-if="thread.show" @click="showStack(thread)">显示</span>
          <span v-if="!thread.show" @click="hideStack(thread)">隐藏</span>
        </a>
      </h6>
      <el-card v-if="!thread.show">
        <div v-for="st of thread.stackTrace">
          <samp>{{st.className}}.{{st.methodName}}(<code>{{st.fileName}}:{{st.lineNumber}}</code>)</samp>
        </div>
      </el-card>
      <el-table :data="[thread]">
        <el-table-column prop="blockedTime" label="阻塞时间"></el-table-column>
        <el-table-column prop="blockedCount" label="阻塞次数"></el-table-column>
        <el-table-column prop="waitedTime" label="等待时间"></el-table-column>
        <el-table-column prop="waitedCount" label="等待次数"></el-table-column>
        <el-table-column prop="lockName" label="锁名称">
          <template slot-scope="prop">
            <code>{{prop.row.lockName}}</code>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>  
</template>

<script>
import { dump } from '@/api/administrator'

export default {
  data() {
    return {
      threads: [],
      threadDumpAll: 0,
      threadDumpBlocked: 0,
      threadDumpRunnable: 0,
      threadDumpTimedWaiting: 0,
      threadDumpWaiting: 0,
      threadFilter: '',
      filterState: 'ALL'
    }
  },
  computed: {
    threadViews() {
      if (this.filterState === 'ALL') {
        return this.threads
      }
      const result = []
      this.threads.forEach(thread => {
        if (thread.threadState === this.filterState) {
          result.push(thread)
        }
      })
      return result
    }
  },
  methods: {
    changeState(state) {
      console.log('state change')
      this.filterState = state
    },
    hideStack(thread) {
      thread.show = true
    },
    showStack(thread) {
      thread.show = false
    },
    getType(threadState) {
      if (threadState === 'RUNNABLE') {
        return 'success'
      } else if (threadState === 'WAITING') {
        return 'info'
      } else if (threadState === 'TIMED_WAITING') {
        return 'warning'
      } else if (threadState === 'BLOCKED') {
        return 'danger'
      }
    },
    init() {
      dump()
        .then(response => {
          if (response.data) {
            response.data.forEach(value => {
              value.show = true
              const threadState = value.threadState
              if (threadState === 'RUNNABLE') {
                this.threadDumpRunnable += 1
              } else if (threadState === 'WAITING') {
                this.threadDumpWaiting += 1
              } else if (threadState === 'TIMED_WAITING') {
                this.threadDumpTimedWaiting += 1
              } else if (threadState === 'BLOCKED') {
                this.threadDumpBlocked += 1
              }
            })
            this.threadDumpAll = this.threadDumpRunnable + this.threadDumpWaiting + this.threadDumpTimedWaiting + this.threadDumpBlocked
            this.threads = response.data
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
  .dump-wrapper {
    code {
      padding: .2rem .4rem;
      font-size: 90%;
      color: #bd4147;
      background-color: #f8f9fa;
      border-radius: .25rem;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      display: inline;
    }
  }
</style>
