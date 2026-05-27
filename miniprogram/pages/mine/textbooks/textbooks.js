const { textbookApi } = require('../../../utils/api')
const { fixImageUrl } = require('../../../utils/request')
const app = getApp()
Page({
  data: {
    list: [],
    statusMap: { ON_SALE: '在售', SOLD: '已售', OFF_SHELF: '已下架', REVIEWING: '审核中', REJECTED: '已驳回' }
  },
  async onShow() {
    const userId = app.globalData.userInfo?.id
    if (!userId) return
    const res = await textbookApi.list({ sellerId: userId, page: 1, size: 50 })
    const list = (res.data.records || []).map(t => ({
      ...t, images: (t.images || []).map(fixImageUrl)
    }))
    this.setData({ list })
  },
  async markSold(e) {
    await textbookApi.update({ id: e.currentTarget.dataset.id, status: 'SOLD' })
    wx.showToast({ title: '已标为已售' })
    this.onShow()
  },
  async deleteItem(e) {
    wx.showModal({
      title: '确认', content: '确定删除此教材吗？',
      success: async (res) => {
        if (res.confirm) { await textbookApi.delete(e.currentTarget.dataset.id); this.onShow() }
      }
    })
  }
})
