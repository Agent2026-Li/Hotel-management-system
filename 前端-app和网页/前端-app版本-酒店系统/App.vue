<script>
export default {
  onLaunch() {
    console.log('App Launch')
  },
  onShow() {
    console.log('App Show')
  },
  onHide() {
    console.log('App Hide')
  }
}
</script>

<style>
page {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: #f5f5f5;
  font-size: 14px;
  color: #333;
}

.container {
  padding: 16px;
  min-height: 100vh;
}

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

/* Card */
.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: #1f1f1f;
  margin-bottom: 12px;
}

/* Stats */
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
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1f1f1f;
}

.stat-change {
  font-size: 11px;
  margin-top: 2px;
}

.stat-change.up { color: #f44336; }
.stat-change.down { color: #4caf50; }

/* Tag */
.tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  margin-right: 4px;
}

.tag-blue { background: #e6f7ff; color: #1677ff; }
.tag-green { background: #f6ffed; color: #52c41a; }
.tag-orange { background: #fff7e6; color: #fa8c16; }
.tag-red { background: #fff1f0; color: #f5222d; }
.tag-gray { background: #f5f5f5; color: #8c8c8c; }

/* Button */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 20px;
  border-radius: 6px;
  font-size: 14px;
  border: none;
  background: #f0f0f0;
  color: #333;
}

.btn-primary {
  background: #1677ff;
  color: #fff;
}

.btn-danger {
  background: #f5222d;
  color: #fff;
}

.btn-success {
  background: #52c41a;
  color: #fff;
}

.btn-sm {
  padding: 4px 12px;
  font-size: 12px;
}

.btn-block {
  display: flex;
  width: 100%;
}

/* Form */
.form-item {
  margin-bottom: 12px;
}

.form-label {
  font-size: 13px;
  color: #595959;
  margin-bottom: 4px;
  display: block;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  min-height: 60px;
  box-sizing: border-box;
}

/* Flex helpers */
.flex { display: flex; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }
.flex-wrap { flex-wrap: wrap; }
.gap-8 { gap: 8px; }
.gap-12 { gap: 12px; }
.mt-12 { margin-top: 12px; }
.mt-16 { margin-top: 16px; }
.mb-12 { margin-bottom: 12px; }
.text-center { text-align: center; }
.text-right { text-align: right; }
.text-gray { color: #8c8c8c; }
.text-sm { font-size: 12px; }
</style>
