const { noteApi, courseApi } = require('../../../utils/api')
const app = getApp()

function uploadFileToServer(filePath) {
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: app.globalData.baseUrl + '/api/common/upload?type=note',
      filePath: filePath,
      name: 'file',
      header: { 'Authorization': app.globalData.token ? 'Bearer ' + app.globalData.token : '' },
      success(res) {
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data.data)
        } else {
          reject(new Error(data.msg || '上传失败'))
        }
      },
      fail(err) { reject(err) }
    })
  })
}

Page({
  data: {
    form: { title: '', courseId: null, noteType: '', isFree: 1, price: '', description: '' },
    files: [], courses: [], selectedCourse: '', loading: false,
    noteTypes: [{ value: 'DOCUMENT', label: '文档' }, { value: 'IMAGE', label: '图片' }, { value: 'MIXED', label: '混合' }],
    typeMap: { DOCUMENT: '文档', IMAGE: '图片', MIXED: '混合' }
  },
  async onLoad() {
    const res = await courseApi.all({})
    this.setData({ courses: res.data || [] })
  },
  onInput(e) { this.setData({ [`form.${e.currentTarget.dataset.field}`]: e.detail.value }) },
  onCourseChange(e) { const c = this.data.courses[e.detail.value]; this.setData({ 'form.courseId': c.id, selectedCourse: c.courseName }) },
  onTypeChange(e) { this.setData({ 'form.noteType': this.data.noteTypes[e.detail.value].value }) },
  onFreeChange(e) { this.setData({ 'form.isFree': e.detail.value ? 1 : 0 }) },
  removeFile(e) {
    const idx = e.currentTarget.dataset.index
    const files = this.data.files.filter((_, i) => i !== idx)
    this.setData({ files })
  },
  chooseFile() {
    wx.chooseMessageFile({
      count: 5, type: 'file',
      success: (res) => {
        const valid = []
        const skipped = []
        res.tempFiles.forEach(f => {
          if (!f.size || f.size <= 0) {
            skipped.push(f.name)
          } else {
            const sizeText = f.size > 1048576 ? (f.size / 1048576).toFixed(1) + 'MB' : (f.size / 1024).toFixed(1) + 'KB'
            valid.push({ name: f.name, path: f.path, size: f.size, sizeText, type: f.name.split('.').pop(), id: Date.now() + '_' + Math.random().toString(36).slice(2, 8) })
          }
        })
        if (skipped.length) {
          wx.showToast({ title: skipped.join('、') + ' 为空文件，已跳过', icon: 'none', duration: 2500 })
        }
        if (valid.length) {
          this.setData({ files: [...this.data.files, ...valid] })
        }
      }
    })
  },
  async handleUpload() {
    const { form, files } = this.data
    if (!form.title || !form.noteType) { return wx.showToast({ title: '请填写必要信息', icon: 'none' }) }
    if (!files.length) { return wx.showToast({ title: '请选择文件', icon: 'none' }) }
    this.setData({ loading: true })
    try {
      wx.showLoading({ title: '上传文件中...', mask: true })
      const uploadedFiles = []
      for (const f of files) {
        const serverUrl = await uploadFileToServer(f.path)
        uploadedFiles.push({ fileName: f.name, fileUrl: serverUrl, fileType: f.type, fileSize: f.size })
      }
      wx.hideLoading()
      const userInfo = app.globalData.userInfo
      const submitData = {
        ...form,
        price: form.isFree === 1 ? 0 : (form.price || 0),
        majorId: userInfo ? userInfo.majorId : null,
        grade: userInfo ? userInfo.grade : null,
        files: uploadedFiles
      }
      await noteApi.upload(submitData)
      wx.showToast({ title: '上传成功，等待审核', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 1000)
    } catch (e) {
      wx.hideLoading()
      wx.showToast({ title: e.message || '上传失败', icon: 'none' })
    } finally { this.setData({ loading: false }) }
  }
})
