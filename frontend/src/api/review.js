import request from '@/utils/request'

export const getReviewList = (params) => {
  return request({
    url: '/review/list',
    method: 'get',
    params
  })
}

export const addReview = (data) => {
  return request({
    url: '/review/add',
    method: 'post',
    data
  })
}

export const getMyReviews = (params) => {
  return request({
    url: '/review/my',
    method: 'get',
    params
  })
}

export const deleteReview = (id) => {
  return request({
    url: `/review/delete/${id}`,
    method: 'delete'
  })
}
