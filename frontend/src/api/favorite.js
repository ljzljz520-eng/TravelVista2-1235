import request from '@/utils/request'

export function addFavorite(scenicId) {
  return request({
    url: '/favorite/add/' + scenicId,
    method: 'post'
  })
}

export function cancelFavorite(scenicId) {
  return request({
    url: '/favorite/cancel/' + scenicId,
    method: 'post'
  })
}

export function getFavoriteList(params) {
  return request({
    url: '/favorite/list',
    method: 'get',
    params
  })
}

export function checkFavorite(scenicId) {
  return request({
    url: '/favorite/check/' + scenicId,
    method: 'get'
  })
}
