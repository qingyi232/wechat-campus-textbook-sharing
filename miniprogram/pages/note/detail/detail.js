const { noteApi } = require('../../../utils/api')
Page({
  data: { detail: null, ratings: [], isFavorited: false },
  async onLoad(options) {
    const res = await noteApi.detail(options.id)
    const detail = res.data
    if (detail && detail.files) {
      detail.files = detail.files.map(f => ({
        ...f,
        fileSizeMB: (f.fileSize / 1024 / 1024).toFixed(1)
      }))
    }
    this.setData({ detail })
    this.loadRatings(options.id)
  },
  async loadRatings(id) {
    const res = await noteApi.ratings(id || this.data.detail.id)
    this.setData({ ratings: res.data || [] })
  },
  async toggleFavorite() {
    await noteApi.favorite(this.data.detail.id)
    this.setData({ isFavorited: !this.data.isFavorited })
  },
  showRate() {
    wx.showModal({
      title: '评价笔记', editable: true, placeholderText: '写一句评价(可选)',
      success: async (res) => {
        if (res.confirm) {
          await noteApi.rate({ noteId: this.data.detail.id, score: 5, comment: res.content || '' })
          wx.showToast({ title: '评价成功' })
          this.loadRatings()
        }
      }
    })
  },
  async downloadAll() {
    await noteApi.download(this.data.detail.id)
    wx.showToast({ title: '下载记录已保存', icon: 'success' })
  },
  downloadFile(e) {
    wx.showToast({ title: '开始下载...', icon: 'loading' })
  }
})
