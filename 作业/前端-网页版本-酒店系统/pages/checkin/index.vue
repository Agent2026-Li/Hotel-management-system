<template>
	<view class="page">
		<!-- PC端布局 -->
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<!-- Tab切换 -->
					<view class="tabs">
						<view class="tab-item" :class="{ active: activeTab === 'new' }" @tap="activeTab = 'new'">新客入住</view>
						<view class="tab-item" :class="{ active: activeTab === 'reserved' }" @tap="activeTab = 'reserved'">预订入住</view>
					</view>
					
					<view class="tab-content">
						<!-- 新客入住 -->
						<view v-if="activeTab === 'new'" class="checkin-form">
							<view class="form-section">
								<view class="section-title">基本信息</view>
								<view class="form-grid">
									<view class="form-group">
										<label class="form-label">客人姓名 <text class="required">*</text></label>
										<input class="form-input" type="text" v-model="formData.name" placeholder="请输入客人姓名" />
									</view>
									<view class="form-group">
										<label class="form-label">手机号 <text class="required">*</text></label>
										<input class="form-input" type="tel" v-model="formData.phone" placeholder="请输入手机号" />
									</view>
									<view class="form-group">
										<label class="form-label">身份证号 <text class="required">*</text></label>
										<input class="form-input" type="idcard" v-model="formData.idcard" placeholder="请输入身份证号" />
									</view>
									<view class="form-group">
										<label class="form-label">性别</label>
										<view class="radio-group">
											<label class="radio-item" :class="{ active: formData.gender === 'male' }">
												<input type="radio" v-model="formData.gender" value="male" hidden />
												男
											</label>
											<label class="radio-item" :class="{ active: formData.gender === 'female' }">
												<input type="radio" v-model="formData.gender" value="female" hidden />
												女
											</label>
										</view>
									</view>
								</view>
							</view>
							
							<view class="form-section">
								<view class="section-title">入住信息</view>
								<view class="form-grid">
									<view class="form-group">
										<label class="form-label">房型 <text class="required">*</text></label>
										<picker :range="roomTypeOptions" range-key="name" @change="onRoomTypeChange">
											<view class="form-input picker-value">{{ formData.roomTypeName || '请选择房型' }}</view>
										</picker>
									</view>
									<view class="form-group">
										<label class="form-label">房间号 <text class="required">*</text></label>
										<picker :range="availableRooms" range-key="number" @change="onRoomChange">
											<view class="form-input picker-value">{{ formData.roomNumber || '请选择房间' }}</view>
										</picker>
									</view>
									<view class="form-group">
										<label class="form-label">入住日期 <text class="required">*</text></label>
										<picker mode="date" :start="startDate" @change="onCheckinDateChange">
											<view class="form-input picker-value">{{ formData.checkin || startDate }}</view>
										</picker>
									</view>
									<view class="form-group">
										<label class="form-label">退房日期 <text class="required">*</text></label>
										<picker mode="date" :start="formData.checkin || startDate" @change="onCheckoutDateChange">
											<view class="form-input picker-value">{{ formData.checkout || '请选择' }}</view>
										</picker>
									</view>
									<view class="form-group">
										<label class="form-label">入住天数</label>
										<input class="form-input" type="number" v-model="formData.nights" readonly />
									</view>
									<view class="form-group">
										<label class="form-label">房间单价</label>
										<input class="form-input" :value="'¥' + formData.price" readonly />
									</view>
								</view>
							</view>
							
							<view class="form-section">
								<view class="section-title">费用信息</view>
								<view class="form-grid">
									<view class="form-group">
										<label class="form-label">预计房费</label>
										<input class="form-input" :value="'¥' + estimatedRoomFee" readonly />
									</view>
									<view class="form-group">
										<label class="form-label">押金 <text class="required">*</text></label>
										<input class="form-input" type="number" v-model="formData.deposit" placeholder="请输入押金" />
									</view>
									<view class="form-group full-width">
										<label class="form-label">支付方式</label>
										<view class="payment-options">
											<view 
												v-for="pay in paymentMethods" 
												:key="pay.value"
												class="payment-item"
												:class="{ active: formData.payment === pay.value }"
												@tap="formData.payment = pay.value"
											>
												{{ pay.label }}
											</view>
										</view>
									</view>
								</view>
							</view>
							
							<view class="form-section">
								<view class="section-title">备注</view>
								<textarea class="form-input" rows="2" v-model="formData.remark" placeholder="备注信息..."></textarea>
							</view>
							
							<view class="form-actions">
								<button class="btn btn-default" @tap="resetForm">重置</button>
								<button class="btn btn-primary" @tap="submitCheckin">确认入住</button>
							</view>
						</view>
						
						<!-- 预订入住 -->
						<view v-if="activeTab === 'reserved'" class="reserved-list">
							<view class="search-bar">
								<input type="text" placeholder="搜索预订号/手机号" v-model="searchKeyword" />
							</view>
							<view class="card">
								<view v-for="(item, index) in pendingReservations" :key="index" class="reserved-item">
									<view class="reserved-info">
										<view class="reserved-header">
											<text class="reserved-id">{{ item.id }}</text>
											<text class="reserved-status">待入住</text>
										</view>
										<view class="reserved-details">
											<text>姓名：{{ item.name }}</text>
											<text>电话：{{ item.phone }}</text>
											<text>房型：{{ item.roomType }}</text>
											<text>入住：{{ item.checkin }}</text>
											<text>退房：{{ item.checkout }}</text>
										</view>
									</view>
									<button class="btn btn-primary btn-sm" @tap="doReservedCheckin(item)">办理入住</button>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端布局 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">入住登记</text>
			</view>
			<view class="mobile-content">
				<!-- Tab -->
				<view class="mobile-tabs">
					<view class="mobile-tab" :class="{ active: activeTab === 'new' }" @tap="activeTab = 'new'">新客入住</view>
					<view class="mobile-tab" :class="{ active: activeTab === 'reserved' }" @tap="activeTab = 'reserved'">预订入住</view>
				</view>
				
				<!-- 新客入住表单 -->
				<view v-if="activeTab === 'new'" class="mobile-form">
					<view class="form-group">
						<label class="form-label">客人姓名 *</label>
						<input class="form-input" type="text" v-model="formData.name" placeholder="请输入姓名" />
					</view>
					<view class="form-group">
						<label class="form-label">手机号 *</label>
						<input class="form-input" type="tel" v-model="formData.phone" placeholder="请输入手机号" />
					</view>
					<view class="form-group">
						<label class="form-label">身份证号 *</label>
						<input class="form-input" type="idcard" v-model="formData.idcard" placeholder="请输入身份证号" />
					</view>
					<view class="form-group">
						<label class="form-label">房型 *</label>
						<picker :range="roomTypeOptions" range-key="name" @change="onRoomTypeChange">
							<view class="form-input picker-value">{{ formData.roomTypeName || '请选择房型' }}</view>
						</picker>
					</view>
					<view class="form-group">
						<label class="form-label">房间号 *</label>
						<picker :range="availableRooms" range-key="number" @change="onRoomChange">
							<view class="form-input picker-value">{{ formData.roomNumber || '请选择房间' }}</view>
						</picker>
					</view>
					<view class="form-row">
						<view class="form-group">
							<label class="form-label">入住日期 *</label>
							<picker mode="date" :start="startDate" @change="onCheckinDateChange">
								<view class="form-input picker-value">{{ formData.checkin || startDate }}</view>
							</picker>
						</view>
						<view class="form-group">
							<label class="form-label">退房日期 *</label>
							<picker mode="date" :start="formData.checkin || startDate" @change="onCheckoutDateChange">
								<view class="form-input picker-value">{{ formData.checkout || '请选择' }}</view>
							</picker>
						</view>
					</view>
					<view class="form-group">
						<label class="form-label">押金 *</label>
						<input class="form-input" type="number" v-model="formData.deposit" placeholder="请输入押金" />
					</view>
					<view class="fee-summary">
						<view class="fee-row">
							<text>房费</text>
							<text>¥{{ estimatedRoomFee }}</text>
						</view>
						<view class="fee-row total">
							<text>合计</text>
							<text>¥{{ estimatedRoomFee }}</text>
						</view>
					</view>
					<button class="btn btn-primary full-width" @tap="submitCheckin">确认入住</button>
				</view>
				
				<!-- 预订入住列表 -->
				<view v-if="activeTab === 'reserved'" class="mobile-reserved-list">
					<view v-for="(item, index) in pendingReservations" :key="index" class="reserved-card">
						<view class="reserved-top">
							<text class="reserved-id">{{ item.id }}</text>
							<text class="reserved-type">{{ item.roomType }}</text>
						</view>
						<view class="reserved-info-grid">
							<view class="info-item">
								<text class="info-label">姓名</text>
								<text class="info-value">{{ item.name }}</text>
							</view>
							<view class="info-item">
								<text class="info-label">电话</text>
								<text class="info-value">{{ item.phone }}</text>
							</view>
							<view class="info-item">
								<text class="info-label">入住</text>
								<text class="info-value">{{ item.checkin }}</text>
							</view>
							<view class="info-item">
								<text class="info-label">退房</text>
								<text class="info-value">{{ item.checkout }}</text>
							</view>
						</view>
						<button class="btn btn-primary full-width" @tap="doReservedCheckin(item)">办理入住</button>
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
			currentPage: 'checkin',
			pageName: '入住登记',
			isPC: true,
			isSidebarCollapsed: false,
			activeTab: 'new',
			searchKeyword: '',
			startDate: '',
			toastShow: false,
			toastMessage: '',
			formData: {
				name: '',
				phone: '',
				idcard: '',
				gender: 'male',
				roomType: '',
				roomTypeName: '',
				roomNumber: '',
				roomId: '',
				checkin: '',
				checkout: '',
				nights: 1,
				price: 0,
				deposit: '',
				payment: 'wechat',
				remark: ''
			},
			roomTypeOptions: [
				{ name: '豪华大床房', price: 488 },
				{ name: '标准双床房', price: 358 },
				{ name: '行政套房', price: 888 },
				{ name: '经济单人房', price: 258 }
			],
			paymentMethods: [
				{ label: '微信支付', value: 'wechat' },
				{ label: '支付宝', value: 'alipay' },
				{ label: '现金', value: 'cash' },
				{ label: '银行卡', value: 'card' }
			],
			availableRooms: [
				{ number: '101', id: 'RT004' },
				{ number: '102', id: 'RT004' },
				{ number: '201', id: 'RT002' },
				{ number: '202', id: 'RT002' },
				{ number: '301', id: 'RT002' },
				{ number: '302', id: 'RT002' },
				{ number: '601', id: 'RT001' },
				{ number: '602', id: 'RT001' },
				{ number: '701', id: 'RT001' },
				{ number: '702', id: 'RT003' }
			],
			pendingReservations: [
				{ id: 'RES2026062701', name: '张三', phone: '13912345678', roomType: '豪华大床房', checkin: '2026-06-28', checkout: '2026-06-30' },
				{ id: 'RES2026062702', name: '李四', phone: '13823456789', roomType: '标准双床房', checkin: '2026-06-29', checkout: '2026-07-01' }
			]
		}
	},
	computed: {
		estimatedRoomFee() {
			return (this.formData.nights || 1) * (this.formData.price || 0)
		}
	},
	onLoad() {
		const now = new Date()
		this.startDate = now.toISOString().split('T')[0]
		this.formData.checkin = this.startDate
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
		onRoomTypeChange(e) {
			const selected = this.roomTypeOptions[e.detail.value]
			this.formData.roomType = selected.name
			this.formData.roomTypeName = selected.name
			this.formData.price = selected.price
		},
		onRoomChange(e) {
			const selected = this.availableRooms[e.detail.value]
			this.formData.roomNumber = selected.number
			this.formData.roomId = selected.id
		},
		onCheckinDateChange(e) {
			this.formData.checkin = e.detail.value
			this.calculateNights()
		},
		onCheckoutDateChange(e) {
			this.formData.checkout = e.detail.value
			this.calculateNights()
		},
		calculateNights() {
			if (this.formData.checkin && this.formData.checkout) {
				const checkin = new Date(this.formData.checkin)
				const checkout = new Date(this.formData.checkout)
				const diffTime = checkout - checkin
				const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
				this.formData.nights = diffDays > 0 ? diffDays : 1
			}
		},
		resetForm() {
			this.formData = {
				name: '',
				phone: '',
				idcard: '',
				gender: 'male',
				roomType: '',
				roomTypeName: '',
				roomNumber: '',
				roomId: '',
				checkin: this.startDate,
				checkout: '',
				nights: 1,
				price: 0,
				deposit: '',
				payment: 'wechat',
				remark: ''
			}
		},
		submitCheckin() {
			if (!this.formData.name || !this.formData.phone || !this.formData.idcard) {
				this.showToast('请填写完整信息')
				return
			}
			if (!this.formData.roomNumber) {
				this.showToast('请选择房间')
				return
			}
			if (!this.formData.checkout) {
				this.showToast('请选择退房日期')
				return
			}
			if (!this.formData.deposit) {
				this.showToast('请输入押金')
				return
			}
			this.showToast('入住办理成功！')
			setTimeout(() => {
				this.resetForm()
			}, 2000)
		},
		doReservedCheckin(item) {
			this.formData.name = item.name
			this.formData.phone = item.phone
			this.formData.roomType = item.roomType
			this.formData.roomTypeName = item.roomType
			this.formData.checkin = item.checkin
			this.formData.checkout = item.checkout
			this.activeTab = 'new'
			this.showToast('已加载预订信息')
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

.tabs {
	display: flex;
	border-bottom: 1px solid #d9d9d9;
	margin-bottom: 20px;
}

.tab-item {
	padding: 12px 20px;
	cursor: pointer;
	border-bottom: 2px solid transparent;
	color: #8c8c8c;
	font-weight: 500;
	transition: all 0.3s;
}

.tab-item:hover {
	color: #1677ff;
}

.tab-item.active {
	color: #1677ff;
	border-bottom-color: #1677ff;
}

.form-section {
	background: #fff;
	border-radius: 8px;
	padding: 20px;
	margin-bottom: 20px;
}

.section-title {
	font-size: 16px;
	font-weight: 600;
	margin-bottom: 16px;
	padding-bottom: 12px;
	border-bottom: 1px solid #f0f0f0;
}

.form-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 16px;
}

.form-group {
	margin-bottom: 16px;
}

.form-group.full-width {
	grid-column: span 3;
}

.required {
	color: #ff4d4f;
}

.radio-group {
	display: flex;
	gap: 16px;
}

.radio-item {
	padding: 8px 20px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
}

.radio-item.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.payment-options {
	display: flex;
	gap: 12px;
}

.payment-item {
	padding: 8px 20px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
	font-size: 14px;
}

.payment-item.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.form-actions {
	display: flex;
	justify-content: flex-end;
	gap: 12px;
	margin-top: 20px;
}

.reserved-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 16px 0;
	border-bottom: 1px solid #f0f0f0;
}

.reserved-item:last-child {
	border-bottom: none;
}

.reserved-header {
	display: flex;
	gap: 12px;
	margin-bottom: 8px;
}

.reserved-id {
	font-weight: 600;
}

.reserved-status {
	color: #faad14;
}

.reserved-details {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
	font-size: 14px;
	color: #595959;
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

.mobile-tabs {
	display: flex;
	background: #fff;
	border-radius: 12rpx;
	margin-bottom: 24rpx;
}

.mobile-tab {
	flex: 1;
	padding: 24rpx;
	text-align: center;
	font-size: 28rpx;
	border-radius: 12rpx;
}

.mobile-tab.active {
	background: #1677ff;
	color: #fff;
}

.mobile-form {
	background: #fff;
	border-radius: 16rpx;
	padding: 24rpx;
}

.form-row {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 24rpx;
}

.fee-summary {
	background: #fafafa;
	border-radius: 12rpx;
	padding: 20rpx;
	margin: 24rpx 0;
}

.fee-row {
	display: flex;
	justify-content: space-between;
	padding: 12rpx 0;
}

.fee-row.total {
	border-top: 1px solid #d9d9d9;
	font-weight: 600;
	font-size: 32rpx;
	color: #1677ff;
}

.full-width {
	width: 100%;
}

.reserved-card {
	background: #fff;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 20rpx;
}

.reserved-top {
	display: flex;
	justify-content: space-between;
	margin-bottom: 16rpx;
}

.reserved-id {
	font-weight: 600;
}

.reserved-type {
	color: #1677ff;
}

.reserved-info-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12rpx;
	padding: 16rpx 0;
	border-top: 1px solid #f0f0f0;
	border-bottom: 1px solid #f0f0f0;
}

.info-item {
	display: flex;
	flex-direction: column;
}

.info-label {
	font-size: 24rpx;
	color: #8c8c8c;
}

.info-value {
	font-size: 28rpx;
	margin-top: 4rpx;
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
