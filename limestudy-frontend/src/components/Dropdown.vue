<template>
    <div class="dropdown" @click.prevent="showDropdown">
        <div class="select">
            <span class="selected">0 - None</span>
            <div class="caret"></div>
        </div>
        <ul class="menu">
            <li class="active">0 - None</li>
            <li v-for="(classes, i) in classes" :key="i">{{ classes.classId }} - {{ classes.class_name }}</li>
        </ul>
    </div>
</template>


<script>
//import DownArrow from "../assets/Icons/caret-down-solid.svg";
export default {
    name: 'Dropdown',
    props: {
        selectedId: Number,
    },
    components: {
        //DownArrow,
    },
    data() {
        return {
            isOpen: false,
        }
    },
    methods: {
        showDropdown() {
            const dropdowns = document.querySelectorAll('.dropdown');
            dropdowns.forEach(dropdown => {
                const select = dropdown.querySelector('.select');
                const caret = dropdown.querySelector('.caret');
                const menu = dropdown.querySelector('.menu');
                const options = dropdown.querySelectorAll('.menu li');
                const selected = dropdown.querySelector('.selected');
                select.addEventListener('click', () => {
                    select.classList.toggle('select-clicked');
                    caret.classList.toggle('caret-rotate');
                    menu.classList.toggle('menu-open');
                });
                options.forEach(option => {
                    option.addEventListener('click', () => {
                        selected.innerText = option.innerText;
                        select.classList.remove('select-clicked');
                        caret.classList.remove('caret-rotate');
                        menu.classList.remove('menu-open');
                        options.forEach(option => {
                            option.classList.remove('active');
                        });
                        option.classList.add('active');
                    });
                });
            });
        },
    },
    computed: {
        classes() {
            //console.log(this.$store.state.classes.length)
            return this.$store.state.classes;
        },
    },
    updated() {
        if (this.$props.selectedId !== null)
        {
            const li = document.querySelectorAll('.menu li');
            li.forEach((temp) => {
                let classIdSplit = temp.innerText.split(' ');
                let parsedId = parseInt(classIdSplit[0]);
                //console.log(parsedId);
                //console.log(parsedId === this.$props.selectedId);
                if (parsedId === this.$props.selectedId)
                {
                    const selected = document.querySelector('.selected');
                    selected.innerText = temp.innerText;
                    console.log(selected.innerText);
                    console.log(temp);
                    li[0].classList.remove('active');
                    temp.classList.add('active');
                }
            });
        }
        //console.log(this.selectedId); 
    },
    created() {
        this.$store.dispatch("getUserClasses");
    },
}


// <Dropdown :items="Classes"/>
</script>


<style lang="scss" scoped>
body
{
    background: #23242A;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
}
.dropdown
{
    min-width: 15em;
    position: relative;
    //margin: 2em;
}
.dropdown *
{
    box-sizing: border-box;
}
.select
{
    background: #2A2F3B;
    color: #FFF;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 2px #2A2F3B solid;
    border-radius: 0.5em;
    padding: 1em;
    cursor: pointer;
    transition: background 0.3s;
}
.select-clicked
{
    border: 2px #26489A solid;
    box-shadow: 0 0 0.8em #26489A;
}
.select:hover
{
    background: #323741;
}
.caret
{
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 6px solid #FFF;
    transition: 0.3s;
}
.caret-rotate
{
    transform: rotate(180deg);
}
.menu
{
    list-style: none;
    padding: 0.2em 0.5em;
    background: #323741;
    border: 1px #363A43 solid;
    box-shadow: 0 0.5em 1em rgba(0, 0, 0, 0.2);
    border-radius: 0.5em;
    color: #9FA5B5;
    position: absolute;
    top: 3em;
    left: 50%;
    width: 100%;
    transform: translateX(-50%);
    opacity: 0;
    display: none;
    transition: 0.2s;
    z-index: 1;
}
.menu li
{
    padding: 0.7em 0.5em;
    margin: 0.3em 0;
    border-radius: 0.5em;
    cursor: pointer;
}
.menu li:hover
{
    background: #2A2D35;
}
.active
{
    background: #23242A;
}
.menu-open
{
    display: block;
    opacity: 1;
}

</style>