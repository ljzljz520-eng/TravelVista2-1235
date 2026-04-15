import request from '@/utils/request'

export function createOrder(data) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

export function payOrder(orderNo) {
  return request({
    url: '/order/pay/' + orderNo,
    method: 'post'
  })
}

export function cancelOrder(orderNo) {
  return request({
    url: '/order/cancel/' + orderNo,
    method: 'post'
  })
}

export function getOrderDetail(orderNo) {
  return request({
    url: '/order/detail/' + orderNo,
    method: 'get'
  })
}

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}
