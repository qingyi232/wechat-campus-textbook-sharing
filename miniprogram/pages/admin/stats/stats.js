const { adminApi } = require('../../../utils/api')

Page({
  data: {
    stats: {}
  },

  onLoad() {
    this.loadStats()
  },

  loadStats() {
    wx.showLoading({ title: '加载中' })
    adminApi.statistics().then(res => {
      this.setData({ stats: res.data || {} })
      wx.hideLoading()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  }
})
