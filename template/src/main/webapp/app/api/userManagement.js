import request from '@/utils/request'

// 获取角色列表
export function roleList() {
  return request({
    url: '/api/users/authorities',
    method: 'get'
  })
}

// 修改密码
export function changePassword(password) {
  return request({
    url: '/api/account/change-password',
    method: 'post',
    data: {
      password
    }
  })
}

// 获取用户列表
export function userList(params) {
  return request({
    url: '/api/users',
    method: 'get',
    params
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/api/users',
    method: 'post',
    data
  })
}

// 修改用户信息用户
export function updateUser(data) {
  return request({
    url: '/api/users',
    method: 'put',
    data
  })
}

// 通过用户名获取用户信息
export function getUserByLogin(login) {
  return request({
    url: '/api/users/' + login,
    method: 'get'
  })
}

// 通过用户名删除用户
export function deleteUserByLogin(login) {
  return request({
    url: '/api/users/' + login,
    method: 'delete'
  })
}
