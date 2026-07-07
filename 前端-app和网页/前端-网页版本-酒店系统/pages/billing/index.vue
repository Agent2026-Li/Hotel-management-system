<template>
	<view class="page">
		<view class="pc-layout" v-if="isPC">
			<Sidebar :currentPage="currentPage" :isCollapsed="isSidebarCollapsed" @navigate="handleNavigate" />
			<view class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
				<Topbar :pageName="pageName" @toggleSidebar="toggleSidebar" />
				<view class="page-container">
					<view class="search-bar">
						<input type="text" placeholder="搜索房号/客人姓名" v-model="searchKeyword" />
						<button v-if="can('billing:edit')" class="btn btn-primary btn-sm" @tap="showChargeModal">+ 新增消费</button>
					</view>
					
					<view class="grid-2">
						<view class="card">
							<view class="card-header">
								<view class="card-title">消费汇总</view>
								<text class="card-total">¥{{ totalCharges }}</text>
							</view>
							<view class="summary-list">
								<view class="summary-item">
									<text>房费收入</text>
									<text class="amount">¥{{ roomRevenue }}</text>
								</view>
								<view class="summary-item">
									<text>餐饮收入</text>
									<text class="amount">¥{{ diningRevenue }}</text>
								</view>
								<view class="summary-item">
									<text>迷你吧收入</text>
									<text class="amount">¥{{ minibarRevenue }}</text>
								</view>
								<view class="summary-item">
									<text>其他收入</text>
									<text class="amount">¥{{ otherRevenue }}</text>
								</view>
							</view>
						</view>
						
						<view class="card">
							<view class="card-header">
								<view class="card-title">消费统计</view>
							</view>
							<view class="stats-row">
								<view class="stat-box">
									<view class="stat-num">{{ billCount }}</view>
									<view class="stat-label">账单数</view>
								</view>
								<view class="stat-box">
									<view class="stat-num">{{ unpaidCount }}</view>
									<view class="stat-label">未结算</view>
								</view>
								<view class="stat-box">
									<view class="stat-num">{{ paidCount }}</view>
									<view class="stat-label">已结算</view>
								</view>
							</view>
						</view>
					</view>
					
					<view class="card">
						<view class="card-header">
							<view class="card-title">消费记录</view>
						</view>
						<view class="table-container">
							<table class="table">
								<thead>
									<tr>
										<th>账单号</th>
										<th>房号</th>
										<th>客人姓名</th>
										<th>消费类型</th>
										<th>消费项目</th>
										<th>金额</th>
										<th>时间</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(item, index) in chargeList" :key="index">
										<td>{{ item.id }}</td>
										<td>{{ item.room }}</td>
										<td>{{ item.name }}</td>
										<td>{{ item.type }}</td>
										<td>{{ item.item }}</td>
										<td>¥{{ item.amount }}</td>
										<td>{{ item.time }}</td>
										<td><span class="status-badge" :class="item.paid ? 'status-vacant' : 'status-dirty'">{{ item.paid ? '已结算' : '未结算' }}</span></td>
										<td>
											<text class="action-link" @tap="settleBill(item)" v-if="!item.paid && can('payment:confirm')">结算</text>
											<text v-if="can('billing:view')" class="action-link" @tap="printBill(item)">打印</text>
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
				<text class="mobile-title">账单管理</text>
			</view>
			<view class="mobile-content">
				<view class="search-box">
					<input type="text" placeholder="搜索房号/姓名" v-model="searchKeyword" />
				</view>
				
				<view class="total-card">
					<text class="total-label">消费汇总</text>
					<text class="total-amount">¥{{ totalCharges }}</text>
				</view>
				
				<view class="bill-list">
					<view v-for="(item, index) in chargeList" :key="index" class="bill-card">
						<view class="bill-header">
							<view class="bill-room">{{ item.room }}</view>
							<view class="bill-name">{{ item.name }}</view>
							<span class="status-badge" :class="item.paid ? 'status-vacant' : 'status-dirty'">{{ item.paid ? '已结算' : '未结算' }}</span>
						</view>
						<view class="bill-body">
							<view class="bill-item">
								<text>{{ item.type }} - {{ item.item }}</text>
								<text class="bill-amount">¥{{ item.amount }}</text>
							</view>
						</view>
						<view class="bill-footer">
							<text class="bill-time">{{ item.time }}</text>
							<button
								v-if="item.paid ? can('billing:view') : can('payment:confirm')"
								class="btn btn-sm"
								:class="item.paid ? 'btn-default' : 'btn-primary'"
								@tap="settleBill(item)"
							>
								{{ item.paid ? '查看' : '结算' }}
							</button>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 新增消费弹窗 -->
		<view class="modal-mask" v-if="showModal" @tap="closeModal">
			<view class="modal-content" @tap.stop>
				<view class="modal-header">
					<text class="modal-title">新增消费</text>
					<text class="modal-close" @tap="closeModal">✕</text>
				</view>
				<view class="modal-body">
					<view class="form-group">
						<label class="form-label">房号 <text class="required">*</text></label>
						<picker :range="roomOptions" range-key="label" @change="onRoomChange">
							<view class="form-input picker-value">{{ chargeData.roomLabel || '请选择房间' }}</view>
						</picker>
					</view>
					<view class="form-group">
						<label class="form-label">消费类型 <text class="required">*</text></label>
						<picker :range="chargeTypes" @change="onTypeChange">
							<view class="form-input picker-value">{{ chargeData.type || '请选择类型' }}</view>
						</picker>
					</view>
					<view class="form-group">
						<label class="form-label">消费项目 <text class="required">*</text></label>
						<input class="form-input" type="text" v-model="chargeData.item" placeholder="请输入消费项目" />
					</view>
					<view class="form-group">
						<label class="form-label">金额 <text class="required">*</text></label>
						<input class="form-input" type="number" v-model="chargeData.amount" placeholder="请输入金额" />
					</view>
					<view class="form-group">
						<label class="form-label">备注</label>
						<textarea class="form-input" rows="2" v-model="chargeData.remark" placeholder="备注信息..."></textarea>
					</view>
				</view>
				<view class="modal-footer">
					<button class="btn btn-default" @tap="closeModal">取消</button>
					<button v-if="can('billing:edit')" class="btn btn-primary" @tap="saveCharge">确认</button>
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
			currentPage: 'billing',
			pageName: '账单管理',
			isPC: true,
			isSidebarCollapsed: false,
			quickRoomNumber: '',
			quickAction: '',
			searchKeyword: '',
			showModal: false,
			toastShow: false,
			toastMessage: '',
			totalCharges: '52,480',
			roomRevenue: '38,500',
			diningRevenue: '8,280',
			minibarRevenue: '4,200',
			otherRevenue: '1,500',
			billCount: 156,
			unpaidCount: 12,
			paidCount: 144,
			chargeData: {},
			roomOptions: [
				{ room: '701', name: '王建国', label: '701 - 王建国' },
				{ room: '502', name: '张先生', label: '502 - 张先生' },
				{ room: '308', name: '陈小姐', label: '308 - 陈小姐' },
				{ room: '603', name: '李娜', label: '603 - 李娜' }
			],
			chargeTypes: ['餐饮', '迷你吧', '洗衣', '赔偿', '其他'],
			chargeList: [
				{ id: 'BILL2026062701', room: '701', name: '王建国', type: '餐饮', item: '午餐', amount: 128, time: '2026-06-27 12:30', paid: false },
				{ id: 'BILL2026062702', room: '502', name: '张先生', type: '迷你吧', item: '啤酒2罐', amount: 50, time: '2026-06-27 22:15', paid: false },
				{ id: 'BILL2026062601', room: '308', name: '陈小姐', type: '洗衣', item: '干洗西装', amount: 85, time: '2026-06-26 09:00', paid: true },
				{ id: 'BILL2026062501', room: '603', name: '李娜', type: '餐饮', item: '晚餐', amount: 200, time: '2026-06-25 19:30', paid: true }
			]
		}
	},
	onLoad(options = {}) {
		this.quickRoomNumber = options.roomNumber || ''
		this.quickAction = options.action || ''
		uni.getSystemInfo({ success: (res) => { this.isPC = res.windowWidth >= 768 } })
		this.loadBillingData()
	},
	methods: {
		async loadBillingData() {
			try {
				const bills = await this.$api.get('/api/bills')
				this.roomOptions = (bills || []).filter(bill => bill.status !== 'paid').map(bill => ({
					billId: bill.id,
					room: bill.roomNumber,
					name: bill.guestName,
					label: `${bill.roomNumber} - ${bill.guestName}`
				}))
				this.chargeList = (bills || []).map(bill => ({
					id: bill.id,
					room: bill.roomNumber,
					name: bill.guestName,
					type: '账单',
					item: (bill.items && bill.items[0] && bill.items[0].itemName) || '房费',
					amount: bill.totalAmount,
					time: '',
					paid: bill.status === 'paid',
					billId: bill.id
				}))
				const total = (bills || []).reduce((sum, bill) => sum + Number(bill.totalAmount || 0), 0)
				this.totalCharges = total.toFixed(2)
				this.roomRevenue = (bills || []).reduce((sum, bill) => sum + Number(bill.roomAmount || 0), 0).toFixed(2)
				this.minibarRevenue = (bills || []).reduce((sum, bill) => sum + Number(bill.serviceAmount || 0), 0).toFixed(2)
				this.diningRevenue = '0.00'
				this.otherRevenue = '0.00'
				this.billCount = (bills || []).length
				this.unpaidCount = (bills || []).filter(bill => bill.status !== 'paid').length
				this.paidCount = (bills || []).filter(bill => bill.status === 'paid').length
				this.openQuickCharge()
			} catch (error) {
				this.showToast(error.message || '账单加载失败')
			}
		},
		openQuickCharge() {
			if (this.quickAction !== 'charge' || !this.quickRoomNumber) {
				return
			}
			const selected = this.roomOptions.find(item => String(item.room) === String(this.quickRoomNumber))
			if (!selected) {
				this.showToast(`房间 ${this.quickRoomNumber} 暂无未结账账单`)
				this.quickAction = ''
				return
			}
			this.chargeData = {
				billId: selected.billId,
				roomLabel: selected.label,
				room: selected.room
			}
			this.showModal = true
			this.quickAction = ''
		},
		toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed },
		handleNavigate(page) {
			this.pageName = this.$rbac.getPageName(page)
			this.navigateToPage(page)
		},
		showChargeModal() {
			if (!this.can('billing:edit')) {
				this.showNoPermission()
				return
			}
			this.chargeData = {}
			this.showModal = true
		},
		closeModal() { this.showModal = false },
		onRoomChange(e) {
			const selected = this.roomOptions[e.detail.value]
			this.chargeData.roomLabel = selected.label
			this.chargeData.billId = selected.billId
		},
		onTypeChange(e) { this.chargeData.type = this.chargeTypes[e.detail.value] },
		async saveCharge() {
			if (!this.can('billing:edit')) {
				this.showNoPermission()
				return
			}
			if (!this.chargeData.billId || !this.chargeData.item || !this.chargeData.amount) {
				this.showToast('请填写完整消费信息')
				return
			}
			try {
				await this.$api.post(`/api/bills/${this.chargeData.billId}/charges`, {
					itemName: this.chargeData.item,
					category: this.chargeData.type || 'SERVICE',
					amount: Number(this.chargeData.amount),
					quantity: 1,
					remark: this.chargeData.remark
				})
				this.showToast('消费记录已添加')
				this.closeModal()
				await this.loadBillingData()
			} catch (error) {
				this.showToast(error.message || '消费入账失败')
			}
		},
		settleBill(item) {
			if (!item.paid && !this.can('payment:confirm')) {
				this.showNoPermission()
				return
			}
			if (item.paid) {
				this.showToast('账单已结清')
				return
			}
			if (!item.room) {
				this.showToast('未找到对应在住房间，无法办理退房结算')
				return
			}
			this.navigateToPage('checkout', { roomNo: item.room })
		},
		printBill(item) {
			if (!this.can('billing:view')) {
				this.showNoPermission()
				return
			}
			this.showToast('正在打印...')
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
.search-bar { display: flex; gap: 12px; margin-bottom: 20px; }
.search-bar input { flex: 1; padding: 8px 12px; border: 1px solid #d9d9d9; border-radius: 6px; }
.grid-2 { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 20px; }
.card-total { font-size: 24px; font-weight: 700; color: #1677ff; }
.summary-list { padding-top: 16px; }
.summary-item { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px dashed #f0f0f0; }
.summary-item:last-child { border-bottom: none; }
.amount { font-weight: 600; }
.stats-row { display: flex; justify-content: space-around; padding-top: 16px; }
.stat-box { text-align: center; }
.stat-num { font-size: 28px; font-weight: 700; color: #1677ff; }
.stat-label { font-size: 14px; color: #8c8c8c; margin-top: 8px; }
.action-link { color: #1677ff; cursor: pointer; margin-right: 8px; }
.mobile-layout { min-height: 100vh; background: #f5f5f5; }
.mobile-header { height: 88rpx; background: linear-gradient(135deg, #001529 0%, #002140 100%); color: #fff; display: flex; align-items: center; justify-content: center; }
.mobile-title { font-size: 32rpx; font-weight: 600; }
.mobile-content { padding: 24rpx; }
.search-box { margin-bottom: 24rpx; }
.search-box input { width: 100%; padding: 20rpx 24rpx; border: 1px solid #d9d9d9; border-radius: 12rpx; }
.total-card { background: #fff; border-radius: 16rpx; padding: 32rpx; text-align: center; margin-bottom: 24rpx; }
.total-label { font-size: 28rpx; color: #8c8c8c; }
.total-amount { display: block; font-size: 56rpx; font-weight: 700; color: #1677ff; margin-top: 12rpx; }
.bill-list { display: flex; flex-direction: column; gap: 20rpx; }
.bill-card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.bill-header { display: flex; align-items: center; gap: 16rpx; margin-bottom: 16rpx; }
.bill-room { font-weight: 700; color: #1677ff; }
.bill-name { flex: 1; font-weight: 500; }
.bill-body { padding: 16rpx 0; border-top: 1px solid #f0f0f0; }
.bill-item { display: flex; justify-content: space-between; }
.bill-amount { font-weight: 600; }
.bill-footer { display: flex; justify-content: space-between; align-items: center; padding-top: 16rpx; border-top: 1px solid #f0f0f0; margin-top: 16rpx; }
.bill-time { font-size: 24rpx; color: #8c8c8c; }
.modal-mask { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: #fff; border-radius: 16rpx; width: 560rpx; max-width: 90vw; }
.modal-header { padding: 32rpx; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; }
.modal-title { font-size: 36rpx; font-weight: 600; }
.modal-close { font-size: 32rpx; color: #8c8c8c; }
.modal-body { padding: 32rpx; }
.modal-footer { padding: 32rpx; border-top: 1px solid #f0f0f0; display: flex; justify-content: flex-end; gap: 24rpx; }
.form-group { margin-bottom: 24rpx; }
.form-label { display: block; margin-bottom: 12rpx; font-size: 28rpx; font-weight: 500; }
.required { color: #ff4d4f; }
.form-input { width: 100%; padding: 16rpx 24rpx; border: 1px solid #d9d9d9; border-radius: 8rpx; font-size: 28rpx; }
.toast { position: fixed; bottom: 120rpx; left: 50%; transform: translateX(-50%) translateY(100rpx); background: rgba(0,0,0,0.75); color: #fff; border-radius: 8rpx; padding: 24rpx 48rpx; font-size: 28rpx; z-index: 9999; opacity: 0; transition: all 0.3s; }
.toast.show { transform: translateX(-50%) translateY(0); opacity: 1; }
</style>
