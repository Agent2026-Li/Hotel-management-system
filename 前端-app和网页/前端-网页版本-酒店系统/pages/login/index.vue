<template>
	<view class="login-page">
		<view class="brand-panel">
			<view class="brand-mark">HR</view>
			<view class="brand-title">酒店管理系统</view>
			<view class="brand-subtitle">客房、预订、入住、账单一体化管理</view>
			<view class="status-row">
				<view class="status-dot"></view>
				<text>后端接口：localhost:8080</text>
			</view>
		</view>

		<view class="login-panel">
			<view class="login-title">账号登录</view>
			<view class="login-desc">请选择测试账号或输入账号密码</view>

			<view class="form-group">
				<text class="form-label">用户名</text>
				<input
					class="form-input"
					v-model="form.username"
					placeholder="请输入用户名"
					confirm-type="next"
				/>
			</view>

			<view class="form-group">
				<text class="form-label">密码</text>
				<input
					class="form-input"
					v-model="form.password"
					type="password"
					password
					placeholder="请输入密码"
					confirm-type="done"
					@confirm="handleLogin"
				/>
			</view>

			<button class="login-button" :disabled="loading" @tap="handleLogin">
				{{ loading ? '登录中...' : '登录' }}
			</button>

			<view class="account-section">
				<view class="account-title">测试账号</view>
				<view class="account-list">
					<view
						v-for="account in accounts"
						:key="account.username"
						class="account-item"
						@tap="fillAccount(account)"
					>
						<view>
							<view class="account-name">{{ account.name }}</view>
							<view class="account-meta">{{ account.username }} / {{ roleName(account.role) }}</view>
						</view>
						<text class="account-fill">填入</text>
					</view>
				</view>
			</view>
		</view>

		<view class="toast" :class="{ show: toastShow }">{{ toastMessage }}</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			loading: false,
			toastShow: false,
			toastMessage: '',
			form: {
				username: '',
				password: ''
			},
			accounts: []
		}
	},
	onLoad() {
		if (this.$api.isLoggedIn()) {
			uni.reLaunch({ url: '/pages/index/index' })
			return
		}
		this.loadAccounts()
	},
	methods: {
		async loadAccounts() {
			try {
				this.accounts = await this.$api.testAccounts()
				if (this.accounts.length > 0) {
					this.fillAccount(this.accounts[0])
				}
			} catch (error) {
				this.showToast(error.message || '测试账号加载失败')
			}
		},
		fillAccount(account) {
			this.form.username = account.username
			this.form.password = account.password
		},
		roleName(role) {
			const names = {
				ADMIN: '系统管理员',
				MANAGER: '经理',
				FRONT_DESK: '前台',
				HOUSEKEEPING: '客房',
				FINANCE: '财务'
			}
			return names[role] || role
		},
		async handleLogin() {
			if (!this.form.username || !this.form.password) {
				this.showToast('请输入用户名和密码')
				return
			}
			if (this.loading) {
				return
			}
			this.loading = true
			try {
				await this.$api.login(this.form.username, this.form.password)
				uni.reLaunch({ url: '/pages/index/index' })
			} catch (error) {
				this.showToast(error.message || '登录失败')
			} finally {
				this.loading = false
			}
		},
		showToast(message) {
			this.toastMessage = message
			this.toastShow = true
			setTimeout(() => {
				this.toastShow = false
			}, 2000)
		}
	}
}
</script>

<style scoped>
.login-page {
	min-height: 100vh;
	background: #eef2f7;
	display: flex;
	align-items: stretch;
	justify-content: center;
	color: #1f2937;
}

.brand-panel {
	width: 45%;
	min-height: 100vh;
	background: linear-gradient(135deg, #13233f 0%, #255e8f 100%);
	color: #fff;
	padding: 80px 64px;
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.brand-mark {
	width: 72px;
	height: 72px;
	border-radius: 8px;
	background: rgba(255, 255, 255, 0.14);
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28px;
	font-weight: 700;
	margin-bottom: 28px;
}

.brand-title {
	font-size: 38px;
	font-weight: 700;
	margin-bottom: 16px;
}

.brand-subtitle {
	font-size: 18px;
	line-height: 1.8;
	color: rgba(255, 255, 255, 0.78);
	max-width: 420px;
}

.status-row {
	margin-top: 48px;
	display: flex;
	align-items: center;
	gap: 10px;
	color: rgba(255, 255, 255, 0.72);
	font-size: 14px;
}

.status-dot {
	width: 8px;
	height: 8px;
	border-radius: 50%;
	background: #3ddc84;
}

.login-panel {
	width: 55%;
	max-width: 560px;
	padding: 72px 64px;
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.login-title {
	font-size: 32px;
	font-weight: 700;
	color: #111827;
}

.login-desc {
	margin-top: 8px;
	margin-bottom: 36px;
	color: #6b7280;
	font-size: 15px;
}

.form-group {
	margin-bottom: 20px;
}

.form-label {
	display: block;
	font-size: 14px;
	color: #374151;
	margin-bottom: 8px;
}

.form-input {
	height: 46px;
	border: 1px solid #d1d5db;
	background: #fff;
	border-radius: 6px;
	padding: 0 14px;
	font-size: 15px;
	box-sizing: border-box;
}

.form-input:focus {
	border-color: #2563eb;
}

.login-button {
	margin-top: 8px;
	height: 48px;
	line-height: 48px;
	border-radius: 6px;
	background: #2563eb;
	color: #fff;
	font-size: 16px;
	font-weight: 600;
}

.login-button[disabled] {
	background: #93c5fd;
	color: #fff;
}

.account-section {
	margin-top: 32px;
}

.account-title {
	font-size: 15px;
	font-weight: 600;
	color: #374151;
	margin-bottom: 12px;
}

.account-list {
	display: grid;
	grid-template-columns: repeat(2, minmax(0, 1fr));
	gap: 12px;
}

.account-item {
	background: #fff;
	border: 1px solid #e5e7eb;
	border-radius: 8px;
	padding: 12px;
	display: flex;
	align-items: center;
	justify-content: space-between;
	cursor: pointer;
	min-height: 54px;
}

.account-item:active {
	border-color: #2563eb;
	background: #eff6ff;
}

.account-name {
	font-size: 14px;
	font-weight: 600;
	color: #111827;
}

.account-meta {
	margin-top: 4px;
	font-size: 12px;
	color: #6b7280;
}

.account-fill {
	color: #2563eb;
	font-size: 13px;
	margin-left: 8px;
}

.toast {
	position: fixed;
	left: 50%;
	bottom: 48px;
	transform: translateX(-50%) translateY(20px);
	background: rgba(0, 0, 0, 0.78);
	color: #fff;
	padding: 10px 18px;
	border-radius: 6px;
	opacity: 0;
	pointer-events: none;
	transition: all 0.2s;
	z-index: 999;
}

.toast.show {
	opacity: 1;
	transform: translateX(-50%) translateY(0);
}

@media (max-width: 768px) {
	.login-page {
		display: block;
		background: #f3f4f6;
	}

	.brand-panel {
		width: 100%;
		min-height: auto;
		padding: 48px 24px 36px;
	}

	.brand-title {
		font-size: 28px;
	}

	.login-panel {
		width: 100%;
		padding: 36px 24px;
	}

	.account-list {
		grid-template-columns: 1fr;
	}
}
</style>
