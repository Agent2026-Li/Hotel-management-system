const BASE_URL = 'http://localhost:8080'
const TOKEN_KEY = 'HRMS_TOKEN'
const USER_KEY = 'HRMS_USER'

function getCurrentRoute() {
	const pages = typeof getCurrentPages === 'function' ? getCurrentPages() : []
	const current = pages[pages.length - 1]
	return current ? current.route : ''
}

function redirectToLogin() {
	if (getCurrentRoute() === 'pages/login/index') {
		return
	}
	uni.reLaunch({ url: '/pages/login/index' })
}

function requestRaw(options) {
	return new Promise((resolve, reject) => {
		uni.request({
			url: `${BASE_URL}${options.url}`,
			method: options.method || 'GET',
			data: options.data || {},
			header: options.header || {},
			success: (res) => resolve(res),
			fail: reject
		})
	})
}

async function login(username, password) {
	const res = await requestRaw({
		url: '/api/auth/login',
		method: 'POST',
		data: { username, password }
	})
	const body = res.data || {}
	if (body.code !== 0 || !body.data || !body.data.token) {
		throw new Error(body.message || '登录失败')
	}
	uni.setStorageSync(TOKEN_KEY, body.data.token)
	uni.setStorageSync(USER_KEY, {
		username: body.data.username,
		name: body.data.name,
		role: body.data.role,
		tokenType: body.data.tokenType,
		expiresInHours: body.data.expiresInHours
	})
	return body.data
}

async function request(options) {
	const token = uni.getStorageSync(TOKEN_KEY)
	if (!token) {
		redirectToLogin()
		throw new Error('请先登录')
	}

	const res = await requestRaw({
		...options,
		header: {
			...(options.header || {}),
			Authorization: `Bearer ${token}`
		}
	})

	const body = res.data || {}
	if (body.code === 401) {
		uni.removeStorageSync(TOKEN_KEY)
		uni.removeStorageSync(USER_KEY)
		redirectToLogin()
		throw new Error(body.message || '登录已过期，请重新登录')
	}
	if (body.code !== 0) {
		throw new Error(body.message || '接口请求失败')
	}
	return body.data
}

export default {
	login,
	async testAccounts() {
		const res = await requestRaw({
			url: '/api/auth/test-accounts',
			method: 'GET'
		})
		const body = res.data || {}
		if (body.code !== 0) {
			throw new Error(body.message || '测试账号加载失败')
		}
		return body.data || []
	},
	logout() {
		uni.removeStorageSync(TOKEN_KEY)
		uni.removeStorageSync(USER_KEY)
		redirectToLogin()
	},
	isLoggedIn() {
		return !!uni.getStorageSync(TOKEN_KEY)
	},
	getCurrentUser() {
		return uni.getStorageSync(USER_KEY) || {}
	},
	get(url, data) {
		return request({ url, method: 'GET', data })
	},
	post(url, data) {
		return request({ url, method: 'POST', data })
	},
	patch(url, data) {
		return request({ url, method: 'PATCH', data })
	}
}
