<template>
    <div class="profile">
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="container">
            <h2>Create New Class</h2>
            <div class="profile-info">
                <div class="initials">{{ $store.state.profileInitials }}</div>
                <!--<div class="admin-badge">
                    <adminIcon class="icon" />
                    <span>Admin</span>
                </div>-->
                <div class="input">
                    <label for="userId">User Id: </label>
                    <input type="text" id="userId" v-model="userId" disabled>
                </div>
                <div class="input">
                    <label for="className">Class Name: </label>
                    <input type="text" id="className" v-model="className">
                </div>
                <button @click="createNewClass">Submit</button>
            </div>
        </div>
    </div>
</template>

<script>
import Modal from "../components/Modal";
import axios from "axios"
//import adminIcon from "../assets/Icons/user-crown-light.svg";

export default {
    name: "CreateNewClass",
    components: {
        Modal,
        //adminIcon,
    },
    data() {
        return {
            modalMessage: [],
            modalActive: null,
            className: "",
        };
    },
    methods: {
        async createNewClass() {
            await axios({
                method: 'POST',
                url: "/api/classes",
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    class_name: this.className,
                    class_created_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"})
                }
            })
            .then((response) => {
                console.log(response)
                console.log("Class created");
                return 1;
            })
            .catch((err) => {
                console.log(err);
                console.log("Class not created");
                return 0;
            })
            let modalMessage1 = "Class created!";
            this.modalMessage.push(modalMessage1);
            this.modalActive = !this.modalActive;
        },
        closeModal() {
            this.modalMessage = [];
            this.modalActive = !this.modalActive;
            //this.$router.push({name: "ViewClasses"});
            this.$router.back();
        },
    },
    computed: {
        userId: {
            get() {
                return this.$store.state.profileUserId
            }
        },
    },
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

            button
            {
                align-self: center;
            }
        }
    }
}
</style>