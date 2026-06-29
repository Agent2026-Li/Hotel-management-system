<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">预订管理</text>
      <text class="btn btn-primary btn-sm" @click="onAdd">+ 新增</text>
    </view>

    <view class="res-tabs">
      <view class="res-tab" :class="{active: activeTab === 'all'}" @click="switchTab('all')">全部</view>
      <view class="res-tab" :class="{active: activeTab === 'today'}" @click="switchTab('today')">今日到店</view>
      <view class="res-tab" :class="{active: activeTab === 'future'}" @click="switchTab('future')">未来预订</view>
    </view>

    <view class="res-list">
      <view class="res-card" v-for="item in filteredList" :key="item.id">
        <view class="flex-between">
          <text class="res-guest">{{item.guest}}</text>
          <text class="tag" :class="item.today ? 'tag-blue' : 'tag-green'">{{item.today ? '今日到店' : '未来预订'}}</text>
        </view>
        <view class="res-info">
          📅 入住 {{item.checkin}} → 退房 {{item.checkout}}（{{item.nights}}晚）<br/>
          🏠 {{item.room}}房 · {{item.roomType}}<br/>
          📱 {{item.phone}}
        </view>
        <view class="res-actions">
          <text class="btn btn-primary btn-sm" v-if="item.today" @click="onCheckin(item)">办理入住</text>
          <text class="btn btn-sm">查看详情</text>
          <text class="btn btn-sm" style="color:#f5222d">取消预订</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      reservations: [
        { id: 1, guest: '赵明辉', checkin: '06/28', checkout: '06/30', nights: 2, room: '702', roomType: '豪华大床房', phone: '138****6721', today: true },
        { id: 2, guest: '刘雨薇', checkin: '07/01', checkout: '07/05', nights: 4, room: '305', roomType: '行政套房', phone: '159****3348', today: false },
        { id: 3, guest: '陈志远', checkin: '06/28', checkout: '06/29', nights: 1, room: '412', roomType: '标准双床房', phone: '186****9012', today: true },
        { id: 4, guest: '王思琪', checkin: '07/03', checkout: '07/06', nights: 3, room: '601', roomType: '豪华大床房', phone: '135****7788', today: false }
      ]
    }
  },
  computed: {
    filteredList() {
      if (this.activeTab === 'today') return this.reservations.filter(r => r.today)
      if (this.activeTab === 'future') return this.reservations.filter(r => !r.today)
      return this.reservations
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
    },
    onAdd() {
      uni.showToast({ title: '新增预订', icon: 'none' })
    },
    onCheckin(item) {
      uni.navigateTo({ url: '/pages/checkin/index' })
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

.res-tabs {
  display: flex;
  background: #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 12px;
}

.res-tab {
  flex: 1;
  text-align: center;
  padding: 10px;
  font-size: 14px;
}

.res-tab.active {
  background: #1677ff;
  color: #fff;
}

.res-card {
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 10px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}

.res-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.res-guest {
  font-size: 16px;
  font-weight: 700;
}

.res-info {
  font-size: 13px;
  color: #595959;
  line-height: 1.8;
}

.res-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}
</style>
