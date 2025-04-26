<template>
  <div class="mod-demo-echarts">
    <el-alert
      title="Hint："
      type="warning"
      :closable="false">
      <div slot-scope="description">
        <p class="el-alert__description">1 </p>
      </div>
    </el-alert>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div id="J_chartLineBox" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="24">
        <el-card>
          <div id="J_chartBarBox" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="J_chartPieBox" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="J_chartScatterBox" class="chart-box"></div>
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
        chartLine: null,
        chartBar: null,
        chartPie: null,
        chartScatter: null
      }
    },
    mounted () {
      this.initChartLine()
      this.initChartBar()
      this.initChartPie()
      this.initChartScatter()
    },
  activated () {
// Since the resize event is added to echart, it needs to be resized and drawn once when the component is activated, otherwise a blank bug will occur
      if (this.chartLine) {
        this.chartLine.resize()
      }
      if (this.chartBar) {
        this.chartBar.resize()
      }
      if (this.chartPie) {
        this.chartPie.resize()
      }
      if (this.chartScatter) {
        this.chartScatter.resize()
      }
    },
    methods: {
// Line chart
initChartLine () {
var option = {
'title': {
'text': 'Line chart stacking'
},
'tooltip': {
'trigger': 'axis'
},
'legend': {
'data': [ 'Email marketing', 'Affiliate advertising', 'Video advertising', 'Direct access', 'Search engine' ]
},
'grid': {
'left': '3%',
'right': '4%',
'bottom': '3%',
'containLabel': true
},
'toolbox': {
'feature': {
'saveAsImage': { }
}
},
'xAxis': {
'type': 'category',
'boundaryGap': false,
'data': [ 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday' ]
},
'yAxis': {
'type': 'value'
},
'series': [
{
'name': 'Email Marketing',
'type': 'line',
'stack': 'Total Amount',
'data': [ 120, 132, 101, 134, 90, 230, 210 ]
},
{
'name': 'Affiliate Advertising',
'type': 'line',
'stack': 'Total Amount',
'data': [ 220, 182, 191, 234, 290, 330, 310 ]
},
{
'name': 'Video Ads',
'type': 'line',
'stack': 'Total',
'data': [ 150, 232, 201, 154, 190, 330, 410 ]
},
{
'name': 'Direct Access',
'type': 'line',
'stack': 'Total',
'data': [ 320, 332, 301, 334, 390, 330, 320 ]
},
{
'name': 'Search Engine',
'type': 'line',
'stack': 'Total',
'data': [ 820, 932, 901, 934, 1290, 1330, 1320 ] 
} 
] 
}
        this.chartLine = echarts.init(document.getElementById('J_chartLineBox'))
        this.chartLine.setOption(option)
        window.addEventListener('resize', () => {
          this.chartLine.resize()
        })
      },
      // Bar chart
initChartBar () {
var option = {
tooltip: {
trigger: 'axis',
axisPointer: {
type: 'shadow'
}
},
legend: {
data: ['Direct access', 'Email marketing', 'Affiliate advertising', 'Video advertising', 'Search engine', 'Baidu', 'Google', 'Bing', 'Other']
},
grid: {
left: '3%',
right: '4%',
bottom: '3%',
containLabel: true
},
xAxis: [
{
type: 'category',
data: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
}
],
yAxis: [
{
type: 'value'
}
],
series: [
{
name: 'Direct access',
type: 'bar',
data: [320, 332, 301, 334, 390, 330, 320]
},
{
name: 'Email marketing',
type: 'bar',
stack: 'Advertisement',
data: [120, 132, 101, 134, 90, 230, 210]
},
{
name: 'Alliance advertising',
type: 'bar',
stack: 'Advertisement',
data: [220, 182, 191, 234, 290, 330, 310]
},
{
name: 'Video advertising',
type: 'bar',
stack: 'Advertisement',
data: [150, 232, 201, 154, 190, 330, 410]
},
{
name: 'Search Engine',
type: 'bar',
data: [862, 1018, 964, 1026, 1679, 1600, 1570],
markLine: {
lineStyle: {
normal: {
type: 'dashed'
}
},
data: [
[{ type: 'min' }, { type: 'max' }]
]
}
},
{
name: 'Baidu',
type: 'bar',
barWidth: 5,
stack: 'Search Engine',
data: [620, 732, 701, 734, 1090, 1130, 1120]
},
{
name: 'Google',
type: 'bar',
stack: 'Search Engine',
data: [120, 132, 101, 134, 290, 230, 220]
},
{
name: 'Bing',
type: 'bar',
stack: 'Search Engine',
data: [60, 72, 71, 74, 190, 130, 110]
},
{
name: 'Other',
type: 'bar',
stack: 'Search Engine',
data: [62, 82, 91, 84, 109, 110, 120]
}
]
}
        this.chartBar = echarts.init(document.getElementById('J_chartBarBox'))
        this.chartBar.setOption(option)
        window.addEventListener('resize', () => {
          this.chartBar.resize()
        })
      },
// pie chart 
initChartPie () { 
var option = { 
backgroundColor: '#2c343c', 
title: { 
text: 'Customized Pie', 
left: 'center', 
top: 20, 
textStyle: { 
color: '#ccc' 
} 
}, 
tooltip: { 
trigger: 'item', 
formatter: '{a} <br/>{b} : {c} ({d}%)' 
}, 
visualMap: { 
show: false, 
min: 80, 
max: 600, 
inRange: { 
colorLightness: [0, 1] 
} 
}, 
series: [ 
{ 
name: 'Access source', 
type: 'pie', 
radius: '55%', 
center: ['50%', '50%'], 
data: [ 
{ value: 335, name: 'Direct access' },
{ value: 310, name: 'Email marketing' },
{ value: 274, name: 'Affiliate advertising' },
{ value: 235, name: 'Video advertising' },
{ value: 400, name: 'Search engine' }
              ].sort(function (a, b) { return a.value - b.value }),
              roseType: 'radius',
              label: {
                normal: {
                  textStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  }
                }
              },
              labelLine: {
                normal: {
                  lineStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  },
                  smooth: 0.2,
                  length: 10,
                  length2: 20
                }
              },
              itemStyle: {
                normal: {
                  color: '#c23531',
                  shadowBlur: 200,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              animationType: 'scale',
              animationEasing: 'elasticOut',
              animationDelay: function (idx) {
                return Math.random() * 200
              }
            }
          ]
        }
        this.chartPie = echarts.init(document.getElementById('J_chartPieBox'))
        this.chartPie.setOption(option)
        window.addEventListener('resize', () => {
          this.chartPie.resize()
        })
      },
     // Scatter chart
initChartScatter () {
var option = {
backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [
{ offset: 0, color: '#f7f8fa' },
{ offset: 1, color: '#cdd0d5' }
]),
title: {
text: 'Life expectancy and GDP of each country in 1990 and 2015'
},
          legend: {
            right: 10,
            data: ['1990', '2015']
          },
          xAxis: {
            splitLine: {
              lineStyle: {
                type: 'dashed'
              }
            }
          },
          yAxis: {
            splitLine: {
              lineStyle: {
                type: 'dashed'
              }
            },
            scale: true
          },
          series: [
            {
              name: '1990',
              data: [
                [28604, 77, 17096869, 'Australia', 1990],
                [31163, 77.4, 27662440, 'Canada', 1990],
                [1516, 68, 1154605773, 'China', 1990],
                [13670, 74.7, 10582082, 'Cuba', 1990],
                [28599, 75, 4986705, 'Finland', 1990],
                [29476, 77.1, 56943299, 'France', 1990],
                [31476, 75.4, 78958237, 'Germany', 1990],
                [28666, 78.1, 254830, 'Iceland', 1990],
                [1777, 57.7, 870601776, 'India', 1990],
                [29550, 79.1, 122249285, 'Japan', 1990],
                [2076, 67.9, 20194354, 'North Korea', 1990],
                [12087, 72, 42972254, 'South Korea', 1990],
                [24021, 75.4, 3397534, 'New Zealand', 1990],
                [43296, 76.8, 4240375, 'Norway', 1990],
                [10088, 70.8, 38195258, 'Poland', 1990],
                [19349, 69.6, 147568552, 'Russia', 1990],
                [10670, 67.3, 53994605, 'Turkey', 1990],
                [26424, 75.7, 57110117, 'United Kingdom', 1990],
                [37062, 75.4, 252847810, 'United States', 1990]
              ],
              type: 'scatter',
              symbolSize: function (data) {
                return Math.sqrt(data[2]) / 5e2
              },
              label: {
                emphasis: {
                  show: true,
                  formatter: function (param) {
                    return param.data[3]
                  },
                  position: 'top'
                }
              },
              itemStyle: {
                normal: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(120, 36, 50, 0.5)',
                  shadowOffsetY: 5,
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    { offset: 0, color: 'rgb(251, 118, 123)' },
                    { offset: 1, color: 'rgb(204, 46, 72)' }
                  ])
                }
              }
            },
            {
              name: '2015',
              data: [
                [44056, 81.8, 23968973, 'Australia', 2015],
                [43294, 81.7, 35939927, 'Canada', 2015],
                [13334, 76.9, 1376048943, 'China', 2015],
                [21291, 78.5, 11389562, 'Cuba', 2015],
                [38923, 80.8, 5503457, 'Finland', 2015],
                [37599, 81.9, 64395345, 'France', 2015],
                [44053, 81.1, 80688545, 'Germany', 2015],
                [42182, 82.8, 329425, 'Iceland', 2015],
                [5903, 66.8, 1311050527, 'India', 2015],
                [36162, 83.5, 126573481, 'Japan', 2015],
                [1390, 71.4, 25155317, 'North Korea', 2015],
                [34644, 80.7, 50293439, 'South Korea', 2015],
                [34186, 80.6, 4528526, 'New Zealand', 2015],
                [64304, 81.6, 5210967, 'Norway', 2015],
                [24787, 77.3, 38611794, 'Poland', 2015],
                [23038, 73.13, 143456918, 'Russia', 2015],
                [19360, 76.5, 78665830, 'Turkey', 2015],
                [38225, 81.4, 64715810, 'United Kingdom', 2015],
                [53354, 79.1, 321773631, 'United States', 2015]
              ],
              type: 'scatter',
              symbolSize: function (data) {
                return Math.sqrt(data[2]) / 5e2
              },
              label: {
                emphasis: {
                  show: true,
                  formatter: function (param) {
                    return param.data[3]
                  },
                  position: 'top'
                }
              },
              itemStyle: {
                normal: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(25, 100, 150, 0.5)',
                  shadowOffsetY: 5,
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    { offset: 0, color: 'rgb(129, 227, 238)' },
                    { offset: 1, color: 'rgb(25, 183, 207)' }
                  ])
                }
              }
            }
          ]
        }
        this.chartPie = echarts.init(document.getElementById('J_chartScatterBox'))
        this.chartPie.setOption(option)
        window.addEventListener('resize', () => {
          this.chartPie.resize()
        })
      }
    }
  }
</script>

<style lang="scss">
  .mod-demo-echarts {
    > .el-alert {
      margin-bottom: 10px;
    }
    > .el-row {
      margin-top: -10px;
      margin-bottom: -10px;
      .el-col {
        padding-top: 10px;
        padding-bottom: 10px;
      }
    }
    .chart-box {
      min-height: 400px;
    }
  }
</style>
