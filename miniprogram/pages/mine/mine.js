const app = getApp()
Page({
  data: { userInfo: {}, roleMap: { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' } },
  onShow() {
    const userInfo = app.globalData.userInfo
    if (!userInfo) { wx.redirectTo({ url: '/pages/login/login' }); return }
    this.setData({ userInfo })
  },
  goTo(e) { wx.navigateTo({ url: e.currentTarget.dataset.url }) },
  handleLogout() {
    wx.showModal({
      title: '提示', content: '确定退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.globalData.token = ''; app.globalData.userInfo = null
          wx.removeStorageSync('token'); wx.removeStorageSync('userInfo')
          wx.redirectTo({ url: '/pages/login/login' })
        }
      }
    })
  }
})
