<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">经营报表</text>
    </view>

    <view class="report-tabs">
      <view class="report-tab" :class="{active: reportType === 'daily'}" @click="reportType='daily'">日报</view>
      <view class="report-tab" :class="{active: reportType === 'weekly'}" @click="reportType='weekly'">周报</view>
      <view class="report-tab" :class="{active: reportType === 'monthly'}" @click="reportType='monthly'">月报</view>
      <view class="report-tab" :class="{active: reportType === 'custom'}" @click="reportType='custom'">自定义</view>
    </view>

    <view class="flex-between mb-12 mt-12">
      <text class="date-picker">{{dateRange}} 📅</text>
      <text class="btn btn-sm" @click="onExport">📥 导出</text>
    </view>

    <view class="kpi-row">
      <view class="kpi-card" v-for="item in kpiData" :key="item.label">
        <text class="kpi-value">{{item.value}}</text>
        <text class="kpi-label">{{item.label}}</text>
      </view>
    </view>

    <view class="card">
      <view class="card-title">经营明细</view>
      <view class="detail-row" v-for="item in detailData" :key="item.label">
        <text class="detail-label">{{item.label}}</text>
        <text class="detail-val">{{item.value}}</text>
      </view>
    </view>

    <view class="card">
      <view class="card-title">渠道分析</view>
      <view class="channel-item" v-for="item in channelData" :key="item.name">
        <view class="channel-left">
          <text class="channel-name">{{item.name}}</text>
          <view class="channel-bar">
            <view class="channel-fill" :style="{width: item.percent}"></view>
          </view>
        </view>
        <view class="channel-right">
          <text>{{item.count}}间</text>
          <text class="channel-amount">{{item.amount}}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      reportType: 'daily',
      dateRange: '2026-06-28',
      kpiData: [
        { label: '入住率', value: '78%' },
        { label: 'ADR', value: '¥428' },
        { label: 'RevPAR', value: '¥334' },
        { label: '总营收', value: '¥18.6k' }
      ],
      detailData: [
        { label: '客房收入', value: '¥15,840' },
        { label: '其他收入', value: '¥2,800' },
        { label: '入住间数', value: '23间' },
        { label: '退房间数', value: '18间' },
        { label: '平均入住天数', value: '2.3天' }
      ],
      channelData: [
        { name: '携程', percent: '42%', count: 10, amount: '¥4,280' },
        { name: '美团', percent: '28%', count: 7, amount: '¥2,996' },
        { name: '直接预订', percent: '20%', count: 5, amount: '¥2,140' },
        { name: '飞猪', percent: '10%', count: 1, amount: '¥428' }
      ]
    }
  },
  methods: {
    onExport() {
      uni.showToast({ title: '报表已导出', icon: 'success' })
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

.report-tabs {
  display: flex;
  background: #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
}

.report-tab {
  flex: 1;
  text-align: center;
  padding: 10px;
  font-size: 14px;
}

.report-tab.active {
  background: #1677ff;
  color: #fff;
}

.date-picker {
  padding: 8px 14px;
  background: #fafafa;
  border-radius: 8px;
  font-size: 14px;
}

.kpi-row {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.kpi-card {
  flex: 1;
  background: #fff;
  border-radius: 10px;
  padding: 14px 8px;
  text-align: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}

.kpi-value {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #1677ff;
}

.kpi-label {
  display: block;
  font-size: 11px;
  color: #8c8c8c;
  margin-top: 2px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-label {
  color: #8c8c8c;
}

.detail-val {
  font-weight: 600;
}

.channel-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.channel-item:last-child {
  border-bottom: none;
}

.channel-left {
  flex: 1;
  margin-right: 12px;
}

.channel-name {
  display: block;
  font-size: 14px;
  margin-bottom: 4px;
}

.channel-bar {
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.channel-fill {
  height: 100%;
  background: #1677ff;
  border-radius: 3px;
}

.channel-right {
  text-align: right;
  font-size: 13px;
}

.channel-amount {
  display: block;
  font-weight: 600;
  color: #1677ff;
}
</style>
