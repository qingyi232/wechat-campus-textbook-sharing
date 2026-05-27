const app = getApp()

const request = (options) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: app.globalData.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': app.globalData.token ? 'Bearer ' + app.globalData.token : ''
      },
      success(res) {
        if (res.data.code === 200) {
          resolve(res.data)
        } else if (res.data.code === 401) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.redirectTo({ url: '/pages/login/login' })
          reject(res.data)
        } else {
          wx.showToast({ title: res.data.msg || '请求失败', icon: 'none' })
          reject(res.data)
        }
      },
      fail(err) {
        wx.showToast({ title: '网络异常', icon: 'none' })
        reject(err)
      }
    })
  })
}

const fixImageUrl = (url) => {
  if (!url || url.startsWith('http') || url.startsWith('data:')) return url
  return app.globalData.baseUrl + url
}

module.exports = { request, fixImageUrl }
