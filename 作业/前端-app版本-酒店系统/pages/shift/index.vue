<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">交班管理</text>
    </view>

    <view class="card">
      <view class="card-title">班次信息</view>
      <view class="summary-grid">
        <view class="summary-item">
          <text class="summary-value">早班</text>
          <text class="summary-label">当前班次</text>
        </view>
        <view class="summary-item">
          <text class="summary-value" style="font-size:16px">08:00-16:00</text>
          <text class="summary-label">班次时间</text>
        </view>
        <view class="summary-item">
          <text class="summary-value">15</text>
          <text class="summary-label">入住间数</text>
        </view>
        <view class="summary-item">
          <text class="summary-value">8</text>
          <text class="summary-label">退房间数</text>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">营收汇总</view>
      <view class="breakdown-item" v-for="item in revenueItems" :key="item.label">
        <text>{{item.label}}</text>
        <text class="breakdown-val" :style="item.style || {}">{{item.value}}</text>
      </view>
      <view class="breakdown-divider"></view>
      <view class="breakdown-item" v-for="item in payBreakdown" :key="item.label">
        <text>{{item.label}}</text>
        <text class="breakdown-val">{{item.value}}</text>
      </view>
    </view>

    <view class="card">
      <view class="card-title">待处理事项</view>
      <view class="pending-item" v-for="(item, index) in pendingItems" :key="index">
        <view class="flex-between">
          <view>
            <text class="tag tag-red" v-if="item.urgent">紧急</text>
            <text class="tag tag-orange" v-else>待处理</text>
            <text>{{item.text}}</text>
          </view>
          <text class="text-sm text-gray">{{item.note}}</text>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">交班备注</view>
      <textarea class="form-textarea" placeholder="交班备注事项..." v-model="remark"></textarea>
    </view>

    <view class="flex gap-8 mt-16" style="padding-bottom:40px">
      <view class="btn btn-block" @click="onPrint">🖨️ 打印报表</view>
      <view class="btn btn-primary btn-block" @click="onConfirm">✅ 确认交班</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      remark: '401房空调已联系维修师傅，预计下午3点到位。508房客人已安抚，赠送果盘。702房赵先生预计14:00到店，已预留房间。',
      revenueItems: [
        { label: '房费收入', value: '¥12,480' },
        { label: '押金收入', value: '¥6,400' },
        { label: '押金退还', value: '-¥3,200', style: { color: '#f44336' } }
      ],
      payBreakdown: [
        { label: '现金', value: '¥5,280' },
        { label: '银行卡', value: '¥4,800' },
        { label: '线上支付', value: '¥5,600' }
      ],
      pendingItems: [
        { urgent: true, text: '401房 空调维修', note: '已报修' },
        { urgent: false, text: '508房 客人投诉', note: '噪音问题' },
        { urgent: false, text: '205房 加床需求', note: '需协调' }
      ]
    }
  },
  methods: {
    onPrint() {
      uni.showToast({ title: '打印报表', icon: 'none' })
    },
    onConfirm() {
      uni.showToast({ title: '交班确认成功！', icon: 'success' })
    }
  }
}
</script>

<style>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f1f1f;
}

.summary-grid {
  display: flex;
  flex-wrap: wrap;
}

.summary-item {
  flex: 0 0 50%;
  padding: 12px 0;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
}

.summary-value {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #1677ff;
}

.summary-label {
  display: block;
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.breakdown-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  border-bottom: 1px solid #f0f0f0;
}

.breakdown-item:last-child {
  border-bottom: none;
}

.breakdown-val {
  font-weight: 600;
}

.breakdown-divider {
  height: 1px;
  background: #e8e8e8;
  margin: 4px 0;
}

.pending-item {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.pending-item:last-child {
  border-bottom: none;
}
</style>
