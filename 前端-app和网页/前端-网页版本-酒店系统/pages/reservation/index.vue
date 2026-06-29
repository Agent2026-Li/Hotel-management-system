<template>
	<view class="page">
		<!-- PC端布局 -->
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<!-- 搜索栏 -->
					<view class="search-bar">
						<view class="search-input">
							<input type="text" placeholder="搜索预订号/客人姓名/手机号" v-model="searchKeyword" />
						</view>
						<view class="filter-group">
							<picker mode="date" :start="startDate" @change="onDateChange">
								<view class="filter-btn">{{ checkinDate || '入住日期' }}</view>
							</picker>
							<picker mode="date" :start="startDate" @change="onDateChange2">
								<view class="filter-btn">{{ checkoutDate || '退房日期' }}</view>
							</picker>
							<picker :range="statusOptions" range-key="label" @change="onStatusChange">
								<view class="filter-btn">{{ selectedStatus.label || '全部状态' }}</view>
							</picker>
							<button class="btn btn-primary btn-sm" @tap="showAddModal">+ 新建预订</button>
						</view>
					</view>
					
					<!-- 预订列表 -->
					<view class="card">
						<view class="card-header">
							<view class="card-title">预订列表</view>
							<text class="card-count">共 {{ reservations.length }} 条</text>
						</view>
						<view class="table-container">
							<table class="table">
								<thead>
									<tr>
										<th>预订号</th>
										<th>客人姓名</th>
										<th>手机号</th>
										<th>房型</th>
										<th>入住日期</th>
										<th>退房日期</th>
										<th>天数</th>
										<th>金额</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(item, index) in reservations" :key="index">
										<td>{{ item.id }}</td>
										<td>{{ item.name }}</td>
										<td>{{ item.phone }}</td>
										<td>{{ item.roomType }}</td>
										<td>{{ item.checkin }}</td>
										<td>{{ item.checkout }}</td>
										<td>{{ item.nights }}晚</td>
										<td>¥{{ item.amount }}</td>
										<td>
											<span class="status-badge" :class="'status-' + item.status">
												{{ item.statusName }}
											</span>
										</td>
										<td>
											<view class="action-btns">
												<text class="action-link" @tap="editReservation(item)">编辑</text>
												<text class="action-link" @tap="checkinReservation(item)">入住</text>
												<text class="action-link danger" @tap="cancelReservation(item)">取消</text>
											</view>
										</td>
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
				<text class="mobile-title">预订管理</text>
				<view class="header-add" @tap="showAddModal">+ 新建</view>
			</view>
			<view class="mobile-content">
				<!-- 搜索框 -->
				<view class="search-box-mobile">
					<input type="text" placeholder="搜索预订号/姓名/手机号" v-model="searchKeyword" />
				</view>
				
				<!-- 预订卡片列表 -->
				<view class="reservation-list">
					<view v-for="(item, index) in reservations" :key="index" class="reservation-card">
						<view class="card-top">
							<text class="res-id">{{ item.id }}</text>
							<span class="status-badge" :class="'status-' + item.status">{{ item.statusName }}</span>
						</view>
						<view class="card-main">
							<view class="guest-info">
								<text class="guest-name">{{ item.name }}</text>
								<text class="guest-phone">{{ item.phone }}</text>
							</view>
							<view class="room-info">
								<text class="room-type">{{ item.roomType }}</text>
								<text class="room-dates">{{ item.checkin }} - {{ item.checkout }}</text>
							</view>
						</view>
						<view class="card-bottom">
							<text class="res-amount">¥{{ item.amount }}</text>
							<view class="card-actions">
								<text class="action-text" @tap="editReservation(item)">编辑</text>
								<text class="action-text primary" @tap="checkinReservation(item)">入住</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 新建/编辑预订弹窗 -->
		<view class="modal-mask" v-if="showModal" @tap="closeModal">
			<view class="modal-content modal-form" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">{{ isEdit ? '编辑预订' : '新建预订' }}</text>
					<text class="modal-close" @tap="closeModal">✕</text>
				</view>
				<view class="modal-body">
					<view class="form-group">
						<label class="form-label">客人姓名 <text class="required">*</text></label>
						<input class="form-input" type="text" v-model="formData.name" placeholder="请输入客人姓名" />
					</view>
					<view class="form-group">
						<label class="form-label">手机号 <text class="required">*</text></label>
						<input class="form-input" type="tel" v-model="formData.phone" placeholder="请输入手机号" />
					</view>
					<view class="form-group">
						<label class="form-label">身份证号</label>
						<input class="form-input" type="idcard" v-model="formData.idcard" placeholder="请输入身份证号" />
					</view>
					<view class="form-row">
						<view class="form-group">
							<label class="form-label">房型 <text class="required">*</text></label>
							<picker :range="roomTypeOptions" range-key="name" @change="onRoomTypeChange">
								<view class="form-input picker-value">{{ formData.roomTypeName || '请选择房型' }}</view>
							</picker>
						</view>
						<view class="form-group">
							<label class="form-label">房间数</label>
							<input class="form-input" type="number" v-model="formData.roomCount" placeholder="1" />
						</view>
					</view>
					<view class="form-row">
						<view class="form-group">
							<label class="form-label">入住日期 <text class="required">*</text></label>
							<picker mode="date" :start="startDate" @change="onFormDateChange">
								<view class="form-input picker-value">{{ formData.checkin || '请选择' }}</view>
							</picker>
						</view>
						<view class="form-group">
							<label class="form-label">退房日期 <text class="required">*</text></label>
							<picker mode="date" :start="formData.checkin || startDate" @change="onFormDateChange2">
								<view class="form-input picker-value">{{ formData.checkout || '请选择' }}</view>
							</picker>
						</view>
					</view>
					<view class="form-group">
						<label class="form-label">押金</label>
						<input class="form-input" type="number" v-model="formData.deposit" placeholder="请输入押金金额" />
					</view>
					<view class="form-group">
						<label class="form-label">备注</label>
						<textarea class="form-input" rows="2" v-model="formData.remark" placeholder="备注信息..."></textarea>
					</view>
				</view>
				<view class="modal-footer">
					<button class="btn btn-default" @tap="closeModal">取消</button>
					<button class="btn btn-primary" @tap="saveReservation">保存</button>
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
			currentPage: 'reservation',
			pageName: '预订管理',
			isPC: true,
			isSidebarCollapsed: false,
			searchKeyword: '',
			checkinDate: '',
			checkoutDate: '',
			startDate: '',
			selectedStatus: {},
			statusOptions: [
				{ label: '全部状态', value: '' },
				{ label: '待入住', value: 'pending' },
				{ label: '已入住', value: 'checked_in' },
				{ label: '已取消', value: 'cancelled' },
				{ label: '已退房', value: 'checked_out' }
			],
			showModal: false,
			isEdit: false,
			toastShow: false,
			toastMessage: '',
			formData: {
				name: '',
				phone: '',
				idcard: '',
				roomType: '',
				roomTypeName: '',
				roomCount: 1,
				checkin: '',
				checkout: '',
				deposit: '',
				remark: ''
			},
			roomTypeOptions: [
				{ name: '豪华大床房', price: 488 },
				{ name: '标准双床房', price: 358 },
				{ name: '行政套房', price: 888 },
				{ name: '经济单人房', price: 258 }
			],
			reservations: [
				{ id: 'RES2026062701', name: '张三', phone: '13912345678', roomType: '豪华大床房', checkin: '2026-06-28', checkout: '2026-06-30', nights: 2, amount: '976', status: 'pending', statusName: '待入住' },
				{ id: 'RES2026062702', name: '李四', phone: '13823456789', roomType: '标准双床房', checkin: '2026-06-29', checkout: '2026-07-01', nights: 2, amount: '716', status: 'pending', statusName: '待入住' },
				{ id: 'RES2026062601', name: '王五', phone: '13734567890', roomType: '行政套房', checkin: '2026-06-26', checkout: '2026-06-28', nights: 2, amount: '1,776', status: 'checked_in', statusName: '已入住' },
				{ id: 'RES2026062501', name: '赵六', phone: '13645678901', roomType: '经济单人房', checkin: '2026-06-25', checkout: '2026-06-27', nights: 2, amount: '516', status: 'checked_out', statusName: '已退房' },
				{ id: 'RES2026062401', name: '钱七', phone: '13556789012', roomType: '标准双床房', checkin: '2026-06-24', checkout: '2026-06-26', nights: 2, amount: '716', status: 'cancelled', statusName: '已取消' }
			]
		}
	},
	onLoad() {
		const now = new Date()
		this.startDate = now.toISOString().split('T')[0]
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
		onDateChange(e) {
			this.checkinDate = e.detail.value
		},
		onDateChange2(e) {
			this.checkoutDate = e.detail.value
		},
		onStatusChange(e) {
			this.selectedStatus = this.statusOptions[e.detail.value]
		},
		onRoomTypeChange(e) {
			const selected = this.roomTypeOptions[e.detail.value]
			this.formData.roomType = selected.name
			this.formData.roomTypeName = selected.name
		},
		onFormDateChange(e) {
			this.formData.checkin = e.detail.value
		},
		onFormDateChange2(e) {
			this.formData.checkout = e.detail.value
		},
		showAddModal() {
			this.isEdit = false
			this.formData = {
				name: '',
				phone: '',
				idcard: '',
				roomType: '',
				roomTypeName: '',
				roomCount: 1,
				checkin: '',
				checkout: '',
				deposit: '',
				remark: ''
			}
			this.showModal = true
		},
		editReservation(item) {
			this.isEdit = true
			this.formData = { ...item }
			this.showModal = true
		},
		checkinReservation(item) {
			this.showToast('跳转入住登记页面')
			uni.navigateTo({
				url: '/pages/checkin/index'
			})
		},
		cancelReservation(item) {
			uni.showModal({
				title: '提示',
				content: '确定要取消该预订吗？',
				success: (res) => {
					if (res.confirm) {
						this.showToast('预订已取消')
					}
				}
			})
		},
		closeModal() {
			this.showModal = false
		},
		saveReservation() {
			if (!this.formData.name || !this.formData.phone) {
				this.showToast('请填写必填项')
				return
			}
			this.showToast(this.isEdit ? '预订已更新' : '预订创建成功')
			this.closeModal()
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

.search-bar {
	display: flex;
	gap: 12px;
	margin-bottom: 20px;
	flex-wrap: wrap;
	align-items: center;
}

.search-input {
	flex: 1;
	min-width: 200px;
}

.search-input input {
	width: 100%;
	padding: 8px 12px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	font-size: 14px;
}

.filter-group {
	display: flex;
	gap: 12px;
	align-items: center;
}

.filter-btn {
	padding: 8px 16px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	background: #fff;
	font-size: 14px;
	min-width: 100px;
	text-align: center;
}

.card-count {
	color: #8c8c8c;
	font-size: 14px;
}

.action-btns {
	display: flex;
	gap: 12px;
}

.action-link {
	color: #1677ff;
	cursor: pointer;
	font-size: 14px;
}

.action-link.danger {
	color: #ff4d4f;
}

.action-link + .action-link::before {
	content: '|';
	margin-right: 12px;
	color: #d9d9d9;
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
	justify-content: space-between;
	padding: 0 32rpx;
}

.mobile-title {
	font-size: 32rpx;
	font-weight: 600;
}

.header-add {
	font-size: 28rpx;
	color: #fff;
}

.mobile-content {
	padding: 24rpx;
}

.search-box-mobile {
	margin-bottom: 24rpx;
}

.search-box-mobile input {
	width: 100%;
	padding: 20rpx 24rpx;
	border: 1px solid #d9d9d9;
	border-radius: 12rpx;
	font-size: 28rpx;
}

.reservation-list {
	display: flex;
	flex-direction: column;
	gap: 20rpx;
}

.reservation-card {
	background: #fff;
	border-radius: 16rpx;
	padding: 24rpx;
}

.card-top {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16rpx;
}

.res-id {
	font-size: 28rpx;
	font-weight: 600;
}

.card-main {
	padding: 16rpx 0;
	border-top: 1px solid #f0f0f0;
	border-bottom: 1px solid #f0f0f0;
}

.guest-info, .room-info {
	display: flex;
	justify-content: space-between;
	margin-bottom: 8rpx;
}

.guest-name, .room-type {
	font-size: 28rpx;
	font-weight: 500;
}

.guest-phone, .room-dates {
	font-size: 24rpx;
	color: #8c8c8c;
}

.card-bottom {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-top: 16rpx;
}

.res-amount {
	font-size: 32rpx;
	font-weight: 700;
	color: #1677ff;
}

.card-actions {
	display: flex;
	gap: 24rpx;
}

.action-text {
	font-size: 26rpx;
	color: #8c8c8c;
}

.action-text.primary {
	color: #1677ff;
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

.modal-form {
	width: 640rpx;
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

.form-row {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 24rpx;
}

.form-group {
	margin-bottom: 24rpx;
}

.form-label {
	display: block;
	margin-bottom: 12rpx;
	font-size: 28rpx;
	font-weight: 500;
}

.required {
	color: #ff4d4f;
}

.form-input {
	width: 100%;
	padding: 16rpx 24rpx;
	border: 1px solid #d9d9d9;
	border-radius: 8rpx;
	font-size: 28rpx;
}

.picker-value {
	background: #fff;
}

.modal-footer {
	padding: 32rpx;
	border-top: 1px solid #f0f0f0;
	display: flex;
	justify-content: flex-end;
	gap: 24rpx;
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
