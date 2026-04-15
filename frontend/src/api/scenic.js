import request from '@/utils/request'

export function getScenicList(params) {
  return request({
    url: '/scenic/list',
    method: 'get',
    params
  })
}

export function getScenicDetail(id) {
  return request({
    url: '/scenic/' + id,
    method: 'get'
  })
}

export function getHotScenic() {
  return request({
    url: '/scenic/hot',
    method: 'get'
  })
}

export function getCategoryList() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}
