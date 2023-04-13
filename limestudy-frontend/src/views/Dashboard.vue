<template>
    <div class="card-wrap">
        <div class="dashboard container">
            <h2 class="name">Welcome back, {{ firstName }}!</h2>
            <div class="outer-container">
                <div class="inner-left"> 
                    <!-- <h3>Member since: {{ new Date(accountCreatedOn).toLocaleString('en-us', {dateStyle: "long"}) }}</h3> -->
                    <div class="line-chart">
                        <h4>Here are your stats for the week: </h4>
                        <Bar :chart-data="chartData" :chart-options="chartOptions" />
                    </div>
                </div>
                <div class="inner-right">
                    <FullCalendar :options="calendarOptions" class="calendar" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import FullCalendar from '@fullcalendar/vue';
import listPlugin from '@fullcalendar/list';
import { Bar } from 'vue-chartjs/legacy'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, LinearScale, CategoryScale, PointElement } from 'chart.js'

ChartJS.register( Title, Tooltip, Legend, BarElement, LinearScale, CategoryScale, PointElement )

export default {
    name: 'Dashboard',
    components: {
        FullCalendar,
        Bar,
    },
    data() {
        return {
            tempLabels: [],
            correct: [],
            calendarOptions: {
                plugins: [ listPlugin ],
                initialView: 'listWeek',
                //dateClick: this.handleDateClick,
                events: [],
                showNonCurrentDates: false,
                //eventClick: this.handleEventClick,
                eventBackgroundColor: '#32CD32',
                headerToolbar: {
                    start: 'title',
                    center: '',
                    end: ''
                },
            },
            chartData: {
                labels: [], // 
                datasets: [
                    { 
                        label: 'Correct',
                        data: [],
                        backgroundColor: '#DAEAF6'
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
    computed: {
        firstName: {
            get() {
                return this.$store.state.profileFirstName;
            }
        },
        accountCreatedOn: {
            get() {
                return this.$store.state.profileAccountCreatedOn;
            }
        }
    },
    methods: {
        async testButton() {
            await axios({
                method: 'GET',
                url: `/api/users/test`,
                withCredentials: true,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                console.log(response.data);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
    },
    async created() {
        this.calendarOptions.events = [];
        await axios({
            method: 'GET',
            url: `/api/events`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here")
            console.log(response.data);
            response.data.forEach((userEvent) => {
                const newEvent = {
                    id: userEvent.eventId,
                    //userId: userEvent.userId,
                    start: new Date(userEvent.event_date).toLocaleDateString('en-CA'), // new Date(userEvent.event_created_on).toLocaleDateString('en-CA'),
                    title: userEvent.event_description,
                    end: new Date(userEvent.event_date).toLocaleDateString('en-CA'),
                }
                this.calendarOptions.events.push(newEvent);
            });
            console.log(this.calendarOptions.events);
            return;
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
            return;
        });
        //this.$store.dispatch("getAllUserEvents");
        //this.calendarOptions.events = this.$store.state.events;
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
                this.correct.push(stat.correct);
            });
            this.chartData.labels = this.tempLabels;
            this.chartData.datasets[0].data = this.correct;
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
        });
    }
}
</script>


<style lang="scss" scoped>
.name
{
    text-align: center;
}
.outer-container
{
    display: flex;
    //flex-direction: column;
    justify-content: space-between;
}
.inner-left
{
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    flex: .5;

    .line-chart
    {
        width: 100%;
        
        h4
        {
            margin-bottom: 20px;
            text-align: center;
        }
    }
}
.inner-right
{
    display: flex;
    flex: .35;
    height: 100%;

    .calendar
    {
        width: 100%;
    }
}
.dashboard
{
    position: relative;
    .name
    {
        display: flex;
        align-items: center;
        position: absolute;
        top: -70px;
        right: calc(100vw/2.5);
    }
}
</style>