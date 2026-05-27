const { authApi } = require('../../utils/api')
const app = getApp()

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },

  onUsernameInput(e) { this.setData({ username: e.detail.value }) },
  onPasswordInput(e) { this.setData({ password: e.detail.value }) },

  fillDemo(e) {
    const username = e.currentTarget.dataset.username
    this.setData({ username, password: '123456' })
    wx.showToast({ title: '已填充，点击登录', icon: 'none', duration: 1500 })
  },

  async handleLogin() {
    if (!this.data.username || !this.data.password) {
      return wx.showToast({ title: '请输入账号和密码', icon: 'none' })
    }
    this.setData({ loading: true })
    try {
      const res = await authApi.login({
        username: this.data.username,
        password: this.data.password
      })
      app.globalData.token = res.data.token
      app.globalData.userInfo = res.data
      wx.setStorageSync('token', res.data.token)
      wx.setStorageSync('userInfo', res.data)
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 500)
    } catch (e) {
    } finally {
      this.setData({ loading: false })
    }
  },

  goRegister() {
    wx.navigateTo({ url: '/pages/register/register' })
  }
})
