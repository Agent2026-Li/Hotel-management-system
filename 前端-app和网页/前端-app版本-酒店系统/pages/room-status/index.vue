<template>
  <view class="container" @click="closeMenu">
    <!-- Floor Tabs -->
    <scroll-view scroll-x class="floor-tabs">
      <view class="floor-tab" :class="{active: activeFloor === item}" v-for="item in [1,2,3,4,5,6,7]" :key="item" @click="switchFloor(item)">
        {{item}}F
      </view>
    </scroll-view>

    <!-- Stats Bar -->
    <view class="stats-bar">
      总 {{statusCounts.total}} | 已入住 {{statusCounts.OC}} | 空闲 {{statusCounts.VC}} | 脏房 {{statusCounts.VD}} | 入住率 {{occupancyRate}}%
    </view>

    <!-- Legend -->
    <view class="legend">
      <view class="legend-item" v-for="item in statusLegend" :key="item.status">
        <view class="legend-dot" :style="{background: item.color}"></view>
        <text>{{item.label}}</text>
      </view>
    </view>

    <!-- Room Grid -->
    <view class="room-grid">
      <view class="room-card" v-for="item in floorRooms" :key="item.id"
        :style="{background: getStatusColor(item.status)}"
        @click="onRoomTap(item)">
        <text class="room-num">{{item.number}}</text>
        <text class="room-type">{{item.type}}</text>
        <text class="room-icon" v-if="item.status === 'OC'">👤</text>
        <text class="room-icon" v-else-if="item.status === 'VD'">🧹</text>
      </view>
      <view class="room-empty" v-if="floorRooms.length === 0">该楼层暂无符合条件房间</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeFloor: 1,
      statusCounts: {
        total: 98,
        OC: 62,
        VC: 15,
        VD: 6,
        RS: 12,
        OOO: 3
      },
      occupancyRate: 78,
      statusLegend: [
        { status: 'OC', label: '已入住', color: '#2196F3' },
        { status: 'VC', label: '空闲', color: '#4CAF50' },
        { status: 'VD', label: '脏房', color: '#FF9800' },
        { status: 'RS', label: '预订', color: '#9E9E9E' },
        { status: 'OOO', label: '维修', color: '#F44336' }
      ],
      allRooms: [],
      floorRooms: []
    }
  },
  onReady() {
    this.generateRooms()
    this.filterByFloor()
  },
  methods: {
    generateRooms() {
      const roomTypes = ['大床', '双床', '套房', '单人']
      const statusList = ['OC', 'OC', 'OC', 'VC', 'VC', 'VD', 'RS', 'OC', 'OC', 'VC', 'OC', 'VD', 'RS', 'OC', 'VC', 'OC', 'OC', 'OC', 'OOO', 'VC']
      const rooms = []
      for (let floor = 1; floor <= 7; floor++) {
        for (let r = 1; r <= 14; r++) {
          const num = floor * 100 + r
          const status = statusList[(num + floor) % statusList.length]
          const type = roomTypes[(num + r) % roomTypes.length]
          rooms.push({ id: num, number: num, status, type, floor })
        }
      }
      this.allRooms = rooms
    },
    filterByFloor() {
      this.floorRooms = this.allRooms.filter(r => r.floor === this.activeFloor)
    },
    switchFloor(floor) {
      this.activeFloor = floor
      this.filterByFloor()
    },
    getStatusColor(status) {
      const colors = {
        'OC': '#2196F3',
        'VC': '#4CAF50',
        'VD': '#FF9800',
        'RS': '#9E9E9E',
        'OOO': '#F44336',
        'INSPECT': '#FFC107'
      }
      return colors[status] || '#03A9F4'
    },
    onRoomTap(room) {
      const statusMap = {
        'OC': '已入住',
        'VC': '空闲',
        'VD': '脏房',
        'RS': '预订',
        'OOO': '维修',
        'INSPECT': '检查中'
      }
      uni.showModal({
        title: `${room.number}房`,
        content: `房型：${room.type}\n状态：${statusMap[room.status]}`,
        showCancel: false
      })
    }
  }
}
</script>

<style>
.floor-tabs {
  white-space: nowrap;
  margin-bottom: 12px;
  padding: 4px 0;
}

.floor-tab {
  display: inline-block;
  padding: 6px 18px;
  border-radius: 20px;
  font-size: 13px;
  background: #f0f0f0;
  margin-right: 8px;
}

.floor-tab.active {
  background: #1677ff;
  color: #fff;
}

.stats-bar {
  font-size: 12px;
  color: #595959;
  background: #fafafa;
  padding: 8px 12px;
  border-radius: 8px;
  margin-bottom: 10px;
}

.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}

.room-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.room-card {
  width: calc(25% - 8px);
  border-radius: 10px;
  padding: 12px 6px;
  text-align: center;
  color: #fff;
  position: relative;
}

.room-num {
  display: block;
  font-size: 16px;
  font-weight: 700;
}

.room-type {
  display: block;
  font-size: 10px;
  opacity: 0.85;
  margin-top: 2px;
}

.room-icon {
  position: absolute;
  top: 4px;
  right: 4px;
  font-size: 14px;
}

.room-empty {
  text-align: center;
  color: #8c8c8c;
  padding: 40px 0;
  width: 100%;
}
</style>
