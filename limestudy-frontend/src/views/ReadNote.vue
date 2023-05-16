<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for reading specified note
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="post-view">
        <div class="container quillWrapper">
            <h2>{{ noteName }}</h2>
            <!-- <img :src="blogCoverPhoto" alt="" /> -->
            <div class="post-content ql-editor" v-html="noteBody">

            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "ReadNote",
    data() {
        return {
            noteBody: "",
            noteName: "",
            noteCreatedOn: null,
            noteCreatedOnTemp: null,
            classId: null,
            userId: null,
            noteId: null
        }
    },
    computed: {

    },
    methods: {

    },
    async created() {
        await axios({
            method: 'GET',
            url: `/api/notes/${this.$route.params.noteId}`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here")
            this.noteBody = response.data.note_body;
            this.noteName = response.data.note_name;
            this.noteCreatedOn = response.data.flashcard_created_on;
            this.noteCreatedOnTemp = new Date(response.data.flashcard_created_on).toLocaleString('en-us', {dateStyle: "long"})
            this.classId = response.data.class_id;
            this.userId = response.data.user_id;
            this.noteId = response.data.note_id;
            return;
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
            return;
        });
    },
}
</script>

<style lang="scss">
.post-view
{
    min-height: 100%;

    .container
    {
        max-width: 1000px;
        padding: 60px 25px;
    }

    .ql-editor
    {
        padding: 0;
    }

    h2
    {
        margin-bottom: 16px;
        font-weight: 300;
        font-size: 32px;
    }

    img
    {
        width: 100%;
        margin-bottom: 32px;
    }
}
</style>