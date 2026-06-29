<template>
  <view class="container">
    <view class="page-header">
      <text class="page-title">客房清洁</text>
      <text class="btn btn-primary btn-sm" @click="onAssign">分配任务</text>
    </view>

    <view class="filter-bar">
      <view class="filter-chip" :class="{active: activeFilter === index}" v-for="(item, index) in filters" :key="index" @click="activeFilter = index">
        {{item}}
      </view>
    </view>

    <view class="task-card urgent" v-for="(task, index) in urgentTasks" :key="'u'+index">
      <view class="task-header">
        <text class="task-room">🏠 {{task.room}}房</text>
        <text class="tag tag-red">紧急 · {{task.tag}}</text>
      </view>
      <text class="task-meta">{{task.type}} · {{task.note}} · {{task.deadline}}</text>
      <view class="task-actions">
        <text class="btn btn-primary btn-sm" @click="onStartClean(task)">🧹 开始清洁</text>
        <text class="btn btn-sm">📋 清洁清单</text>
      </view>
    </view>

    <view class="task-card" v-for="(task, index) in pendingTasks" :key="'p'+index" style="border-left-color:#FF9800">
      <view class="task-header">
        <text class="task-room">🏠 {{task.room}}房</text>
        <text class="tag tag-orange">待分配 · {{task.tag}}</text>
      </view>
      <text class="task-meta">{{task.type}} · {{task.note}}</text>
      <view class="task-actions">
        <text class="btn btn-primary btn-sm">🧹 开始清洁</text>
        <text class="btn btn-sm">📋 清洁清单</text>
      </view>
    </view>

    <view class="task-card" v-for="(task, index) in inProgressTasks" :key="'i'+index" style="border-left-color:#2196F3">
      <view class="task-header">
        <text class="task-room">🏠 {{task.room}}房</text>
        <text class="tag tag-blue">进行中</text>
      </view>
      <text class="task-meta">{{task.type}} · 清洁员：{{task.cleaner}}</text>
      <view class="task-actions">
        <text class="btn btn-success btn-sm" @click="onComplete(task)">✅ 标记完成</text>
        <text class="btn btn-sm">📋 清洁清单</text>
      </view>
    </view>

    <view class="task-card done" v-for="(task, index) in doneTasks" :key="'d'+index">
      <view class="task-header">
        <text class="task-room">🏠 {{task.room}}房</text>
        <text class="tag tag-green">已完成</text>
      </view>
      <text class="task-meta">{{task.type}} · 清洁员：{{task.cleaner}} · {{task.doneTime}}完成</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      filters: ['全部 (8)', '待分配 (3)', '进行中 (2)', '已完成 (3)'],
      activeFilter: 0,
      urgentTasks: [
        { room: '208', tag: '退房清洁', type: '豪华大床房', note: '客人已退房', deadline: '需在14:00前完成' }
      ],
      pendingTasks: [
        { room: '512', tag: '退房清洁', type: '标准双床房', note: '客人已退房 · 常规清洁' }
      ],
      inProgressTasks: [
        { room: '403', type: '行政套房', note: '住客房清洁', cleaner: '王阿姨' }
      ],
      doneTasks: [
        { room: '105', type: '经济单人房', note: '退房清洁', cleaner: '李阿姨', doneTime: '11:30' },
        { room: '618', type: '豪华大床房', note: '住客房清洁', cleaner: '王阿姨', doneTime: '10:45' },
        { room: '302', type: '标准双床房', note: '退房清洁', cleaner: '张阿姨', doneTime: '09:20' }
      ]
    }
  },
  methods: {
    onAssign() {
      uni.showToast({ title: '分配任务', icon: 'none' })
    },
    onStartClean(task) {
      uni.showToast({ title: `开始清洁 ${task.room}房`, icon: 'none' })
    },
    onComplete(task) {
      uni.showToast({ title: `${task.room}房清洁完成`, icon: 'success' })
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

.filter-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  overflow-x: auto;
  padding: 4px 0;
}

.filter-chip {
  white-space: nowrap;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  background: #f0f0f0;
}

.filter-chip.active {
  background: #1677ff;
  color: #fff;
}

.task-card {
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 10px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  border-left: 4px solid #FF9800;
}

.task-card.urgent {
  border-left-color: #F44336;
}

.task-card.done {
  border-left-color: #4CAF50;
  opacity: 0.7;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.task-room {
  font-size: 16px;
  font-weight: 700;
}

.task-meta {
  font-size: 12px;
  color: #8c8c8c;
}

.task-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}
</style>
