import request from '@/utils/request'

export function adminLogin(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function adminLogout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

export function getAdminInfo() {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

export function getUserList(params) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  })
}

export function updateUserStatus(userId, status) {
  return request({
    url: '/admin/user/status/' + userId,
    method: 'put',
    params: { status }
  })
}

export function deleteReview(reviewId) {
  return request({
    url: '/admin/review/' + reviewId,
    method: 'delete'
  })
}

export function getStatistics() {
  return request({
    url: '/admin/statistics',
    method: 'get'
  })
}
