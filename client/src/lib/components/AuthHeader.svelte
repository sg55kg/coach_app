<script lang="ts">
    import {auth0Client, isAuthenticated, userDB} from "../stores/authStore";
    import UserService from "../service/userService";
    import {onMount} from "svelte";
    import {authUser} from "$lib/stores/authStore";
    import CoachNav from "$lib/components/CoachNav.svelte";

    export let ssr = false

    let isMobile: boolean = false

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST'
        })
        const body = await res.json()
        if (body.redirectUrl) {
            window.location.replace(body.redirectUrl)
        }
    }

    let showDropdown: boolean = false

    onMount(() => {
        const mobileDevices  = [
            /Android/i,
            /webOS/i,
            /iPhone/i,
            /iPad/i,
            /iPod/i,
            /BlackBerry/i,
            /Windows Phone/i
        ]

        if (mobileDevices.some(d => navigator.userAgent.match(d))) {
            isMobile = true
        }
    })

</script>

<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle">
    <CoachNav />
    <div class="flex items-center align-baseline">
        {#if $userDB}
            <a href="/home" class="font-semibold tracking-widest uppercase text-2xl ml-1 text-yellow-lt">
                Coachable
            </a>
        {:else}
            <a href="/" class="font-semibold tracking-widest uppercase text-2xl ml-1 text-yellow-lt">
                Coachable
            </a>
        {/if}
    </div>
    <div class="relative">
        <button class="align-middle" on:click={() => { showDropdown = !showDropdown }}>
            <img src={$authUser.picture} alt="profile" class="rounded-full h-[40px] lg:h-[45px]">
        </button>
        {#if showDropdown}
            <div on:blur={() => showDropdown = false}
                 id="header-dropdown"
                 class="absolute right-0 w-56 flex flex-col z-40 bg-gray-300 items-center justify-center py-5 drop-shadow-2xl drop-shadow-black">
                {#if isMobile}
                    {#if $userDB.coachData}
                        <a class="w-full py-2 hover:bg-gray-200 text-center" href="/home/coach/{$userDB.coachData.id}">Coach Dashboard</a>
                    {:else}
                        <a class="w-full py-2 hover:bg-gray-200 text-center" href="/home/coach/get-started">Coach Dashboard</a>
                    {/if}
                    <a class="w-full py-2 hover:bg-gray-200 text-center" href="/home/athlete">Athlete Dashboard</a>
                {/if}
                <a on:click={() => showDropdown = false} class="w-full py-2 hover:bg-gray-200 text-center" href="/home/user">Settings</a>
                <button on:click={logout} class="w-full py-2 hover:bg-gray-200">Sign Out</button>
            </div>
        {/if}
    </div>
</header>

<style>

</style>