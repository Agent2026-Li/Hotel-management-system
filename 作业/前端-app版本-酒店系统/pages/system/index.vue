<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">系统管理</text>
    </view>

    <!-- 酒店信息 -->
    <view class="card">
      <view class="card-title">酒店信息</view>
      <view class="form-item">
        <text class="form-label">酒店名称</text>
        <input class="form-input" v-model="hotelInfo.name">
      </view>
      <view class="form-item">
        <text class="form-label">酒店地址</text>
        <input class="form-input" v-model="hotelInfo.address">
      </view>
      <view class="form-item">
        <text class="form-label">联系电话</text>
        <input class="form-input" v-model="hotelInfo.phone">
      </view>
      <view class="btn btn-primary btn-block" @click="onSaveHotel">保存</view>
    </view>

    <!-- 房型配置 -->
    <view class="card">
      <view class="flex-between mb-12">
        <text class="card-title" style="margin-bottom:0">房型配置</text>
        <text class="btn btn-primary btn-sm" @click="onAddRoomType">+ 新增</text>
      </view>
      <view class="roomtype-item" v-for="(item, index) in roomTypes" :key="item.type">
        <view class="flex-between">
          <text class="roomtype-name">{{item.type}}</text>
          <view class="flex gap-8">
            <text class="roomtype-price">¥{{item.price}}/晚</text>
            <text class="text-sm text-gray">{{item.count}}间</text>
          </view>
        </view>
        <text class="btn btn-sm text-gray" style="margin-top:4px" @click="onEditRoomType(index)">编辑</text>
      </view>
    </view>

    <!-- 用户管理 -->
    <view class="card">
      <view class="flex-between mb-12">
        <text class="card-title" style="margin-bottom:0">用户管理</text>
        <text class="btn btn-primary btn-sm" @click="onAddUser">+ 新增</text>
      </view>
      <view class="user-item" v-for="(user, index) in users" :key="user.name">
        <view class="flex gap-8">
          <view class="avatar" :style="{background: user.avatarColor}">{{user.name[0]}}</view>
          <view>
            <text class="user-name">{{user.name}}</text>
            <text class="text-sm text-gray" style="display:block">{{user.phone}}</text>
          </view>
        </view>
        <text class="tag tag-blue">{{user.role}}</text>
      </view>
    </view>

    <!-- 其他设置 -->
    <view class="card">
      <view class="card-title">其他设置</view>
      <view class="setting-item" v-for="item in settings" :key="item.label">
        <text>{{item.label}}</text>
        <text class="text-gray">{{item.value}}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hotelInfo: {
        name: '锦华国际酒店',
        address: '山东省济南市历下区经十路168号',
        phone: '0531-88886666'
      },
      roomTypes: [
        { type: '豪华大床房', price: 488, count: 28 },
        { type: '标准双床房', price: 358, count: 35 },
        { type: '行政套房', price: 888, count: 15 },
        { type: '经济单人房', price: 258, count: 20 }
      ],
      users: [
        { name: '陈经理', phone: '138****1001', role: '管理员', avatarColor: '#1677ff' },
        { name: '刘前台', phone: '139****2002', role: '前台', avatarColor: '#52c41a' },
        { name: '王主管', phone: '137****3003', role: '客房主管', avatarColor: '#fa8c16' }
      ],
      settings: [
        { label: '楼栋管理', value: 'A栋, B栋 ›' },
        { label: '押金默认金额', value: '¥800 ›' },
        { label: '入住时间', value: '14:00 ›' },
        { label: '退房时间', value: '12:00 ›' },
        { label: '系统版本', value: 'v1.0.0' }
      ]
    }
  },
  methods: {
    onSaveHotel() {
      uni.showToast({ title: '保存成功', icon: 'success' })
    },
    onAddRoomType() {
      uni.showToast({ title: '新增房型', icon: 'none' })
    },
    onEditRoomType(index) {
      uni.showToast({ title: `编辑 ${this.roomTypes[index].type}`, icon: 'none' })
    },
    onAddUser() {
      uni.showToast({ title: '新增用户', icon: 'none' })
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

.roomtype-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.roomtype-item:last-child {
  border-bottom: none;
}

.roomtype-name {
  font-weight: 600;
}

.roomtype-price {
  color: #1677ff;
  font-weight: 600;
}

.user-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.user-item:last-child {
  border-bottom: none;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #1677ff;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.user-name {
  font-weight: 600;
  font-size: 14px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.setting-item:last-child {
  border-bottom: none;
}
</style>
