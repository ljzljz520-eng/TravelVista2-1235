import request from '@/utils/request'

export const getScenicList = (params) => {
  return request({
    url: '/scenic/list',
    method: 'get',
    params
  })
}

export const getScenicDetail = (id) => {
  return request({
    url: `/scenic/detail/${id}`,
    method: 'get'
  })
}

export const getHotScenic = () => {
  return request({
    url: '/scenic/hot',
    method: 'get'
  })
}

export const getRecommendScenic = () => {
  return request({
    url: '/scenic/recommend',
    method: 'get'
  })
}

export const addScenic = (data) => {
  return request({
    url: '/scenic/add',
    method: 'post',
    data
  })
}

export const updateScenic = (data) => {
  return request({
    url: '/scenic/update',
    method: 'put',
    data
  })
}

export const deleteScenic = (id) => {
  return request({
    url: `/scenic/delete/${id}`,
    method: 'delete'
  })
}
