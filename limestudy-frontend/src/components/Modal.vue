<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    Component for Modal, which displays small pop-up with information that is passed in
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="modal">
        <div class="modal-content">
            <p v-for="(modalMessage, i) in modalMessage" :key="i">{{ modalMessage }}</p>
            <button @click="closeModal">Close</button>
        </div>
    </div>
</template>


<script>

export default {
    props: ["modalMessage"],
    methods: {
        closeModal() {
            this.$emit("close-modal");
        },
    },
};
</script>


<style lang="scss" scoped>
.modal
{
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 101;
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    background-color: rgba(0, 0, 0, 0.7);

    .modal-content
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        border-radius: 8px;
        width: 300px;
        padding: 40px 30px;
        background-color: #fff;

        p
        {
            text-align: center;
            margin-bottom: 7px;
        }

        button
        {
            margin-top: 7px;
            align-self: center;
        }
    }
}
</style>