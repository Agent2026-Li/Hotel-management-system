import App from './App'
import Vue from 'vue'
import api from './utils/api'
import rbac from './utils/rbac'

Vue.config.productionTip = false
Vue.prototype.$api = api
Vue.prototype.$rbac = rbac

function currentRoute() {
	const pages = typeof getCurrentPages === 'function' ? getCurrentPages() : []
	const current = pages[pages.length - 1]
	return current ? current.route : ''
}

Vue.mixin({
	methods: {
		currentRole() {
			const user = this.$api.getCurrentUser()
			return user && user.role ? user.role : ''
		},
		can(permission) {
			return this.$rbac.can(permission, this.currentRole())
		},
		canAny(permissions) {
			return this.$rbac.canAny(permissions, this.currentRole())
		},
		canAccessPage(page) {
			return this.$rbac.canAccessPage(page, this.currentRole())
		},
		showNoPermission() {
			uni.showToast({
				title: '当前账号无权限操作',
				icon: 'none'
			})
		},
		navigateToPage(page, query) {
			if (!this.canAccessPage(page)) {
				this.showNoPermission()
				return false
			}
			uni.navigateTo({
				url: this.$rbac.pageUrl(page, query || {})
			})
			return true
		},
		guardCurrentPage() {
			const route = currentRoute()
			const page = this.$rbac.pageFromRoute(route)
			if (!page || page === 'login') {
				return
			}
			if (!this.$api.isLoggedIn()) {
				uni.reLaunch({ url: '/pages/login/index' })
				return
			}
			const role = this.currentRole()
			if (!this.$rbac.canAccessPage(page, role)) {
				const target = this.$rbac.firstAllowedPage(role)
				if (target !== page) {
					uni.showToast({
						title: '当前账号无权限访问该页面',
						icon: 'none'
					})
					uni.reLaunch({ url: this.$rbac.pageUrl(target) })
				}
			}
		}
	},
	onShow() {
		this.guardCurrentPage()
	}
})

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
