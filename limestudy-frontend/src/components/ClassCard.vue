<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    Component for Class card, which is small card to view Class information
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="card">
        <div v-show="getEditClass" class="icons">
            <div @click="editClass" class="icon">
                <Edit class="edit" />
            </div>
            <div @click="deleteClass" class="icon">
                <Delete class="delete" />
            </div>
        </div>
        <!--<img :src="post.blogCoverPhoto" alt="">-->
        <div class="card-info">
            <h4>{{ classes.class_name }}</h4>
            <h6>Created on: {{ new Date(classes.class_created_on).toLocaleString('en-us', {dateStyle: "long"}) }}</h6> <!-- new Date(post.blogDate).toLocaleString('en-us', {dateStyle: "long"}) -->
            <router-link class="link" :to="{name: 'ViewDecks', params: { classId: classes.classId }}">View Class Decks <Arrow class="arrow" /></router-link> <!--:to="{name: 'ViewBlog', params: {blogid: this.post.blogID}}"-->
        </div>
    </div>
</template>

<script>
import Arrow from "../assets/Icons/arrow-right-light.svg"
import Edit from "../assets/Icons/edit-regular.svg"
import Delete from "../assets/Icons/trash-regular.svg"
import axios from "axios"

export default {
    name: "ClassCard",
    props: ["classes"],
    components: {
        Arrow,
        Edit,
        Delete
    },
    methods: {
        async deleteClass() {
            //console.log("classId from ClassCard: " + this.classes.classId)
            //this.$store.dispatch("deleteClass", this.classes.classId);
            //console.log("Payload from store: " + classId)
            if (confirm("Do you really want to delete?"))
            {
                await axios({
                    method: 'DELETE',
                    url: `/api/classes/${this.classes.classId}`,
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('user'),
                        'Content-Type': 'application/json'
                    },
                })
                .then((response) => {
                    console.log(response)
                    console.log("Class deleted");

                })
                .catch((err) => {
                    console.log(err);
                    console.log("Class not deleted");
                })
                this.$router.go();
            }
        },
        editClass() {
            this.$router.push({ name: 'UpdateClass', params: { classId: this.classes.classId } });
        },
    },
    computed: {
        getEditClass() {
            return this.$store.state.editClass;
        },
    }
}
</script>


<style lang="scss" scoped>
.card
{
    position: relative;
    //cursor: pointer;
    display: flex;
    flex-direction: column;
    border-radius: 8px;
    background-color: #fff;
    min-height: 225px; // original was 420px
    transition: .5s ease all;

    &:hover
    {
        transform: rotateZ(-1deg) scale(1.01);
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    }

    .icons
    {
        display: flex;
        position: absolute;
        top: 10px;
        right: 10px;
        z-index: 99;

        .icon
        {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 35px;
            height: 35px;
            border-radius: 50%;
            background-color: #fff;
            transition: .5s ease all;

            &:hover
            {
                background-color: var(--lime);


                .edit,
                .delete
                {
                    path
                    {
                        fill: #fff;
                    }
                }
            }

            &:nth-child(1)
            {
                margin-right: 8px;
            }

            .edit,
            .delete
            {
                pointer-events: none;
                height: 15px;
                width: auto;
            }
        }
    }

    img
    {
        display: block;
        border-radius: 8px 8px 0 0;
        z-index: 1;
        width: 100%;
        min-height: 200px;
        object-fit: cover;
    }

    .card-info
    {
        display: flex;
        flex-direction: column;
        height: 100%;
        z-index: 3;
        padding: 32px 16px;
        color: #000;

        h4
        {
            padding-bottom: 8px;
            font-size: 20px;
            font-weight: 300;
        }

        h6
        {
            font-weight: 400;
            font-size: 12px;
            padding-bottom: 16px;
        }

        .link
        {
            display: inline-flex;
            align-items: center;
            margin-top: auto;
            font-weight: 500;
            padding-top: 20px;
            font-size: 12px;
            padding-bottom: 4px;
            transition: .5 ease-in all;

            &:hover
            {
                color: rgba(48, 48, 48, 0.8);
            }

            .arrow
            {
                width: 10px;
            }
        }
    }
}
</style>