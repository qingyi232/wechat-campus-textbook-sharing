const { textbookApi, noteApi } = require('../../../utils/api')
Page({
  data: { tab: 'textbook', textbooks: [], notes: [] },
  async onShow() {
    const [tbRes, noteRes] = await Promise.all([textbookApi.myFavorites(), noteApi.myFavorites()])
    this.setData({ textbooks: tbRes.data || [], notes: noteRes.data || [] })
  },
  switchTab(e) { this.setData({ tab: e.currentTarget.dataset.tab }) },
  goTextbook(e) { wx.navigateTo({ url: '/pages/textbook/detail/detail?id=' + e.currentTarget.dataset.id }) },
  goNote(e) { wx.navigateTo({ url: '/pages/note/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
