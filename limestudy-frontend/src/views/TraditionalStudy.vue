<template>
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <div class="editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" disabled /> <!-- @image-added="imageHandler" -->
            </div>
            <div class="editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" disabled />
            </div>
            <div class="blog-actions">
                <button @click.prevent="flashcardResponse" id="good">Good</button>
                <button @click.prevent="flashcardResponse" id="ok">Ok</button>
                <button @click.prevent="flashcardResponse" id="bad">Bad</button>
                <!--<router-link class="router-button" :to="{name: 'BlogPreview'}">Post Preview</router-link>-->
            </div>
        </div>
    </div>
</template>

<script>
import Quill from "quill";
import "firebase/storage";
import BlogCoverPreview from "../components/BlogCoverPreview.vue";
import Loading from "../components/Loading";


window.Quill = Quill;


export default {
    name: "TraditionalStudy",
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
            flashcards: null,
            editorSettingsQuestion: {
                modules: {
                    toolbar: false
                },
            },
            editorSettingsAnswer: {
                modules: {
                    toolbar: false
                },
            },
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
        getRandomCard() {
            let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
            this.question = this.$store.state.flashcards[index].question;
            this.answer = this.$store.state.flashcards[index].answer;
        },
        flashcardResponse() {
            this.changeCardSide();
            setTimeout(() => {
                this.changeCardSide();
                this.getRandomCard();
            },5000);

        },
    },
    computed: {

    },
    created() {
        //this.$store.dispatch("getUserClassDeckFlashcards", {deckId: this.$route.params.deckId, classId: this.$route.params.classId});
        let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
        this.question = this.$store.state.flashcards[index].question;
        this.answer = this.$store.state.flashcards[index].answer;
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
        ::-webkit-scrollbar
        {
            display: none;
        }


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
        display: flex;
        justify-content: center;

        button
        {
            margin-right: 16px;
            flex: .15;
        }
    }
}
button#good
{
    background-color: green;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#good:hover
{
    background-color: rgba(1, 50, 32, 0.7);
}
button#ok
{
    background-color: yellow;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#ok:hover
{
    background-color: rgba(155, 135, 12, 0.7);
}
button#bad
{
    background-color: red;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#bad:hover
{
    background-color: rgba(139, 0, 0, 0.7);
}
</style>