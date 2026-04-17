import request from '../utils/request'

export function getScenicPage(params) {
  return request({
    url: '/scenic/page',
    method: 'get',
    params
  })
}

export function getScenicDetail(id) {
  return request({
    url: `/scenic/detail/${id}`,
    method: 'get'
  })
}

export function getHotScenic(limit) {
  return request({
    url: '/scenic/hot',
    method: 'get',
    params: { limit }
  })
}

export function getRecommendScenic(limit) {
  return request({
    url: '/scenic/recommend',
    method: 'get',
    params: { limit }
  })
}
