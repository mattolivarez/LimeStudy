<template>
    <div>
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="calendar-container">
            <FullCalendar :options="calendarOptions" />
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import Modal from '../components/Modal.vue'
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
    components: {
        FullCalendar, // make the <FullCalendar> tag available
        Modal,
    },
    data() {
        return {
            modalActive: false,
            modalMessage: "",
            calendarOptions: {
                plugins: [ dayGridPlugin, interactionPlugin ],
                initialView: 'dayGridMonth',
                dateClick: this.handleDateClick,
                events: [],
                showNonCurrentDates: false,
                eventClick: this.handleEventClick,
            }
        }
    },
    methods: {
        handleDateClick(arg) {
            console.log(arg.dateStr + " date clicked");
            console.log(this.$store.state.events);
        },
        handleEventClick(info) {
            this.modalMessage = info.event.title;
            this.modalActive = !this.modalActive;
            console.log(this.modalMessage + " " + this.modalActive)
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            this.modalMessage = "";
        },
    },
    computed: {

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
            //console.log(response.data);
            response.data.forEach((userEvent) => {
                const newEvent = {
                    id: userEvent.eventId,
                    //userId: userEvent.userId,
                    start: userEvent.event_date,
                    title: userEvent.event_description,
                    end: userEvent.event_created_on,
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
    }
}
</script>

<style lang="scss">
.calendar-container
{
    max-width: 1440px;
    width: 900px;
    height: 625px;
    margin: 30px auto;
    overflow: hidden;
}
</style>
