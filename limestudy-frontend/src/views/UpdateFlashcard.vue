<template>
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <div class="blog-info">
                <input type="text" placeholder="Enter Flashcard Tags..." /> <!--v-model="blogTitle"-->
                <div class="upload-file">
                    <!--<label for="blog-photo">Upload Cover Photo</label>-->
                    <!--<input type="file" ref="blogPhoto" id="blog-photo" @change="fileChange" accept=".png, .jpg, .jpeg" />-->
                    <button class="preview" :class="{'button-inactive': showQuestion}" @click="changeCardSide">Question</button>
                    <button class="preview" :class="{'button-inactive': showAnswer}" @click="changeCardSide">Answer</button>
                    <!--<span>File Chosen: {{ this.$store.state.blogPhotoName }}</span>-->
                </div>
            </div>
            <div class="editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" useCustomImageHandler @image-added="imageHandler" /> <!-- @image-added="imageHandler" -->
            </div>
            <div class="editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" useCustomImageHandler @image-added="imageHandler" />
            </div>
            <div class="blog-actions">
                <button @click="updateFlashcard">Update</button>
                <!--<router-link class="router-button" :to="{name: 'BlogPreview'}">Post Preview</router-link>-->
            </div>
        </div>
    </div>
</template>


<script>
import Quill from "quill";
import firebase from "firebase/app";
import "firebase/storage";
import BlogCoverPreview from "../components/BlogCoverPreview.vue";
import Loading from "../components/Loading";
import axios from "axios"

window.Quill = Quill;
const ImageResize = require("quill-image-resize-module").default;
Quill.register("modules/imageResize", ImageResize);

export default {
    name: "UpdateFlashcard",
    components: {
        BlogCoverPreview,
        Loading,
    },
    data() {
        return {
            file: null,
            error: null,
            errorMessage: null,
            loading: null,
            showQuestion: true,
            showAnswer: false,
            question: "",
            answer: "",
            userId: null,
            classId: null,
            deckId: null,
            flashcardId: null,
            flashcardCreatedOn: null,
            flashcardCreatedOnTemp: null,
            correct: null,
            incorrect: null,
            last_studied_on: null,
            occurrence_rate: null,
            occurence_rate_input: null,
            editorSettingsQuestion: {
                modules: {
                    imageResize: {},
                },
            },
            editorSettingsAnswer: {
                modules: {
                    imageResize: {},
                },
            },
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
        imageHandler(file, Editor, cursorLocation, resetUploader) {
            const storageRef = firebase.storage().ref();
            const docRef = storageRef.child(`documents/blogPostPhoto/${file.name}`);
            docRef.put(file).on("state_changed", (snapshot) => {
                console.log(snapshot)
            }, (err) => {
                console.log(err);
            }, async () => {
                const downloadURL = await docRef.getDownloadURL();
                Editor.insertEmbed(cursorLocation, "image", downloadURL);
                resetUploader();
            });
        },
        async updateFlashcard() {
            if (this.question.length !== 0 && this.answer.length !== 0)
            {
                this.loading = true;
                await axios({
                    method: 'PUT',
                    url: `/api/classes/${this.classId}/decks/${this.deckId}/flashcards/${this.flashcardId}`,
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('user'),
                        'Content-Type': 'application/json'
                    },
                    data: {
                        question: this.question,
                        answer: this.answer,
                        flashcard_created_on: this.flashcardCreatedOn,
                        userId: this.userId,
                        classId: this.classId,
                        deckId: this.deckId,
                        flashcardId: this.flashcardId,
                        correct: this.correct,
                        incorrect: this.incorrect,
                        last_studied_on: this.last_studied_on,
                        occurrence_rate: this.occurrence_rate,
                        occurence_rate_input: this.occurence_rate_input
                    }
                })
                .then((response) => {
                    console.log(response)
                    console.log("Flashcard updated");
                })
                .catch((err) => {
                    console.log(err);
                    console.log("Flashcard not update");
                    //console.log(JSON.stringify(this.question))
                    //console.log(this.answer)
                })
                this.loading = false;
                this.$router.push({name: "ViewFlashcards", params: {classId: this.$route.params.classId, deckId: this.$route.params.deckId}});
                return;
            }
            this.error = true;
            this.errorMessage = "Please ensure question and answer are both filled out!";
            setTimeout(() => {
                this.error = false;
            }, 5000);
            return;
        },
    },
    computed: {

    },
    async created() {
        await axios({
                method: 'GET',
                url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards/${this.$route.params.flashcardId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log(response)
                this.question = response.data.question;
                this.answer = response.data.answer;
                this.flashcardCreatedOn = response.data.flashcard_created_on;
                this.flashcardCreatedOnTemp = new Date(response.data.flashcard_created_on).toLocaleString('en-us', {dateStyle: "long"})
                this.classId = response.data.classId;
                this.userId = response.data.userId;
                this.deckId = response.data.deckId;
                this.flashcardId = response.data.flashcardId;
                this.correct = response.data.correct;
                this.incorrect = response.data.incorrect;
                this.last_studied_on = response.data.last_studied_on;
                this.occurrence_rate = response.data.occurrence_rate;
                this.occurence_rate_input = response.data.occurence_rate_input;
            })
            .catch((err) => {
                console.log(err)
            })
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
    },
};
</script>


<style lang="scss">
.create-post
{
    position: relative;
    height: 100%;

    button
    {
        margin-top: 0;
    }

    .router-button
    {
        text-decoration: none;
        color: #fff;
    }

    label,
    button,
    .router-button
    {
        transition: .5s ease-in-out all;
        align-self: center;
        font-size: 14px;
        cursor: pointer;
        border-radius: 20px;
        padding: 12px 24px;
        color: #fff;
        background-color: #303030;
        text-decoration: none;

        &:hover
        {
            background-color: rgba(48, 48, 48, 0.7);
        }
    }

    .container
    {
        position: relative;
        height: 100%;
        padding: 10px 25px 60px;
    }

    // error styling
    .invisible
    {
        opacity: 0 !important;
    }

    .err-message
    {
        width: 100%;
        padding: 12px;
        border-radius: 8px;
        color: #fff;
        margin-bottom: 10px;
        background-color: #303030;
        opacity: 1;
        transition: .5s ease all;

        p
        {
            font-size: 14px;
        }

        span
        {
            font-weight: 600;
        }
    }

    .blog-info
    {
        display: flex;
        margin-bottom: 32px;

        input:nth-child(1)
        {
            min-width: 300px;
        }

        input
        {
            transition: .5s ease-in-out all;
            padding: 10px 4px;
            border: none;
            border-bottom: 1px solid #303030;

            &:focus
            {
                outline: none;
                box-shadow: 0 1px 0 0 #303030;
            }
        }

        .upload-file
        {
            flex: 1;
            margin-left: 16px;
            position: relative;
            display: flex;

            input
            {
                display: none;
            }

            .preview
            {
                margin-left: 16px;
                text-transform: initial;
            }

            span
            {
                font-size: 12px;
                margin-left: 16px;
                align-self: center;
            }
        }
    }

    .editor
    {
        height: 60vh;
        display: flex;
        flex-direction: column;


        .quillWrapper
        {
            position: relative;
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .ql-container
        {
            display: flex;
            flex-direction: column;
            height: 100%;
            overflow: scroll;
        }

        .ql-editor
        {
            padding: 20px 16px 30px;
        }
    }

    .blog-actions
    {
        margin-top: 32px;

        button
        {
            margin-right: 16px;
        }
    }
}
</style>