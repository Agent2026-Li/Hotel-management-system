<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">办理入住</text>
    </view>

    <view class="card">
      <view class="card-title">证件信息</view>
      <view class="id-upload" @click="onScanId">
        <text class="upload-icon">📷</text>
        <text v-if="!idScanned">点击扫描/上传身份证</text>
        <text v-else style="color:#52c41a">✅ 证件已识别：{{guestName}} / {{guestGender}}</text>
      </view>
      <view class="form-item">
        <text class="form-label">姓名</text>
        <input class="form-input" placeholder="请输入客人姓名" v-model="guestName">
      </view>
      <view class="form-item">
        <text class="form-label">身份证号</text>
        <input class="form-input" placeholder="请输入身份证号" v-model="idNumber">
      </view>
      <view class="form-item">
        <text class="form-label">手机号</text>
        <input class="form-input" placeholder="请输入手机号" v-model="phone">
      </view>
    </view>

    <view class="card">
      <view class="card-title">入住信息</view>
      <view class="form-item">
        <text class="form-label">入住日期</text>
        <picker mode="date" :value="checkinDate" @change="onCheckinDateChange">
          <view class="form-input">{{checkinDate}}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">预计退房</text>
        <picker mode="date" :value="checkoutDate" @change="onCheckoutDateChange">
          <view class="form-input">{{checkoutDate}}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">房型选择</text>
        <picker :range="roomTypes" :value="selectedRoomType" @change="onRoomTypeChange">
          <view class="form-input">{{roomTypes[selectedRoomType]}}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">分配房间</text>
        <view class="room-assign">
          <view class="room-option" :class="{selected: selectedRoom === index}" v-for="(room, index) in availableRooms" :key="index" @click="selectRoom(index)">
            {{room}}房
          </view>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">押金与支付</view>
      <view class="form-item">
        <text class="form-label">押金金额</text>
        <input class="form-input" v-model="deposit" placeholder="请输入押金金额">
      </view>
      <view class="form-item">
        <text class="form-label">支付方式</text>
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
    </view>

    <view class="btn btn-primary btn-block" style="padding:14px;font-size:16px;margin-top:8px" @click="onConfirm">✅ 确认入住</view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      idScanned: false,
      guestName: '张伟',
      guestGender: '男',
      idNumber: '3701**********1234',
      phone: '138****5678',
      checkinDate: '2026-06-28',
      checkoutDate: '2026-06-30',
      roomTypes: ['豪华大床房 · ¥488/晚', '标准双床房 · ¥358/晚', '行政套房 · ¥888/晚', '经济单人房 · ¥258/晚'],
      selectedRoomType: 0,
      availableRooms: ['201', '203', '305', '308', '410', '412', '501', '506', '601', '608', '702', '705'],
      selectedRoom: 2,
      deposit: '¥800',
      payMethod: 'cash'
    }
  },
  methods: {
    onScanId() {
      this.idScanned = true
    },
    onCheckinDateChange(e) {
      this.checkinDate = e.detail.value
    },
    onCheckoutDateChange(e) {
      this.checkoutDate = e.detail.value
    },
    onRoomTypeChange(e) {
      this.selectedRoomType = e.detail.value
    },
    selectRoom(index) {
      this.selectedRoom = index
    },
    onConfirm() {
      uni.showToast({ title: '入住登记成功！', icon: 'success' })
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

.id-upload {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  color: #8c8c8c;
  margin-bottom: 16px;
}

.upload-icon {
  font-size: 40px;
  display: block;
  margin-bottom: 8px;
}

.room-assign {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}

.room-option {
  padding: 10px 14px;
  border-radius: 8px;
  text-align: center;
  font-size: 13px;
  background: #fafafa;
  border: 2px solid transparent;
  cursor: pointer;
}

.room-option.selected {
  border-color: #1677ff;
  background: #e6f7ff;
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
