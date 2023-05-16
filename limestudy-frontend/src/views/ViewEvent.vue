<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View to view specific event
    Contains template (HTML), CSS, and JavaScript
    -->
    <div>
        <div class="profile" v-if="!pickingDate">
            <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
            <div class="container">
                <h2 v-if="isViewing">Viewing Event</h2>
                <h2 v-else-if="isUpdating">Updating Event</h2>
                <div class="profile-info">
                    <!-- <div class="initials">{{ $store.state.profileInitials }}</div> -->
                    <!--<div class="admin-badge">
                        <adminIcon class="icon" />
                        <span>Admin</span>
                    </div>-->
                    <!-- <h4>Please ensure event date format is mm/dd/yyyy before submitting</h4> -->
                    <div class="input">
                        <label for="userId">User Id: </label>
                        <input type="text" id="userId" v-model="userId" disabled>
                    </div>
                    <div class="input">
                        <label for="eventId">Event Id: </label>
                        <input type="text" id="eventId" v-model="eventId" disabled>
                    </div>
                    <div class="input">
                        <label for="eventDescription">Event Description: </label>
                        <input type="text" id="eventDescription" v-model="eventDescription" disabled>
                    </div>
                    <div class="input" @click.prevent="selectDate">
                        <label for="eventDate">Event Date: </label>
                        <input type="text" id="eventDate" v-model="eventDate" disabled>
                    </div>
                    <div class="buttons">
                        <button @click="deleteEvent" v-if="isViewing">Delete</button>
                        <button @click="switchToUpdate" v-if="isViewing">Update</button>
                        <button @click="switchToView" v-if="isUpdating">Cancel</button>
                        <button @click="updateEvent" v-if="isUpdating">Submit</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="calendar-container" v-if="pickingDate">
            <FullCalendar :options="calendarOptions" />
        </div>
    </div>
</template>

<script>
import Modal from "../components/Modal";
import axios from "axios"
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
//import adminIcon from "../assets/Icons/user-crown-light.svg";

export default {
    name: "ViewEvent",
    components: {
        Modal,
        FullCalendar
        //adminIcon,
    },
    data() {
        return {
            pickingDate: false,
            modalMessage: "Event updated!",
            modalActive: null,
            className: "",
            eventId: null,
            userId: null,
            eventDescription: "",
            eventDate: null,
            eventCreatedOn: null,
            isViewing: true,
            isUpdating: false,
            calendarOptions: {
                plugins: [ dayGridPlugin, interactionPlugin ],
                initialView: 'dayGridMonth',
                dateClick: this.handleDateClick,
                showNonCurrentDates: false,
                eventClick: this.handleEventClick,
                eventBackgroundColor: '#32CD32',
            }
        };
    },
    methods: {
        async deleteEvent() {
            if (confirm("Are you sure you want to delete this event?"))
            {
                await axios({
                    method: 'DELETE',
                    url: `/api/events/${this.$route.params.eventId}`,
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('user'),
                        'Content-Type': 'application/json'
                    },
                })
                .then((response) => {
                    console.log("response starts here")
                    console.log(response);
                }).catch((err) => {
                    console.log("error starts here")
                    console.log(err);
                });
                this.$router.push({name: "Calendar"});
            }
        },
        switchToUpdate() {
            this.isViewing = !this.isViewing;
            this.isUpdating = !this.isUpdating;
            this.toggleDisabled();
        },
        async updateEvent() {
            await axios({
                method: 'PUT',
                url: `/api/events/${this.$route.params.eventId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    userId: this.userId,
                    eventId: this.eventId,
                    event_description: this.eventDescription,
                    event_date: this.eventDate, //new Date(this.eventDate).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                    event_created_on: this.eventCreatedOn
                }
            })
            .then((response) => {
                console.log("response starts here")
                console.log(response);

            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
            });
            this.$router.push({name: "Calendar"});
        },
        switchToView() {
            this.isViewing = !this.isViewing;
            this.isUpdating = !this.isUpdating;
            this.toggleDisabled();
        },
        toggleDisabled() {
            document.getElementById('eventDescription').disabled = !document.getElementById('eventDescription').disabled;
            //document.getElementById('eventDate').disabled = !document.getElementById('eventDate').disabled;
        },
        selectDate() {
            if (this.isUpdating)
            {
                console.log("pressed");
                this.pickingDate = !this.pickingDate;
            }
            
        },
        handleDateClick(arg) {
            if (confirm("Update event date to this date?"))
            {
                this.isCreating = true;
                console.log(arg.dateStr + " date clicked");
                this.eventDate = arg.date.toLocaleString('en-us', {dateStyle: "short"});
                console.log(this.eventDate);
                this.pickingDate = !this.pickingDate;
                //console.log(this.$store.state.events);
                //this.$router.push({name: "CreateNewEvent", props: { eventDate: arg.dateStr}});
            }
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            //this.$router.push({name: "ViewClasses"});
            this.$router.back();
        },
    },
    async created() {
        await axios({
            method: 'GET',
            url: `/api/events/${this.$route.params.eventId}`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here")
            console.log(response.data);
            this.eventId = response.data.eventId;
            this.userId = response.data.userId;
            this.eventDescription = response.data.event_description;
            this.eventDate = response.data.event_date;
            this.eventCreatedOn = response.data.event_created_on;
            return;
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
            return;
        });
    }
}
</script>


<style lang="scss" scoped>
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
</style>