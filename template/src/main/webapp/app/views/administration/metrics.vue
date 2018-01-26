<template>
  <div class="metrics-wrapper">
    <h2>
      <span>应用程序资源监控</span>
      <el-button @click="getMetris" icon="el-icon-refresh" type="primary" class="float-right">刷新</el-button>
    </h2>

    <h3>JVM 资源监控</h3>
    <el-row v-if="!updatingMetrics">
      <el-col :span="8">
        <b>内存</b>
        <p><span>总内存</span>({{ metrics.gauges['jvm.memory.total.used'].value / 1000000 | fixFloat }}M / {{ metrics.gauges['jvm.memory.total.max'].value / 1000000 | fixFloat}}M)</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.memory.total.used'].value, metrics.gauges['jvm.memory.total.max'].value)"></el-progress>

        <p><span>堆内存</span>({{ metrics.gauges['jvm.memory.heap.used'].value / 1000000 | fixFloat }}M / {{ metrics.gauges['jvm.memory.heap.max'].value / 1000000 | fixFloat}}M)</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.memory.heap.used'].value, metrics.gauges['jvm.memory.heap.max'].value)"></el-progress>

        <p><span>非堆内存</span>({{ metrics.gauges['jvm.memory.non-heap.committed'].value / 1000000 | fixFloat }}M / {{ metrics.gauges['jvm.memory.non-heap.used'].value / 1000000 | fixFloat}}M)</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.memory.non-heap.used'].value, metrics.gauges['jvm.memory.non-heap.committed'].value)"></el-progress>
      </el-col>
      <el-col :span="8">
        <b>线程</b>(Total: {{metrics.gauges['jvm.threads.count'].value}}) <a class="hand" @click.prevent="threadView"><i class="el-icon-view"></i></a>
        <p><span>可运行</span>({{metrics.gauges['jvm.threads.runnable.count'].value}})</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.threads.runnable.count'].value, metrics.gauges['jvm.threads.count'].value)"></el-progress>

        <p><span>定时等待</span>({{metrics.gauges['jvm.threads.timed_waiting.count'].value}})</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.threads.timed_waiting.count'].value, metrics.gauges['jvm.threads.count'].value)"></el-progress>

        <p><span>等待中</span>({{metrics.gauges['jvm.threads.waiting.count'].value}})</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.threads.waiting.count'].value, metrics.gauges['jvm.threads.count'].value)"></el-progress>

        <p><span>阻塞中</span>({{metrics.gauges['jvm.threads.blocked.count'].value}})</p>
        <el-progress :text-inside="true" status="success" :stroke-width="18" :percentage="percentage(metrics.gauges['jvm.threads.blocked.count'].value, metrics.gauges['jvm.threads.count'].value)"></el-progress>
      </el-col>
      <el-col :span="8">
        <b>垃圾回收</b>
        <el-row v-if="metrics.gauges['jvm.garbage.PS-MarkSweep.count']">
          <el-col :span="18">标记清除次数</el-col>
          <el-col :span="6">{{metrics.gauges['jvm.garbage.PS-MarkSweep.count'].value}}</el-col>
        </el-row>
        <el-row v-if="metrics.gauges['jvm.garbage.PS-MarkSweep.time']">
          <el-col :span="18">标记清除耗时</el-col>
          <el-col :span="6">{{metrics.gauges['jvm.garbage.PS-MarkSweep.time'].value}}ms</el-col>
        </el-row>
        <el-row v-if="metrics.gauges['jvm.garbage.PS-Scavenge.count']">
          <el-col :span="18">回收次数</el-col>
          <el-col :span="6">{{metrics.gauges['jvm.garbage.PS-Scavenge.count'].value}}</el-col>
        </el-row>
        <el-row v-if="metrics.gauges['jvm.garbage.PS-Scavenge.time']">
          <el-col :span="18">回收耗时</el-col>
          <el-col :span="6">{{metrics.gauges['jvm.garbage.PS-Scavenge.time'].value}}ms</el-col>
        </el-row>
      </el-col>
    </el-row>

    <h3>HTTP 请求 (事件 / 秒)</h3>
    <p v-if="metrics.counters">
      <span>使用中请求:</span><b>{{metrics.counters['com.codahale.metrics.servlet.InstrumentedFilter.activeRequests'].count}}</b> - <span>请求总数:</span><b>{{metrics.timers['com.codahale.metrics.servlet.InstrumentedFilter.requests'].count}}</b>
    </p>
    <el-row>
      <el-table :data="responseStats">
        <el-table-column prop="key" label="状态码"></el-table-column>
        <el-table-column prop="count" label="次数"></el-table-column>
        <el-table-column prop="mean_rate" label="平均数">
          <template slot-scope="prop">
            {{ prop.row.mean_rate | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="m1_rate" label="平均值 (1 min)">
          <template slot-scope="prop">
            {{ prop.row.m1_rate | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="m5_rate" label="平均值 (5 min)">
          <template slot-scope="prop">
            {{ prop.row.m5_rate | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="m15_rate" label="平均值 (15 min)">
          <template slot-scope="prop">
            {{ prop.row.m15_rate | fixFloat }}
          </template>
        </el-table-column>
      </el-table>
    </el-row>

    <h3>服务统计 (时间单位为毫秒)</h3>
    <el-row>
      <el-table :data="servicesStats">
        <el-table-column prop="key" label="服务名称"></el-table-column>
        <el-table-column prop="count" label="计数"></el-table-column>
        <el-table-column prop="mean" label="平均值">
          <template slot-scope="prop">
            {{ prop.row.mean | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="min" label="最小值"></el-table-column>
        <el-table-column prop="p50" label="p50">
          <template slot-scope="prop">
            {{ prop.row.p50 | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="p75" label="p75">
          <template slot-scope="prop">
            {{ prop.row.p75 | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="p95" label="p95">
          <template slot-scope="prop">
            {{ prop.row.p95 | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="p99" label="p99">
          <template slot-scope="prop">
            {{ prop.row.p99 | fixFloat }}
          </template>
        </el-table-column>
        <el-table-column prop="max" label="最大值"></el-table-column>
      </el-table>
    </el-row>
    <el-dialog :visible.sync="threadDialog" title="线程转储">
      <thread-dump></thread-dump>
    </el-dialog>
  </div>
</template>

<script>
  import { metrics } from '@/api/administrator'
  import ThreadDump from './components/threadDump'
  const JCACHE_KEY = 'jcache.statistics'
  export default {
    components: {
      'thread-dump': ThreadDump
    },
    data() {
      return {
        updatingMetrics: false,
        metrics: {},
        servicesStats: [],
        cachesStats: {},
        responseStats: [],
        threadDialog: false
      }
    },
    methods: {
      threadView() {
        this.threadDialog = true
      },
      percentage(value, total) {
        const result = (value / total) * 100
        return Number(result.toFixed(2))
      },
      getMetris() {
        this.updatingMetrics = true
        metrics()
          .then(response => {
            const data = response.data
            this.metrics = data
            this.updatingMetrics = false

            Object.keys(data.timers).forEach(key => {
              const value = data.timers[key]
              if (key.includes('web.rest') || key.includes('service')) {
                this.servicesStats.push({
                  key,
                  ...value
                })
              }
            })

            Object.keys(data.gauges).forEach(key => {
              if (key.includes('jcache.statistics')) {
                const value = data.gauges[key].value
                // remove gets or puts
                const index = key.lastIndexOf('.')
                const newKey = key.substr(0, index)

                this.cachesStats[newKey] = {
                  name: JCACHE_KEY.length,
                  value: value
                }
              }
            })

            Object.keys(data.meters).forEach(key => {
              if (key.includes('com.codahale.metrics.servlet.InstrumentedFilter.responseCodes')) {
                const value = data.meters[key]
                const index = key.lastIndexOf('.')
                const newKey = key.substr(index + 1)
                this.responseStats.push({
                  key: newKey,
                  ...value
                })
              }
            })
          })
      },
      init() {
        this.getMetris()
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style lang="scss">
  @import "src/main/webapp/app/styles/index.scss";
  .metrics-wrapper {
    margin: 10px 20px;
  }
</style>