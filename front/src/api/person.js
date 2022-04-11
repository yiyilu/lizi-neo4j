import request from '@/utils/request'

export function getPersonWebMap() {
  return request({
    url: '/people/web/map',
    method: 'get'
  })
}

export function getPersonWebMapFromTo(from, to) {
  return request({
    url: '/people/web/map/'+from+'/'+to,
    method: 'get'
  })
}

export function getPersonWebMapFrom(from) {
  return request({
    url: '/people/web/map/'+from,
    method: 'get'
  })
}

export function getOutNeighbors(from) {
  return request({
    url: '/people/web/outNeighbors/'+from,
    method: 'get'
  })
}
export function getPersonWebList() {
  return request({
    url: '/people/web/list',
    method: 'get'
  })
}

export function getPersonWebListByPage(pageNum,pageSize) {
  return request({
    url: '/people/web/listByPage/'+pageNum + '/' + pageSize,
    method: 'get'
  })
}

export function getPersonWebNum() {
  return request({
    url: '/people/web/total',
    method: 'get'
  })
}

export function getPersonWebListByName(peopleWebName) {
  return request({
    url: '/people/web/listByName/'+peopleWebName,
    method: 'get'
  })
}

export function getPersonWebSearch(personName) {
  return request({
    url: '/people/web/search/' + personName,
    method: 'get'
  })
}

export function getPersonWebSearchOther(personId, personName) {
  return request({
    url: '/people/web/search/' + personId + '/' + personName,
    method: 'get'
  })
}

export function getPersonWebInfo(personId) {
  return request({
    url: '/people/web/person/' + personId,
    method: 'get'
  })
}

export function addPersonWeb(data) {
  return request({
    url: '/people/web/person',
    method: 'post',
    data: data
  })
}

export function editPersonWeb(data) {
  return request({
    url: '/people/web/person',
    method: 'put',
    data: data
  })
}

export function removePersonWeb(personId) {
  return request({
    url: '/people/web/person/' + personId,
    method: 'delete'
  })
}

export function getInfoConnectWeb(connectId) {
  return request({
    url: '/people/web/connect/' + connectId,
    method: 'get'
  })
}

export function addConnectWeb(data) {
  return request({
    url: '/people/web/connect',
    method: 'post',
    data: data
  })
}

export function editConnectWeb(data) {
  return request({
    url: '/people/web/connect',
    method: 'put',
    data: data
  })
}

export function removeConnectWeb(connectId) {
  return request({
    url: '/people/web/connect/' + connectId,
    method: 'delete'
  })
}
