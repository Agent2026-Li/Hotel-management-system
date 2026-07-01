<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="stats-grid">
						<view class="stat-card">
							<view class="stat-icon blue">💰</view>
							<view class="stat-info">
								<view class="stat-value">¥{{ stats.totalRevenue }}</view>
								<view class="stat-label">本月营收</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon green">🛏️</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.avgOccupancy }}%</view>
								<view class="stat-label">平均入住率</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon orange">👥</view>
							<view class="stat-info">
								<view class="stat-value">{{ stats.totalGuests }}</view>
								<view class="stat-label">入住人数</view>
							</view>
						</view>
						<view class="stat-card">
							<view class="stat-icon red">📊</view>
							<view class="stat-info">
								<view class="stat-value">¥{{ stats.avgRoomRate }}</view>
								<view class="stat-label">平均房价</view>
							</view>
						</view>
					</view>
					
					<view class="grid-2">
						<view class="card">
							<view class="card-header">
								<view class="card-title">营收趋势</view>
							</view>
							<view class="chart-placeholder">
								<view v-for="(item, index) in revenueData" :key="index" class="chart-bar">
									<view class="bar-fill" :style="{ height: item.value / maxRevenue * 200 + 'px' }"></view>
									<text class="bar-label">{{ item.label }}</text>
									<text class="bar-value">¥{{ item.value }}</text>
								</view>
							</view>
						</view>
						
						<view class="card">
							<view class="card-header">
								<view class="card-title">入住率统计</view>
							</view>
							<view class="chart-placeholder">
								<view v-for="(item, index) in occupancyData" :key="index" class="chart-bar">
									<view class="bar-fill" :style="{ height: item.value / 100 * 200 + 'px' }"></view>
									<text class="bar-label">{{ item.label }}</text>
									<text class="bar-value">{{ item.value }}%</text>
								</view>
							</view>
						</view>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">房型销售排行</view>
						</view>
						<view class="table-container">
							<table class="table">
								<thead>
									<tr>
										<th>排名</th>
										<th>房型</th>
										<th>销售数量</th>
										<th>销售额</th>
										<th>占比</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(item, index) in roomTypeRanking" :key="index">
										<td><span class="rank-badge" :class="'rank-' + (index + 1)">{{ index + 1 }}</span></td>
										<td>{{ item.type }}</td>
										<td>{{ item.count }}间</td>
										<td>¥{{ item.revenue }}</td>
										<td>{{ item.percent }}%</td>
									</tr>
								</tbody>
							</table>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">报表统计</text>
			</view>
			<view class="mobile-content">
				<view class="mobile-stats-grid">
					<view class="m-stat-card">
						<text class="m-stat-value">¥{{ stats.totalRevenue }}</text>
						<text class="m-stat-label">本月营收</text>
					</view>
					<view class="m-stat-card">
						<text class="m-stat-value">{{ stats.avgOccupancy }}%</text>
						<text class="m-stat-label">入住率</text>
					</view>
					<view class="m-stat-card">
						<text class="m-stat-value">{{ stats.totalGuests }}</text>
						<text class="m-stat-label">入住人数</text>
					</view>
					<view class="m-stat-card">
						<text class="m-stat-value">¥{{ stats.avgRoomRate }}</text>
						<text class="m-stat-label">平均房价</text>
					</view>
				</view>
				
				<view class="report-section">
					<view class="section-title">房型销售排行</view>
					<view v-for="(item, index) in roomTypeRanking" :key="index" class="rank-card">
						<view class="rank-num">{{ index + 1 }}</view>
						<view class="rank-info">
							<text class="rank-type">{{ item.type }}</text>
							<text class="rank-detail">{{ item.count }}间 | ¥{{ item.revenue }}</text>
						</view>
						<text class="rank-percent">{{ item.percent }}%</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import Sidebar from '@/components/sidebar.vue'
import Topbar from '@/components/topbar.vue'

export default {
	components: { Sidebar, Topbar },
	data() {
		return {
			currentPage: 'reports',
			pageName: '报表统计',
			isPC: true,
			isSidebarCollapsed: false,
			stats: {
				totalRevenue: '1,286,580',
				avgOccupancy: 76,
				totalGuests: 2846,
				avgRoomRate: 452
			},
			revenueData: [
				{ label: '1月', value: 98000 },
				{ label: '2月', value: 125000 },
				{ label: '3月', value: 108000 },
				{ label: '4月', value: 132000 },
				{ label: '5月', value: 145000 },
				{ label: '6月', value: 128580 }
			],
			occupancyData: [
				{ label: '周一', value: 68 },
				{ label: '周二', value: 72 },
				{ label: '周三', value: 75 },
				{ label: '周四', value: 78 },
				{ label: '周五', value: 82 },
				{ label: '周六', value: 88 },
				{ label: '周日', value: 85 }
			],
			roomTypeRanking: [
				{ type: '标准双床房', count: 486, revenue: '174,108', percent: 35 },
				{ type: '豪华大床房', count: 358, revenue: '174,704', percent: 28 },
				{ type: '经济单人房', count: 298, revenue: '76,884', percent: 22 },
				{ type: '行政套房', count: 86, revenue: '76,388', percent: 15 }
			]
		}
	},
	computed: {
		maxRevenue() {
			return Math.max(...this.revenueData.map(d => d.value))
		}
	},
	onLoad() {
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
		this.loadReports()
	},
	methods: {
		async loadReports() {
			try {
				const occupancy = await this.$api.get('/api/reports/occupancy')
				const revenue = await this.$api.get('/api/reports/revenue')
				const rooms = await this.$api.get('/api/rooms')
				this.stats = {
					totalRevenue: revenue.totalRevenue,
					avgOccupancy: Number(occupancy.occupancyRate || 0),
					totalGuests: occupancy.occupiedRooms,
					avgRoomRate: occupancy.occupiedRooms ? (Number(revenue.roomRevenue || 0) / occupancy.occupiedRooms).toFixed(2) : 0
				}
				this.revenueData = [
					{ label: '房费', value: Number(revenue.roomRevenue || 0) },
					{ label: '服务', value: Number(revenue.serviceRevenue || 0) },
					{ label: '已收', value: Number(revenue.paidAmount || 0) },
					{ label: '未收', value: Number(revenue.unpaidAmount || 0) }
				]
				this.occupancyData = [
					{ label: '空房', value: Number(occupancy.vacantRooms || 0) },
					{ label: '入住', value: Number(occupancy.occupiedRooms || 0) },
					{ label: '预订', value: Number(occupancy.reservedRooms || 0) },
					{ label: '脏房', value: Number(occupancy.dirtyRooms || 0) }
				]
				const totalRooms = (rooms || []).length || 1
				const grouped = {}
				;(rooms || []).forEach(room => {
					grouped[room.typeName] = (grouped[room.typeName] || 0) + 1
				})
				this.roomTypeRanking = Object.keys(grouped).map(type => ({
					type,
					count: grouped[type],
					revenue: 0,
					percent: Math.round(grouped[type] * 100 / totalRooms)
				}))
			} catch (error) {
				console.error(error)
			}
		},
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			const pageNames = { 'index': '仪表盘', 'room-status': '房态管理', 'reservation': '预订管理', 'checkin': '入住登记', 'checkout': '退房结算', 'billing': '账单管理', 'housekeeping': '客房清洁', 'shift': '交接班管理', 'guest-history': '客户档案', 'reports': '报表统计', 'system': '系统设置' }
			this.pageName = pageNames[page] || page
			uni.navigateTo({ url: `/pages/${page}/index` })
		}
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
.grid-2 { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 20px; }
.chart-placeholder { display: flex; justify-content: space-around; align-items: flex-end; height: 250px; padding: 20px 0; }
.chart-bar { display: flex; flex-direction: column; align-items: center; width: 60px; }
.bar-fill { width: 40px; background: linear-gradient(180deg, #1677ff 0%, rgba(22,119,255,0.6) 100%); border-radius: 4px 4px 0 0; transition: height 0.3s; }
.bar-label { font-size: 12px; color: #8c8c8c; margin-top: 8px; }
.bar-value { font-size: 11px; font-weight: 600; }
.rank-badge { display: inline-flex; align-items: center; justify-content: center; width: 24px; height: 24px; border-radius: 50%; font-size: 12px; font-weight: 600; }
.rank-1 { background: #ffd700; color: #fff; }
.rank-2 { background: #c0c0c0; color: #fff; }
.rank-3 { background: #cd7f32; color: #fff; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.mobile-stats-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16rpx; margin-bottom: 24rpx; }
.m-stat-card { background: #fff; border-radius: 12rpx; padding: 24rpx; text-align: center; }
.m-stat-value { display: block; font-size: 32rpx; font-weight: 700; color: #1677ff; }
.m-stat-label { font-size: 24rpx; color: #8c8c8c; }
.report-section { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.section-title { font-size: 32rpx; font-weight: 600; margin-bottom: 20rpx; }
.rank-card { display: flex; align-items: center; padding: 16rpx 0; border-bottom: 1px solid #f0f0f0; }
.rank-card:last-child { border-bottom: none; }
.rank-num { width: 48rpx; height: 48rpx; border-radius: 50%; background: #1677ff; color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 600; margin-right: 16rpx; }
.rank-info { flex: 1; }
.rank-type { display: block; font-weight: 500; }
.rank-detail { font-size: 24rpx; color: #8c8c8c; }
.rank-percent { font-size: 28rpx; font-weight: 600; color: #1677ff; }
</style>
