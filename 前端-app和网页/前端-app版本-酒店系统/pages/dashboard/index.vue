<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">经营仪表盘</text>
      <text class="text-gray text-sm">数据刷新于 {{refreshTime}}</text>
    </view>

    <!-- Stat Cards -->
    <view class="stat-row">
      <view class="stat-card" v-for="(item, index) in statCards" :key="index" :style="{borderTop: '3px solid ' + item.color}">
        <text class="stat-icon">{{item.icon}}</text>
        <view class="stat-info">
          <text class="stat-label">{{item.title}}</text>
          <text class="stat-value">{{item.prefix}}{{item.value}}{{item.suffix}}</text>
          <text class="stat-change" :class="item.change >= 0 ? 'up' : 'down'">
            {{item.change >= 0 ? '↑' : '↓'}}{{Math.abs(item.change)}}% 较昨日
          </text>
        </view>
      </view>
    </view>

    <!-- Status Summary -->
    <view class="card">
      <view class="card-title">实时房态概览</view>
      <view class="status-bar">
        <view class="status-item">
          <view class="status-dot" style="background:#2196F3"></view>
          <text>已入住 {{statusCounts.OC}}</text>
        </view>
        <view class="status-item">
          <view class="status-dot" style="background:#4CAF50"></view>
          <text>空闲 {{statusCounts.VC}}</text>
        </view>
        <view class="status-item">
          <view class="status-dot" style="background:#FF9800"></view>
          <text>脏房 {{statusCounts.VD}}</text>
        </view>
        <view class="status-item">
          <view class="status-dot" style="background:#9E9E9E"></view>
          <text>预订 {{statusCounts.RS}}</text>
        </view>
        <view class="status-item">
          <view class="status-dot" style="background:#F44336"></view>
          <text>维修 {{statusCounts.OOO}}</text>
        </view>
      </view>
      <view class="occ-bar">
        <text>入住率</text>
        <view class="progress-bar">
          <view class="progress-fill" :style="{width: occupancyRate + '%'}"></view>
        </view>
        <text class="occ-value">{{occupancyRate}}%</text>
      </view>
    </view>

    <!-- Activities -->
    <view class="card">
      <view class="card-title">今日实时动态</view>
      <view v-if="activities.length === 0" class="text-center text-gray" style="padding:30px">暂无动态</view>
      <view class="activity-item" v-for="(item, index) in activities" :key="index">
        <text class="tag tag-blue" v-if="item.type === 'checkin'">{{item.time}}</text>
        <text class="tag tag-orange" v-else-if="item.type === 'checkout'">{{item.time}}</text>
        <text class="tag tag-green" v-else-if="item.type === 'clean'">{{item.time}}</text>
        <text class="tag tag-red" v-else-if="item.type === 'maintain'">{{item.time}}</text>
        <text class="tag tag-gray" v-else>{{item.time}}</text>
        <text>{{item.text}}</text>
      </view>
    </view>

    <!-- Quick Actions -->
    <view class="card">
      <view class="card-title">快捷操作</view>
      <view class="quick-actions">
        <view class="action-btn" @click="goPage('/pages/checkin/index')">🛎️ 办理入住</view>
        <view class="action-btn" @click="goPage('/pages/checkout/index')">🧾 退房结算</view>
        <view class="action-btn" @click="goPage('/pages/reservation/index')">📝 新增预订</view>
        <view class="action-btn" @click="goPage('/pages/room-status/index')">📋 房态看板</view>
        <view class="action-btn" @click="goPage('/pages/billing/index')">💳 消费入账</view>
        <view class="action-btn" @click="goPage('/pages/housekeeping/index')">🧹 客房清洁</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      refreshTime: '13:45',
      statCards: [
        { icon: '🏨', title: '今日入住', value: 23, prefix: '', suffix: '', change: 12, color: '#1677ff' },
        { icon: '💰', title: '今日营收', value: 18640, prefix: '¥', suffix: '', change: 8, color: '#52c41a' }
      ],
      statusCounts: {
        OC: 62,
        VC: 15,
        VD: 6,
        RS: 12,
        OOO: 3
      },
      occupancyRate: 78,
      activities: [
        { time: '13:20', type: 'checkin', text: '305房 张先生 办理入住' },
        { time: '12:50', type: 'checkout', text: '512房 李女士 办理退房' },
        { time: '12:30', type: 'clean', text: '208房 清洁完成 已查房' },
        { time: '11:45', type: 'maintain', text: '401房 空调报修' },
        { time: '11:20', type: 'checkin', text: '610房 王先生 办理入住' },
        { time: '10:30', type: 'other', text: '新增预订：702房 赵先生 6/28-6/30' }
      ]
    }
  },
  methods: {
    goPage(path) {
      uni.navigateTo({ url: path })
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

.stat-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.stat-card {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-icon {
  font-size: 32px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 12px;
  color: #8c8c8c;
  margin-bottom: 4px;
  display: block;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1f1f1f;
  display: block;
}

.stat-change {
  font-size: 11px;
  margin-top: 2px;
  display: block;
}

.stat-change.up { color: #f44336; }
.stat-change.down { color: #4caf50; }

.status-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  background: #fafafa;
  padding: 4px 10px;
  border-radius: 12px;
}

.status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.occ-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1677ff, #52c41a);
  border-radius: 4px;
  transition: width 0.5s;
}

.occ-value {
  font-weight: 700;
  color: #1677ff;
  font-size: 16px;
  min-width: 48px;
  text-align: right;
}

.activity-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.activity-item:last-child {
  border-bottom: none;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.action-btn {
  flex: 0 0 calc(33.33% - 7px);
  padding: 14px 8px;
  background: #fafafa;
  border-radius: 10px;
  text-align: center;
  font-size: 13px;
}

.action-btn:active {
  background: #e6f7ff;
}
</style>
