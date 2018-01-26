import request from '@/utils/request'

export function metrics() {
  return request({
    url: '/management/metrics',
    method: 'get'
  })
}

export function dump() {
  return request({
    url: '/management/dump',
    method: 'get'
  })
}

export function health() {
  return request({
    url: '/management/health',
    method: 'get'
  })
}

export function configprops() {
  return request({
    url: '/management/configprops',
    method: 'get'
  })
}

export function env() {
  return request({
    url: '/management/env',
    method: 'get'
  })
}

export function audits(params) {
  return request({
    url: '/management/audits',
    method: 'get',
    params
  })
}

export function logs() {
  return request({
    url: '/management/logs',
    method: 'get'
  })
}

export function modifyLogs(data) {
  return request({
    url: '/management/logs',
    method: 'put',
    data
  })
}

export function entities() {
  return request({
    url: '/api/audits/entity/all',
    method: 'get'
  })
}

export function entityChanges(params) {
  return request({
    url: '/api/audits/entity/changes',
    method: 'get',
    params
  })
}

export function entityPrevious(params) {
  return request({
    url: '/api/audits/entity/changes/version/previous',
    method: 'get',
    params
  })
}
