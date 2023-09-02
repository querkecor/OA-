/*
用户管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysUser'

export default {

  /*
  获取用户分页列表(带搜索)
  */
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  removeUserById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  saveUser(sysUser) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: sysUser
    })
  },
  getUserById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },
  updateUser(sysUser) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: sysUser
    })
  },
  batchRemove(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  },
  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  }
}
