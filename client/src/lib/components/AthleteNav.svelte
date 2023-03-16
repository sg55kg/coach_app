<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte'
    import logo from '../images/logo-yellow.png'
    import {authUser, userDB} from "$lib/stores/authStore.js";
    import FaDoorOpen from 'svelte-icons/fa/FaDoorOpen.svelte'
    import FaWrench from 'svelte-icons/fa/FaWrench.svelte'

    let showNav: boolean = false
    //w-screen max-w-lg right-0 absolute bg-white h-full shadow-xl delay-400 duration-500 ease-in-out transition-all transform
    //absolute top-0 left-0 w-7/12 bg-gray-100 h-full z-50 opacity-100

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST'
        })
        const body = await res.json()
        if (body.redirectUrl) {
            window.location.replace(body.redirectUrl)
        }
    }
</script>

<div class="h-full flex items-center justify-center">
    <div class="w-10 hover:cursor-pointer text-yellow-lt hover:text-yellow" on:click={() => showNav = true}>
        <MdMenu />
    </div>
</div>
<div class="fixed overflow-hidden z-40 overlay inset-0 transform ease-in-out {showNav ? 'transition-opacity opacity-100 duration-300 translate-x-0' : 'transition-all delay-100 opacity-0 translate-x-full'}">
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
                <a class="hover:text-link text-textblue text-lg my-2 text-center underline" href="/home/athlete/teams">Join a team</a>
            {/if}
            <div class="mt-12 w-full flex flex-col">
                <img src={$authUser.picture} alt="profile" class="rounded-full h-14 mb-4 self-center">
                <div class="flex flex-col items-center w-full pl-4">
                    <a class="text-link hover:text-link-shade rounded border border-link p-1 lg:pr-8 my-2" on:click={() => { showNav = false; logout() }}>
                        <button class="h-8 flex items-center text-lg my-1 font-medium">
                            <span class="h-4 w-8 lg:mr-4">
                                <FaDoorOpen />
                            </span>
                            Sign Out
                        </button>
                    </a>
                    <a class="text-link hover:text-link-shade rounded border border-link p-1 my-2" href="/home/user" on:click={() => showNav = false}>
                        <button class="h-8 flex items-center text-lg lg:mr-8 font-medium">
                            <span class="h-4 w-8 lg:mr-4">
                                <FaWrench />
                            </span>
                            Settings
                        </button>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
{#if showNav}
    <div class="w-2/12 lg:w-9/12 absolute right-0 top-0 h-screen hover:cursor-pointer z-50" on:click={() => showNav = false}>

    </div>
{/if}


<style>
    .overlay {
        background-color: rgba(0, 0, 0, 0.63)
    }
</style>