import request from '@/utils/request'

export function addReview(data) {
  return request({
    url: '/review/add',
    method: 'post',
    data
  })
}

export function getReviewList(params) {
  return request({
    url: '/review/list',
    method: 'get',
    params
  })
}
