<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte'
    import logo from '../images/logo-yellow.png'
    import {userDB} from "$lib/stores/authStore.js";

    let showNav: boolean = false
    //w-screen max-w-lg right-0 absolute bg-white h-full shadow-xl delay-400 duration-500 ease-in-out transition-all transform
    //absolute top-0 left-0 w-7/12 bg-gray-100 h-full z-50 opacity-100
</script>

<div class="h-full flex items-center justify-center">
    <div class="w-10 hover:cursor-pointer text-yellow-lt hover:text-yellow" on:click={() => showNav = true}>
        <MdMenu />
    </div>
</div>
<div class="fixed overflow-hidden z-40 overlay inset-0 transform ease-in-out {showNav ? 'transition-opacity opacity-100 duration-300 translate-x-0' : 'transition-all delay-500 opacity-0 translate-x-full'}">
    <div class="w-9/12 lg:w-3/12 left-0 absolute bg-gray-100 h-full shadow-xl delay-200 duration-200 ease-in-out transition-all transform {showNav ? 'translate-x-0' : 'translate-x-[-30em]'}">
        <div class="flex p-2 mt-2 justify-center">
            <img class="h-10" src={logo} alt="Coachable Logo">
<!--            <button class="font-semibold tracking-widest uppercase text-2xl ml-1 p-2 text-yellow-lt">-->
<!--                Coachable-->
<!--            </button>-->
        </div>
        <div class="flex flex-col items-center mt-4">
            <a class="hover:text-link text-textblue font-bold text-lg my-2" href="/home/athlete" on:click={() => showNav = false}>
                Athlete Home
            </a>
            <a class="hover:text-link text-textblue font-bold text-lg my-2" href="/home/athlete/program" on:click={() => showNav = false}>
                My Program
            </a>
            <a class="hover:text-link text-textblue font-bold text-lg my-2" href={`/home/athlete/${$userDB?.athleteData?.id}`} on:click={() => showNav = false}>
                My Progress
            </a>
            {#if $userDB?.athleteData?.team}
                <a class=" text-gray-400 font-bold text-lg my-2 hover:cursor-default" href="">
                    {$userDB?.athleteData?.team?.name ? $userDB.athleteData.team.name : 'Your Team'}
                </a>
                <a class="hover:text-link text-textblue font-bold text-lg my-2" href="/home/athlete/teams">Browse Teams</a>
            {:else}
                <a class="text-lg text-center md:text-left underline text-link" href="/home/athlete/teams">Join a team</a>
            {/if}
        </div>
    </div>
</div>
<div class="w-2/12 lg:w-9/12 absolute right-0 top-0 h-screen hover:cursor-pointer z-50" on:click={() => showNav = false}>

</div>

<style>
    .overlay {
        background-color: rgba(0, 0, 0, 0.63)
    }
</style>