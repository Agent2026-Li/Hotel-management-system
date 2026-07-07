<template>
	<view class="sidebar" :class="{ collapsed: isCollapsed }">
		<view class="sidebar-header">
			<text class="sidebar-icon">🏨</text>
			<text class="sidebar-title">酒店管理系统</text>
		</view>
		<view class="sidebar-menu">
			<view
				v-for="item in visibleMenus"
				:key="item.page"
				class="menu-item"
				:class="{ active: currentPage === item.page }"
				@tap="navigateTo(item.page)"
			>
				<text class="menu-icon">{{ item.icon }}</text>
				<text class="menu-text">{{ item.name }}</text>
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
	computed: {
		visibleMenus() {
			const user = this.$api.getCurrentUser()
			return this.$rbac.getAllowedMenus(user.role)
		}
	},
	methods: {
		navigateTo(page) {
			if (!this.canAccessPage(page)) {
				this.showNoPermission()
				return
			}
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
