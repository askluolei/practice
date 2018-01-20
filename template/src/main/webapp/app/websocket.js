import SocketJS from 'sockjs-client'
import Stomp from 'webstomp-client'

export default {

  install(Vue, opts) {
    let subscribers
    let connectionUrl
    if (opts && typeof opts === 'string') {
      connectionUrl = opts
    } else {
      connectionUrl = opts.url
    }
    let socketClient

    // 防止刷新的时候 重建连接还未完成 导致 subscribe 失败
    let connection
    let connectedPromise

    function createConnection() {
      return new Promise((resolve, reject) => { connectedPromise = resolve })
    }

    connection = createConnection()

    // 这里的 socketClient 只有当调用了 connect 连接后才会有值
    Vue.prototype.$socket = socketClient

    const connect = function(queryString) {
      if (connectedPromise == null) {
        connection = createConnection()
      }
      let url = connectionUrl
      if (queryString) {
        url = url + '?' + queryString
      }
      const socket = new SocketJS(url)
      socketClient = Stomp.over(socket)
      Vue.prototype.$socket = socketClient
      const headers = {}
      socketClient.connect(headers, () => {
        console.log('websocket 连接成功')
        connectedPromise('success')
        connectedPromise = null
        subscribers = new Map()
      })
    }

    const disconnect = function() {
      if (socketClient) {
        socketClient.disconnect()
      }
      subscribers.forEach((key, func) => {
        func.unsubscribe()
      })
      subscribers = null
    }

    const send = function(url, data) {
      if (socketClient && socketClient.connected) {
        socketClient.send(url, JSON.stringify(data), {})
      }
    }

    const addListeners = function() {
      if (this.$options['socket']) {
        const conf = this.$options.socket
        if (conf.events) {
          const prefix = conf.prefix || ''
          Object.keys(conf.events).forEach(key => {
            const func = conf.events[key].bind(this)
            const realKey = prefix + key
            connection.then(() => {
              subscribers[realKey] = this.$socket.subscribe(realKey, func)
            })
          })
        }
      }
    }

    const removeListeners = function() {
      if (this.$options['socket']) {
        const conf = this.$options.socket
        if (conf.events) {
          const prefix = conf.prefix || ''
          Object.keys(conf.events).forEach(key => {
            const realKey = prefix + key
            if (subscribers[realKey]) {
              subscribers[realKey].unsubscribe()
              subscribers.delete(realKey)
            }
          })
        }
      }
    }

    // 一个页面通常只有一个websocket连接 直接设置为 Vue的全局方法
    Vue.$connect = connect
    Vue.$disconnect = disconnect
    Vue.$send = send

    // send 注册一个实例方法 方便调用
    Vue.prototype.$send = send

    Vue.mixin({
      // Vue v1.x
      beforeCompile: addListeners,

      // Vue v2.x
      beforeCreate: addListeners,

      beforeDestroy: removeListeners
    })
  }
}
