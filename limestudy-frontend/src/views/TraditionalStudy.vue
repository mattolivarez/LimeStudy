<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for study mode
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="container">
            <!-- <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div> -->
            <div class="title-message" id="focused">
                <button v-if="backEnabled" class="back-button" @click.prevent="backButton">Back</button>
                <h3>Study Mode for {{ this.deckName }} Deck ({{ count }} / {{ this.studyFlashcards.length }})</h3>
                <button class="help-box" @click.prevent="openHelpBox">Help</button>
            </div>
            <!-- <div class="editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" disabled /> @image-added="imageHandler" -->
            <!--</div>
            <div class="editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" disabled />
            </div> -->
            <div class="editor">
                <h3 v-if="!flipped && success">Question being viewed</h3>
                <h3 v-if="flipped && success">Answer being viewed</h3>
                <div v-if="success" class="card">
                    <div class="card-inner" @click.prevent="userFlip">
                        <div class="card-face card-face-front">
                            <p v-html="currentFlashcard.question"></p>
                        </div>
                        <div class="card-face card-face-back">
                            <p v-html="currentFlashcard.answer"></p>
                        </div>
                    </div>
                </div>
                <h2 v-else>Today's flashcard set completed!</h2>
            </div>
            
            <div class="blog-actions">
                <!-- <button v-if="!cardAnswered">View Relevant Note</button> -->
                <button v-if="!cardAnswered && success" @click.prevent="readyToAnswer">Click to see the answer</button>
                <button v-if="cardAnswered && !rateSelected" @click.prevent="showLess">Show less often</button> 
                <button v-if="cardAnswered && !rateSelected" @click.prevent="showSame">Show at same rate</button>
                <button v-if="cardAnswered && !rateSelected" @click.prevent="showMore">Show more often</button>
                <button v-if="cardAnswered && rateSelected" @click.prevent="showNextCard">Get Next Card</button>
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
import Modal from "../components/Modal";
// import StudyCard from "../components/StudyCard";
import axios from 'axios';

window.Quill = Quill;

// const card = document.querySelector('.card-inner')
// card.addEventListener('click', () => {
//     card.classList.toggle('is-flipped');
// })

export default {
    name: "TraditionalStudy",
    components: {
        BlogCoverPreview,
        Loading,
        Modal,
        // StudyCard
    },
    data() {
        return {
            studyFlashcards: [],
            currentFlashcard: null,
            count: 0,
            showQuestion: null,
            showAnswer: null,
            loading: false,
            success: null,
            error: null,
            errorMessage: "",
            cardAnswered: false,
            rateSelected: false,
            flipped: false,
            existingSession: null,
            deckName: "",
            modalActive: false,
            modalMessage: [],
            final: false,
            backEnabled: true,
            // editorSettingsQuestion: {
            //     modules: {
            //         toolbar: false
            //     },
            // },
            // editorSettingsAnswer: {
            //     modules: {
            //         toolbar: false
            //     },
            // },
        };
    },
    methods: {
        // changeCardSide() {
        //     this.showQuestion = !this.showQuestion;
        //     this.showAnswer = !this.showAnswer;
        // },
        // getRandomCard() {
        //     let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
        //     this.question = this.$store.state.flashcards[index].question;
        //     this.answer = this.$store.state.flashcards[index].answer;
        // },
        readyToAnswer() {
            this.cardAnswered = !this.cardAnswered;
            this.flipCard();
        },
        getNextCard() {
            if (this.count < this.studyFlashcards.length)
            {
                this.currentFlashcard = this.studyFlashcards[this.count];
                this.count++;
            }
            else
            {
                this.currentFlashcard = null;
                this.success = false;
            }
        },
        flipCard() {
            const card = document.querySelector('.card-inner');
            card.classList.toggle('is-flipped');
            this.flipped = !this.flipped;
        },
        showNextCard() {
            this.loading = true;
            console.log(this.currentFlashcard);
            this.$store.dispatch("updateUserFlashcard", { flashcard: this.currentFlashcard });
            this.cardAnswered = false;
            this.rateSelected = false;
            this.final = false;
            this.flipped = false;
            this.getNextCard();
            this.backEnabled = true;
            setTimeout(() => {
                this.loading = false;
            }, 1000);
        },
        turnCards() {
            const cards = document.querySelectorAll('.card-inner');
            for (let i = 0; i < cards.length; i++)
            {
                if (cards[i].classList.length > 1)
                {
                    cards[i].classList.toggle('is-flipped');
                }
            }
        },
        userFlip() {
            if (this.cardAnswered && !this.final) 
            {
                this.flipCard();
            }
        },
        async showLess() {
            this.loading = true;
            this.currentFlashcard.correct += 1;
            this.currentFlashcard.occurrence_rate_input += 1;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.existingSession.session_correct += 1;
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 1, incorrect: 0 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            this.cardAnswered = true;
            //this.flipCard();
            this.turnCards();
            this.final = true;
            this.backEnabled = false;
            this.rateSelected = true;
            this.flipped = !this.flipped;
            this.loading = false;
        },
        async showSame() {
            this.loading = true;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 0, incorrect: 0 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            this.cardAnswered = true;
            //this.flipCard();
            this.turnCards();
            this.final = true;
            this.backEnabled = false;
            this.rateSelected = true;
            this.flipped = !this.flipped;
            this.loading = false;
        },
        async showMore() {
            this.loading = true;
            this.currentFlashcard.incorrect += 1;
            this.currentFlashcard.occurrence_rate_input -= 1;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.existingSession.session_incorrect += 1;
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 0, incorrect: 1 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            this.cardAnswered = true;
            //this.flipCard();
            this.turnCards();
            this.final = true;
            this.backEnabled = false;
            this.rateSelected = true;
            this.flipped = !this.flipped;
            this.loading = false;
        },
        async checkForExistingSession() {
            await axios({
                method: 'GET',
                url: `/api/classes/${this.currentFlashcard.classId}/decks/${this.currentFlashcard.deckId}/flashcards/${this.currentFlashcard.flashcardId}/sessions/check-for-session`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                console.log(response.data)
                this.existingSession = {
                    sessionId: response.data.sessionId,
                    flashcardId: response.data.flashcardId,
                    deckId: response.data.deckId,
                    classId: response.data.classId,
                    userId: response.data.userId,
                    session_date: response.data.session_date,
                    session_correct: response.data.session_correct,
                    session_incorrect: response.data.session_incorrect
                }
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                this.existingSession = null;
            });
        },
        openHelpBox() {
            let modalMessage1 = "1. Once you think you know the answer, click to see it.";
            let modalMessage2 = "2. Then click one of the options to change how often you see that flashcard.";
            let modalMessage3 = "3. Then click to get another flashcard(the flashcard can continue to be flipped if necessary).";
            this.modalMessage.push(modalMessage1);
            this.modalMessage.push(modalMessage2);
            this.modalMessage.push(modalMessage3);
            this.modalActive = true;
        },
        backButton() {
            this.$router.push({name: 'ViewFlashcards', params: {classId: this.$route.params.classId, deckId: this.$route.params.deckId}})
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            this.modalMessage = [];
        },
    },
    computed: {

    },
    async created() {
        this.loading = true;
        this.studyFlashcards = [];
        await axios({
            method: 'GET',
            url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards/traditional-study`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here");
            console.log(response.data.length);
            if (response.data.length > 0)
            {
                response.data.forEach((studySetFlashcard) => {
                    const newFlashcard = {
                        flashcardId: studySetFlashcard.flashcardId,
                        deckId: studySetFlashcard.deckId,
                        classId: studySetFlashcard.classId,
                        userId: studySetFlashcard.userId,
                        question: studySetFlashcard.question,
                        answer: studySetFlashcard.answer,
                        flashcard_created_on: studySetFlashcard.flashcard_created_on,
                        correct: studySetFlashcard.correct,
                        incorrect: studySetFlashcard.incorrect,
                        last_studied_on: studySetFlashcard.last_studied_on,
                        occurrence_rate: studySetFlashcard.occurrence_rate,
                        occurrence_rate_input: studySetFlashcard.occurrence_rate_input
                    }
                    this.studyFlashcards.push(newFlashcard);
                });
                this.success = true;
                console.log(this.studyFlashcards);
                this.getNextCard();
                console.log(this.currentFlashcard);
            }
            else
            {
                this.success = false;
            }
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
            this.success = false;
        });
        console.log(this.success);
        await axios({
            method: 'GET',
            url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here")
            this.deckName = response.data.deck_name; 
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
        });
        this.loading = false;
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
        if (this.flipped)
        {
            this.flipCard();
            this.flipped = false;
        }
        this.cardAnswered = false;
        this.existingSession = null;
        this.rateSelected = false;
        this.modalMessage = [];
        this.count = 0;
    },
};
</script>


<style lang="scss" scoped>
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

    .title-message
    {
        width: 100%;
        padding: 12px 12px 0 12px;
        border-radius: 8px;
        color: #000;
        //margin-bottom: 10px;
        background-color: #FFF;
        opacity: 1;
        transition: .5s ease all;
        display: flex;
        justify-content: flex-end;
        align-items: center;

        h3
        {
            flex: .9;
            font-size: 24px;
            text-align: center;
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
        justify-content: center;
        align-items: center;

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
.card
{
    margin: 0 auto;
    margin-top: 10px;
    width: 700px;
    height: 350px;
}
.card-inner 
{
    width: 100%;
    height: 100%;
    transition: transform 1s;
    transform-style: preserve-3d;
    //cursor: pointer;
    position: relative;
}
.card-inner.is-flipped
{
    transform: rotateY(180deg);
}
.card-face
{
    position: absolute;
    width: 100%;
    height: 100%;
    backface-visibility: hidden;
    -webkit-backface-visibility: hidden;
    overflow: hidden;
    border-radius: 16px;
    box-shadow: 0px 3px 18px 3px rgba(0, 0, 0, 0.2);
    //text-transform: uppercase;
    text-align: center;
}
.card-face-front
{
    background-image: linear-gradient(to bottom right, var(--lime), var(--light));
    display: flex;
    align-items: center;
    justify-content: center;
}
.card-face-front p
{
    color: #000;
    font-size: 32px;
}
.card-face-back
{
    background-image: linear-gradient(to bottom right, var(--light), var(--darkLime));
    transform: rotateY(180deg);
    display: flex;
    align-items: center;
    justify-content: center;
}
.card-face-back p
{
    color: #000;
    font-size: 32px;
}
</style>