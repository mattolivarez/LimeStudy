<template>
    <div class="container">
        <div class="buttons">
            <button class="preview" :class="{'button-inactive': showDecks}">Daily</button>
            <button class="preview" :class="{'button-inactive': showNotes}">Weekly</button>
            <button class="preview" :class="{'button-inactive': showNotes}">Monthly</button>
            <button class="preview" :class="{'button-inactive': showNotes}">All Time</button>
        </div>
        <div class="line-chart">
            <LineChartGenerator :chart-data="chartData" :chart-options="chartOptions" />
        </div>
        
    </div>
</template>

<script>
import { Line as LineChartGenerator } from 'vue-chartjs/legacy'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
} from 'chart.js'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
)
export default {
    name: 'ViewStatistics',
    components: {
        LineChartGenerator
    },
    data() {
        return {
            showDecks: true,
            showNotes: false,
            chartData: {
                labels: ['January', 'February', 'March'], // 
                datasets: [{ data: [40, 20, 12] }],
            },
            chartOptions: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { 
                        display: false,
                    }
                },
            },
        }
    },
    created() {
        //console.log(Legend);
    },
}

</script>


<style lang="scss" scoped>
.buttons
{
    display: flex;
    align-items: center;
    justify-content: center;
}
.preview
{
    margin-left: 16px;
    text-transform: initial;
}
</style>