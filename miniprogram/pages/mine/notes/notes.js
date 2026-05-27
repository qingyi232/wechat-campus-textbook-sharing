const { noteApi } = require('../../../utils/api')
const app = getApp()
Page({
  data: { list: [], statusMap: { PUBLISHED: '已发布', REVIEWING: '审核中', REJECTED: '已驳回', OFF_SHELF: '已下架' } },
  async onShow() {
    const userId = app.globalData.userInfo?.id
    if (!userId) return
    const res = await noteApi.list({ authorId: userId, page: 1, size: 50, status: '' })
    this.setData({ list: res.data.records || [] })
  },
  goDetail(e) { wx.navigateTo({ url: '/pages/note/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
