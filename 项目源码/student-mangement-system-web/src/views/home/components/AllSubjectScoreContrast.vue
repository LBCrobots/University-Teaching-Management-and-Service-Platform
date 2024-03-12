<template>
  <div :id="id" :class="className" :style="{ height: height, width: width }" />
</template>

<script lang="ts" setup >
import * as echarts from 'echarts'
import { EChartsType } from 'echarts/core'
import { onMounted,watch, ref, Ref } from 'vue'
import westeros from '../../../assets/subjects/westeros.json';
let props = defineProps({
  legendData: {
    type:Array,
    default: []
  },
  categoryData: {
    type:Array,
    default: []
  },
  seriesData: {
    type:Array,
    default: []
  },
  className: {
    type: String,
    default: 'chart',
  },
  config: {
    type: Object,
    default: () => {},
  },
  id: {
    type: String,
    default: 'chart',
  },
  width: {
    type: String,
    default: '200px',
  },
  height: {
    type: String,
    default: '200px',
  },
})
const options = {
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  toolbox: {
    show: true,
    feature: {
      saveAsImage: {
        show: true
      }
    }
  },
  legend: {
    data: props.legendData
  },
  grid: {
    top: 30,
    left: '2%',
    right: '2%',
    bottom: '4%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: props.categoryData,
  },
  yAxis: {
    type: 'value',
  },
  series: props.seriesData,
}
let chart: EChartsType
let chartElement: Ref<HTMLElement | null> = ref(null)

const initChart = () => {
  let element = chartElement.value
  if(element){
    echarts.registerTheme('westeros', westeros)
    let newChart = echarts.init(element, 'westeros');
    if (newChart != undefined) return newChart
  }
}

watch([()=>props.categoryData, () => props.seriesData], ([newCategoryData, newSeriesData]) => {
  console.log(`[eChart] x is ${newCategoryData} and y is `,newSeriesData)
  options.series = newSeriesData
  options.xAxis.data = newCategoryData
  chart.setOption(options) // update options data
})
onMounted(() => {
  console.log('onMounted')
  chartElement.value = document.getElementById(props.id)
  chart = initChart()
  window.addEventListener('resize', function () {
    chart && chart.resize()
  })
  if(props.seriesData.length) {
    chart.setOption(options)
  }
})
</script>

