<script>
export default {
	onLaunch: function() {
		console.log('App Launch')
	},
	onShow: function() {
		console.log('App Show')
	},
	onHide: function() {
		console.log('App Hide')
	}
}
</script>

<style>
@import "@/uni.scss";
/* 全局样式 */
page {
	background-color: #f5f5f5;
	font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
	font-size: 14px;
	color: #262626;
}

.container {
	padding: 24rpx;
}

/* 卡片样式 */
.card {
	background: #ffffff;
	border-radius: 8rpx;
	box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
	padding: 32rpx;
	margin-bottom: 24rpx;
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24rpx;
}

.card-title {
	font-size: 32rpx;
	font-weight: 600;
}

/* 按钮样式 */
.btn {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	padding: 16rpx 32rpx;
	border-radius: 8rpx;
	font-size: 28rpx;
	font-weight: 500;
	border: 1px solid transparent;
}

.btn-primary {
	background: #1677ff;
	color: #ffffff;
	border-color: #1677ff;
}

.btn-default {
	background: #ffffff;
	color: #262626;
	border-color: #d9d9d9;
}

.btn-danger {
	background: #ffffff;
	color: #ff4d4f;
	border-color: #ff4d4f;
}

.btn-sm {
	padding: 8rpx 24rpx;
	font-size: 24rpx;
}

/* 表单样式 */
.form-group {
	margin-bottom: 24rpx;
}

.form-label {
	display: block;
	margin-bottom: 12rpx;
	font-size: 28rpx;
	font-weight: 500;
}

.form-input {
	width: 100%;
	padding: 16rpx 24rpx;
	border: 1px solid #d9d9d9;
	border-radius: 8rpx;
	font-size: 28rpx;
}

/* 状态徽章 */
.status-badge {
	display: inline-flex;
	align-items: center;
	padding: 6rpx 16rpx;
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

/* 统计卡片 */
.stat-card {
	background: #ffffff;
	border-radius: 8rpx;
	padding: 32rpx;
	box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
	display: flex;
	align-items: center;
	gap: 24rpx;
}

.stat-icon {
	width: 100rpx;
	height: 100rpx;
	border-radius: 12rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 48rpx;
}

.stat-icon.blue {
	background: rgba(22, 119, 255, 0.1);
	color: #1677ff;
}

.stat-icon.green {
	background: rgba(82, 196, 26, 0.1);
	color: #52c41a;
}

.stat-icon.orange {
	background: rgba(250, 173, 20, 0.1);
	color: #faad14;
}

.stat-icon.red {
	background: rgba(255, 77, 79, 0.1);
	color: #ff4d4f;
}

.stat-info {
	flex: 1;
}

.stat-value {
	font-size: 56rpx;
	font-weight: 700;
	line-height: 1;
}

.stat-label {
	color: #8c8c8c;
	font-size: 28rpx;
	margin-top: 8rpx;
}

/* 表格样式 */
.table-container {
	overflow-x: auto;
}

.table {
	width: 100%;
	border-collapse: collapse;
}

.table th, .table td {
	padding: 24rpx;
	text-align: left;
	border-bottom: 1px solid #f0f0f0;
}

.table th {
	background: #fafafa;
	font-weight: 600;
	font-size: 26rpx;
	color: #8c8c8c;
}

/* 弹窗样式 */
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
	background: #ffffff;
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
	width: 64rpx;
	height: 64rpx;
	border: none;
	background: transparent;
	font-size: 36rpx;
	color: #8c8c8c;
}

.modal-body {
	padding: 32rpx;
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
	transform: translateX(-50%);
	background: rgba(0, 0, 0, 0.75);
	color: #ffffff;
	border-radius: 8rpx;
	padding: 24rpx 48rpx;
	font-size: 28rpx;
	z-index: 9999;
}
</style>
