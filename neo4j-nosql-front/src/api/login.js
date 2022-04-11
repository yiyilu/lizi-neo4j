import request from '@/utils/request'

// 登录方法
export function login(username, password) {
  const data = {
    username,
    password,
    //code,
    //uuid
  }
  return request({
    url: '/people/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
