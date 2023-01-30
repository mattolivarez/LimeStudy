<template>
    <div class="profile">
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="container">
            <h2>Update {{ this.className }} Class</h2>
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
                    <label for="classId">Class Id: </label>
                    <input type="text" id="classId" v-model="classId" disabled>
                </div>
                <div class="input">
                    <label for="classCreatedOnTemp">Class Created On: </label>
                    <input type="text" id="classCreatedOnTemp" v-model="classCreatedOnTemp" disabled>
                </div>
                <div class="input">
                    <label for="className">Class Name: </label>
                    <input type="text" id="className" v-model="className">
                </div>
                <button @click="updateClass">Submit</button>
            </div>
        </div>
    </div>
</template>

<script>
import Modal from "../components/Modal";
import axios from "axios"
//import adminIcon from "../assets/Icons/user-crown-light.svg";

export default {
    name: "Profile",
    //props: ["selected"],
    components: {
        Modal,
        //adminIcon,
    },
    data() {
        return {
            modalMessage: "Class updated!",
            modalActive: null,
            className: "",
            classCreatedOn: null,
            classId: null,
            userId: null,
            classCreatedOnTemp: null,
        };
    },
    methods: {
        async updateClass() {
            await axios({
                method: 'PUT',
                url: `/api/classes/${this.$route.params.classId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    class_name: this.className,
                    class_created_on: this.classCreatedOn,
                    classId: this.classId,
                    userId: this.userId,
                }
            })
            .then((response) => {
                console.log(response)
                console.log("Class updated");

            })
            .catch((err) => {
                console.log(err);
                console.log("Class not updated");
            })
            this.modalActive = !this.modalActive;
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            this.$router.push({name: "ViewClasses"});
        },
    },
    async created() {
        await axios({
                method: 'GET',
                url: `/api/classes/${this.$route.params.classId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log(response)
                this.className = response.data.class_name;
                this.classCreatedOn = response.data.class_created_on;
                this.classCreatedOnTemp = new Date(response.data.class_created_on).toLocaleString('en-us', {dateStyle: "long"})
                this.classId = response.data.classId;
                this.userId = response.data.userId;
            })
            .catch((err) => {
                console.log(err)
            })
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