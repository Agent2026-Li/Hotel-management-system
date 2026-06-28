<template>
	<view class="page">
		<!-- PC端布局 -->
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<!-- 统计卡片 -->
					<view class="stats-grid">
						<view class="stat-card">
							<view class="stat-icon blue">🛏️</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.vacant }}</view>
								<view class="stat-label">空闲房间</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon green">👤</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.occupied }}</view>
								<view class="stat-label">已入住</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon orange">📝</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.reserved }}</view>
								<view class="stat-label">待入住</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon red">🧹</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.dirty }}</view>
								<view class="stat-label">待清洁</view>
							</view>
						</view>
					</view>
					
					<!-- 图表卡片 -->
					<view class="grid-2">
						<view class="card">
							<view class="card-header">
								<view class="card-title">入住率趋势</view>
							</view>
							<view class="chart-container">
								<view v-for="(item, index) in occupancyData" :key="index" class="chart-bar">
									<view class="chart-value">{{ item.value }}%</view>
									<view class="chart-bar-inner" :style="{ height: item.value * 2 + 'rpx' }"></view>
									<view class="chart-label">{{ item.label }}</view>
								</view>
							</view>
						</view>
						<view class="card">
							<view class="card-header">
								<view class="card-title">今日营收</view>
							</view>
							<view class="revenue-card">
								<view class="revenue-main">
									<view class="revenue-value">¥{{ todayRevenue.total }}</view>
									<view class="revenue-label">今日总营收</view>
								</view>
								<view class="revenue-list">
									<view class="revenue-item">
										<text class="revenue-icon">🛏️</text>
										<text class="revenue-text">房费收入</text>
										<text class="revenue-amount">¥{{ todayRevenue.room }}</text>
									</view>
									<view class="revenue-item">
										<text class="revenue-icon">🍽️</text>
										<text class="revenue-text">餐饮收入</text>
										<text class="revenue-amount">¥{{ todayRevenue.dining }}</text>
									</view>
									<view class="revenue-item">
										<text class="revenue-icon">🥤</text>
										<text class="revenue-text">迷你吧收入</text>
										<text class="revenue-amount">¥{{ todayRevenue.minibar }}</text>
									</view>
								</view>
							</view>
						</view>
					</view>
					
					<!-- 待办事项 -->
					<view class="card">
						<view class="card-header">
							<view class="card-title">待办事项</view>
							<text class="todo-count">{{ todoList.length }}项</text>
						</view>
						<view class="todo-list">
							<view v-for="(item, index) in todoList" :key="index" class="todo-item" @tap="handleTodo(item)">
								<view class="todo-icon">{{ item.icon }}</view>
								<view class="todo-content">
									<view class="todo-title">{{ item.title }}</view>
									<view class="todo-desc">{{ item.desc }}</view>
								</view>
								<view class="todo-time">{{ item.time }}</view>
							</view>
						</view>
					</view>
					
					<!-- 最近入住 -->
					<view class="card">
						<view class="card-header">
							<view class="card-title">最近入住</view>
						</view>
						<view class="table-container">
							<table class="table">
								<thead>
									<tr>
										<th>房号</th>
										<th>客人姓名</th>
										<th>房型</th>
										<th>入住日期</th>
										<th>退房日期</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(item, index) in recentCheckins" :key="index">
										<td>{{ item.room }}</td>
										<td>{{ item.name }}</td>
										<td>{{ item.type }}</td>
										<td>{{ item.checkin }}</td>
										<td>{{ item.checkout }}</td>
										<td><span class="status-badge status-occupied">在住</span></td>
									</tr>
								</tbody>
							</table>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端布局 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">酒店客房管理系统</text>
			</view>
			<view class="mobile-content">
				<!-- 统计卡片 -->
				<view class="stats-grid-mobile">
					<view class="stat-card" @tap="goToPage('room-status')">
						<view class="stat-icon blue">🛏️</view>
						<view class="stat-info">
							<view class="stat-value">{{ stats.vacant }}</view>
							<view class="stat-label">空闲</view>
						</view>
					</view>
					<view class="stat-card">
						<view class="stat-icon green">👤</view>
						<view class="stat-info">
							<view class="stat-value">{{ stats.occupied }}</view>
							<view class="stat-label">入住</view>
						</view>
					</view>
					<view class="stat-card">
						<view class="stat-icon orange">📝</view>
						<view class="stat-info">
							<view class="stat-value">{{ stats.reserved }}</view>
							<view class="stat-label">预订</view>
						</view>
					</view>
					<view class="stat-card">
						<view class="stat-icon red">🧹</view>
						<view class="stat-info">
							<view class="stat-value">{{ stats.dirty }}</view>
							<view class="stat-label">待清洁</view>
						</view>
					</view>
				</view>
				
				<!-- 快捷入口 -->
				<view class="quick-entry">
					<view class="quick-title">快捷操作</view>
					<view class="quick-grid">
						<view class="quick-item" @tap="goToPage('checkin')">
							<text class="quick-icon">🔑</text>
							<text class="quick-text">入住登记</text>
						</view>
						<view class="quick-item" @tap="goToPage('checkout')">
							<text class="quick-icon">💳</text>
							<text class="quick-text">退房结算</text>
						</view>
						<view class="quick-item" @tap="goToPage('reservation')">
							<text class="quick-icon">📝</text>
							<text class="quick-text">预订房间</text>
						</view>
						<view class="quick-item" @tap="goToPage('housekeeping')">
							<text class="quick-icon">🧹</text>
							<text class="quick-text">客房清洁</text>
						</view>
					</view>
				</view>
				
				<!-- 今日营收 -->
				<view class="card">
					<view class="card-header">
						<view class="card-title">今日营收</view>
					</view>
					<view class="revenue-summary">
						<text class="revenue-main">¥{{ todayRevenue.total }}</text>
					</view>
					<view class="revenue-detail">
						<view class="revenue-col">
							<text class="revenue-icon">🛏️</text>
							<text>房费 ¥{{ todayRevenue.room }}</text>
						</view>
						<view class="revenue-col">
							<text class="revenue-icon">🍽️</text>
							<text>餐饮 ¥{{ todayRevenue.dining }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- Toast提示 -->
		<view class="toast" :class="{ show: toastShow }">
			{{ toastMessage }}
		</view>
	</view>
</template>

<script>
import Sidebar from '@/components/sidebar.vue'
import Topbar from '@/components/topbar.vue'

export default {
	components: {
		Sidebar,
		Topbar
	},
	data() {
		return {
			currentPage: 'index',
			pageName: '仪表盘',
			isPC: true,
			isSidebarCollapsed: false,
			toastShow: false,
			toastMessage: '',
			stats: {
				vacant: 18,
				occupied: 76,
				reserved: 5,
				dirty: 4
			},
			occupancyData: [
				{ label: '6/22', value: 62 },
				{ label: '6/23', value: 65 },
				{ label: '6/24', value: 68 },
				{ label: '6/25', value: 70 },
				{ label: '6/26', value: 72 },
				{ label: '6/27', value: 74 },
				{ label: '6/28', value: 76 }
			],
			todayRevenue: {
				total: '45,280',
				room: '38,500',
				dining: '4,280',
				minibar: '2,500'
			},
			todoList: [
				{ icon: '🔑', title: '801房间入住', desc: 'VIP客人张三，豪华套房', time: '14:30' },
				{ icon: '🧹', title: '305房间清洁', desc: '马桶漏水，已通知工程部', time: '13:00' },
				{ icon: '📝', title: '507房间续住', desc: '李四先生申请续住1晚', time: '12:00' },
				{ icon: '💳', title: '602房间退房', desc: '王五先生退房，结账金额¥1,280', time: '11:00' }
			],
			recentCheckins: [
				{ room: '701', name: '王建国', type: '豪华大床房', checkin: '2026-06-27', checkout: '2026-06-29' },
				{ room: '502', name: '张先生', type: '标准双床房', checkin: '2026-06-27', checkout: '2026-06-30' },
				{ room: '308', name: '陈小姐', type: '经济单人房', checkin: '2026-06-27', checkout: '2026-06-28' },
				{ room: '603', name: '李娜', type: '标准双床房', checkin: '2026-06-26', checkout: '2026-06-29' }
			]
		}
	},
	onLoad() {
		// 检测设备类型
		uni.getSystemInfo({
			success: (res) => {
				this.isPC = res.windowWidth >= 768
			}
		})
	},
	methods: {
		toggleSidebar() {
			this.isSidebarCollapsed = !this.isSidebarCollapsed
		},
		handleNavigate(page) {
			this.currentPage = page
			const pageNames = {
				'index': '仪表盘',
				'room-status': '房态管理',
				'reservation': '预订管理',
				'checkin': '入住登记',
				'checkout': '退房结算',
				'billing': '账单管理',
				'housekeeping': '客房清洁',
				'shift': '交接班管理',
				'guest-history': '客户档案',
				'reports': '报表统计',
				'system': '系统设置'
			}
			this.pageName = pageNames[page] || page
			uni.navigateTo({
				url: `/pages/${page}/index`
			})
		},
		goToPage(page) {
			const pageNames = {
				'room-status': '房态管理',
				'reservation': '预订管理',
				'checkin': '入住登记',
				'checkout': '退房结算',
				'housekeeping': '客房清洁'
			}
			uni.navigateTo({
				url: `/pages/${page}/index`
			})
		},
		handleTodo(item) {
			this.showToast(item.title)
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
.page {
	min-height: 100vh;
	background: #f5f5f5;
}

/* PC端布局 */
.pc-layout {
	display: flex;
	min-height: 100vh;
}

.main-content {
	flex: 1;
	margin-left: 220px;
	transition: margin-left 0.3s;
}

.main-content.sidebar-collapsed {
	margin-left: 64px;
}

.page-container {
	padding: 24px;
}

.stats-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 20px;
	margin-bottom: 24px;
}

.grid-2 {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 20px;
}

.chart-container {
	height: 300px;
	display: flex;
	align-items: flex-end;
	justify-content: space-around;
	padding: 20px;
	background: linear-gradient(180deg, transparent 0%, rgba(22, 119, 255, 0.05) 100%);
	border-radius: 8px;
}

.chart-bar {
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 40px;
}

.chart-bar-inner {
	width: 100%;
	background: linear-gradient(180deg, #1677ff 0%, rgba(22, 119, 255, 0.6) 100%);
	border-radius: 4px 4px 0 0;
	transition: height 0.3s;
}

.chart-value {
	font-size: 12px;
	font-weight: 600;
	color: #262626;
	margin-bottom: 8px;
}

.chart-label {
	font-size: 12px;
	color: #8c8c8c;
	margin-top: 8px;
}

.revenue-card {
	padding: 16px 0;
}

.revenue-main {
	text-align: center;
	padding: 20px 0;
}

.revenue-value {
	font-size: 36px;
	font-weight: 700;
	color: #1677ff;
}

.revenue-label {
	font-size: 14px;
	color: #8c8c8c;
	margin-top: 4px;
}

.revenue-list {
	border-top: 1px solid #f0f0f0;
	padding-top: 16px;
}

.revenue-item {
	display: flex;
	align-items: center;
	padding: 12px 0;
}

.revenue-icon {
	margin-right: 12px;
	font-size: 16px;
}

.revenue-text {
	flex: 1;
	font-size: 14px;
}

.revenue-amount {
	font-weight: 600;
}

.todo-count {
	color: #8c8c8c;
	font-size: 14px;
}

.todo-list {
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.todo-item {
	display: flex;
	align-items: center;
	padding: 16px;
	background: #fafafa;
	border-radius: 8px;
	cursor: pointer;
	transition: all 0.3s;
}

.todo-item:active {
	background: #f0f0f0;
}

.todo-icon {
	font-size: 20px;
	margin-right: 16px;
}

.todo-content {
	flex: 1;
}

.todo-title {
	font-size: 14px;
	font-weight: 500;
}

.todo-desc {
	font-size: 12px;
	color: #8c8c8c;
	margin-top: 4px;
}

.todo-time {
	font-size: 12px;
	color: #8c8c8c;
}

/* 移动端布局 */
.mobile-layout {
	min-height: 100vh;
	background: #f5f5f5;
}

.mobile-header {
	height: 88rpx;
	background: linear-gradient(135deg, #001529 0%, #002140 100%);
	color: #fff;
	display: flex;
	align-items: center;
	justify-content: center;
}

.mobile-title {
	font-size: 32rpx;
	font-weight: 600;
}

.mobile-content {
	padding: 24rpx;
}

.stats-grid-mobile {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 20rpx;
	margin-bottom: 24rpx;
}

.quick-entry {
	background: #fff;
	border-radius: 16rpx;
	padding: 32rpx;
	margin-bottom: 24rpx;
}

.quick-title {
	font-size: 32rpx;
	font-weight: 600;
	margin-bottom: 24rpx;
}

.quick-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 20rpx;
}

.quick-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 12rpx;
	padding: 24rpx;
	background: #f5f5f5;
	border-radius: 12rpx;
}

.quick-icon {
	font-size: 40rpx;
}

.quick-text {
	font-size: 22rpx;
	color: #595959;
}

.revenue-summary {
	text-align: center;
	padding: 20rpx 0;
}

.revenue-main {
	font-size: 56rpx;
	font-weight: 700;
	color: #1677ff;
}

.revenue-detail {
	display: flex;
	justify-content: space-around;
	padding-top: 20rpx;
	border-top: 1px solid #f0f0f0;
}

.revenue-col {
	display: flex;
	align-items: center;
	gap: 8rpx;
	font-size: 24rpx;
	color: #8c8c8c;
}

.revenue-col .revenue-icon {
	margin-right: 0;
}

/* Toast */
.toast {
	position: fixed;
	bottom: 120rpx;
	left: 50%;
	transform: translateX(-50%) translateY(100rpx);
	background: rgba(0, 0, 0, 0.75);
	color: #fff;
	border-radius: 8rpx;
	padding: 24rpx 48rpx;
	font-size: 28rpx;
	z-index: 9999;
	opacity: 0;
	transition: all 0.3s;
}

.toast.show {
	transform: translateX(-50%) translateY(0);
	opacity: 1;
}
</style>
