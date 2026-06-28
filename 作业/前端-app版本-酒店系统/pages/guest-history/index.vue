<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">客史管理</text>
    </view>

    <view class="search-bar flex gap-8 mb-12">
      <input class="form-input" placeholder="姓名/手机号/证件号" v-model="searchKeyword" style="flex:1">
      <view class="btn btn-primary" @click="onSearch">搜索</view>
    </view>

    <view class="guest-card" v-for="item in filteredGuests" :key="item.id" @click="showDetail(item)">
      <view class="flex-between">
        <view class="flex gap-8">
          <text class="guest-name">{{item.name}}</text>
          <text class="tag tag-blue" v-if="item.level === '金卡'">{{item.level}}</text>
          <text class="tag tag-gray" v-else>{{item.level}}</text>
        </view>
        <text class="text-sm text-gray">{{item.phone}}</text>
      </view>
      <view class="mt-12">
        <text class="text-sm text-gray">累计入住 {{item.totalStays}}次 | 累计消费 ¥{{item.totalSpent}}</text>
      </view>
      <view class="flex gap-8 mt-12">
        <text class="tag tag-green" v-for="tag in item.tags" :key="tag">{{tag}}</text>
      </view>
      <view class="mt-12 text-sm">
        <text class="text-gray">最近：</text>
        <text>{{item.lastStay.date}} {{item.lastStay.room}}房 {{item.lastStay.type}} ¥{{item.lastStay.amount}}</text>
      </view>
    </view>

    <view v-if="filteredGuests.length === 0" class="text-center text-gray" style="padding:60px">
      暂无客史记录
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      guests: [
        {
          id: 1, name: '李明远', level: '金卡', phone: '138****6721',
          totalStays: 12, totalSpent: 18640,
          tags: ['商务常客', '高消费', '偏好高层'],
          lastStay: { date: '06/20', room: '305', type: '豪华大床房', amount: 1464 }
        },
        {
          id: 2, name: '张婷', level: '银卡', phone: '159****3348',
          totalStays: 5, totalSpent: 6280,
          tags: ['旅游度假', '周末常客'],
          lastStay: { date: '06/15', room: '610', type: '行政套房', amount: 2664 }
        },
        {
          id: 3, name: '王建国', level: '普通', phone: '186****9012',
          totalStays: 2, totalSpent: 1952,
          tags: ['商务出差'],
          lastStay: { date: '06/22', room: '412', type: '标准双床房', amount: 716 }
        }
      ]
    }
  },
  computed: {
    filteredGuests() {
      if (!this.searchKeyword) return this.guests
      const kw = this.searchKeyword.toLowerCase()
      return this.guests.filter(g =>
        g.name.toLowerCase().includes(kw) ||
        g.phone.includes(kw)
      )
    }
  },
  methods: {
    onSearch() {
      // filteredGuests computed will handle filtering
    },
    showDetail(item) {
      uni.showModal({
        title: item.name,
        content: `等级：${item.level}\n入住次数：${item.totalStays}次\n累计消费：¥${item.totalSpent}\n标签：${item.tags.join('、')}`,
        showCancel: false
      })
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

.search-bar {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.guest-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 10px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}

.guest-name {
  font-size: 17px;
  font-weight: 700;
}
</style>
