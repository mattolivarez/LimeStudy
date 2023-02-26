<template>
    <div class="card">
        <div class="info">
            <Book class="addIcon" />
            <router-link class="link" :to="{name: 'MultiChoiceStudy', params: { classId: this.$route.params.classId, deckId: this.$route.params.deckId }}">
                Multiple Choice Study <Arrow class="arrow" />
            </router-link>
        </div>
    </div>
</template>

<script>
import Arrow from "../assets/Icons/arrow-right-light.svg"
import Book from "../assets/Icons/book-open-solid.svg"

export default {
    name: "MultiChoiceStudy",
    components: {
        Arrow,
        Book,
    },
    methods: {
        addFlashcard() {
            this.$store.dispatch("addNewFlashcard");
        },
    },
}
</script>


<style lang="scss" scoped>
.card
{
    position: relative;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    border-radius: 8px;
    background-color: #fff;
    min-height: 125px; // original was 420px
    transition: .5s ease all;

    // &:hover
    // {
    //     transform: rotateZ(-1deg) scale(1.01);
    //     box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    // }

    .info
    {
        display: flex;
        flex-direction: column;
        align-items: center; // can delete
        justify-content: center; // can delete
        height: 100%;
        z-index: 3;
        padding: 32px 16px;
        color: #000;

        .addIcon
        {
            max-height: 120px;
            padding-bottom: 8px;
            //font-size: 10px;
            //font-weight: 100;
        }

        .link
        {
            display: inline-flex;
            align-items: center;
            justify-content: center;
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