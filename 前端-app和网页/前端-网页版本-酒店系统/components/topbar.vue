<template>
	<view class="header">
		<view class="header-left">
			<view class="toggle-btn" @tap="toggleSidebar">
				<text>≡</text>
			</view>
			<view class="breadcrumb">
				<text class="breadcrumb-current">{{ pageName }}</text>
			</view>
		</view>
		<view class="header-right">
			<view class="header-time">
				<text>{{ currentTime }}</text>
			</view>
			<view class="header-user">
				<view class="user-avatar">
					<text>{{ userInitial }}</text>
				</view>
				<view class="user-info">
					<text class="user-name">{{ user.name || user.username || '用户' }}</text>
					<text class="user-role">{{ roleName(user.role) }}</text>
				</view>
			</view>
			<view class="logout-btn" @tap="logout">退出</view>
			<view class="header-icon" @tap="showNotifications">
				<text>消息</text>
				<view class="notification-badge"></view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'Topbar',
	props: {
		pageName: {
			type: String,
			default: '仪表盘'
		}
	},
	data() {
		return {
			currentTime: '',
			timer: null,
			user: {}
		}
	},
	computed: {
		userInitial() {
			const name = this.user.name || this.user.username || 'U'
			return name.substring(0, 1)
		}
	},
	mounted() {
		this.user = this.$api.getCurrentUser()
		this.updateTime()
		this.timer = setInterval(() => {
			this.updateTime()
		}, 1000)
	},
	beforeDestroy() {
		if (this.timer) {
			clearInterval(this.timer)
		}
	},
	methods: {
		updateTime() {
			const now = new Date()
			this.currentTime = now.toLocaleString('zh-CN', {
				year: 'numeric',
				month: '2-digit',
				day: '2-digit',
				hour: '2-digit',
				minute: '2-digit',
				second: '2-digit'
			})
		},
		toggleSidebar() {
			this.$emit('toggleSidebar')
		},
		roleName(role) {
			const names = {
				ADMIN: '系统管理员',
				MANAGER: '经理',
				FRONT_DESK: '前台',
				HOUSEKEEPING: '客房',
				FINANCE: '财务'
			}
			return names[role] || role || '用户'
		},
		logout() {
			this.$api.logout()
		},
		showNotifications() {
			uni.showToast({
				title: '暂无新通知',
				icon: 'none'
			})
		}
	}
}
</script>

<style scoped>
.header {
	height: 56px;
	background: #ffffff;
	border-bottom: 1px solid #d9d9d9;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 24px;
}

.header-left {
	display: flex;
	align-items: center;
	gap: 16px;
}

.toggle-btn {
	width: 36px;
	height: 36px;
	border: none;
	background: transparent;
	cursor: pointer;
	border-radius: 6px;
	color: #8c8c8c;
	font-size: 22px;
	text-align: center;
	line-height: 34px;
}

.toggle-btn:active {
	background: #f5f5f5;
}

.breadcrumb {
	color: #8c8c8c;
	font-size: 14px;
}

.breadcrumb-current {
	color: #262626;
}

.header-right {
	display: flex;
	align-items: center;
	gap: 16px;
}

.header-time {
	font-size: 14px;
	color: #8c8c8c;
}

.header-user {
	display: flex;
	align-items: center;
	gap: 8px;
}

.user-avatar {
	width: 32px;
	height: 32px;
	border-radius: 50%;
	background: #1677ff;
	color: #fff;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 14px;
}

.user-info {
	display: flex;
	flex-direction: column;
}

.user-name {
	font-size: 14px;
	font-weight: 500;
}

.user-role {
	font-size: 12px;
	color: #8c8c8c;
}

.logout-btn {
	height: 32px;
	line-height: 32px;
	padding: 0 12px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	color: #595959;
	font-size: 13px;
	cursor: pointer;
}

.logout-btn:active {
	color: #1677ff;
	border-color: #1677ff;
}

.header-icon {
	height: 32px;
	line-height: 32px;
	position: relative;
	padding: 0 10px;
	border-radius: 6px;
	font-size: 13px;
	color: #595959;
	cursor: pointer;
}

.header-icon:active {
	background: #f5f5f5;
}

.notification-badge {
	position: absolute;
	top: 5px;
	right: 4px;
	width: 8px;
	height: 8px;
	background: #ff4d4f;
	border-radius: 50%;
}

@media (max-width: 768px) {
	.header-time {
		display: none;
	}

	.user-info {
		display: none;
	}
}
</style>
