<template>
  <div style="width: 1000px;">
    <div ref="chart"  style="width: 480px;height: 300px;float: left;"></div>
    <div ref="chart3" style="width: 480px;height: 300px;float: left;"></div>
  </div>
</template>

<script>

  import * as echarts from 'echarts'
  // eslint-disable-next-line no-unused-vars
  var xData = ['2021-2','2021-3', '2021-4']
  var yData = [15,78,52]
  var xData1= ['2021-4-15', '2021-4-16', '2021-4-17', '2021-4-18']
  var yData1 = [8, 10, 5, 13]
  var xData2= ['2021-4-15', '2021-4-16', '2021-4-17', '2021-4-18']
  var yData2 = [21,15,9,33]
  var data=['java', 'vue教程', '云']
  var Data=[{value: 20, name: 'java'},
    {value: 15, name: 'vue教程'},
    {value: 1, name: '云'}]
  // eslint-disable-next-line no-unused-vars,no-undef
  export default {
    methods: {
      initChart (container, option) {
        const myChart = echarts.init(container)
        myChart.setOption(option)
      }
    },
    mounted () {
      // 基于准备好的dom，初始化echarts实例
      setTimeout(() => {
        let option = {
          title: {
            text: '按天统计视频修改次数'
          },
          tooltip: {}, // 悬浮提示，不写就没有提示
          xAxis: { // 横坐标
            // data: JSON.stringify(this.y)
            type: 'category',
            data: xData1
          },
          yAxis: {
            type: 'value'
          }, // 纵坐标，可以不写根据data里的数据列出来
          series: [{
            name: '数量', // 可以不写
            type: 'bar', // 柱形
            data: yData1
          }]
        }
        // 绘制图表
        this.initChart(this.$refs.chart, option)
        // let option1 = {
        //   title: {
        //     text: '按天统计视频的修改次数'
        //   },
        //   tooltip: {},
        //   xAxis: {
        //     type: 'category',
        //     data: xData1
        //   },
        //   yAxis: {
        //     type: 'value'
        //   },
        //   series: [{
        //     data: yData1,
        //     type: 'line'
        //   }]
        // }
        // 绘制图表
        // this.initChart(this.$refs.chart1, option1)
        // let option2 = {
        //   title: {
        //     text: '按天统计修改的文本数'
        //   },
        //   tooltip: {},
        //   xAxis: {
        //     type: 'category',
        //     data: xData2
        //   },
        //   yAxis: {
        //     type: 'value'
        //   },
        //   series: [{
        //     data: yData2,
        //     type: 'line'
        //   }]
        // }
        // 绘制图表
        // this.initChart(this.$refs.chart2, option2)
        let option3 = {
          title: {
            text: '视频修改次数占比',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: data,
          },
          series: [
            {
              name: '占比',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: Data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        this.initChart(this.$refs.chart3, option3)
      }, 50)
    },
    created () {
      //const _this = this
      // axios.get('http://localhost:8181/adminselectCountbydate').then(function (resp) {
      //     console.log(resp)
      //   for(var i=0,len1=xData.length;i<len1;i++){
      //     xData.splice(i)
      //   }
      //   for(var t=0,len2=yData.length;t<len2;t++){
      //     yData.splice(t)
      //   }
      //   for (var j = 0, len = resp.data.length; j < len; j++) {
      //     yData.push(resp.data[j].count)
      //     xData.push(resp.data[j].scheduledTime)
      //   }
      //   }
      // )
      // axios.get('http://localhost:8181/adminselectCountbytime').then(function (resp) {
      //     console.log(resp)
      //   for(var i=0,len1=xData1.length;i<len1;i++){
      //     xData1.splice(i)
      //   }
      //   for(var t=0,len2=yData1.length;t<len2;t++){
      //     yData1.splice(t)
      //   }
      //   for (var j = 0, len = resp.data.length; j < len; j++) {
      //     // alert(_this.x[j].recycleOrderId)
      //     yData1.push(resp.data[j].count)
      //     xData1.push(resp.data[j].scheduledTime+":00")
      //   }
      //   }
      // )
      // axios.get('http://localhost:8181/adminselectCountByitemid').then(function (resp) {
      //     console.log(resp)
      //   for(var i=0,len1=Data.length;i<len1;i++){
      //     Data.splice(i)
      //   }
      //   for(var t=0,len2=Data.length;t<len2;t++){
      //     data.splice(t)
      //   }
      //   for (var j = 0, len = resp.data.length; j < len; j++) {
      //     Data.push(resp.data[j])
      //     data.push(resp.data[j].name)
      //   }
      //   }
      // )
      // axios.get('http://localhost:8181/adminselectAmountbydate').then(function (resp) {
      //     console.log(resp)
      //     /*   Data=resp.data;
      //        alert(JSON.stringify(Data))*/
      //   for(var i=0,len1=xData2.length;i<len1;i++){
      //     xData2.splice(i)
      //   }
      //   for(var t=0,len2=yData2.length;t<len2;t++){
      //     yData2.splice(t)
      //   }
      //   for (var j = 0, len = resp.data.length; j < len; j++) {
      //     yData2.push(resp.data[j].count)
      //     xData2.push(resp.data[j].scheduledTime)
      //   }
      //   }
      // )
    },
    data () {
      return {
      }
    }
  }
</script>

<style scoped>

</style>
