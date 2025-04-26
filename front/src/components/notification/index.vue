<template>
  <div class="notification-container">
    <el-dropdown trigger="click" @command="handleCommand">
      <div class="notification-icon">
        <el-badge :value="notificationCount" :hidden="notificationCount === 0">
          <i class="el-icon-bell"></i>
        </el-badge>
      </div>
      <el-dropdown-menu slot="dropdown">
        <div class="notification-dropdown">
          <div class="notification-header">
            <span>Notifications</span>
            <el-button v-if="notifications.length > 0" type="text" @click.stop="markAllRead">Mark all read</el-button>
          </div>
          <div class="notification-list" v-if="notifications.length > 0">
            <el-dropdown-item 
              v-for="(item, index) in notifications" 
              :key="index"
              :command="item">
              <div :class="['notification-item', item.type.toLowerCase()]">
                <div class="notification-item-title">
                  <i :class="getIconClass(item.type)"></i>
                  <span>{{ item.title }}</span>
                </div>
                <div class="notification-item-content">{{ truncateContent(item.content) }}</div>
                <div class="notification-item-time">{{ formatTime(item.publishTime) }}</div>
              </div>
            </el-dropdown-item>
          </div>
          <div class="notification-empty" v-else>
            <i class="el-icon-chat-dot-square"></i>
            <span>No new notifications</span>
          </div>
        </div>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- Notification Detail Dialog -->
    <el-dialog
      title="Notification"
      :visible.sync="detailVisible"
      width="500px"
      @closed="selectedNotification = null">
      <div v-if="selectedNotification" class="notification-detail">
        <div :class="['notification-detail-header', selectedNotification.type.toLowerCase()]">
          <i :class="getIconClass(selectedNotification.type)"></i>
          <span class="notification-detail-title">{{ selectedNotification.title }}</span>
        </div>
        <div class="notification-detail-content" v-html="selectedNotification.content"></div>
        <div class="notification-detail-meta">
          <div v-if="selectedNotification.publishTime">Published: {{ formatDate(selectedNotification.publishTime) }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Notification',
  data() {
    return {
      notifications: [],
      selectedNotification: null,
      detailVisible: false,
      refreshInterval: null
    }
  },
  computed: {
    notificationCount() {
      return this.notifications.length
    }
  },
  created() {
    // Fetch notifications on component creation
    this.fetchNotifications()
    
    // Set up automatic refresh every 5 minutes
    this.refreshInterval = setInterval(() => {
      this.fetchNotifications()
    }, 300000) // 5 minutes
  },
  beforeDestroy() {
    // Clear interval when component is destroyed
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval)
    }
  },
  methods: {
    // Fetch active notifications from the server
    fetchNotifications() {
      this.$http({
        url: this.$http.adornUrl('/sys/notification/active'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.notifications = data.notifications || []
        }
      })
    },
    
    // Handle clicking on a notification
    handleCommand(notification) {
      this.selectedNotification = notification
      this.detailVisible = true
      
      // Mark this notification as read
      this.markAsRead([notification.id])
    },
    
    // Mark all notifications as read
    markAllRead(e) {
      e.stopPropagation()
      
      if (this.notifications.length === 0) return
      
      // Get all notification IDs
      const notificationIds = this.notifications.map(item => item.id)
      
      // Call API to mark all as read
      this.markAsRead(notificationIds)
      
      // Clear the notifications list
      this.notifications = []
    },
    
    // Mark specific notifications as read
    markAsRead(notificationIds) {
      if (!notificationIds || notificationIds.length === 0) return
      
      this.$http({
        url: this.$http.adornUrl('/sys/notification/markAsRead'),
        method: 'post',
        data: this.$http.adornData(notificationIds)
      }).then(({data}) => {
        if (data && data.code === 0) {
          // If we're marking specific notifications as read,
          // remove them from the notifications list
          if (notificationIds.length < this.notifications.length) {
            this.notifications = this.notifications.filter(item => !notificationIds.includes(item.id))
          }
        }
      })
    },
    
    // Get icon class based on notification type
    getIconClass(type) {
      switch (type) {
        case 'INFO': return 'el-icon-info'
        case 'WARNING': return 'el-icon-warning'
        case 'ERROR': return 'el-icon-error'
        case 'SUCCESS': return 'el-icon-success'
        default: return 'el-icon-info'
      }
    },
    
    // Truncate content for preview
    truncateContent(content) {
      if (!content) return ''
      // Remove HTML tags and truncate to 100 characters
      let plainText = content.replace(/<[^>]*>/g, '')
      return plainText.length > 100 ? plainText.substring(0, 100) + '...' : plainText
    },
    
    // Format time for relative display
    formatTime(dateString) {
      if (!dateString) return ''
      
      const now = new Date()
      const date = new Date(dateString)
      const diffMs = now - date
      
      // Convert to seconds, minutes, hours, days
      const diffSec = Math.floor(diffMs / 1000)
      const diffMin = Math.floor(diffSec / 60)
      const diffHours = Math.floor(diffMin / 60)
      const diffDays = Math.floor(diffHours / 24)
      
      if (diffSec < 60) {
        return 'Just now'
      } else if (diffMin < 60) {
        return `${diffMin} min${diffMin > 1 ? 's' : ''} ago`
      } else if (diffHours < 24) {
        return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`
      } else if (diffDays < 7) {
        return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`
      } else {
        return this.formatDate(dateString)
      }
    },
    
    // Format date for standard display
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleString()
    }
  }
}
</script>

<style scoped>
.notification-container {
  display: inline-block;
  margin-right: 20px;
}

.notification-icon {
  font-size: 22px;
  cursor: pointer;
  color: #606266;
}

.notification-dropdown {
  width: 350px;
  max-height: 400px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px;
  border-bottom: 1px solid #EBEEF5;
  background-color: #f5f7fa;
}

.notification-header span {
  font-weight: bold;
  font-size: 15px;
}

.notification-list {
  overflow-y: auto;
  max-height: 350px;
}

.notification-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #EBEEF5;
}

.notification-item:hover {
  background-color: #f5f7fa;
}

.notification-item-title {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  font-weight: bold;
}

.notification-item-title i {
  margin-right: 8px;
}

.notification-item.info i {
  color: #909399;
}

.notification-item.warning i {
  color: #E6A23C;
}

.notification-item.error i {
  color: #F56C6C;
}

.notification-item.success i {
  color: #67C23A;
}

.notification-item-content {
  font-size: 12px;
  color: #606266;
  margin-bottom: 5px;
  line-height: 1.5;
}

.notification-item-time {
  font-size: 11px;
  color: #909399;
  text-align: right;
}

.notification-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 0;
  color: #909399;
}

.notification-empty i {
  font-size: 36px;
  margin-bottom: 10px;
}

.notification-detail {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  overflow: hidden;
}

.notification-detail-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #f4f4f5;
  border-bottom: 1px solid #e9e9eb;
}

.notification-detail-header.info {
  background-color: #f4f4f5;
  border-bottom-color: #e9e9eb;
}

.notification-detail-header.warning {
  background-color: #fdf6ec;
  border-bottom-color: #faecd8;
}

.notification-detail-header.error {
  background-color: #fef0f0;
  border-bottom-color: #fde2e2;
}

.notification-detail-header.success {
  background-color: #f0f9eb;
  border-bottom-color: #e1f3d8;
}

.notification-detail-header i {
  font-size: 18px;
  margin-right: 10px;
}

.notification-detail-title {
  font-weight: bold;
  font-size: 16px;
}

.notification-detail-content {
  padding: 16px;
  min-height: 100px;
  background-color: #fff;
}

.notification-detail-meta {
  padding: 8px 16px;
  background-color: #f9f9f9;
  color: #909399;
  font-size: 12px;
}
</style> 