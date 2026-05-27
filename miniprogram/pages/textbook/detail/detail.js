const { textbookApi } = require('../../../utils/api')
const { fixImageUrl } = require('../../../utils/request')
Page({
  data: {
    detail: null, comments: [], isFavorited: false, commentText: '',
    conditionMap: { NEW: '全新', LIKE_NEW: '几乎全新', GOOD: '良好', FAIR: '一般', WORN: '较旧' }
  },
  async onLoad(options) {
    try {
      const res = await textbookApi.detail(options.id)
      const detail = res.data
      if (detail && detail.images) {
        detail.images = detail.images.map(fixImageUrl)
      }
      this.setData({ detail, isFavorited: !!detail.isFavorited })
      this.loadComments(options.id)
      this.checkFavorite(options.id)
    } catch (e) {
      wx.showToast({ title: '加载失败', icon: 'none' })
    }
  },
  async checkFavorite(id) {
    try {
      const app = getApp()
      if (!app.globalData.token) return
      const res = await textbookApi.myFavorites()
      const favIds = (res.data || []).map(t => t.id)
      this.setData({ isFavorited: favIds.includes(Number(id)) })
    } catch (e) {}
  },
  async loadComments(id) {
    const res = await textbookApi.comments(id || this.data.detail.id)
    this.setData({ comments: res.data || [] })
  },
  async toggleFavorite() {
    try {
      const res = await textbookApi.favorite(this.data.detail.id)
      this.setData({ isFavorited: !this.data.isFavorited })
      wx.showToast({ title: res.msg || (this.data.isFavorited ? '已收藏' : '已取消'), icon: 'none' })
    } catch (e) {
      wx.showToast({ title: '操作失败，请重试', icon: 'none' })
    }
  },
  onCommentInput(e) { this.setData({ commentText: e.detail.value }) },
  async sendComment() {
    if (!this.data.commentText.trim()) return
    await textbookApi.comment({ textbookId: this.data.detail.id, content: this.data.commentText, parentId: 0 })
    this.setData({ commentText: '' })
    this.loadComments()
    wx.showToast({ title: '评论成功' })
  },
  contactSeller() {
    const detail = this.data.detail
    const contactType = detail.contactType
    const contactInfo = detail.contactInfo
    const items = []
    if (contactType === 'PHONE' || /^1\d{10}$/.test(contactInfo)) {
      items.push('拨打电话: ' + contactInfo)
    }
    items.push('复制联系方式')
    items.push('发送站内消息')
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        const selected = items[res.tapIndex]
        if (selected.startsWith('拨打电话')) {
          wx.makePhoneCall({ phoneNumber: contactInfo, fail: () => {} })
        } else if (selected === '复制联系方式') {
          wx.setClipboardData({ data: contactInfo, success: () => wx.showToast({ title: '已复制联系方式' }) })
        } else if (selected === '发送站内消息') {
          wx.navigateTo({ url: '/pages/notice/chat/chat?targetId=' + detail.sellerId + '&targetName=' + encodeURIComponent(detail.sellerName) + '&textbookId=' + detail.id + '&textbookTitle=' + encodeURIComponent(detail.title) })
        }
      }
    })
  },
  showReport() {
    wx.showModal({
      title: '举报教材', content: '确认举报此教材信息吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await textbookApi.report({ textbookId: this.data.detail.id, reason: '信息不实' })
            wx.showToast({ title: '举报已提交', icon: 'success' })
          } catch (e) {
            wx.showToast({ title: '举报失败，请重试', icon: 'none' })
          }
        }
      }
    })
  }
})
