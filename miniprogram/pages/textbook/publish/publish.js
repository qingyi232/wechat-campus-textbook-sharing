const { textbookApi, courseApi } = require('../../../utils/api')
const app = getApp()
Page({
  data: {
    form: { title: '', author: '', publisher: '', originalPrice: '', price: '', bookCondition: '', courseId: null, description: '', contactInfo: '' },
    images: [], courses: [], selectedCourse: '', loading: false,
    conditions: [{ value: 'NEW', label: '全新' }, { value: 'LIKE_NEW', label: '几乎全新' }, { value: 'GOOD', label: '良好' }, { value: 'FAIR', label: '一般' }, { value: 'WORN', label: '较旧' }],
    conditionMap: { NEW: '全新', LIKE_NEW: '几乎全新', GOOD: '良好', FAIR: '一般', WORN: '较旧' }
  },
  async onLoad() {
    const res = await courseApi.all({})
    this.setData({ courses: res.data || [] })
    const userInfo = app.globalData.userInfo
    if (userInfo) this.setData({ 'form.contactInfo': userInfo.wechat || userInfo.phone || '' })
  },
  onInput(e) { this.setData({ [`form.${e.currentTarget.dataset.field}`]: e.detail.value }) },
  onConditionChange(e) { this.setData({ 'form.bookCondition': this.data.conditions[e.detail.value].value }) },
  onCourseChange(e) {
    const c = this.data.courses[e.detail.value]
    this.setData({ 'form.courseId': c.id, selectedCourse: c.courseName })
  },
  chooseImage() {
    wx.chooseImage({
      count: 6 - this.data.images.length, sizeType: ['compressed'],
      success: (res) => { this.setData({ images: [...this.data.images, ...res.tempFilePaths] }) }
    })
  },
  previewImg(e) { wx.previewImage({ current: e.currentTarget.dataset.url, urls: this.data.images }) },
  async handlePublish() {
    const { form } = this.data
    if (!form.title || !form.price || !form.bookCondition) {
      return wx.showToast({ title: '请填写必要信息', icon: 'none' })
    }
    this.setData({ loading: true })
    try {
      const userInfo = app.globalData.userInfo
      await textbookApi.publish({ ...form, majorId: userInfo.majorId, images: this.data.images })
      wx.showToast({ title: '发布成功', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 1000)
    } catch (e) {} finally { this.setData({ loading: false }) }
  }
})
