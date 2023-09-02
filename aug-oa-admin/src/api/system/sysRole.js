/*
角色管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysRole'

export default {

  /*
  获取角色分页列表(带搜索)
  */
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  removeRoleById(id,page, limit, searchObj) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete',
      params: searchObj
    })
  },
  saveRole(sysRole) {
    return request({
      url: `${api_name}/save/role`,
      method: 'post',
      data: sysRole
    })
  },
  getRoleById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },
  updateRole(sysRole) {
    return request({
      url: `${api_name}/update/role`,
      method: 'put',
      data: sysRole
    })
  },
  batchRemove(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  },
  getRoles(adminId) {
    return request({
      url: `${api_name}/toAssign/${adminId}`,
      method: 'get'
    })
  },
  
  assignRoles(assignRoleVo) {
    return request({
      url: `${api_name}/doAssign`,
      method: 'post',
      data: assignRoleVo
    })
  }
}
