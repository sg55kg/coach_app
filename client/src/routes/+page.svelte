<script lang="ts">
    import { auth0Client, isAuthenticated } from '$lib/stores/authStore';
    import UserService from '../lib/service/UserService';
    import { goto } from '$app/navigation';
    import type { PageServerData } from './$types';
    import { page } from '$app/stores';
    import { authUser, loadingAuth } from '$lib/stores/authStore.js';
    import { onMount } from 'svelte';

    export let data: PageServerData;

    const { state, redirectUri, baseUrl } = data;

    const loginUrl = baseUrl + state;

    onMount(() => {
        // TODO: potentially handle redirect logic here
    });
</script>

<svelte:head>
    <title>Coachable</title>
    <meta
        name="description"
        content="Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable."
    />
</svelte:head>

<section class="mt-0 flex w-screen flex-col md:mt-8">
    <div class="w-full px-16 md:w-9/12">
        <h1 class="text-5xl font-bold tracking-wider">
            Connect with your Team
        </h1>
        <div class="w-full md:w-6/12">
            <h1 class="text-wrap my-4 w-full text-xl tracking-wider md:w-2">
                Track all of your athletes' progress and stay organized across
                multiple teams. Use athlete feedback and data from your programs
                to make better decisions. Write better programs with Coachable.
            </h1>
        </div>
        <div
            class="m-2 mt-8 flex flex-col items-center justify-center text-center"
        >
            <a
                href="{loginUrl + '&screen_hint=signup'}"
                on:click="{() => ($loadingAuth = true)}"
                class="mx-2 mt-5 mb-2 justify-center rounded bg-yellow p-4 px-6 text-center font-bold text-gray-200 hover:bg-yellow-shade"
            >
                Get Started
            </a>
            <div class="flex flex-col items-start">
                <small>Already have an account?</small>
                <a
                    href="{loginUrl}"
                    on:click="{() => ($loadingAuth = true)}"
                    class="self-center font-semibold tracking-wider hover:text-yellow-lt"
                >
                    Login
                </a>
            </div>
        </div>
    </div>
</section>

<style>
    section {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 0.6;
    }

    h1 {
        width: 100%;
    }

    .welcome {
        display: block;
        position: relative;
        width: 100%;
        height: 0;
        padding: 0 0 calc(100% * 495 / 2048) 0;
    }

    .welcome img {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        display: block;
    }
</style>
