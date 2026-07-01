<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="search-bar">
						<input type="text" placeholder="搜索姓名/手机号/身份证" v-model="searchKeyword" />
						<button class="btn btn-primary btn-sm" @tap="showAddModal">+ 添加客户</button>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">客户列表</view>
							<text class="card-count">共 {{ guests.length }} 位客户</text>
						</view>
						<view class="table-container">
							<table class="table">
								<thead>
									<tr>
										<th>姓名</th>
										<th>性别</th>
										<th>手机号</th>
										<th>身份证</th>
										<th>入住次数</th>
										<th>累计消费</th>
										<th>会员等级</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(guest, index) in guests" :key="index">
										<td>{{ guest.name }}</td>
										<td>{{ guest.gender }}</td>
										<td>{{ guest.phone }}</td>
										<td>{{ guest.idcard }}</td>
										<td>{{ guest.stayCount }}次</td>
										<td>¥{{ guest.totalConsume }}</td>
										<td><span class="vip-badge" :class="'vip-' + guest.vipLevel">{{ guest.vipLevelName }}</span></td>
										<td>
											<text class="action-link" @tap="viewDetail(guest)">详情</text>
											<text class="action-link" @tap="editGuest(guest)">编辑</text>
										</td>
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
				<text class="mobile-title">客户档案</text>
			</view>
			<view class="mobile-content">
				<view class="search-box">
					<input type="text" placeholder="搜索姓名/手机号" v-model="searchKeyword" />
				</view>
				
				<view class="guest-list">
					<view v-for="(guest, index) in guests" :key="index" class="guest-card" @tap="viewDetail(guest)">
						<view class="guest-header">
							<text class="guest-name">{{ guest.name }}</text>
							<span class="vip-badge" :class="'vip-' + guest.vipLevel">{{ guest.vipLevelName }}</span>
						</view>
						<view class="guest-info">
							<text>{{ guest.phone }}</text>
							<text>入住{{ guest.stayCount }}次</text>
						</view>
						<view class="guest-consume">
							累计消费: ¥{{ guest.totalConsume }}
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
			currentPage: 'guest-history',
			pageName: '客户档案',
			isPC: true,
			isSidebarCollapsed: false,
			searchKeyword: '',
			toastShow: false,
			toastMessage: '',
			guests: [
				{ name: '王建国', gender: '男', phone: '13812345678', idcard: '110101199001011234', stayCount: 12, totalConsume: '45,680', vipLevel: 'gold', vipLevelName: '金卡会员' },
				{ name: '张先生', gender: '男', phone: '13923456789', idcard: '310101198505055678', stayCount: 8, totalConsume: '28,960', vipLevel: 'silver', vipLevelName: '银卡会员' },
				{ name: '陈小姐', gender: '女', phone: '13734567890', idcard: '440101199202128901', stayCount: 5, totalConsume: '15,340', vipLevel: 'silver', vipLevelName: '银卡会员' },
				{ name: '李娜', gender: '女', phone: '13645678901', idcard: '510101198808088765', stayCount: 15, totalConsume: '68,520', vipLevel: 'diamond', vipLevelName: '钻石会员' },
				{ name: '刘伟', gender: '男', phone: '13556789012', idcard: '320101199512125432', stayCount: 3, totalConsume: '8,640', vipLevel: 'normal', vipLevelName: '普通会员' }
			]
		}
	},
	onLoad() {
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
		this.loadGuests()
	},
	methods: {
		async loadGuests() {
			try {
				const guests = await this.$api.get('/api/guests', { keyword: this.searchKeyword })
				this.guests = (guests || []).map(guest => ({
					...guest,
					gender: '',
					idcard: guest.idCard,
					stayCount: guest.totalOrders,
					totalConsume: guest.totalAmount,
					vipLevel: 'normal',
					vipLevelName: guest.memberLevel || '普通会员'
				}))
			} catch (error) {
				this.showToast(error.message || '客史加载失败')
			}
		},
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			const pageNames = { 'index': '仪表盘', 'room-status': '房态管理', 'reservation': '预订管理', 'checkin': '入住登记', 'checkout': '退房结算', 'billing': '账单管理', 'housekeeping': '客房清洁', 'shift': '交接班管理', 'guest-history': '客户档案', 'reports': '报表统计', 'system': '系统设置' }
			this.pageName = pageNames[page] || page
			uni.navigateTo({ url: `/pages/${page}/index` })
		},
		showAddModal() { this.showToast('添加客户功能') },
		viewDetail(guest) { this.showToast(`查看 ${guest.name} 详情`) },
		editGuest(guest) { this.showToast(`编辑 ${guest.name}`) },
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
.search-bar { display: flex; gap: 12px; margin-bottom: 20px; }
.search-bar input { flex: 1; padding: 8px 12px; border: 1px solid #d9d9d9; border-radius: 6px; }
.card-count { color: #8c8c8c; font-size: 14px; }
.action-link { color: #1677ff; cursor: pointer; margin-right: 8px; }
.vip-badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; }
.vip-diamond { background: rgba(161,105,247,0.1); color: #a169f7; }
.vip-gold { background: rgba(250,173,20,0.1); color: #faad14; }
.vip-silver { background: rgba(158,158,158,0.1); color: #8c8c8c; }
.vip-normal { background: #f5f5f5; color: #8c8c8c; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.search-box { margin-bottom: 24rpx; }
.search-box input { width: 100%; padding: 20rpx 24rpx; border: 1px solid #d9d9d9; border-radius: 12rpx; }
.guest-list { display: flex; flex-direction: column; gap: 20rpx; }
.guest-card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.guest-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12rpx; }
.guest-name { font-size: 32rpx; font-weight: 600; }
.guest-info { display: flex; gap: 24rpx; font-size: 26rpx; color: #8c8c8c; margin-bottom: 12rpx; }
.guest-consume { font-size: 28rpx; color: #ff4d4f; font-weight: 600; }
.toast { position: fixed; bottom: 120rpx; left: 50%; transform: translateX(-50%) translateY(100rpx); background: rgba(0,0,0,0.75); color: #fff; border-radius: 8rpx; padding: 24rpx 48rpx; font-size: 28rpx; z-index: 9999; opacity: 0; transition: all 0.3s; }
.toast.show { transform: translateX(-50%) translateY(0); opacity: 1; }
</style>
