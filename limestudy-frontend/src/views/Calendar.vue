<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for Calendar, which contains the Calendar Page
    Contains template (HTML), CSS, and JavaScript
    -->
    <div>
        <!-- <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" /> -->
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="calendar-container" v-if="!isCreating">
            <FullCalendar :options="calendarOptions" />
        </div>
        <div class="profile" v-if="isCreating">
            <!-- <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" /> -->
            <div class="container">
                <h2>Create New Class</h2>
                <div class="profile-info">
                    <!-- <div class="initials">{{ $store.state.profileInitials }}</div> -->
                    <!--<div class="admin-badge">
                        <adminIcon class="icon" />
                        <span>Admin</span>
                    </div>-->
                    <div class="input">
                        <label for="eventDescription">Event Description: </label>
                        <input type="text" id="eventDescription" v-model="eventDescription">
                    </div>
                    <div class="input">
                        <label for="eventDate">Event Date: </label>
                        <input type="text" id="eventDate" v-model="eventDate" disabled>
                    </div>
                    <div class="buttons">
                        <button @click.prevent="backToCalendar">Cancel</button>
                        <button @click.prevent="createNewEvent">Submit</button>
                    </div>
                </div>
            </div>
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
            eventDescription: "",
            eventDate: null,
            isCreating: false,
            modalActive: false,
            modalMessage: [],
            calendarOptions: {
                plugins: [ dayGridPlugin, interactionPlugin ],
                initialView: 'dayGridMonth',
                dateClick: this.handleDateClick,
                events: [],
                showNonCurrentDates: false,
                eventClick: this.handleEventClick,
                eventBackgroundColor: '#32CD32',
                customButtons: {
                    helpButton: {
                        text: "Help",
                        click: () => {
                            let modalMessage1 = "Click a date to add an event or";
                            let modalMessage2 = "Click an event to View/Edit/Delete";
                            this.modalMessage.push(modalMessage1);
                            this.modalMessage.push(modalMessage2);
                            this.modalActive = true;
                            console.log(this.modalMessage)
                        }
                    }
                },
                headerToolbar: {
                    right: 'helpButton today prev,next'
                }
            }
        }
    },
    methods: {
        handleDateClick(arg) {
            if (confirm("Do you want to create an event on this date?"))
            {
                this.isCreating = true;
                console.log(arg.dateStr + " date clicked");
                this.eventDate = arg.date.toLocaleString('en-us', {dateStyle: "short"});
                console.log(this.eventDate);
                //console.log(this.$store.state.events);
                //this.$router.push({name: "CreateNewEvent", props: { eventDate: arg.dateStr}});
            }
        },
        handleEventClick(info) {
            // this.modalMessage = info.event.title;
            // this.modalActive = !this.modalActive;
            // console.log(this.modalMessage + " " + this.modalActive)
            console.log(info);
            this.$router.push({name: "ViewEvent", params: { eventId: info.event.id } });
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            this.modalMessage = [];
        },
        backToCalendar() {
            this.isCreating = false;
        },
        async createNewEvent() {
            await axios({
                method: 'POST',
                url: `/api/events`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    event_description: this.eventDescription,
                    event_date: this.eventDate, //new Date(this.eventDate).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                    event_created_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"})
                }
            })
            .then((response) => {
                console.log("response starts here")
                console.log(response);

            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
            this.$router.go();
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
                    start: new Date(userEvent.event_date).toLocaleDateString('en-CA'), //new Date(userEvent.event_created_on).toLocaleDateString('en-CA'),
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
    },
    beforeDestroy() {
        this.isCreating = false;
    }
}
</script>

<style lang="scss" scoped>
.calendar-container
{
    max-width: 1440px;
    width: 900px;
    height: 625px;
    margin: 30px auto;
    overflow: hidden;
}
.fc .fc-button-primary:disabled
{
    background-color: #32CD32;
    border-color: #32CD32;
}
.fc .fc-button-primary
{
    background-color: #32CD32;
    border: #32CD32;
}
.profile
{
    .container
    {
        max-width: 1000px;
        padding: 60px 25px;

        h2
        {
            text-align: center;
            margin-bottom: 16px;
            font-weight: 300;
            font-size: 32px;
        }

        .profile-info
        {
            border-radius: 8px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            padding: 32px;
            background-color: #f1f1f1;
            display: flex;
            flex-direction: column;
            max-width: 600px;
            margin: 32px auto;

            .initials
            {
                position: initial;
                width: 80px;
                height: 80px;
                font-size: 32px;
                background-color: #303030;
                color: #fff;
                display: flex;
                align-self: center;
                align-items: center;
                justify-content: center;
                border-radius: 50%;
            }

            .admin-badge
            {
                display: flex;
                align-self: center;
                color: #fff;
                font-size: 14px;
                padding: 8px 24px;
                border-radius: 8px;
                background-color: #303030;
                margin: 16px 0;
                text-align: center;
                text-transform: capitalize;

                .icon
                {
                    width: 14px;
                    height: auto;
                    margin-right: 8px;
                }
            }

            .input
            {
                margin: 16px 0;

                label
                {
                    font-size: 14px;
                    display: block;
                    padding-bottom: 6px;
                }

                input
                {
                    width: 100%;
                    border: none;
                    background-color: #f2f7f6;
                    padding: 8px;
                    height: 50px;
                    @media (min-width: 900px)
                    {

                    }

                    &:focus
                    {
                        outline: none;
                    }
                }
            }
            .buttons
            {
                display: flex;
                align-items: center;
                justify-content: space-evenly;

                button
                {
                    align-self: center;
                }
            }
        }
    }
}
</style>
