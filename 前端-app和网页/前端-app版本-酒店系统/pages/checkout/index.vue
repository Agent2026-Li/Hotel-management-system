<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">退房结算</text>
    </view>

    <view class="card">
      <view class="card-title">房间信息</view>
      <view class="flex-between">
        <text style="font-size:18px;font-weight:700">305房</text>
        <text class="tag tag-blue">豪华大床房</text>
      </view>
      <view style="margin-top:8px;font-size:13px;color:#595959;line-height:1.8">
        👤 客人：张伟<br/>
        📅 入住 06/26 → 退房 06/28（2晚）<br/>
        💰 房费：¥488/晚 × 2晚 = ¥976
      </view>
    </view>

    <view class="card">
      <view class="card-title">消费明细</view>
      <view class="bill-section">
        <view class="bill-row" v-for="item in billItems" :key="item.label">
          <text class="bill-label">{{item.label}}</text>
          <text class="bill-val">{{item.value}}</text>
        </view>
        <view class="bill-row total">
          <text>应收总计</text>
          <text>¥1,089.00</text>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">押金抵扣</view>
      <view class="bill-section">
        <view class="bill-row">
          <text class="bill-label">已收押金</text>
          <text class="bill-val">¥800.00</text>
        </view>
        <view class="bill-row">
          <text class="bill-label">应退/应补</text>
          <text class="bill-val" style="color:#f44336">需补交 ¥289.00</text>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">结算方式</view>
      <view class="pay-methods">
        <view class="pay-method" :class="{selected: payMethod === 'cash'}" @click="payMethod='cash'">
          <text class="pay-icon">💵</text>
          <text>现金</text>
        </view>
        <view class="pay-method" :class="{selected: payMethod === 'card'}" @click="payMethod='card'">
          <text class="pay-icon">💳</text>
          <text>银行卡</text>
        </view>
        <view class="pay-method" :class="{selected: payMethod === 'wechat'}" @click="payMethod='wechat'">
          <text class="pay-icon">📱</text>
          <text>微信</text>
        </view>
        <view class="pay-method" :class="{selected: payMethod === 'alipay'}" @click="payMethod='alipay'">
          <text class="pay-icon">🔵</text>
          <text>支付宝</text>
        </view>
      </view>
    </view>

    <view class="flex gap-8 mt-16" style="padding-bottom:20px">
      <text class="btn btn-block">🖨️ 打印账单</text>
      <text class="btn btn-primary btn-block" @click="onConfirm">✅ 确认结算</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      payMethod: 'cash',
      billItems: [
        { label: '房费（2晚）', value: '¥976.00' },
        { label: '迷你吧消费', value: '¥68.00' },
        { label: '洗衣服务', value: '¥45.00' },
        { label: '客房服务', value: '¥0.00' }
      ]
    }
  },
  methods: {
    onConfirm() {
      uni.showToast({ title: '退房结算完成！', icon: 'success' })
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

.bill-section {
  margin-bottom: 16px;
}

.bill-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  border-bottom: 1px solid #f5f5f5;
}

.bill-row:last-child {
  border-bottom: none;
}

.bill-row.total {
  font-size: 18px;
  font-weight: 700;
  color: #1677ff;
  border-top: 2px solid #1677ff;
  border-bottom: none;
  padding-top: 12px;
}

.bill-label {
  color: #595959;
}

.bill-val {
  font-weight: 600;
}

.pay-methods {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

.pay-method {
  flex: 1;
  padding: 14px;
  border-radius: 10px;
  text-align: center;
  background: #fafafa;
  border: 2px solid transparent;
  font-size: 13px;
}

.pay-method.selected {
  border-color: #1677ff;
  background: #e6f7ff;
}

.pay-icon {
  font-size: 28px;
  display: block;
  margin-bottom: 4px;
}
</style>
