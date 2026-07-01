<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="stats-grid">
						<view class="stat-card">
							<view class="stat-icon orange">🧹</view>
							<view class="stat-info">
								<view class="stat-value">{{ pendingTasks }}</view>
								<view class="stat-label">待清洁</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon blue">🔄</view>
							<view class="stat-info">
								<view class="stat-value">{{ cleaningTasks }}</view>
								<view class="stat-label">清洁中</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon green">✅</view>
							<view class="stat-info">
								<view class="stat-value">{{ completedToday }}</view>
								<view class="stat-label">今日完成</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon red">👩‍🔧</view>
							<view class="stat-info">
								<view class="stat-value">{{ staffCount }}</view>
								<view class="stat-label">清洁人员</view>
							</view>
						</view>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">清洁任务列表</view>
							<view class="filter-tabs">
								<text class="filter-tab" :class="{ active: filterStatus === 'all' }" @tap="filterStatus = 'all'">全部</text>
								<text class="filter-tab" :class="{ active: filterStatus === 'pending' }" @tap="filterStatus = 'pending'">待清洁</text>
								<text class="filter-tab" :class="{ active: filterStatus === 'cleaning' }" @tap="filterStatus = 'cleaning'">清洁中</text>
							</view>
						</view>
						<view class="task-list">
							<view v-for="(task, index) in filteredTasks" :key="index" class="task-item" :class="'task-' + task.status">
								<view class="task-room">
									<text class="room-num">{{ task.room }}</text>
									<text class="room-type">{{ task.roomType }}</text>
								</view>
								<view class="task-info">
									<text class="task-type">{{ task.type }}</text>
									<text class="task-time">{{ task.time }}</text>
								</view>
								<view class="task-staff" v-if="task.staff">
									<text>👩 {{ task.staff }}</text>
								</view>
								<view class="task-actions">
									<button class="btn btn-sm btn-primary" v-if="task.status === 'pending'" @tap="startClean(task)">开始</button>
									<button class="btn btn-sm btn-default" v-if="task.status === 'cleaning'" @tap="completeClean(task)">完成</button>
									<span class="status-badge" :class="'status-' + task.status">{{ task.statusName }}</span>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">客房清洁</text>
			</view>
			<view class="mobile-content">
				<view class="mobile-stats">
					<view class="mobile-stat">
						<text class="stat-num">{{ pendingTasks }}</text>
						<text class="stat-lbl">待清洁</text>
					</view>
					<view class="mobile-stat">
						<text class="stat-num">{{ cleaningTasks }}</text>
						<text class="stat-lbl">清洁中</text>
					</view>
					<view class="mobile-stat">
						<text class="stat-num">{{ completedToday }}</text>
						<text class="stat-lbl">今日完成</text>
					</view>
				</view>
				
				<view class="task-list-mobile">
					<view v-for="(task, index) in filteredTasks" :key="index" class="task-card">
						<view class="task-card-header">
							<view class="task-room-info">
								<text class="task-room">{{ task.room }}</text>
								<text class="task-type">{{ task.type }}</text>
							</view>
							<span class="status-badge" :class="'status-' + task.status">{{ task.statusName }}</span>
						</view>
						<view class="task-card-body">
							<text class="task-detail">{{ task.roomType }}</text>
							<text class="task-time">{{ task.time }}</text>
						</view>
						<view class="task-card-footer">
							<button class="btn btn-sm btn-primary full" v-if="task.status === 'pending'" @tap="startClean(task)">开始清洁</button>
							<button class="btn btn-sm btn-default full" v-if="task.status === 'cleaning'" @tap="completeClean(task)">标记完成</button>
						</view>
					</view>
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
			currentPage: 'housekeeping',
			pageName: '客房清洁',
			isPC: true,
			isSidebarCollapsed: false,
			filterStatus: 'all',
			toastShow: false,
			toastMessage: '',
			pendingTasks: 4,
			cleaningTasks: 3,
			completedToday: 28,
			staffCount: 5,
			tasks: [
				{ room: '305', roomType: '标准双床房', type: '退房清洁', time: '10:30', status: 'pending', statusName: '待清洁' },
				{ room: '408', roomType: '标准双床房', type: '退房清洁', time: '11:00', status: 'pending', statusName: '待清洁' },
				{ room: '512', roomType: '标准双床房', type: '日常清洁', time: '11:30', status: 'cleaning', statusName: '清洁中', staff: '张阿姨' },
				{ room: '601', roomType: '豪华大床房', type: '退房清洁', time: '12:00', status: 'cleaning', statusName: '清洁中', staff: '李阿姨' },
				{ room: '205', roomType: '经济单人房', type: '日常清洁', time: '14:00', status: 'pending', statusName: '待清洁' },
				{ room: '703', roomType: '行政套房', type: 'VIP清洁', time: '14:30', status: 'cleaning', statusName: '清洁中', staff: '王阿姨' }
			]
		}
	},
	computed: {
		filteredTasks() {
			if (this.filterStatus === 'all') return this.tasks
			return this.tasks.filter(t => t.status === this.filterStatus)
		}
	},
	onLoad() {
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
		this.loadTasks()
	},
	methods: {
		async loadTasks() {
			try {
				const tasks = await this.$api.get('/api/housekeeping/tasks')
				this.tasks = (tasks || []).map(task => {
					const uiStatus = task.status === 'doing' ? 'cleaning' : task.status
					return {
						...task,
						room: task.roomNumber,
						roomType: '',
						type: task.taskType,
						time: task.createdAt || '',
						staff: task.assignedTo,
						status: uiStatus
					}
				})
				this.pendingTasks = this.tasks.filter(task => task.status === 'pending').length
				this.cleaningTasks = this.tasks.filter(task => task.status === 'cleaning').length
				this.completedToday = this.tasks.filter(task => task.status === 'completed' || task.status === 'done').length
			} catch (error) {
				this.showToast(error.message || '清洁任务加载失败')
			}
		},
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			const pageNames = { 'index': '仪表盘', 'room-status': '房态管理', 'reservation': '预订管理', 'checkin': '入住登记', 'checkout': '退房结算', 'billing': '账单管理', 'housekeeping': '客房清洁', 'shift': '交接班管理', 'guest-history': '客户档案', 'reports': '报表统计', 'system': '系统设置' }
			this.pageName = pageNames[page] || page
			uni.navigateTo({ url: `/pages/${page}/index` })
		},
		startClean(task) { task.status = 'cleaning'; task.statusName = '清洁中'; task.staff = '张阿姨'; this.showToast(`${task.room}开始清洁`) },
		completeClean(task) { task.status = 'completed'; task.statusName = '已完成'; this.completedToday++; this.pendingTasks--; this.showToast(`${task.room}清洁完成`) },
		async startClean(task) {
			try {
				await this.$api.patch(`/api/housekeeping/tasks/${task.id}`, { status: 'doing' })
				this.showToast(`${task.room}开始清洁`)
				await this.loadTasks()
			} catch (error) {
				this.showToast(error.message || '开始清洁失败')
			}
		},
		async completeClean(task) {
			try {
				await this.$api.patch(`/api/housekeeping/tasks/${task.id}`, { status: 'completed' })
				this.showToast(`${task.room}清洁完成`)
				await this.loadTasks()
			} catch (error) {
				this.showToast(error.message || '清洁完成失败')
			}
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
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.filter-tabs { display: flex; gap: 16px; }
.filter-tab { padding: 4px 12px; cursor: pointer; border-radius: 4px; }
.filter-tab.active { background: #1677ff; color: #fff; }
.task-list { display: flex; flex-direction: column; gap: 12px; }
.task-item { display: flex; align-items: center; padding: 16px; background: #fafafa; border-radius: 8px; gap: 20px; }
.task-room { display: flex; flex-direction: column; }
.room-num { font-size: 20px; font-weight: 700; color: #1677ff; }
.room-type { font-size: 12px; color: #8c8c8c; }
.task-info { flex: 1; }
.task-type { display: block; font-weight: 500; }
.task-time { font-size: 12px; color: #8c8c8c; }
.task-actions { display: flex; align-items: center; gap: 12px; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.mobile-stats { display: flex; justify-content: space-around; background: #fff; border-radius: 16rpx; padding: 32rpx; margin-bottom: 24rpx; }
.mobile-stat { text-align: center; }
.stat-num { display: block; font-size: 40rpx; font-weight: 700; color: #1677ff; }
.stat-lbl { font-size: 24rpx; color: #8c8c8c; }
.task-list-mobile { display: flex; flex-direction: column; gap: 20rpx; }
.task-card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.task-card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16rpx; }
.task-room-info { display: flex; flex-direction: column; }
.task-room { font-size: 36rpx; font-weight: 700; color: #1677ff; }
.task-type { font-size: 26rpx; color: #8c8c8c; }
.task-card-body { display: flex; justify-content: space-between; padding: 12rpx 0; border-top: 1px solid #f0f0f0; }
.task-detail, .task-time { font-size: 26rpx; color: #8c8c8c; }
.task-card-footer { padding-top: 16rpx; }
.full { width: 100%; }
.toast { position: fixed; bottom: 120rpx; left: 50%; transform: translateX(-50%) translateY(100rpx); background: rgba(0,0,0,0.75); color: #fff; border-radius: 8rpx; padding: 24rpx 48rpx; font-size: 28rpx; z-index: 9999; opacity: 0; transition: all 0.3s; }
.toast.show { transform: translateX(-50%) translateY(0); opacity: 1; }
</style>
