<script lang="ts">
    import { onMount } from 'svelte';
    import { authUser, userDB } from '$lib/stores/authStore';
    import { goto } from '$app/navigation';
    import { page } from '$app/stores';
    import type { LayoutData } from './$types';
    import { User } from '$lib/classes/user';

    export let data: LayoutData;

    const { user, userData } = data;

    if (user && userData) {
        $userDB = User.build(userData);
        $authUser = user;
    }

    onMount(async () => {});
</script>

<svelte:head>
    <title>Home</title>
    <meta name="description" content="View athlete and coach dashboards" />
</svelte:head>

{#if $userDB}
    <section class="flex flex-col">
        <h1
            class="m-10 text-center text-3xl font-semibold uppercase tracking-widest text-textgray"
        >
            Welcome
        </h1>
        <div
            class="flex w-5/12 flex-col self-center lg:flex-row lg:justify-around"
        >
            {#if $userDB.coachData}
                <a
                    class="my-2 rounded bg-yellow p-2 text-center text-sm font-bold text-gray-200 hover:bg-yellow-shade lg:my-0 lg:w-5/12"
                    href="/home/coach/{$userDB.coachData.id}"
                >
                    Coach Dashboard
                </a>
            {:else}
                <a
                    class="my-2 rounded bg-yellow p-2 text-center text-sm font-bold text-gray-200 hover:bg-yellow-shade lg:my-0 lg:w-5/12"
                    href="/home/coach/get-started"
                >
                    Coach Dashboard
                </a>
            {/if}
            <a
                class="my-2 rounded bg-yellow p-2 text-center text-sm font-bold text-gray-200 hover:bg-yellow-shade lg:my-0 lg:w-5/12"
                href="/home/athlete"
            >
                Athlete Dashboard
            </a>
        </div>
    </section>
{/if}

<style>
</style>
