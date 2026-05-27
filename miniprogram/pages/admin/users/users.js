const { adminApi } = require('../../../utils/api')

Page({
  data: {
    users: [],
    keyword: '',
    roleFilter: '',
    page: 1,
    loading: false,
    hasMore: true,
    roleMap: { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' }
  },

  onLoad() {
    this.loadUsers()
  },

  onKeyword(e) {
    this.setData({ keyword: e.detail.value })
  },

  search() {
    this.setData({ page: 1, users: [], hasMore: true })
    this.loadUsers()
  },

  filterRole(e) {
    this.setData({ roleFilter: e.currentTarget.dataset.role, page: 1, users: [], hasMore: true })
    this.loadUsers()
  },

  loadUsers() {
    if (this.data.loading) return
    this.setData({ loading: true })
    const params = { page: this.data.page, size: 10 }
    if (this.data.keyword) params.keyword = this.data.keyword
    if (this.data.roleFilter) params.role = this.data.roleFilter

    adminApi.userList(params).then(res => {
      const records = res.data.records || res.data.list || []
      this.setData({
        users: this.data.users.concat(records),
        hasMore: records.length >= 10,
        loading: false
      })
    }).catch(() => {
      this.setData({ loading: false })
    })
  },

  loadMore() {
    this.setData({ page: this.data.page + 1 })
    this.loadUsers()
  },

  toggleStatus(e) {
    const { id, status } = e.currentTarget.dataset
    const newStatus = status === 1 ? 0 : 1
    const action = newStatus === 1 ? '启用' : '禁用'
    wx.showModal({
      title: '确认', content: `确定${action}该用户吗？`,
      success: res => {
        if (!res.confirm) return
        adminApi.auditUser(id, newStatus).then(() => {
          wx.showToast({ title: `${action}成功` })
          this.setData({ page: 1, users: [], hasMore: true })
          this.loadUsers()
        })
      }
    })
  },

  resetPwd(e) {
    const { id, name } = e.currentTarget.dataset
    wx.showModal({
      title: '确认', content: `确定重置 ${name} 的密码吗？`,
      success: res => {
        if (!res.confirm) return
        adminApi.resetPwd(id).then(() => {
          wx.showToast({ title: '重置成功' })
        })
      }
    })
  },

  deleteUser(e) {
    const { id, name } = e.currentTarget.dataset
    wx.showModal({
      title: '警告', content: `确定删除用户 ${name} 吗？此操作不可撤销！`,
      success: res => {
        if (!res.confirm) return
        adminApi.deleteUser(id).then(() => {
          wx.showToast({ title: '删除成功' })
          this.setData({ page: 1, users: [], hasMore: true })
          this.loadUsers()
        })
      }
    })
  }
})
