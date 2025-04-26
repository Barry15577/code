<template>
  <div class="mod-notification-dashboard">
    <el-card class="dashboard-header">
      <div slot="header">
        <span>Notification System Dashboard</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refresh">
          <i class="el-icon-refresh"></i> Refresh
        </el-button>
      </div>
      
      <el-row :gutter="20" class="dashboard-summary">
        <el-col :span="6">
          <div class="summary-card bg-primary">
            <div class="summary-icon">
              <i class="el-icon-bell"></i>
            </div>
            <div class="summary-content">
              <div class="summary-title">Total Notifications</div>
              <div class="summary-value">{{ totalNotifications }}</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="summary-card bg-success">
            <div class="summary-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="summary-content">
              <div class="summary-title">Read Notifications</div>
              <div class="summary-value">{{ readNotifications }} ({{ readPercentage }}%)</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="summary-card bg-warning">
            <div class="summary-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="summary-content">
              <div class="summary-title">Unread Notifications</div>
              <div class="summary-value">{{ unreadNotifications }}</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="summary-card bg-info">
            <div class="summary-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="summary-content">
              <div class="summary-title">Active Users</div>
              <div class="summary-value">{{ activeUsers }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">Notifications by Type</div>
          <div id="chart-type" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">Notifications by Priority</div>
          <div id="chart-priority" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">Notifications by Status</div>
          <div id="chart-status" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">Notifications by Target Audience</div>
          <div id="chart-target" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header">Notifications Created Over Time (Last 7 Days)</div>
          <div id="chart-time" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">Notification Read Statistics</div>
          <div id="chart-read" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="data-summary">
          <div slot="header">Dashboard Summary</div>
          <div class="summary-list">
            <div class="summary-list-item">
              <div class="summary-list-label">Total Notifications:</div>
              <div class="summary-list-value">{{ totalNotifications }}</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Read Notifications:</div>
              <div class="summary-list-value">{{ readNotifications }}</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Unread Notifications:</div>
              <div class="summary-list-value">{{ unreadNotifications }}</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Active Notifications:</div>
              <div class="summary-list-value">{{ enabledNotifications }}</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Inactive Notifications:</div>
              <div class="summary-list-value">{{ disabledNotifications }}</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Read Percentage:</div>
              <div class="summary-list-value">{{ readPercentage }}%</div>
            </div>
            <div class="summary-list-item">
              <div class="summary-list-label">Last Updated:</div>
              <div class="summary-list-value">{{ lastUpdated }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  data () {
    return {
      // Charts
      chartType: null,
      chartPriority: null,
      chartStatus: null,
      chartTarget: null,
      chartTime: null,
      chartRead: null,
      
      // Data
      totalNotifications: 0,
      readNotifications: 0,
      unreadNotifications: 0,
      enabledNotifications: 0,
      disabledNotifications: 0,
      readPercentage: 0,
      activeUsers: 0,
      lastUpdated: '-',
      
      // Chart Data
      typeData: { types: [], counts: [] },
      priorityData: { priorities: [], counts: [] },
      statusData: { statuses: [], counts: [] },
      targetData: { targets: [], counts: [] },
      timeData: { dates: [], counts: [] },
      readData: { statuses: [], counts: [] },
      
      // Loading state
      loading: false
    }
  },
  created () {
    this.fetchData()
  },
  mounted () {
    this.initCharts()
  },
  methods: {
    // Fetch data from API
    fetchData () {
      this.loading = true
      
      this.$http({
        url: this.$http.adornUrl('/sys/dashboard/notification/stats/all'),
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const statsData = data.data
          
          // Update chart data
          this.typeData = statsData.byType
          this.priorityData = statsData.byPriority
          this.statusData = statsData.byStatus
          this.targetData = statsData.byTarget
          this.timeData = statsData.byTime
          this.readData = statsData.readRatio
          
          // Update summary data
          if (statsData.readRatio && statsData.readRatio.stats) {
            const readStats = statsData.readRatio.stats
            this.totalNotifications = readStats.total || 0
            this.readNotifications = readStats.read || 0
            this.unreadNotifications = readStats.unread || 0
            this.readPercentage = readStats.readPercentage || 0
          }
          
          // Update status data
          if (statsData.byStatus && statsData.byStatus.statuses) {
            const enabledIndex = statsData.byStatus.statuses.indexOf('Enabled')
            if (enabledIndex !== -1) {
              this.enabledNotifications = statsData.byStatus.counts[enabledIndex] || 0
            }
            
            const disabledIndex = statsData.byStatus.statuses.indexOf('Disabled')
            if (disabledIndex !== -1) {
              this.disabledNotifications = statsData.byStatus.counts[disabledIndex] || 0
            }
          }
          
          // Set active users count (mock data for now)
          this.activeUsers = Math.floor(Math.random() * 50) + 10
          
          // Update last updated time
          this.lastUpdated = new Date().toLocaleString()
          
          // Update charts
          this.updateCharts()
        }
        
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    
    // Initialize all charts
    initCharts () {
      // Type chart
      this.chartType = echarts.init(document.getElementById('chart-type'))
      
      // Priority chart
      this.chartPriority = echarts.init(document.getElementById('chart-priority'))
      
      // Status chart
      this.chartStatus = echarts.init(document.getElementById('chart-status'))
      
      // Target chart
      this.chartTarget = echarts.init(document.getElementById('chart-target'))
      
      // Time chart
      this.chartTime = echarts.init(document.getElementById('chart-time'))
      
      // Read chart
      this.chartRead = echarts.init(document.getElementById('chart-read'))
      
      // Add resize listeners
      window.addEventListener('resize', this.resizeCharts)
      
      // Update charts with initial data
      this.updateCharts()
    },
    
    // Update all charts with new data
    updateCharts () {
      this.updateTypeChart()
      this.updatePriorityChart()
      this.updateStatusChart()
      this.updateTargetChart()
      this.updateTimeChart()
      this.updateReadChart()
    },
    
    // Resize all charts
    resizeCharts () {
      if (this.chartType) this.chartType.resize()
      if (this.chartPriority) this.chartPriority.resize()
      if (this.chartStatus) this.chartStatus.resize()
      if (this.chartTarget) this.chartTarget.resize()
      if (this.chartTime) this.chartTime.resize()
      if (this.chartRead) this.chartRead.resize()
    },
    
    // Update Type Chart
    updateTypeChart () {
      if (!this.chartType) return
      
      const colors = {
        'INFO': '#409EFF',
        'WARNING': '#E6A23C',
        'ERROR': '#F56C6C',
        'SUCCESS': '#67C23A',
        'UNKNOWN': '#909399'
      }
      
      const typeColors = this.typeData.types.map(type => colors[type] || '#909399')
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle',
          data: this.typeData.types
        },
        series: [
          {
            name: 'Notification Type',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.typeData.types.map((type, index) => ({
              value: this.typeData.counts[index],
              name: type,
              itemStyle: {
                color: typeColors[index]
              }
            }))
          }
        ]
      }
      
      this.chartType.setOption(option)
    },
    
    // Update Priority Chart
    updatePriorityChart () {
      if (!this.chartPriority) return
      
      const colors = {
        'LOW': '#67C23A',
        'MEDIUM': '#E6A23C',
        'HIGH': '#F56C6C',
        'UNKNOWN': '#909399'
      }
      
      const priorityColors = this.priorityData.priorities.map(priority => colors[priority] || '#909399')
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.priorityData.priorities
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Count',
            type: 'bar',
            data: this.priorityData.priorities.map((priority, index) => ({
              value: this.priorityData.counts[index],
              itemStyle: {
                color: priorityColors[index]
              }
            })),
            label: {
              show: true,
              position: 'top'
            }
          }
        ]
      }
      
      this.chartPriority.setOption(option)
    },
    
    // Update Status Chart
    updateStatusChart () {
      if (!this.chartStatus) return
      
      const colors = {
        'Enabled': '#67C23A',
        'Disabled': '#909399'
      }
      
      const statusColors = this.statusData.statuses.map(status => colors[status] || '#909399')
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle',
          data: this.statusData.statuses
        },
        series: [
          {
            name: 'Status',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: this.statusData.statuses.map((status, index) => ({
              value: this.statusData.counts[index],
              name: status,
              itemStyle: {
                color: statusColors[index]
              }
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      
      this.chartStatus.setOption(option)
    },
    
    // Update Target Chart
    updateTargetChart () {
      if (!this.chartTarget) return
      
      const colors = {
        'ALL': '#409EFF',
        'ADMIN': '#F56C6C',
        'USER': '#67C23A',
        'UNKNOWN': '#909399'
      }
      
      const targetColors = this.targetData.targets.map(target => colors[target] || '#909399')
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.targetData.targets
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Count',
            type: 'bar',
            data: this.targetData.targets.map((target, index) => ({
              value: this.targetData.counts[index],
              itemStyle: {
                color: targetColors[index]
              }
            })),
            label: {
              show: true,
              position: 'top'
            }
          }
        ]
      }
      
      this.chartTarget.setOption(option)
    },
    
    // Update Time Chart
    updateTimeChart () {
      if (!this.chartTime) return
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.timeData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Notifications',
            type: 'line',
            smooth: true,
            data: this.timeData.counts,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.8)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
                ]
              }
            },
            itemStyle: {
              color: '#409EFF'
            },
            symbolSize: 8
          }
        ]
      }
      
      this.chartTime.setOption(option)
    },
    
    // Update Read Chart
    updateReadChart () {
      if (!this.chartRead) return
      
      const colors = {
        'Read': '#67C23A',
        'Unread': '#F56C6C'
      }
      
      const readColors = this.readData.statuses.map(status => colors[status] || '#909399')
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle',
          data: this.readData.statuses
        },
        series: [
          {
            name: 'Read Status',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: this.readData.statuses.map((status, index) => ({
              value: this.readData.counts[index],
              name: status,
              itemStyle: {
                color: readColors[index]
              }
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      
      this.chartRead.setOption(option)
    },
    
    // Refresh the dashboard
    refresh () {
      this.fetchData()
    }
  },
  // Clean up event listeners
  beforeDestroy () {
    window.removeEventListener('resize', this.resizeCharts)
    
    if (this.chartType) this.chartType.dispose()
    if (this.chartPriority) this.chartPriority.dispose()
    if (this.chartStatus) this.chartStatus.dispose()
    if (this.chartTarget) this.chartTarget.dispose()
    if (this.chartTime) this.chartTime.dispose()
    if (this.chartRead) this.chartRead.dispose()
  }
}
</script>

<style lang="scss">
.mod-notification-dashboard {
  .dashboard-header {
    margin-bottom: 20px;
  }
  
  .dashboard-summary {
    margin-top: 10px;
  }
  
  .summary-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 4px;
    color: #fff;
    height: 100px;
    
    &.bg-primary {
      background: linear-gradient(to right, #409EFF, #53a8ff);
    }
    
    &.bg-success {
      background: linear-gradient(to right, #67C23A, #85ce61);
    }
    
    &.bg-warning {
      background: linear-gradient(to right, #E6A23C, #ebb563);
    }
    
    &.bg-info {
      background: linear-gradient(to right, #909399, #a6a9ad);
    }
    
    .summary-icon {
      font-size: 50px;
      width: 60px;
      text-align: center;
    }
    
    .summary-content {
      flex: 1;
      padding-left: 15px;
      
      .summary-title {
        font-size: 16px;
        margin-bottom: 8px;
      }
      
      .summary-value {
        font-size: 24px;
        font-weight: bold;
      }
    }
  }
  
  .chart-row {
    margin-bottom: 20px;
  }
  
  .chart-card {
    height: 400px;
    
    .chart-container {
      height: 320px;
    }
  }
  
  .data-summary {
    height: 400px;
    
    .summary-list {
      padding: 10px 20px;
      
      .summary-list-item {
        display: flex;
        justify-content: space-between;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
        }
        
        .summary-list-label {
          font-weight: bold;
          color: #606266;
        }
        
        .summary-list-value {
          color: #303133;
        }
      }
    }
  }
}
</style> 