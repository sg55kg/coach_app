<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte'
    import logo from '../images/logo-yellow.png'
    import {userDB} from "$lib/stores/authStore.js";
    import FaAngleDoubleLeft from 'svelte-icons/fa/FaAngleDoubleLeft.svelte'

    let showNav: boolean = false
</script>


<div class="h-full flex items-center justify-center">
    <div class="w-10 hover:cursor-pointer text-yellow-lt hover:text-yellow" on:click={() => showNav = true}>
        <MdMenu />
    </div>
</div>
<div class="fixed overflow-hidden z-40 overlay inset-0 transform ease-in-out
    {showNav ?
    'transition-opacity opacity-100 duration-300 translate-x-0' :
    'transition-all delay-500 opacity-0 translate-x-full'}"
>
    <div class="w-9/12 lg:w-2/12 left-0 absolute bg-gray-100 h-full shadow-xl delay-200 duration-200 ease-in-out transition-all transform {showNav ? 'translate-x-0' : 'translate-x-[-30em]'}">
        <div class="flex p-2 mt-2 items-center w-full">
            <img class="h-10 m-auto ml-32" src={logo} alt="Coachable Logo">
            <div class="text-yellow-lt hover:text-yellow w-8 hover:cursor-pointer" on:click={() => showNav = false}>
                <FaAngleDoubleLeft />
            </div>
        </div>
        <div class="flex flex-col items-center mt-4">
            <a class="self-start pl-4 font-medium text-lg"
               href="/home/coach"
            >
                <h3>My Teams</h3>
            </a>
            <hr class="w-full h-1 my-2 text-gray-400">
            {#each $userDB.coachData.teams as team}
                <a class="text-textblue hover:text-link text-md font-medium hover:cursor-pointer" href="/home/coach/team/{team.id}">
                    {team.name}
                </a>
            {/each}
            <h3 class="self-start pl-4 font-medium text-lg mt-8">Feed</h3>
            <hr class="w-full h-1 my-2 text-gray-400">
        </div>

    </div>
</div>
{#if showNav}
    <div class="w-2/12 lg:w-9/12 absolute right-0 top-0 h-screen hover:cursor-pointer z-50" on:click={() => showNav = false}>

    </div>
{/if}

<style></style>