<template>
	<view class="page">
		<!-- PC端布局 -->
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<!-- 楼层标签 -->
					<view class="floor-tabs">
						<view 
							v-for="floor in 7" 
							:key="floor" 
							class="floor-tab"
							:class="{ active: currentFloor === floor }"
							@tap="switchFloor(floor)"
						>
							{{ floor }}层
						</view>
					</view>
					
					<!-- 图例 -->
					<view class="room-legend">
						<view class="legend-item">
							<view class="legend-color" style="background: #e6f4ff; border: 1px solid #91caff;"></view>
							<text>空闲</text>
						</view>
						<view class="legend-item">
							<view class="legend-color" style="background: #d6e4ff; border: 1px solid #69b1ff;"></view>
							<text>已入住</text>
						</view>
						<view class="legend-item">
							<view class="legend-color" style="background: #f5f5f5; border: 1px solid #d9d9d9;"></view>
							<text>已预订</text>
						</view>
						<view class="legend-item">
							<view class="legend-color" style="background: #fff7e6; border: 1px solid #ffd591;"></view>
							<text>脏房</text>
						</view>
						<view class="legend-item">
							<view class="legend-color" style="background: #e6f4ff; border: 1px solid #91d5ff;"></view>
							<text>清洁中</text>
						</view>
						<view class="legend-item">
							<view class="legend-color" style="background: #fff1f0; border: 1px solid #ffccc7;"></view>
							<text>维修中</text>
						</view>
					</view>
					
					<!-- 房间网格 -->
					<view class="room-grid-container">
						<view class="room-grid">
							<view 
								v-for="room in floorRooms" 
								:key="room.number" 
								class="room-cell"
								:class="room.status"
								@tap="showRoomDetail(room)"
							>
								<text class="room-number">{{ room.number }}</text>
								<text class="room-type">{{ room.typeName }}</text>
							</view>
						</view>
					</view>
					
					<!-- 房型价格 -->
					<view class="card">
						<view class="card-header">
							<view class="card-title">房型价格</view>
						</view>
						<view class="room-types">
							<view class="room-type-item">
								<text class="type-name">豪华大床房</text>
								<text class="type-price">¥488/晚</text>
							</view>
							<view class="room-type-item">
								<text class="type-name">标准双床房</text>
								<text class="type-price">¥358/晚</text>
							</view>
							<view class="room-type-item">
								<text class="type-name">行政套房</text>
								<text class="type-price">¥888/晚</text>
							</view>
							<view class="room-type-item">
								<text class="type-name">经济单人房</text>
								<text class="type-price">¥258/晚</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端布局 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">房态管理</text>
			</view>
			<view class="mobile-content">
				<!-- 楼层选择 -->
				<scroll-view scroll-x class="floor-scroll">
					<view class="floor-tabs-mobile">
						<view 
							v-for="floor in 7" 
							:key="floor" 
							class="floor-tab-mobile"
							:class="{ active: currentFloor === floor }"
							@tap="switchFloor(floor)"
						>
							{{ floor }}层
						</view>
					</view>
				</scroll-view>
				
				<!-- 图例 -->
				<view class="legend-mobile">
					<view class="legend-item" v-for="item in legendItems" :key="item.status">
						<view class="legend-color" :style="item.style"></view>
						<text>{{ item.label }}</text>
					</view>
				</view>
				
				<!-- 房间列表 -->
				<view class="room-list">
					<view 
						v-for="room in floorRooms" 
						:key="room.number" 
						class="room-item"
						:class="room.status"
						@tap="showRoomDetail(room)"
					>
						<view class="room-info">
							<text class="room-num">{{ room.number }}</text>
							<text class="room-name">{{ room.typeName }}</text>
						</view>
						<view class="room-status-text">{{ room.statusName }}</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 房间详情弹窗 -->
		<view class="modal-mask" v-if="showModal" @tap="closeModal">
			<view class="modal-content" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">房间详情</text>
					<text class="modal-close" @tap="closeModal">✕</text>
				</view>
				<view class="modal-body">
					<view class="room-detail-header">
						<view class="stat-icon blue" style="width: 120rpx; height: 120rpx; font-size: 48rpx;">
							🛏️
						</view>
						<view class="room-detail-info">
							<view class="room-detail-number">{{ selectedRoom.number }}</view>
							<view class="room-detail-meta">
								{{ selectedRoom.typeName }} | {{ selectedRoom.floor }}层 | ¥{{ selectedRoom.price }}/晚
							</view>
							<view class="room-status-badge" :class="'status-' + selectedRoom.status">
								{{ selectedRoom.statusName }}
							</view>
						</view>
					</view>
					
					<view class="detail-section" v-if="selectedRoom.guest">
						<view class="detail-title">在住客人信息</view>
						<view class="detail-grid">
							<view class="detail-item">
								<text class="detail-label">姓名</text>
								<text class="detail-value">{{ selectedRoom.guest.name }}</text>
							</view>
							<view class="detail-item">
								<text class="detail-label">手机号</text>
								<text class="detail-value">{{ selectedRoom.guest.phone }}</text>
							</view>
							<view class="detail-item">
								<text class="detail-label">入住日期</text>
								<text class="detail-value">{{ selectedRoom.guest.checkin }}</text>
							</view>
							<view class="detail-item">
								<text class="detail-label">预计退房</text>
								<text class="detail-value">{{ selectedRoom.guest.checkout }}</text>
							</view>
						</view>
					</view>
					
					<view class="detail-section">
						<view class="detail-title">快速操作</view>
						<view class="action-buttons">
							<view v-if="can('room:change')" class="action-btn" @tap="handleAction('change')">🔄 换房</view>
							<view v-if="can('room:extend')" class="action-btn" @tap="handleAction('extend')">⏰ 续住</view>
							<view v-if="can('billing:edit')" class="action-btn" @tap="handleAction('charge')">➕ 加消费</view>
							<view v-if="can('room:update')" class="action-btn" @tap="handleAction('clean')">🧹 清洁</view>
							<view v-if="can('checkout:handle')" class="action-btn danger" @tap="handleAction('checkout')">💳 退房</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 换房弹窗 -->
		<view class="modal-mask" v-if="showChangeModal" @tap="closeOperationModals">
			<view class="modal-content small-modal" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">办理换房</text>
					<text class="modal-close" @tap="closeOperationModals">✕</text>
				</view>
				<view class="modal-body">
					<view class="operation-row">
						<text class="operation-label">原房间</text>
						<text class="operation-value">{{ selectedRoom.number }} {{ selectedRoom.typeName }}</text>
					</view>
					<view class="operation-row">
						<text class="operation-label">目标房间</text>
						<picker :range="vacantRooms" range-key="label" @change="onChangeTargetRoom">
							<view class="operation-input">{{ changeTargetLabel || '请选择空闲房' }}</view>
						</picker>
					</view>
					<view class="modal-actions">
						<button class="plain-btn" @tap="closeOperationModals">取消</button>
						<button class="primary-btn" @tap="confirmChangeRoom">确认换房</button>
					</view>
				</view>
			</view>
		</view>

		<!-- 续住弹窗 -->
		<view class="modal-mask" v-if="showExtendModal" @tap="closeOperationModals">
			<view class="modal-content small-modal" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">办理续住</text>
					<text class="modal-close" @tap="closeOperationModals">✕</text>
				</view>
				<view class="modal-body">
					<view class="operation-row">
						<text class="operation-label">房间</text>
						<text class="operation-value">{{ selectedRoom.number }} {{ selectedRoom.typeName }}</text>
					</view>
					<view class="operation-row">
						<text class="operation-label">新的预计退房日期</text>
						<picker mode="date" :start="today" @change="onExtendDateChange">
							<view class="operation-input">{{ extendCheckout || '请选择日期' }}</view>
						</picker>
					</view>
					<view class="modal-actions">
						<button class="plain-btn" @tap="closeOperationModals">取消</button>
						<button class="primary-btn" @tap="confirmExtendStay">确认续住</button>
					</view>
				</view>
			</view>
		</view>

		<!-- Toast -->
		<view class="toast" :class="{ show: toastShow }">{{ toastMessage }}</view>
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
			currentPage: 'room-status',
			pageName: '房态管理',
			isPC: true,
			isSidebarCollapsed: false,
			currentFloor: 1,
			showModal: false,
			showChangeModal: false,
			showExtendModal: false,
			toastShow: false,
			toastMessage: '',
			selectedRoom: {},
			vacantRooms: [],
			changeTargetRoom: '',
			changeTargetLabel: '',
			extendCheckout: '',
			today: new Date().toISOString().split('T')[0],
			legendItems: [
				{ status: 'vacant', label: '空闲', style: 'background: #e6f4ff; border: 1px solid #91caff;' },
				{ status: 'occupied', label: '已入住', style: 'background: #d6e4ff; border: 1px solid #69b1ff;' },
				{ status: 'reserved', label: '已预订', style: 'background: #f5f5f5; border: 1px solid #d9d9d9;' },
				{ status: 'dirty', label: '脏房', style: 'background: #fff7e6; border: 1px solid #ffd591;' },
				{ status: 'cleaning', label: '清洁中', style: 'background: #e6f4ff; border: 1px solid #91d5ff;' },
				{ status: 'maintenance', label: '维修中', style: 'background: #fff1f0; border: 1px solid #ffccc7;' }
			],
			roomTypes: {
				'RT001': { name: '豪华大床房', price: 488 },
				'RT002': { name: '标准双床房', price: 358 },
				'RT003': { name: '行政套房', price: 888 },
				'RT004': { name: '经济单人房', price: 258 }
			},
			statusLabels: {
				'vacant': '空闲',
				'occupied': '已入住',
				'reserved': '已预订',
				'dirty': '脏房',
				'cleaning': '清洁中',
				'maintenance': '维修中'
			},
			allRooms: []
		}
	},
	computed: {
		floorRooms() {
			return this.allRooms.filter(r => r.floor === this.currentFloor)
		}
	},
	onLoad() {
		this.loadRooms()
		uni.getSystemInfo({
			success: (res) => {
				this.isPC = res.windowWidth >= 768
			}
		})
	},
	methods: {
		async loadRooms() {
			try {
				const rooms = await this.$api.get('/api/rooms')
				this.allRooms = rooms || []
			} catch (error) {
				this.showToast(error.message || '房态加载失败，已使用本地演示数据')
				this.generateRooms()
			}
		},
		generateRooms() {
			this.allRooms = []
			const statuses = ['vacant', 'occupied', 'reserved', 'dirty', 'cleaning', 'maintenance']
			for (let floor = 1; floor <= 7; floor++) {
				for (let room = 1; room <= 14; room++) {
					const roomNum = floor * 100 + room
					let type, price, typeName
					
					if (floor === 6 || floor === 7) {
						if (floor === 7 && room <= 10) {
							type = 'RT003'
							price = 888
							typeName = '行政套房'
						} else {
							type = 'RT001'
							price = 488
							typeName = '豪华大床房'
						}
					} else if (floor >= 2 && floor <= 5) {
						type = 'RT002'
						price = 358
						typeName = '标准双床房'
					} else {
						type = 'RT004'
						price = 258
						typeName = '经济单人房'
					}
					
					const rand = Math.random()
					let status
					if (rand < 0.18) status = 'vacant'
					else if (rand < 0.78) status = 'occupied'
					else if (rand < 0.85) status = 'reserved'
					else if (rand < 0.90) status = 'dirty'
					else if (rand < 0.95) status = 'cleaning'
					else status = 'maintenance'
					
					let guest = null
					if (status === 'occupied') {
						const guests = ['王建国', '张先生', '陈小姐', '李娜', '刘伟', '赵敏', '孙杰', '周婷']
						guest = {
							name: guests[Math.floor(Math.random() * guests.length)],
							phone: '138****5678',
							checkin: '2026-06-27',
							checkout: '2026-06-29'
						}
					}
					
					this.allRooms.push({
						number: roomNum,
						floor: floor,
						type: type,
						price: price,
						typeName: typeName,
						status: status,
						statusName: this.statusLabels[status],
						guest: guest
					})
				}
			}
		},
		switchFloor(floor) {
			this.currentFloor = floor
		},
		showRoomDetail(room) {
			this.selectedRoom = room
			this.showModal = true
		},
		closeModal() {
			this.showModal = false
		},
		handleNavigate(page) {
			this.currentPage = page
			this.pageName = this.$rbac.getPageName(page)
			this.navigateToPage(page)
		},
		async openChangeRoomModal() {
			try {
				const rooms = await this.$api.get('/api/rooms', { status: 'vacant' })
				this.vacantRooms = (rooms || []).map(room => ({
					...room,
					label: `${room.number} - ${room.typeName} - ¥${room.price}/晚`
				}))
				if (this.vacantRooms.length === 0) {
					this.showToast('当前没有可换的空闲房')
					return
				}
				this.changeTargetRoom = ''
				this.changeTargetLabel = ''
				this.showModal = false
				this.showChangeModal = true
			} catch (error) {
				this.showToast(error.message || '空闲房加载失败')
			}
		},
		openExtendStayModal() {
			this.extendCheckout = this.nextDate(1)
			this.showModal = false
			this.showExtendModal = true
		},
		onChangeTargetRoom(e) {
			const selected = this.vacantRooms[e.detail.value]
			if (!selected) {
				return
			}
			this.changeTargetRoom = selected.number
			this.changeTargetLabel = selected.label
		},
		onExtendDateChange(e) {
			this.extendCheckout = e.detail.value
		},
		async confirmChangeRoom() {
			if (!this.can('room:change')) {
				this.showNoPermission()
				return
			}
			if (!this.changeTargetRoom) {
				this.showToast('请选择目标房间')
				return
			}
			try {
				await this.$api.post(`/api/rooms/${this.selectedRoom.number}/change-room`, {
					targetRoomNumber: this.changeTargetRoom,
					reason: 'quick-action'
				})
				this.closeOperationModals()
				await this.loadRooms()
				this.showToast(`已换至 ${this.changeTargetRoom} 房`)
			} catch (error) {
				this.showToast(error.message || '换房失败')
			}
		},
		async confirmExtendStay() {
			if (!this.can('room:extend')) {
				this.showNoPermission()
				return
			}
			if (!this.extendCheckout) {
				this.showToast('请选择续住日期')
				return
			}
			try {
				await this.$api.patch(`/api/rooms/${this.selectedRoom.number}/extend-stay`, {
					checkout: this.extendCheckout,
					reason: 'quick-action'
				})
				this.closeOperationModals()
				await this.loadRooms()
				this.showToast(`已续住至 ${this.extendCheckout}`)
			} catch (error) {
				this.showToast(error.message || '续住失败')
			}
		},
		closeOperationModals() {
			this.showChangeModal = false
			this.showExtendModal = false
		},
		nextDate(days) {
			const date = new Date()
			date.setDate(date.getDate() + days)
			return date.toISOString().split('T')[0]
		},
		actionPermission(action) {
			const permissions = {
				change: 'room:change',
				extend: 'room:extend',
				charge: 'billing:edit',
				clean: 'room:update',
				checkout: 'checkout:handle'
			}
			return permissions[action]
		},
		async performRoomAction(action) {
			const permission = this.actionPermission(action)
			if (permission && !this.can(permission)) {
				this.showNoPermission()
				return
			}
			const roomNumber = this.selectedRoom && this.selectedRoom.number
			const roomStatus = this.selectedRoom && this.selectedRoom.status
			if (!roomNumber) {
				this.showToast('请先选择房间')
				return
			}
			if (action === 'change') {
				if (roomStatus !== 'occupied') {
					this.showToast('只有在住房可以办理换房')
					return
				}
				await this.openChangeRoomModal()
				return
			}
			if (action === 'extend') {
				if (roomStatus !== 'occupied') {
					this.showToast('只有在住房可以办理续住')
					return
				}
				this.openExtendStayModal()
				return
			}
			if (action === 'charge') {
				if (roomStatus !== 'occupied') {
					this.showToast('只有在住房可以加消费')
					return
				}
				this.closeModal()
				this.navigateToPage('billing', { action: 'charge', roomNumber })
				return
			}
			if (action === 'clean') {
				if (roomStatus === 'occupied') {
					this.showToast('在住房不能直接清洁')
					return
				}
				try {
					await this.$api.patch(`/api/rooms/${roomNumber}/status`, { status: 'cleaning', reason: 'quick-action' })
					await this.loadRooms()
					this.closeModal()
					this.showToast('已提交清洁任务')
				} catch (error) {
					this.showToast(error.message || '清洁提交失败')
				}
				return
			}
			if (action === 'checkout') {
				if (roomStatus !== 'occupied') {
					this.showToast('只有在住房可以退房')
					return
				}
				this.closeModal()
				this.navigateToPage('checkout', { roomNumber })
			}
		},
		async handleAction(action) {
			await this.performRoomAction(action)
		},
		toggleSidebar() {
			this.isSidebarCollapsed = !this.isSidebarCollapsed
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

/* PC布局 */
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

.floor-tabs {
	display: flex;
	gap: 8px;
	margin-bottom: 20px;
	flex-wrap: wrap;
}

.floor-tab {
	padding: 8px 16px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
	transition: all 0.3s;
	background: #fff;
	font-size: 14px;
}

.floor-tab:hover {
	border-color: #1677ff;
	color: #1677ff;
}

.floor-tab.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.room-legend {
	display: flex;
	gap: 20px;
	margin-bottom: 20px;
	flex-wrap: wrap;
}

.legend-item {
	display: flex;
	align-items: center;
	gap: 6px;
	font-size: 13px;
}

.legend-color {
	width: 16px;
	height: 16px;
	border-radius: 4px;
}

.room-grid-container {
	background: #fff;
	border-radius: 8px;
	padding: 20px;
}

.room-grid {
	display: grid;
	grid-template-columns: repeat(14, 1fr);
	gap: 8px;
}

.room-cell {
	aspect-ratio: 1;
	border-radius: 6px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	transition: all 0.3s;
	font-size: 12px;
	font-weight: 500;
	min-height: 50px;
}

.room-cell:hover {
	transform: translateY(-2px);
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.room-cell.vacant {
	background: #e6f4ff;
	color: #1677ff;
	border: 1px solid #91caff;
}

.room-cell.occupied {
	background: #d6e4ff;
	color: #0958d9;
	border: 1px solid #69b1ff;
}

.room-cell.reserved {
	background: #f5f5f5;
	color: #595959;
	border: 1px solid #d9d9d9;
}

.room-cell.dirty {
	background: #fff7e6;
	color: #d46b08;
	border: 1px solid #ffd591;
}

.room-cell.cleaning {
	background: #e6f4ff;
	color: #0891b2;
	border: 1px solid #91d5ff;
}

.room-cell.maintenance {
	background: #fff1f0;
	color: #cf1322;
	border: 1px solid #ffccc7;
}

.room-number {
	font-weight: 600;
}

.room-type {
	font-size: 10px;
	opacity: 0.8;
}

.room-types {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 16px;
}

.room-type-item {
	display: flex;
	justify-content: space-between;
	padding: 16px;
	background: #fafafa;
	border-radius: 8px;
}

.type-name {
	color: #262626;
}

.type-price {
	font-weight: 600;
	color: #1677ff;
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

.floor-scroll {
	width: 100%;
	margin-bottom: 20rpx;
}

.floor-tabs-mobile {
	display: flex;
	gap: 16rpx;
	padding: 0 24rpx;
}

.floor-tab-mobile {
	padding: 16rpx 32rpx;
	background: #fff;
	border: 1px solid #d9d9d9;
	border-radius: 8rpx;
	font-size: 28rpx;
	white-space: nowrap;
}

.floor-tab-mobile.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.legend-mobile {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
	padding: 20rpx;
	background: #fff;
	border-radius: 12rpx;
	margin-bottom: 24rpx;
}

.room-list {
	display: flex;
	flex-direction: column;
	gap: 12rpx;
}

.room-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 24rpx;
	background: #fff;
	border-radius: 12rpx;
}

.room-item.vacant {
	border-left: 6rpx solid #4caf50;
}

.room-item.occupied {
	border-left: 6rpx solid #2196f3;
}

.room-item.reserved {
	border-left: 6rpx solid #9e9e9e;
}

.room-item.dirty {
	border-left: 6rpx solid #ff9800;
}

.room-item.cleaning {
	border-left: 6rpx solid #03a9f4;
}

.room-item.maintenance {
	border-left: 6rpx solid #f44336;
}

.room-info {
	display: flex;
	flex-direction: column;
}

.room-num {
	font-size: 32rpx;
	font-weight: 600;
}

.room-name {
	font-size: 24rpx;
	color: #8c8c8c;
	margin-top: 4rpx;
}

.room-status-text {
	font-size: 26rpx;
	color: #8c8c8c;
}

/* 弹窗 */
.modal-mask {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.45);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 1000;
}

.modal-content {
	background: #fff;
	border-radius: 16rpx;
	width: 560rpx;
	max-width: 90vw;
	max-height: 80vh;
	overflow: auto;
}

.modal-header {
	padding: 32rpx;
	border-bottom: 1px solid #f0f0f0;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.modal-title {
	font-size: 36rpx;
	font-weight: 600;
}

.modal-close {
	font-size: 32rpx;
	color: #8c8c8c;
}

.modal-body {
	padding: 32rpx;
}

.room-detail-header {
	display: flex;
	gap: 24rpx;
	margin-bottom: 32rpx;
}

.room-detail-info {
	flex: 1;
}

.room-detail-number {
	font-size: 48rpx;
	font-weight: 700;
}

.room-detail-meta {
	color: #8c8c8c;
	font-size: 26rpx;
	margin-top: 8rpx;
}

.room-status-badge {
	display: inline-block;
	margin-top: 12rpx;
	padding: 8rpx 16rpx;
	border-radius: 6rpx;
	font-size: 24rpx;
	font-weight: 500;
}

.status-vacant {
	background: rgba(76, 175, 80, 0.1);
	color: #4caf50;
}

.status-occupied {
	background: rgba(33, 150, 243, 0.1);
	color: #2196f3;
}

.status-reserved {
	background: rgba(158, 158, 158, 0.1);
	color: #9e9e9e;
}

.status-dirty {
	background: rgba(255, 152, 0, 0.1);
	color: #ff9800;
}

.status-cleaning {
	background: rgba(3, 169, 244, 0.1);
	color: #03a9f4;
}

.status-maintenance {
	background: rgba(244, 67, 54, 0.1);
	color: #f44336;
}

.detail-section {
	margin-top: 32rpx;
	padding-top: 32rpx;
	border-top: 1px solid #f0f0f0;
}

.detail-title {
	font-size: 30rpx;
	font-weight: 600;
	margin-bottom: 20rpx;
}

.detail-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 16rpx;
}

.detail-item {
	display: flex;
	flex-direction: column;
}

.detail-label {
	font-size: 24rpx;
	color: #8c8c8c;
}

.detail-value {
	font-size: 28rpx;
	font-weight: 500;
	margin-top: 4rpx;
}

.action-buttons {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.action-btn {
	padding: 16rpx 24rpx;
	background: #f5f5f5;
	border-radius: 8rpx;
	font-size: 26rpx;
}

.action-btn.danger {
	background: rgba(255, 77, 79, 0.1);
	color: #ff4d4f;
}

.small-modal {
	max-width: 420px;
}

.operation-row {
	margin-bottom: 24rpx;
}

.operation-label {
	display: block;
	font-size: 24rpx;
	color: #8c8c8c;
	margin-bottom: 10rpx;
}

.operation-value {
	font-size: 28rpx;
	font-weight: 600;
	color: #262626;
}

.operation-input {
	height: 76rpx;
	line-height: 76rpx;
	border: 1px solid #d9d9d9;
	border-radius: 8rpx;
	padding: 0 20rpx;
	font-size: 26rpx;
	background: #fff;
	color: #262626;
}

.modal-actions {
	display: flex;
	justify-content: flex-end;
	gap: 16rpx;
	margin-top: 32rpx;
}

.plain-btn,
.primary-btn {
	margin: 0;
	height: 72rpx;
	line-height: 72rpx;
	padding: 0 28rpx;
	border-radius: 8rpx;
	font-size: 26rpx;
}

.plain-btn {
	background: #fff;
	color: #595959;
	border: 1px solid #d9d9d9;
}

.primary-btn {
	background: #1677ff;
	color: #fff;
	border: 1px solid #1677ff;
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
