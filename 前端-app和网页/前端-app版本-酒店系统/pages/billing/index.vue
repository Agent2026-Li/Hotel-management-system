<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">消费入账</text>
    </view>

    <view class="card">
      <view class="form-item">
        <text class="form-label">选择房间</text>
        <picker :range="roomList" :value="selectedRoom" @change="onRoomChange">
          <view class="form-input">{{roomList[selectedRoom]}}</view>
        </picker>
      </view>
      <view class="card-title" style="margin-top:8px">消费类型</view>
      <view class="consume-type">
        <view class="consume-chip" :class="{active: activeType === index}" v-for="(item, index) in consumeTypes" :key="index" @click="activeType = index">
          {{item}}
        </view>
      </view>
      <view class="form-item">
        <text class="form-label">消费金额</text>
        <input class="form-input" placeholder="请输入金额" v-model="amount">
      </view>
      <view class="form-item">
        <text class="form-label">备注</text>
        <textarea class="form-textarea" placeholder="备注消费详情..." v-model="remark"></textarea>
      </view>
      <view class="btn btn-primary btn-block" @click="onSubmit">✅ 确认入账</view>
    </view>

    <view class="card">
      <view class="card-title">305房 消费记录</view>
      <view class="consume-list">
        <view class="consume-item" v-for="(item, index) in records" :key="index">
          <view>
            <text class="tag" :class="item.tagClass">{{item.type}}</text>
            <text>{{item.detail}}</text>
          </view>
          <text style="font-weight:600">{{item.amount}}</text>
        </view>
      </view>
      <view style="text-align:right;margin-top:10px;font-size:13px;color:#8c8c8c">
        累计消费：<text style="color:#1677ff;font-weight:700">¥113.00</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      roomList: ['305房 - 张伟（豪华大床房）', '412房 - 李明（标准双床房）', '610房 - 王建国（行政套房）'],
      selectedRoom: 0,
      consumeTypes: ['🍺 迷你吧', '👔 洗衣', '🍜 客房送餐', '📞 电话费', '🅿️ 停车', '✏️ 其他'],
      activeType: 0,
      amount: '¥68.00',
      remark: '矿泉水×2 + 啤酒×2',
      records: [
        { type: '迷你吧', tagClass: 'tag-blue', detail: '矿泉水×2 + 啤酒×2', amount: '¥68.00' },
        { type: '洗衣', tagClass: 'tag-orange', detail: '衬衫干洗×1', amount: '¥45.00' },
        { type: '客房送餐', tagClass: 'tag-green', detail: '早餐×2', amount: '¥0.00' }
      ]
    }
  },
  methods: {
    onRoomChange(e) {
      this.selectedRoom = e.detail.value
    },
    onSubmit() {
      uni.showToast({ title: '入账成功！', icon: 'success' })
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

.consume-type {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.consume-chip {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  background: #f0f0f0;
}

.consume-chip.active {
  background: #1677ff;
  color: #fff;
}

.consume-list .consume-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.consume-list .consume-item:last-child {
  border-bottom: none;
}
</style>
