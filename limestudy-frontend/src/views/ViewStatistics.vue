<template>
    <div class="card-wrap">
        <div class="container">
            <div class="buttons">
                <button class="preview" :class="{'button-inactive': showDaily}" @click.prevent="getDailyStatistics">Daily</button>
                <button class="preview" :class="{'button-inactive': showWeekly}" @click.prevent="getWeeklyStatistics">Weekly</button>
                <button class="preview" :class="{'button-inactive': showMonthly}" @click.prevent="getMonthlyStatistics">Monthly</button>
                <button class="preview" :class="{'button-inactive': showLifeTime}" @click.prevent="getLifeTimeStatistics">All Time</button>
            </div>
            <div class="line-chart">
                <Bar :chart-data="chartData" :chart-options="chartOptions" />
            </div>
            
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { Bar } from 'vue-chartjs/legacy'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, LinearScale, CategoryScale, PointElement } from 'chart.js'

ChartJS.register( Title, Tooltip, Legend, BarElement, LinearScale, CategoryScale, PointElement )

export default {
    name: 'ViewStatistics',
    components: {
        Bar
    },
    data() {
        return {
            showDaily: false,
            showWeekly: false,
            showMonthly: false,
            showLifeTime: false,
            totals: [],
            correct: [],
            incorrect: [],
            totalCards: [],
            tempLabels: [],
            chartData: {
                labels: [], // 
                datasets: [
                    { 
                        label: 'Correct',
                        data: [],
                        backgroundColor: '#DAEAF6'
                    },
                    { 
                        label: 'Incorrect',
                        data: [],
                        backgroundColor: '#FFB3C6'
                    },
                    // { 
                    //     label: 'Total Flashcards',
                    //     data: [],
                    //     backgroundColor: '#FCF5C7'
                    // },
                    { 
                        label: 'Total of Correct & Incorrect',
                        data: [],
                        backgroundColor: '#C1F0B2'
                    },
                ],
            },
            chartOptions: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { 
                        display: true,
                    }
                },
                scales: {
                    x: {
                        stacked: true,
                    },
                    // y: {
                    //     stacked: true
                    // }
                },
            },
        }
    },
    methods: {
        clearChartData() {
            this.chartData.label = [];
            for ( let i = 0; i < this.chartData.datasets.length; i++ ) 
            {
                this.chartData.datasets[i].data = [];
            }
        },
        clearTempData() {
            this.totals = [];
            this.correct = [];
            this.incorrect = [];
            this.totalCards = [];
            this.tempLabels = [];
        },
        flipDaily() {
            this.showDaily = true;
            this.showWeekly = false;
            this.showMonthly = false;
            this.showLifeTime = false;
        },
        flipWeekly() {
            this.showDaily = false;
            this.showWeekly = true;
            this.showMonthly = false;
            this.showLifeTime = false;
        },
        flipMonthly() {
            this.showDaily = false;
            this.showWeekly = false;
            this.showMonthly = true;
            this.showLifeTime = false;
        },
        flipLifeTime() {
            this.showDaily = false;
            this.showWeekly = false;
            this.showMonthly = false;
            this.showLifeTime = true;
        },
        async getDailyStatistics() {
            this.clearChartData();
            this.clearTempData();
            await axios({
                method: 'GET',
                url: `/api/statistics/daily`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                //console.log(response.data);
                //console.log(response.data);
                response.data.forEach((stat) => {
                    this.tempLabels.push(stat.deck_name);
                    this.totals.push(stat.total);
                    this.correct.push(stat.correct);
                    this.incorrect.push(stat.incorrect);
                    this.totalCards.push(stat.total_cards);
                });
                this.chartData.labels = this.tempLabels;
                this.chartData.datasets[0].data = this.correct;
                this.chartData.datasets[1].data = this.incorrect;
                //this.chartData.datasets[2].data = this.totalCards;
                this.chartData.datasets[2].data = this.totals;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
        },
        async getWeeklyStatistics() {
            this.clearChartData();
            this.clearTempData();
            await axios({
                method: 'GET',
                url: `/api/statistics/weekly`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                //console.log(response.data);
                response.data.forEach((stat) => {
                    this.tempLabels.push(stat.date);
                    this.totals.push(stat.total);
                    this.correct.push(stat.correct);
                    this.incorrect.push(stat.incorrect);
                    this.totalCards.push(stat.total_cards);
                });
                this.chartData.labels = this.tempLabels;
                this.chartData.datasets[0].data = this.correct;
                this.chartData.datasets[1].data = this.incorrect;
                //this.chartData.datasets[2].data = this.totalCards;
                this.chartData.datasets[2].data = this.totals;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
        },
        async getMonthlyStatistics() {
            this.clearChartData();
            this.clearTempData();
            await axios({
                method: 'GET',
                url: `/api/statistics/monthly`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                //console.log(response.data);
                response.data.forEach((stat) => {
                    this.tempLabels.push(stat.deck_name);
                    this.totals.push(stat.total);
                    this.correct.push(stat.correct);
                    this.incorrect.push(stat.incorrect);
                    this.totalCards.push(stat.total_cards);
                });
                this.chartData.labels = this.tempLabels;
                this.chartData.datasets[0].data = this.correct;
                this.chartData.datasets[1].data = this.incorrect;
                //this.chartData.datasets[2].data = this.totalCards;
                this.chartData.datasets[2].data = this.totals;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
        },
        async getLifeTimeStatistics() {
            this.clearChartData();
            this.clearTempData();
            await axios({
                method: 'GET',
                url: `/api/statistics/life-time`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                //console.log(response.data);
                response.data.forEach((stat) => {
                    this.tempLabels.push(stat.deck_name);
                    this.totals.push(stat.total);
                    this.correct.push(stat.correct);
                    this.incorrect.push(stat.incorrect);
                    this.totalCards.push(stat.total_cards);
                });
                this.chartData.labels = this.tempLabels;
                this.chartData.datasets[0].data = this.correct;
                this.chartData.datasets[1].data = this.incorrect;
                //this.chartData.datasets[2].data = this.totalCards;
                this.chartData.datasets[2].data = this.totals;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
        },
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
    justify-content: space-between;
    margin-bottom: 15px;

    button
    {
        width: 20%;
    }
}
.preview
{
    margin-left: 2px;
    text-transform: initial;
}
</style>