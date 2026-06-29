<template>
	<view class="sidebar" :class="{ collapsed: isCollapsed }">
		<view class="sidebar-header">
			<text class="sidebar-icon">🏨</text>
			<text class="sidebar-title">酒店管理系统</text>
		</view>
		<view class="sidebar-menu">
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'index' }"
				@tap="navigateTo('index')"
			>
				<text class="menu-icon">📊</text>
				<text class="menu-text">仪表盘</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'room-status' }"
				@tap="navigateTo('room-status')"
			>
				<text class="menu-icon">🛏️</text>
				<text class="menu-text">房态管理</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'reservation' }"
				@tap="navigateTo('reservation')"
			>
				<text class="menu-icon">📝</text>
				<text class="menu-text">预订管理</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'checkin' }"
				@tap="navigateTo('checkin')"
			>
				<text class="menu-icon">🔑</text>
				<text class="menu-text">入住登记</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'checkout' }"
				@tap="navigateTo('checkout')"
			>
				<text class="menu-icon">💳</text>
				<text class="menu-text">退房结算</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'billing' }"
				@tap="navigateTo('billing')"
			>
				<text class="menu-icon">📋</text>
				<text class="menu-text">账单管理</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'housekeeping' }"
				@tap="navigateTo('housekeeping')"
			>
				<text class="menu-icon">🧹</text>
				<text class="menu-text">客房清洁</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'shift' }"
				@tap="navigateTo('shift')"
			>
				<text class="menu-icon">🔄</text>
				<text class="menu-text">交接班管理</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'guest-history' }"
				@tap="navigateTo('guest-history')"
			>
				<text class="menu-icon">👥</text>
				<text class="menu-text">客户档案</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'reports' }"
				@tap="navigateTo('reports')"
			>
				<text class="menu-icon">📈</text>
				<text class="menu-text">报表统计</text>
			</view>
			<view 
				class="menu-item" 
				:class="{ active: currentPage === 'system' }"
				@tap="navigateTo('system')"
			>
				<text class="menu-icon">⚙️</text>
				<text class="menu-text">系统设置</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'Sidebar',
	props: {
		currentPage: {
			type: String,
			default: 'index'
		},
		isCollapsed: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		navigateTo(page) {
			this.$emit('navigate', page)
		}
	}
}
</script>

<style scoped>
.sidebar {
	width: 220px;
	background: linear-gradient(180deg, #001529 0%, #002140 100%);
	color: #fff;
	position: fixed;
	height: 100vh;
	left: 0;
	top: 0;
	z-index: 100;
	transition: width 0.3s;
}

.sidebar.collapsed {
	width: 64px;
}

.sidebar-header {
	height: 56px;
	display: flex;
	align-items: center;
	padding: 0 20px;
	border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-icon {
	font-size: 24px;
	margin-right: 12px;
}

.sidebar-title {
	font-size: 16px;
	font-weight: 600;
	white-space: nowrap;
}

.sidebar.collapsed .sidebar-title {
	display: none;
}

.sidebar-menu {
	padding: 12px 0;
}

.menu-item {
	display: flex;
	align-items: center;
	padding: 12px 20px;
	color: rgba(255, 255, 255, 0.65);
	cursor: pointer;
	transition: all 0.3s;
	border-left: 3px solid transparent;
}

.menu-item:hover {
	color: #fff;
	background: rgba(255, 255, 255, 0.08);
}

.menu-item.active {
	color: #fff;
	background: rgba(22, 119, 255, 0.25);
	border-left-color: #1677ff;
}

.menu-icon {
	width: 24px;
	font-size: 16px;
	text-align: center;
}

.menu-text {
	margin-left: 12px;
	white-space: nowrap;
}

.sidebar.collapsed .menu-text {
	display: none;
}

.sidebar.collapsed .menu-item {
	justify-content: center;
	padding: 12px;
}
</style>
