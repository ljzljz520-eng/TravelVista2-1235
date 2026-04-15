import request from '@/utils/request'

export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export const register = (data) => {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export const getUserInfo = () => {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export const updateUserInfo = (data) => {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

export const adminLogin = (data) => {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export const getAdminReviews = (params) => {
  return request({
    url: '/admin/reviews',
    method: 'get',
    params
  })
}

export const deleteAdminReview = (id) => {
  return request({
    url: `/admin/review/${id}`,
    method: 'delete'
  })
}

export const getAdminUsers = (params) => {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

export const updateUserStatus = (id, status) => {
  return request({
    url: '/admin/user/status',
    method: 'put',
    params: { id, status }
  })
}

export const deleteAdminUser = (id) => {
  return request({
    url: `/admin/user/${id}`,
    method: 'delete'
  })
}
