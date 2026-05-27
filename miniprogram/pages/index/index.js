const { textbookApi, noteApi, notificationApi } = require('../../utils/api')
const { fixImageUrl } = require('../../utils/request')
const app = getApp()
Page({
  data: {
    userInfo: {}, hotTextbooks: [], recommendNotes: [],
    roleMap: { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' },
    unreadCount: 0
  },
  onShow() {
    const userInfo = app.globalData.userInfo
    if (!userInfo) { wx.redirectTo({ url: '/pages/login/login' }); return }
    this.setData({ userInfo })
    this.loadData()
    this.loadUnread()
  },
  async loadData() {
    try {
      const [tbRes, noteRes] = await Promise.all([
        textbookApi.list({ page: 1, size: 6, orderBy: 'popular' }),
        noteApi.list({ page: 1, size: 5, recommended: 1, orderBy: 'rating' })
      ])
      const textbooks = (tbRes.data.records || []).map(t => ({
        ...t, images: (t.images || []).map(fixImageUrl)
      }))
      this.setData({ hotTextbooks: textbooks, recommendNotes: noteRes.data.records || [] })
    } catch (e) {}
  },
  async loadUnread() {
    try {
      const res = await notificationApi.unread()
      this.setData({ unreadCount: res.data || 0 })
    } catch (e) {}
  },
  goSearch() { wx.navigateTo({ url: '/pages/search/search' }) },
  goTo(e) { wx.navigateTo({ url: e.currentTarget.dataset.url }) },
  switchToTextbook() { wx.switchTab({ url: '/pages/textbook/list/list' }) },
  switchToNote() { wx.switchTab({ url: '/pages/note/list/list' }) },
  goTextbookDetail(e) { wx.navigateTo({ url: '/pages/textbook/detail/detail?id=' + e.currentTarget.dataset.id }) },
  goNoteDetail(e) { wx.navigateTo({ url: '/pages/note/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
