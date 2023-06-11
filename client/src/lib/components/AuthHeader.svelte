<script lang="ts">
    import { userDB } from '../stores/authStore';
    import { onMount } from 'svelte';
    import FaInbox from 'svelte-icons/fa/FaInbox.svelte';
    import Navbar from '$lib/components/Navbar/Navbar.svelte';

    export let ssr = false;

    let isMobile: boolean = false;

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST',
        });
        const body = await res.json();
        if (body.redirectUrl) {
            window.location.replace(body.redirectUrl);
        }
    };

    let showDropdown: boolean = false;

    onMount(() => {
        const mobileDevices = [
            /Android/i,
            /webOS/i,
            /iPhone/i,
            /iPad/i,
            /iPod/i,
            /BlackBerry/i,
            /Windows Phone/i,
        ];

        if (mobileDevices.some(d => navigator.userAgent.match(d))) {
            isMobile = true;
        }
    });
</script>

<header class="flex justify-between bg-gray-200 p-2 align-middle text-textgray">
    <Navbar />
    <div class="flex items-center align-baseline">
        {#if $userDB}
            <a
                href="/home"
                class="ml-1 text-2xl font-semibold uppercase tracking-widest text-yellow-lt"
            >
                Coachable
            </a>
        {:else}
            <a
                href="/"
                class="ml-1 text-2xl font-semibold uppercase tracking-widest text-yellow-lt"
            >
                Coachable
            </a>
        {/if}
    </div>
    <div class="flex items-center">
        <div class="h-6 w-8 hover:cursor-pointer hover:text-yellow-lt lg:mx-2">
            <a href="/home/chat">
                <FaInbox />
            </a>
        </div>
    </div>
</header>

<style>
</style>
