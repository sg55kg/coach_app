<script lang="ts">
    import { userDB } from '../stores/authStore';
    import { onMount } from 'svelte';
    import AthleteNav from '$lib/components/AthleteNav.svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import FaInbox from 'svelte-icons/fa/FaInbox.svelte';

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST',
        });
        const body = await res.json();
        if (body.redirectUrl) {
            window.location.replace(body.redirectUrl);
        }
    };

    onMount(() => {});
</script>

<header
    class="flex w-screen items-center justify-between bg-gray-200 p-2 align-middle text-textgray lg:mb-4"
>
    <AthleteNav />
    <div
        class="flex items-center {$isMobile && 'justify-center'} align-baseline"
    >
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
        {#if isMobile === false}
            <p>&nbsp | &nbsp</p>
            {#if $userDB.coachData}
                <a href="/home/coach/{$userDB?.coachData?.id}">Coach</a>
            {:else}
                <a href="home/coach/get-started">Coach</a>
            {/if}
            <p>&nbsp | &nbsp</p>
            <a href="/home/athlete">Athlete</a>
        {/if}
    </div>

    <div class="h-6 w-8 hover:cursor-pointer hover:text-yellow-lt lg:mx-2">
        <a href="/home/chat">
            <FaInbox />
        </a>
    </div>
</header>

<style>
    img {
        border-radius: 50%;
        height: 45px;
    }
</style>
