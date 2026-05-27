const { textbookApi, noteApi } = require('../../utils/api')
Page({
  data: { keyword: '', tab: 'textbook', textbooks: [], notes: [], searched: false },
  onInput(e) { this.setData({ keyword: e.detail.value }) },
  async doSearch() {
    if (!this.data.keyword.trim()) return
    const keyword = this.data.keyword
    const [tbRes, noteRes] = await Promise.all([
      textbookApi.list({ keyword, page: 1, size: 20 }),
      noteApi.list({ keyword, page: 1, size: 20 })
    ])
    this.setData({ textbooks: tbRes.data.records || [], notes: noteRes.data.records || [], searched: true })
  },
  switchTab(e) { this.setData({ tab: e.currentTarget.dataset.tab }) },
  goTextbook(e) { wx.navigateTo({ url: '/pages/textbook/detail/detail?id=' + e.currentTarget.dataset.id }) },
  goNote(e) { wx.navigateTo({ url: '/pages/note/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
