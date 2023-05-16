<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for Practice Mode functionality
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
                <button class="back-button" @click.prevent="backButton">Back</button>
                <h3>Practice Mode for {{ this.deckName }} Deck</h3>
                <button class="help-box" @click.prevent="openHelpBox">Help</button>
            </div>
            <div class="editor">
                <div v-if="success" class="card">
                    <div class="card-inner">
                        <div class="card-face card-face-front">
                            <p v-html="this.keyCard.question"></p>
                        </div>
                        <div class="card-face card-face-back">
                            <p v-html="this.keyCard.answer"></p>
                        </div>
                    </div>
                </div>
                <h2 v-else>None and/or not enough cards to make practice set</h2>
            </div>
            <div class="choices" v-if="success && active">
                <div class="choice" @click.prevent="cardClicked1">
                    <div class="card">
                        <div class="card-inner">
                            <div class="card-face card-face-front">
                                <p v-html="this.practiceFlashcards[0].answer"></p>
                            </div>
                            <div class="card-face card-face-back">
                                <p v-html="this.practiceFlashcards[0].question"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="choice" @click.prevent="cardClicked2">
                    <div class="card">
                        <div class="card-inner">
                            <div class="card-face card-face-front">
                                <p v-html="this.practiceFlashcards[1].answer"></p>
                            </div>
                            <div class="card-face card-face-back">
                                <p v-html="this.practiceFlashcards[1].question"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="choice" @click.prevent="cardClicked3">
                    <div class="card">
                        <div class="card-inner">
                            <div class="card-face card-face-front">
                                <p v-html="this.practiceFlashcards[2].answer"></p>
                            </div>
                            <div class="card-face card-face-back">
                                <p v-html="this.practiceFlashcards[2].question"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="choice" @click.prevent="cardClicked4">
                    <div class="card">
                        <div class="card-inner">
                            <div class="card-face card-face-front">
                                <p v-html="this.practiceFlashcards[3].answer"></p>
                            </div>
                            <div class="card-face card-face-back">
                                <p v-html="this.practiceFlashcards[3].question"></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div :class="{invisible: active}" class="err-message" v-else-if="!active">
                <button @click.prevent="getRandomCards">Get Next Card</button>
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
import axios from "axios";


window.Quill = Quill;


export default {
    name: "MultiChoiceStudy",
    components: {
        BlogCoverPreview,
        Loading,
        Modal,
    },
    data() {
        return {
            file: null,
            success: null,
            error: null,
            errorMessage: null,
            loading: null,
            showQuestion: true,
            showAnswer: false,
            newGame: true,
            deckName: null,
            practiceFlashcards: [],
            keyCard: null,
            active: true,
            modalMessage: [],
            modalActive: null,
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
        async getKeyCard() {
            let index = Math.floor(Math.random() * 4);
            this.keyCard = this.practiceFlashcards[index];
            console.log(this.keyCard.question);
        },
        async getRandomCards() {
            this.keyCard = null;
            this.active = true;
            this.loading = true;
            this.success = null;
            this.practiceFlashcards = [];
            await axios({
                method: 'GET',
                url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards/practice`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("practice set \n" + response.data);
                if (response.data)
                {
                    response.data.forEach((practiceFlashcard) => {
                        const newFlashcard = {
                            flashcardId: practiceFlashcard.flashcardId,
                            deckId: practiceFlashcard.deckId,
                            classId: practiceFlashcard.classId,
                            userId: practiceFlashcard.userId,
                            question: practiceFlashcard.question,
                            answer: practiceFlashcard.answer,
                            flashcard_created_on: practiceFlashcard.flashcard_created_on,
                            correct: practiceFlashcard.correct,
                            incorrect: practiceFlashcard.incorrect,
                            last_studied_on: practiceFlashcard.last_studied_on,
                            occurrence_rate: practiceFlashcard.occurrence_rate,
                            occurrence_rate_input: practiceFlashcard.occurrence_rate_input
                        }
                        this.practiceFlashcards.push(newFlashcard);
                    });
                    console.log(this.practiceFlashcards);
                    this.success = true;
                    this.getKeyCard();
                    this.turnCards();
                }
                else
                {
                    console.log("None and/or not enough cards to make practice set");
                    this.success = false;
                }
                return;
            }).catch((err) => {
                console.log("practice set error");
                console.log(err);
                return;
            });
            //this.turnCards();
            this.loading = false;
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
        cardClicked1() {
            console.log(this.keyCard.flashcardId === this.practiceFlashcards[0].flashcardId);
            const card = document.querySelectorAll('.card-inner')[1];
            if (card.classList.length < 2)
            {
                card.classList.toggle('is-flipped');
            }
            if (this.keyCard.flashcardId === this.practiceFlashcards[0].flashcardId)
            {
                const card = document.querySelectorAll('.card-inner')[0];
                card.classList.toggle('is-flipped');
                this.active = false;
            }
        },
        cardClicked2() {
            console.log(this.keyCard.flashcardId === this.practiceFlashcards[1].flashcardId);
            const card = document.querySelectorAll('.card-inner')[2];
            if (card.classList.length < 2)
            {
                card.classList.toggle('is-flipped');
            }
            if (this.keyCard.flashcardId === this.practiceFlashcards[1].flashcardId)
            {
                const card = document.querySelectorAll('.card-inner')[0];
                card.classList.toggle('is-flipped');
                this.active = false;
            }
        },
        cardClicked3() {
            console.log(this.keyCard.flashcardId === this.practiceFlashcards[2].flashcardId);
            const card = document.querySelectorAll('.card-inner')[3];
            if (card.classList.length < 2)
            {
                card.classList.toggle('is-flipped');
            }
            if (this.keyCard.flashcardId === this.practiceFlashcards[2].flashcardId)
            {
                const card = document.querySelectorAll('.card-inner')[0];
                card.classList.toggle('is-flipped');
                this.active = false;
            }
        },
        cardClicked4() {
            console.log(this.keyCard.flashcardId === this.practiceFlashcards[3].flashcardId);
            const card = document.querySelectorAll('.card-inner')[4];
            if (card.classList.length < 2)
            {
                card.classList.toggle('is-flipped');
            }
            if (this.keyCard.flashcardId === this.practiceFlashcards[3].flashcardId)
            {
                const card = document.querySelectorAll('.card-inner')[0];
                card.classList.toggle('is-flipped');
                this.active = false;
            }
        },
        openHelpBox() {
            let temp = "Click one of the four choice below that you think is the correct answer. " + 
                                "You will be allowed to click until you have guessed correctly.";
            this.modalMessage.push(temp);
            this.modalActive = true;
        },
        backButton() {
            //this.$router.back()
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
        this.practiceFlashcards = [];
        await axios({
            method: 'GET',
            url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards/practice`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("practice set");
            console.log(response.data.length);
            if (response.data.length > 0)
            {
                response.data.forEach((practiceFlashcard) => {
                    const newFlashcard = {
                        flashcardId: practiceFlashcard.flashcardId,
                        deckId: practiceFlashcard.deckId,
                        classId: practiceFlashcard.classId,
                        userId: practiceFlashcard.userId,
                        question: practiceFlashcard.question,
                        answer: practiceFlashcard.answer,
                        flashcard_created_on: practiceFlashcard.flashcard_created_on,
                        correct: practiceFlashcard.correct,
                        incorrect: practiceFlashcard.incorrect,
                        last_studied_on: practiceFlashcard.last_studied_on,
                        occurrence_rate: practiceFlashcard.occurrence_rate,
                        occurrence_rate_input: practiceFlashcard.occurrence_rate_input
                    }
                    this.practiceFlashcards.push(newFlashcard);
                });
                console.log(this.practiceFlashcards);
                this.success = true;
                this.getKeyCard();
                //this.cardClicked();
            }
            else
            {
                console.log("None and/or not enough cards to make practice set");
                this.success = false;
            }
        }).catch((err) => {
            console.log("practice set error")
            console.log(err);
            this.success = false;
        });
        window.location.hash = "#focused";
        //let focused = document.getElementById("focused");
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
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
        this.newGame = true;
        this.modalMessage = [];
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
        background-color: #FFF;
        opacity: 1;
        transition: .5s ease all;
        display: flex;
        justify-content: center;

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
            flex: .95;
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
.choices
{
    display: flex;
    justify-content: space-between;
    margin-top: 5px;
    height: 40vh;
}
.choice
{
    flex: 1;
    display: flex;
    flex-direction: column;
    &:nth-child(1)
    {
        margin-right: 10px;
    }
    &:nth-child(2)
    {
        margin-right: 10px;
    }
    &:nth-child(3)
    {
        margin-right: 10px;
    }
    .select-button
    {
        margin-top: 10px;
    }
}
.card
{
    //margin: 15px auto 0;
    width: 400px;
    height: 600px;
    max-width: 300px;
    max-height: 200px;
}
.card-inner 
{
    width: 100%;
    height: 100%;
    transition: transform 1s;
    transform-style: preserve-3d;
    cursor: pointer;
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
    background-image: linear-gradient(to bottom right, var(--lime), var(--light));
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
a
{
    text-decoration: none;
}
</style>