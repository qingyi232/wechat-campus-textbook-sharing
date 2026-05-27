import request from './request'

export const authApi = {
  login: data => request.post('/api/auth/login', data)
}

export const adminApi = {
  statistics: () => request.get('/api/admin/statistics'),
  reports: params => request.get('/api/admin/reports', { params }),
  handleReport: data => request.put('/api/admin/report/handle', data),
  feedback: params => request.get('/api/admin/feedback', { params }),
  replyFeedback: data => request.put('/api/admin/feedback/reply', data)
}

export const userApi = {
  list: params => request.get('/api/user/list', { params }),
  audit: (id, status) => request.put(`/api/user/audit/${id}`, { status }),
  delete: id => request.delete(`/api/user/${id}`)
}

export const textbookApi = {
  list: params => request.get('/api/textbook/list', { params }),
  review: data => request.post('/api/textbook/review', data),
  delete: id => request.delete(`/api/textbook/${id}`)
}

export const noteApi = {
  list: params => request.get('/api/note/list', { params }),
  review: data => request.post('/api/note/review', data),
  updateStatus: data => request.put('/api/note/status', data),
  delete: id => request.delete(`/api/note/${id}`)
}

export const notificationApi = {
  send: data => request.post('/api/notification/send', data)
}
