import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(JSON.parse(localStorage.getItem('admin_info') || 'null'))
  const token = ref(localStorage.getItem('admin_token') || '')
  const isLoggedIn = computed(() => !!token.value)

  function setUser(info, t) {
    userInfo.value = info
    token.value = t
    localStorage.setItem('admin_info', JSON.stringify(info))
    localStorage.setItem('admin_token', t)
  }

  function logout() {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('admin_info')
    localStorage.removeItem('admin_token')
  }

  return { userInfo, token, isLoggedIn, setUser, logout }
})
