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
						<view class="tab-item" :class="{ active: activeTab === 'checkout' }" @tap="activeTab = 'checkout'">退房结算</view>
						<view class="tab-item" :class="{ active: activeTab === 'history' }" @tap="activeTab = 'history'">退房记录</view>
					</view>
					
					<view class="tab-content" v-if="activeTab === 'checkout'">
						<view class="search-bar">
							<input type="text" placeholder="搜索房号/客人姓名" v-model="searchKeyword" />
							<button v-if="can('checkout:handle')" class="btn btn-primary btn-sm" @tap="showCheckoutModal">+ 办理退房</button>
						</view>
						
						<!-- 在住客人列表 -->
						<view class="card">
							<view class="card-header">
								<view class="card-title">在住客人</view>
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
											<th>入住天数</th>
											<th>累计消费</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="(item, index) in currentGuests" :key="index">
											<td>{{ item.room }}</td>
											<td>{{ item.name }}</td>
											<td>{{ item.roomType }}</td>
											<td>{{ item.checkin }}</td>
											<td>{{ item.checkout }}</td>
											<td>{{ item.nights }}晚</td>
											<td>¥{{ item.total }}</td>
											<td>
												<button v-if="can('checkout:handle')" class="btn btn-primary btn-sm" @tap="doCheckout(item)">退房</button>
											</td>
										</tr>
									</tbody>
								</table>
							</view>
						</view>
						
						<!-- 今日退房 -->
						<view class="card">
							<view class="card-header">
								<view class="card-title">今日待退房</view>
								<text class="checkout-count">{{ todayCheckouts.length }}间</text>
							</view>
							<view class="checkout-grid">
								<view v-for="(item, index) in todayCheckouts" :key="index" class="checkout-item">
									<view class="checkout-room">{{ item.room }}</view>
									<view class="checkout-info">
										<text>{{ item.name }}</text>
										<text class="checkout-time">{{ item.checkoutTime }}</text>
									</view>
									<button v-if="can('checkout:handle')" class="btn btn-sm" :class="item.checkedOut ? 'btn-default' : 'btn-primary'" 
										:disabled="item.checkedOut" @tap="doCheckout(item)">
										{{ item.checkedOut ? '已退房' : '退房' }}
									</button>
								</view>
							</view>
						</view>
					</view>
					
					<view class="tab-content" v-if="activeTab === 'history'">
						<view class="search-bar">
							<input type="text" placeholder="搜索房号/客人姓名" v-model="searchKeyword2" />
						</view>
						<view class="card">
							<view class="table-container">
								<table class="table">
									<thead>
										<tr>
											<th>退房时间</th>
											<th>房号</th>
											<th>客人姓名</th>
											<th>房型</th>
											<th>入住天数</th>
											<th>消费金额</th>
											<th>结算方式</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="(item, index) in checkoutHistory" :key="index">
											<td>{{ item.time }}</td>
											<td>{{ item.room }}</td>
											<td>{{ item.name }}</td>
											<td>{{ item.roomType }}</td>
											<td>{{ item.nights }}晚</td>
											<td>¥{{ item.amount }}</td>
											<td>{{ item.payment }}</td>
										</tr>
									</tbody>
								</table>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移动端布局 -->
		<view class="mobile-layout" v-else>
			<view class="mobile-header">
				<text class="mobile-title">退房结算</text>
			</view>
			<view class="mobile-content">
				<!-- Tab -->
				<view class="mobile-tabs">
					<view class="mobile-tab" :class="{ active: activeTab === 'checkout' }" @tap="activeTab = 'checkout'">退房结算</view>
					<view class="mobile-tab" :class="{ active: activeTab === 'history' }" @tap="activeTab = 'history'">退房记录</view>
				</view>
				
				<view v-if="activeTab === 'checkout'">
					<view class="search-box-mobile">
						<input type="text" placeholder="搜索房号/姓名" v-model="searchKeyword" />
					</view>
					
					<view class="guest-list-mobile">
						<view v-for="(item, index) in currentGuests" :key="index" class="guest-card">
							<view class="guest-top">
								<view class="room-badge">{{ item.room }}</view>
								<text class="guest-name">{{ item.name }}</text>
							</view>
							<view class="guest-details">
								<view class="detail-row">
									<text class="detail-label">房型</text>
									<text class="detail-value">{{ item.roomType }}</text>
								</view>
								<view class="detail-row">
									<text class="detail-label">入住</text>
									<text class="detail-value">{{ item.checkin }}</text>
								</view>
								<view class="detail-row">
									<text class="detail-label">退房</text>
									<text class="detail-value">{{ item.checkout }}</text>
								</view>
								<view class="detail-row">
									<text class="detail-label">天数</text>
									<text class="detail-value">{{ item.nights }}晚</text>
								</view>
							</view>
							<view class="guest-footer">
								<view class="total-amount">
									<text class="amount-label">消费合计</text>
									<text class="amount-value">¥{{ item.total }}</text>
								</view>
								<button v-if="can('checkout:handle')" class="btn btn-primary" @tap="doCheckout(item)">办理退房</button>
							</view>
						</view>
					</view>
				</view>
				
				<view v-if="activeTab === 'history'">
					<view class="history-list">
						<view v-for="(item, index) in checkoutHistory" :key="index" class="history-item">
							<view class="history-main">
								<text class="history-room">{{ item.room }}</text>
								<text class="history-name">{{ item.name }}</text>
							</view>
							<view class="history-sub">
								<text>{{ item.time }}</text>
								<text>{{ item.payment }}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 退房结算弹窗 -->
		<view class="modal-mask" v-if="showModal">
			<view class="modal-content modal-large" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">退房结算 - {{ checkoutData.room }}</text>
					<text class="modal-close" @tap="closeModal">✕</text>
				</view>
				<view class="modal-body">
					<view class="checkout-summary">
						<view class="summary-row">
							<text class="summary-label">房号</text>
							<text class="summary-value">{{ checkoutData.room }}</text>
						</view>
						<view class="summary-row">
							<text class="summary-label">客人姓名</text>
							<text class="summary-value">{{ checkoutData.name }}</text>
						</view>
						<view class="summary-row">
							<text class="summary-label">房型</text>
							<text class="summary-value">{{ checkoutData.roomType }}</text>
						</view>
						<view class="summary-row">
							<text class="summary-label">入住日期</text>
							<text class="summary-value">{{ checkoutData.checkin }}</text>
						</view>
						<view class="summary-row">
							<text class="summary-label">入住天数</text>
							<text class="summary-value">{{ checkoutData.nights }}晚</text>
						</view>
					</view>
					
					<view class="fee-detail">
						<view class="fee-title">费用明细</view>
						<view class="fee-row">
							<text>房费</text>
							<text>¥{{ checkoutData.roomFee }}</text>
						</view>
						<view class="fee-row">
							<text>餐饮消费</text>
							<text>¥{{ checkoutData.dining }}</text>
						</view>
						<view class="fee-row">
							<text>迷你吧消费</text>
							<text>¥{{ checkoutData.minibar }}</text>
						</view>
						<view class="fee-row">
							<text>其他消费</text>
							<text>¥0</text>
						</view>
						<view class="fee-row total">
							<text>账单总额</text>
							<text>¥{{ checkoutData.total }}</text>
						</view>
						<view class="fee-row">
							<text>已收押金</text>
							<text>¥{{ checkoutData.deposit }}</text>
						</view>
						<view class="fee-row highlight">
							<text>应补金额</text>
							<text class="amount-danger">¥{{ checkoutData.total - checkoutData.deposit }}</text>
						</view>
					</view>
					
					<view class="form-group">
						<label class="form-label">结算方式</label>
						<view class="payment-options">
							<view v-for="pay in paymentMethods" :key="pay.value" 
								class="payment-item" :class="{ active: selectedPayment === pay.value }"
								@tap="selectedPayment = pay.value">
								{{ pay.label }}
							</view>
						</view>
					</view>
					
					<view class="form-group">
						<label class="form-label">备注</label>
						<textarea class="form-input" rows="2" v-model="checkoutRemark" placeholder="退房备注..."></textarea>
					</view>
					
					<view class="checkout-options">
						<label class="option-item">
							<input type="checkbox" v-model="printBill" />
							<text>打印账单</text>
						</label>
						<label class="option-item">
							<input type="checkbox" v-model="printInvoice" />
							<text>开具发票</text>
						</label>
					</view>
				</view>
				<view class="modal-footer">
					<button class="btn btn-default" @tap="closeModal">取消</button>
					<button v-if="can('checkout:handle')" class="btn btn-primary" @tap="confirmCheckout">确认退房</button>
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
			currentPage: 'checkout',
			pageName: '退房结算',
			isPC: true,
			isSidebarCollapsed: false,
			quickRoomNumber: '',
			activeTab: 'checkout',
			searchKeyword: '',
			searchKeyword2: '',
			showModal: false,
			toastShow: false,
			toastMessage: '',
			selectedPayment: 'wechat',
			checkoutRemark: '',
			printBill: true,
			printInvoice: false,
			paymentMethods: [
				{ label: '微信支付', value: 'wechat' },
				{ label: '支付宝', value: 'alipay' },
				{ label: '现金', value: 'cash' },
				{ label: '银行卡', value: 'card' }
			],
			checkoutData: {},
			currentGuests: [
				{ room: '701', name: '王建国', roomType: '豪华大床房', checkin: '2026-06-27', checkout: '2026-06-29', nights: 3, roomFee: 1464, dining: 128, minibar: 0, total: 1592, deposit: 1000 },
				{ room: '502', name: '张先生', roomType: '标准双床房', checkin: '2026-06-27', checkout: '2026-06-30', nights: 3, roomFee: 1074, dining: 85, minibar: 50, total: 1209, deposit: 1000 },
				{ room: '308', name: '陈小姐', roomType: '经济单人房', checkin: '2026-06-27', checkout: '2026-06-28', nights: 1, roomFee: 258, dining: 45, minibar: 20, total: 323, deposit: 300 },
				{ room: '603', name: '李娜', roomType: '标准双床房', checkin: '2026-06-26', checkout: '2026-06-29', nights: 3, roomFee: 1074, dining: 200, minibar: 80, total: 1354, deposit: 1000 },
				{ room: '801', name: '刘伟', roomType: '行政套房', checkin: '2026-06-25', checkout: '2026-06-28', nights: 3, roomFee: 2664, dining: 350, minibar: 120, total: 3134, deposit: 2000 }
			],
			todayCheckouts: [
				{ room: '308', name: '陈小姐', checkoutTime: '14:00', checkedOut: false },
				{ room: '601', name: '赵六', checkoutTime: '12:00', checkedOut: true },
				{ room: '702', name: '钱七', checkoutTime: '16:00', checkedOut: false },
				{ room: '402', name: '孙八', checkoutTime: '11:00', checkedOut: false }
			],
			checkoutHistory: [
				{ time: '2026-06-27 10:30', room: '305', name: '周经理', roomType: '豪华大床房', nights: 2, amount: 976, payment: '微信支付' },
				{ time: '2026-06-27 09:15', room: '508', name: '吴小姐', roomType: '标准双床房', nights: 1, amount: 358, payment: '支付宝' },
				{ time: '2026-06-26 18:20', room: '203', name: '郑先生', roomType: '经济单人房', nights: 3, amount: 774, payment: '现金' },
				{ time: '2026-06-26 14:45', room: '601', name: '王总', roomType: '行政套房', nights: 2, amount: 1776, payment: '银行卡' }
			]
		}
	},
	onLoad(options = {}) {
		this.quickRoomNumber = options.roomNo || options.roomNumber || ''
		uni.getSystemInfo({
			success: (res) => {
				this.isPC = res.windowWidth >= 768
			}
		})
		this.loadCheckoutData()
	},
	mounted() {
		if (typeof document !== 'undefined') {
			document.addEventListener('keydown', this.handleEscClose)
		}
	},
	beforeDestroy() {
		if (typeof document !== 'undefined') {
			document.removeEventListener('keydown', this.handleEscClose)
		}
	},
	methods: {
		async loadCheckoutData() {
			try {
				const canViewRooms = this.can('room:view')
				const canViewBills = this.can('billing:view')
				const results = await Promise.all([
					canViewRooms ? this.$api.get('/api/rooms', { status: 'occupied' }) : Promise.resolve([]),
					canViewBills ? this.$api.get('/api/bills') : Promise.resolve([])
				])
				const rooms = results[0] || []
				const bills = results[1] || []
				const unpaidBills = (bills || []).filter(bill => bill.status !== 'paid')
				if (canViewRooms) {
					this.currentGuests = rooms.map(room => {
						const bill = unpaidBills.find(item => item.roomNumber === room.number) || {}
						return {
							room: room.number,
							name: room.guest ? room.guest.name : bill.guestName,
							roomType: room.typeName,
							checkin: room.guest && room.guest.checkin ? room.guest.checkin : '',
							checkout: room.guest && room.guest.checkout ? room.guest.checkout : '',
							nights: 1,
							roomFee: Number(bill.roomAmount || 0),
							dining: 0,
							minibar: Number(bill.serviceAmount || 0),
							total: Number(bill.totalAmount || 0),
							deposit: Number(bill.paidAmount || 0),
							billId: bill.id
						}
					})
				} else {
					this.currentGuests = unpaidBills.map(bill => ({
						room: bill.roomNumber,
						name: bill.guestName,
						roomType: '',
						checkin: '',
						checkout: '',
						nights: 1,
						roomFee: Number(bill.roomAmount || 0),
						dining: 0,
						minibar: Number(bill.serviceAmount || 0),
						total: Number(bill.totalAmount || 0),
						deposit: Number(bill.paidAmount || 0),
						billId: bill.id
					}))
				}
				this.todayCheckouts = this.currentGuests.map(item => ({
					room: item.room,
					name: item.name,
					checkoutTime: '待退房',
					checkedOut: false,
					...item
				}))
				this.checkoutHistory = (bills || [])
					.filter(bill => bill.status === 'paid')
					.map(bill => ({
						time: '',
						room: bill.roomNumber,
						name: bill.guestName,
						roomType: '',
						nights: 1,
						amount: bill.totalAmount,
						payment: bill.statusName || '已结清'
					}))
				this.openQuickCheckout()
			} catch (error) {
				this.showToast(error.message || '退房数据加载失败')
			}
		},
		openQuickCheckout() {
			if (!this.quickRoomNumber) {
				return
			}
			const selected = this.currentGuests.find(item => String(item.room) === String(this.quickRoomNumber))
			if (!selected) {
				this.showToast('未找到对应在住房间，无法办理退房结算')
				this.quickRoomNumber = ''
				return
			}
			if (this.showModal && String(this.checkoutData.room) === String(selected.room)) {
				this.quickRoomNumber = ''
				return
			}
			this.activeTab = 'checkout'
			this.checkoutData = { ...selected }
			this.showModal = true
			this.quickRoomNumber = ''
		},
		toggleSidebar() {
			this.isSidebarCollapsed = !this.isSidebarCollapsed
		},
		handleNavigate(page) {
			this.currentPage = page
			this.pageName = this.$rbac.getPageName(page)
			this.navigateToPage(page)
		},
		showCheckoutModal() {
			if (!this.can('checkout:handle')) {
				this.showNoPermission()
				return
			}
			if (this.currentGuests.length > 0) {
				this.checkoutData = { ...this.currentGuests[0] }
			}
			this.showModal = true
		},
		doCheckout(item) {
			if (!this.can('checkout:handle')) {
				this.showNoPermission()
				return
			}
			this.checkoutData = { ...item }
			this.showModal = true
		},
		closeModal() {
			this.showModal = false
		},
		handleEscClose(event) {
			if (event.key === 'Escape' && this.showModal) {
				this.closeModal()
			}
		},
		async confirmCheckout() {
			if (!this.can('checkout:handle')) {
				this.showNoPermission()
				return
			}
			if (!this.checkoutData.room) {
				this.showToast('请选择退房房间')
				return
			}
			try {
				await this.$api.post('/api/checkout', {
					roomNumber: this.checkoutData.room,
					paymentMethod: this.selectedPayment,
					paidAmount: this.checkoutData.total || null
				})
				this.showToast('退房办理成功')
				this.closeModal()
				await this.loadCheckoutData()
			} catch (error) {
				this.showToast(error.message || '退房办理失败')
			}
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
}

.tab-item.active {
	color: #1677ff;
	border-bottom-color: #1677ff;
}

.search-bar {
	display: flex;
	gap: 12px;
	margin-bottom: 20px;
}

.search-bar input {
	flex: 1;
	padding: 8px 12px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
}

.checkout-count {
	color: #8c8c8c;
}

.checkout-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 16px;
}

.checkout-item {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: #fafafa;
	border-radius: 8px;
}

.checkout-room {
	font-size: 24px;
	font-weight: 700;
	color: #1677ff;
}

.checkout-info {
	flex: 1;
	font-size: 14px;
}

.checkout-time {
	display: block;
	font-size: 12px;
	color: #8c8c8c;
}

/* 移动端 */
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
}

.mobile-tab.active {
	background: #1677ff;
	color: #fff;
	border-radius: 12rpx;
}

.search-box-mobile {
	margin-bottom: 24rpx;
}

.search-box-mobile input {
	width: 100%;
	padding: 20rpx 24rpx;
	border: 1px solid #d9d9d9;
	border-radius: 12rpx;
}

.guest-card {
	background: #fff;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 20rpx;
}

.guest-top {
	display: flex;
	align-items: center;
	gap: 16rpx;
	margin-bottom: 16rpx;
}

.room-badge {
	padding: 8rpx 16rpx;
	background: #1677ff;
	color: #fff;
	border-radius: 8rpx;
	font-weight: 600;
}

.guest-name {
	font-size: 32rpx;
	font-weight: 600;
}

.guest-details {
	border-top: 1px solid #f0f0f0;
	padding: 16rpx 0;
}

.detail-row {
	display: flex;
	justify-content: space-between;
	padding: 8rpx 0;
}

.detail-label {
	color: #8c8c8c;
}

.guest-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-top: 16rpx;
	border-top: 1px solid #f0f0f0;
	margin-top: 16rpx;
}

.amount-label {
	color: #8c8c8c;
}

.amount-value {
	font-size: 32rpx;
	font-weight: 700;
	color: #ff4d4f;
}

.history-list {
	display: flex;
	flex-direction: column;
	gap: 16rpx;
}

.history-item {
	background: #fff;
	border-radius: 12rpx;
	padding: 20rpx;
}

.history-main {
	display: flex;
	gap: 16rpx;
	margin-bottom: 8rpx;
}

.history-room {
	font-weight: 600;
	color: #1677ff;
}

.history-sub {
	display: flex;
	justify-content: space-between;
	font-size: 24rpx;
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

.modal-large {
	width: 640rpx;
}

.modal-header {
	padding: 32rpx;
	border-bottom: 1px solid #f0f0f0;
	display: flex;
	justify-content: space-between;
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

.checkout-summary {
	background: #fafafa;
	padding: 20rpx;
	border-radius: 8px;
	margin-bottom: 20px;
}

.summary-row {
	display: flex;
	justify-content: space-between;
	padding: 8px 0;
}

.summary-label {
	color: #8c8c8c;
}

.fee-detail {
	border: 1px solid #f0f0f0;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 20px;
}

.fee-title {
	font-weight: 600;
	margin-bottom: 12px;
}

.fee-row {
	display: flex;
	justify-content: space-between;
	padding: 8px 0;
	font-size: 14px;
}

.fee-row.total {
	border-top: 1px solid #f0f0f0;
	padding-top: 12px;
	margin-top: 8px;
	font-weight: 600;
	font-size: 16px;
}

.fee-row.highlight {
	background: rgba(255, 77, 79, 0.05);
	padding: 12px;
	border-radius: 6px;
	margin-top: 8px;
}

.amount-danger {
	font-size: 18px;
	color: #ff4d4f;
}

.payment-options {
	display: flex;
	gap: 12px;
	flex-wrap: wrap;
}

.payment-item {
	padding: 12px 20px;
	border: 1px solid #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
}

.payment-item.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.checkout-options {
	display: flex;
	gap: 24px;
	margin-top: 16px;
}

.option-item {
	display: flex;
	align-items: center;
	gap: 8px;
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
