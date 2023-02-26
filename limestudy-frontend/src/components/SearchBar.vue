<template>
    <div class="search">
        <MagGlass class="glass"/>
        <input type="text" placeholder="Search" @keyup="filterPage">
    </div>
</template>

<script>
import MagGlass from "../assets/Icons/magnifying-glass-solid.svg"

export default {
    name: 'SearchBar',
    components: {
        MagGlass
    },
    methods: {
        filterPage() {
            this.$emit('filter-page');
        }
    },
}
</script>


<style lang="scss" scoped>
// .container{
//     width: 100%;
//     min-height: 100vh;
//     padding: 5%;
//     //background-image: ;
// }
.search
{
    width: 100%;
    max-width: 20%;
    //background-color: rgba(255,255,0,0.2);
    position: relative;
    display: flex;
    flex-direction: row;
    align-items: center;
    //border-radius: 60px;
    padding: 10px 20px;
}
.search input
{
    background: transparent;
    flex: 1;
    border: 0;
    outline: none;
    // padding-top: 10px;
    // padding-bottom: 10px;
    // padding-left: 5px;
    // padding-right: 5px;
    padding: 10px 5px;
    font-size: 20px;
    color: black;
    font-weight: 300;
}
::placeholder
{
    color: black;
}
.search .glass
{
    width: 25px;
    max-width: 18px;
    flex: 1;
    margin-right: 5px;
}
</style>