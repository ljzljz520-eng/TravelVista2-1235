import request from '../utils/request'

export function addReview(data) {
  return request({
    url: '/review/add',
    method: 'post',
    data
  })
}

export function getReviewByScenic(scenicSpotId, params) {
  return request({
    url: `/review/page/scenic/${scenicSpotId}`,
    method: 'get',
    params
  })
}

export function getMyReviews(params) {
  return request({
    url: '/review/page/user',
    method: 'get',
    params
  })
}

export function likeReview(id) {
  return request({
    url: `/review/like/${id}`,
    method: 'post'
  })
}
