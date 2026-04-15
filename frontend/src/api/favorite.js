import request from '@/utils/request'

export const addFavorite = (scenicId) => {
  return request({
    url: `/favorite/add/${scenicId}`,
    method: 'post'
  })
}

export const removeFavorite = (scenicId) => {
  return request({
    url: `/favorite/remove/${scenicId}`,
    method: 'delete'
  })
}

export const checkFavorite = (scenicId) => {
  return request({
    url: `/favorite/check/${scenicId}`,
    method: 'get'
  })
}

export const getFavoriteList = (params) => {
  return request({
    url: '/favorite/list',
    method: 'get',
    params
  })
}
