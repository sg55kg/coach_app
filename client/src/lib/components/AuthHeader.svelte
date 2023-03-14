<script lang="ts">
    import {auth0Client, isAuthenticated, userDB} from "../stores/authStore";
    import UserService from "../service/userService";
    import {onMount} from "svelte";
    import {authUser} from "$lib/stores/authStore";
    import FaInbox from 'svelte-icons/fa/FaInbox.svelte'
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
    <div class="flex items-center">
        <div class="h-6 mx-2 hover:cursor-pointer hover:text-yellow-lt">
            <a href="/home/chat">
                <FaInbox />
            </a>
        </div>
    </div>
</header>

<style>

</style>