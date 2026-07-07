<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="settings-grid">
						<view class="card setting-card" @tap="showSection('room')">
							<view class="setting-icon">🛏️</view>
							<view class="setting-title">房间设置</view>
							<view class="setting-desc">楼层、房型、房间管理</view>
						</view>
						<view class="card setting-card" @tap="showSection('price')">
							<view class="setting-icon">💰</view>
							<view class="setting-title">价格设置</view>
							<view class="setting-desc">房价、押金、附加费</view>
						</view>
						<view class="card setting-card" @tap="showSection('staff')">
							<view class="setting-icon">👥</view>
							<view class="setting-title">员工管理</view>
							<view class="setting-desc">员工账号、角色权限</view>
						</view>
						<view class="card setting-card" @tap="showSection('system')">
							<view class="setting-icon">⚙️</view>
							<view class="setting-title">系统设置</view>
							<view class="setting-desc">基础参数、操作日志</view>
						</view>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">系统信息</view>
						</view>
						<view class="info-list">
							<view class="info-item">
								<text class="info-label">系统版本</text>
								<text class="info-value">v1.0.0</text>
							</view>
							<view class="info-item">
								<text class="info-label">酒店名称</text>
								<text class="info-value">阳光酒店</text>
							</view>
							<view class="info-item">
								<text class="info-label">总房间数</text>
								<text class="info-value">98间 (7层×14间)</text>
							</view>
							<view class="info-item">
								<text class="info-label">当前用户</text>
								<text class="info-value">管理员</text>
							</view>
						</view>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">快捷操作</view>
						</view>
						<view class="quick-actions">
							<button class="btn btn-default" @tap="backupData">📦 数据备份</button>
							<button class="btn btn-default" @tap="exportData">📤 数据导出</button>
							<button class="btn btn-default" @tap="clearCache">🗑️ 清除缓存</button>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">系统设置</text>
			</view>
			<view class="mobile-content">
				<view class="settings-list">
					<view class="settings-item" @tap="showSection('room')">
						<text class="item-icon">🛏️</text>
						<text class="item-title">房间设置</text>
						<text class="item-arrow">›</text>
					</view>
					<view class="settings-item" @tap="showSection('price')">
						<text class="item-icon">💰</text>
						<text class="item-title">价格设置</text>
						<text class="item-arrow">›</text>
					</view>
					<view class="settings-item" @tap="showSection('staff')">
						<text class="item-icon">👥</text>
						<text class="item-title">员工管理</text>
						<text class="item-arrow">›</text>
					</view>
					<view class="settings-item" @tap="showSection('system')">
						<text class="item-icon">⚙️</text>
						<text class="item-title">系统设置</text>
						<text class="item-arrow">›</text>
					</view>
				</view>
				
				<view class="info-section">
					<view class="info-row">
						<text>系统版本</text>
						<text>v1.0.0</text>
					</view>
					<view class="info-row">
						<text>酒店名称</text>
						<text>阳光酒店</text>
					</view>
					<view class="info-row">
						<text>总房间数</text>
						<text>98间</text>
					</view>
				</view>
				
				<view class="action-buttons">
					<button class="btn btn-default full" @tap="backupData">数据备份</button>
					<button class="btn btn-default full" @tap="exportData">数据导出</button>
					<button class="btn btn-danger full" @tap="logout">退出登录</button>
				</view>
			</view>
		</view>
		
		<view class="toast" :class="{ show: toastShow }">{{ toastMessage }}</view>
	</view>
</template>

<script>
import Sidebar from '@/components/sidebar.vue'
import Topbar from '@/components/topbar.vue'

export default {
	components: { Sidebar, Topbar },
	data() {
		return {
			currentPage: 'system',
			pageName: '系统设置',
			isPC: true,
			isSidebarCollapsed: false,
			toastShow: false,
			toastMessage: ''
		}
	},
	onLoad() {
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
	},
	methods: {
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			this.pageName = this.$rbac.getPageName(page)
			this.navigateToPage(page)
		},
		showSection(section) {
			const sectionNames = { room: '房间设置', price: '价格设置', staff: '员工管理', system: '系统设置' }
			this.showToast(`打开${sectionNames[section]}`)
		},
		backupData() { this.showToast('数据备份成功') },
		exportData() { this.showToast('数据导出成功') },
		clearCache() { this.showToast('缓存已清除') },
		logout() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				success: (res) => {
					if (res.confirm) {
						this.showToast('已退出登录')
					}
				}
			})
		},
		showToast(message) { this.toastMessage = message; this.toastShow = true; setTimeout(() => { this.toastShow = false }, 2000) }
	}
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f5f5; }
.pc-layout { display: flex; min-height: 100vh; }
.main-content { flex: 1; margin-left: 220px; transition: margin-left 0.3s; }
.main-content.sidebar-collapsed { margin-left: 64px; }
.page-container { padding: 24px; }
.settings-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.setting-card { text-align: center; cursor: pointer; transition: all 0.3s; }
.setting-card:hover { transform: translateY(-4px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
.setting-icon { font-size: 40px; margin-bottom: 12px; }
.setting-title { font-size: 16px; font-weight: 600; margin-bottom: 8px; }
.setting-desc { font-size: 14px; color: #8c8c8c; }
.info-list { padding-top: 16px; }
.info-item { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px dashed #f0f0f0; }
.info-item:last-child { border-bottom: none; }
.info-label { color: #8c8c8c; }
.info-value { font-weight: 500; }
.quick-actions { display: flex; gap: 12px; padding-top: 16px; }
.full { width: 100%; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.settings-list { background: #fff; border-radius: 16rpx; margin-bottom: 24rpx; }
.settings-item { display: flex; align-items: center; padding: 28rpx 24rpx; border-bottom: 1px solid #f0f0f0; }
.settings-item:last-child { border-bottom: none; }
.item-icon { font-size: 32rpx; margin-right: 16rpx; }
.item-title { flex: 1; font-size: 30rpx; }
.item-arrow { font-size: 32rpx; color: #8c8c8c; }
.info-section { background: #fff; border-radius: 16rpx; padding: 24rpx; margin-bottom: 24rpx; }
.info-row { display: flex; justify-content: space-between; padding: 16rpx 0; border-bottom: 1px solid #f0f0f0; }
.info-row:last-child { border-bottom: none; }
.action-buttons { display: flex; flex-direction: column; gap: 16rpx; }
.toast { position: fixed; bottom: 120rpx; left: 50%; transform: translateX(-50%) translateY(100rpx); background: rgba(0,0,0,0.75); color: #fff; border-radius: 8rpx; padding: 24rpx 48rpx; font-size: 28rpx; z-index: 9999; opacity: 0; transition: all 0.3s; }
.toast.show { transform: translateX(-50%) translateY(0); opacity: 1; }
</style>
