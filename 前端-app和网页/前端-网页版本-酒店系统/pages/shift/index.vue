<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="shift-card">
						<view class="shift-header">
							<view class="shift-title">🌅 当前班次</view>
							<view class="shift-time">07:00 - 15:00</view>
						</view>
						<view class="shift-info">
							<view class="shift-stat">
								<view class="stat-value">{{ shiftStats.checkin }}</view>
								<view class="stat-label">入住</view>
							</view>
							<view class="shift-stat">
								<view class="stat-value">{{ shiftStats.checkout }}</view>
								<view class="stat-label">退房</view>
							</view>
							<view class="shift-stat">
								<view class="stat-value">{{ shiftStats.reservation }}</view>
								<view class="stat-label">新预订</view>
							</view>
							<view class="shift-stat">
								<view class="stat-value">¥{{ shiftStats.revenue }}</view>
								<view class="stat-label">营收</view>
							</view>
						</view>
					</view>
					
					<view class="grid-2">
						<view class="card">
							<view class="card-header">
								<view class="card-title">交接班记录</view>
							</view>
							<view class="handover-list">
								<view v-for="(record, index) in handoverRecords" :key="index" class="handover-item">
									<view class="handover-time">{{ record.time }}</view>
									<view class="handover-detail">
										<text>交班：{{ record.from }}</text>
										<text>接班：{{ record.to }}</text>
									</view>
									<view class="handover-stats">
										<text>入住{{ record.checkin }} | 退房{{ record.checkout }} | 营收¥{{ record.revenue }}</text>
									</view>
								</view>
							</view>
						</view>
						
						<view class="card">
							<view class="card-header">
								<view class="card-title">注意事项</view>
							</view>
							<view class="notice-list">
								<view v-for="(notice, index) in notices" :key="index" class="notice-item">
									<text class="notice-icon">{{ notice.icon }}</text>
									<text class="notice-text">{{ notice.text }}</text>
								</view>
							</view>
							<view class="action-area">
								<button class="btn btn-primary full" @tap="showHandoverModal">交接班</button>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">交接班管理</text>
			</view>
			<view class="mobile-content">
				<view class="shift-card-mobile">
					<view class="shift-time-mobile">07:00 - 15:00</view>
					<view class="shift-stats-mobile">
						<view class="shift-stat-m">
							<text class="stat-v">{{ shiftStats.checkin }}</text>
							<text class="stat-l">入住</text>
						</view>
						<view class="shift-stat-m">
							<text class="stat-v">{{ shiftStats.checkout }}</text>
							<text class="stat-l">退房</text>
						</view>
						<view class="shift-stat-m">
							<text class="stat-v">¥{{ shiftStats.revenue }}</text>
							<text class="stat-l">营收</text>
						</view>
					</view>
				</view>
				
				<view class="notice-section">
					<view class="section-title">注意事项</view>
					<view v-for="(notice, index) in notices" :key="index" class="notice-card">
						<text class="notice-icon">{{ notice.icon }}</text>
						<text>{{ notice.text }}</text>
					</view>
				</view>
				
				<button class="btn btn-primary full" @tap="showHandoverModal">交接班</button>
			</view>
		</view>
		
		<!-- 交接班弹窗 -->
		<view class="modal-mask" v-if="showModal" @tap="closeModal">
			<view class="modal-content" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">交接班确认</text>
					<text class="modal-close" @tap="closeModal">✕</text>
				</view>
				<view class="modal-body">
					<view class="handover-summary">
						<view class="summary-item">
							<text class="label">交班人</text>
							<text class="value">{{ handoverData.from }}</text>
						</view>
						<view class="summary-item">
							<text class="label">接班人</text>
							<text class="value">{{ handoverData.to }}</text>
						</view>
						<view class="summary-item">
							<text class="label">班次时间</text>
							<text class="value">07:00 - 15:00</text>
						</view>
					</view>
					<view class="handover-stats-grid">
						<view class="hstat">
							<text class="hstat-v">{{ shiftStats.checkin }}</text>
							<text class="hstat-l">入住</text>
						</view>
						<view class="hstat">
							<text class="hstat-v">{{ shiftStats.checkout }}</text>
							<text class="hstat-l">退房</text>
						</view>
						<view class="hstat">
							<text class="hstat-v">¥{{ shiftStats.revenue }}</text>
							<text class="hstat-l">营收</text>
						</view>
					</view>
					<view class="form-group">
						<label class="form-label">交接备注</label>
						<textarea class="form-input" rows="3" v-model="handoverRemark" placeholder="请输入交接备注..."></textarea>
					</view>
				</view>
				<view class="modal-footer">
					<button class="btn btn-default" @tap="closeModal">取消</button>
					<button class="btn btn-primary" @tap="confirmHandover">确认交接</button>
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
			currentPage: 'shift',
			pageName: '交接班管理',
			isPC: true,
			isSidebarCollapsed: false,
			showModal: false,
			toastShow: false,
			toastMessage: '',
			handoverRemark: '',
			handoverData: { from: '张明华', to: '王丽' },
			shiftStats: { checkin: 5, checkout: 3, reservation: 8, revenue: '18,560' },
			handoverRecords: [
				{ time: '2026-06-27 15:00', from: '张明华', to: '王丽', checkin: 5, checkout: 3, revenue: '18,560' },
				{ time: '2026-06-27 07:00', from: '王丽', to: '张明华', checkin: 6, checkout: 4, revenue: '21,340' },
				{ time: '2026-06-26 15:00', from: '李晓', to: '张明华', checkin: 4, checkout: 5, revenue: '16,780' }
			],
			notices: [
				{ icon: '⚠️', text: '801房间客人申请延迟退房至16:00' },
				{ icon: '🔧', text: '305房间马桶漏水，已通知工程部' },
				{ icon: '👑', text: '明早有VIP客人入住，需提前准备' }
			]
		}
	},
	onLoad() {
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
	},
	methods: {
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			const pageNames = { 'index': '仪表盘', 'room-status': '房态管理', 'reservation': '预订管理', 'checkin': '入住登记', 'checkout': '退房结算', 'billing': '账单管理', 'housekeeping': '客房清洁', 'shift': '交接班管理', 'guest-history': '客户档案', 'reports': '报表统计', 'system': '系统设置' }
			this.pageName = pageNames[page] || page
			uni.navigateTo({ url: `/pages/${page}/index` })
		},
		showHandoverModal() { this.showModal = true },
		closeModal() { this.showModal = false },
		confirmHandover() { this.showToast('交接班完成'); this.closeModal() },
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
.shift-card { background: linear-gradient(135deg, #1677ff 0%, #4096ff 100%); color: #fff; border-radius: 8px; padding: 20px; margin-bottom: 20px; }
.shift-header { display: flex; justify-content: space-between; margin-bottom: 16px; }
.shift-title { font-size: 18px; font-weight: 600; }
.shift-time { font-size: 14px; opacity: 0.9; }
.shift-info { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.shift-stat { text-align: center; }
.stat-value { font-size: 24px; font-weight: 700; }
.stat-label { font-size: 12px; opacity: 0.8; margin-top: 4px; }
.grid-2 { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.handover-list { padding-top: 16px; }
.handover-item { padding: 12px 0; border-bottom: 1px dashed #f0f0f0; }
.handover-item:last-child { border-bottom: none; }
.handover-time { font-size: 14px; color: #8c8c8c; }
.handover-detail { display: flex; gap: 16px; margin: 8px 0; font-size: 14px; }
.handover-stats { font-size: 12px; color: #8c8c8c; }
.notice-list { padding-top: 16px; }
.notice-item { display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.notice-icon { font-size: 16px; }
.notice-text { font-size: 14px; }
.action-area { margin-top: 20px; }
.full { width: 100%; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.shift-card-mobile { background: linear-gradient(135deg, #1677ff 0%, #4096ff 100%); color: #fff; border-radius: 16rpx; padding: 32rpx; margin-bottom: 24rpx; text-align: center; }
.shift-time-mobile { font-size: 28rpx; opacity: 0.9; margin-bottom: 20rpx; }
.shift-stats-mobile { display: flex; justify-content: space-around; }
.shift-stat-m { text-align: center; }
.stat-v { display: block; font-size: 36rpx; font-weight: 700; }
.stat-l { font-size: 24rpx; opacity: 0.8; }
.notice-section { margin-bottom: 24rpx; }
.section-title { font-size: 32rpx; font-weight: 600; margin-bottom: 16rpx; }
.notice-card { background: #fff; border-radius: 12rpx; padding: 20rpx; margin-bottom: 12rpx; display: flex; gap: 12rpx; align-items: center; }
.modal-mask { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: #fff; border-radius: 16rpx; width: 560rpx; max-width: 90vw; }
.modal-header { padding: 32rpx; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; }
.modal-title { font-size: 36rpx; font-weight: 600; }
.modal-close { font-size: 32rpx; color: #8c8c8c; }
.modal-body { padding: 32rpx; }
.handover-summary { background: #fafafa; padding: 20rpx; border-radius: 8px; margin-bottom: 20px; }
.summary-item { display: flex; justify-content: space-between; padding: 8px 0; }
.label { color: #8c8c8c; }
.value { font-weight: 600; }
.handover-stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 20px; }
.hstat { text-align: center; padding: 16px; background: rgba(22,119,255,0.1); border-radius: 8px; }
.hstat-v { display: block; font-size: 24px; font-weight: 700; color: #1677ff; }
.hstat-l { font-size: 12px; color: #8c8c8c; }
.form-group { margin-top: 16px; }
.form-label { display: block; margin-bottom: 12rpx; font-size: 28rpx; font-weight: 500; }
.form-input { width: 100%; padding: 16rpx 24rpx; border: 1px solid #d9d9d9; border-radius: 8rpx; font-size: 28rpx; }
.modal-footer { padding: 32rpx; border-top: 1px solid #f0f0f0; display: flex; justify-content: flex-end; gap: 24rpx; }
.toast { position: fixed; bottom: 120rpx; left: 50%; transform: translateX(-50%) translateY(100rpx); background: rgba(0,0,0,0.75); color: #fff; border-radius: 8rpx; padding: 24rpx 48rpx; font-size: 28rpx; z-index: 9999; opacity: 0; transition: all 0.3s; }
.toast.show { transform: translateX(-50%) translateY(0); opacity: 1; }
</style>
