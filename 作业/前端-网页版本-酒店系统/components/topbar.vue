<template>
	<view class="header">
		<view class="header-left">
			<view class="toggle-btn" @tap="toggleSidebar">
				<text>☰</text>
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
					<text>管</text>
				</view>
				<view class="user-info">
					<text class="user-name">管理员</text>
					<text class="user-role">系统管理员</text>
				</view>
			</view>
			<view class="header-icon" @tap="showNotifications">
				<text>🔔</text>
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
			currentTime: ''
		}
	},
	mounted() {
		this.updateTime()
		setInterval(() => {
			this.updateTime()
		}, 1000)
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
	font-size: 18px;
	text-align: center;
	line-height: 36px;
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
	gap: 20px;
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

.header-icon {
	width: 36px;
	height: 36px;
	position: relative;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 18px;
}

.notification-badge {
	position: absolute;
	top: 4px;
	right: 4px;
	width: 8px;
	height: 8px;
	background: #ff4d4f;
	border-radius: 50%;
}
</style>
