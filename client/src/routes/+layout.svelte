<script lang="ts">
    import '../app.css';
    import {
        authUser,
        isMobile,
        loadingAuth,
        userDB,
    } from '$lib/stores/authStore';
    import type { LayoutServerData } from '../../.svelte-kit/types/src/routes/$types';
    import { User } from '$lib/classes/user';
    import { onMount } from 'svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';

    export let data: LayoutServerData;

    if (data.user && data.userData) {
        $userDB = User.createFrom(data.userData);
        $authUser = data.user;
    } else {
        $userDB = null;
        $authUser = undefined;
    }
    let deferredPrompt;
    onMount(async () => {
    //     window.addEventListener('beforeinstallprompt', e => {
    //         // Prevent Chrome 67 and earlier from automatically showing the prompt
    //         e.preventDefault();
    //         // Stash the event so it can be triggered later.
    //         deferredPrompt = e;
    //         const addBtn = document.getElementById('add-btn');
    //         // Update UI to notify the user they can add to home screen
    //         addBtn.classList.remove('hidden');
    //         //addBtn.style.display = "block";
    //
    //         addBtn.addEventListener('click', e => {
    //             // hide our user interface that shows our A2HS button
    //             addBtn.style.display = 'none';
    //             // Show the prompt
    //             deferredPrompt.prompt();
    //             // Wait for the user to respond to the prompt
    //             deferredPrompt.userChoice.then(choiceResult => {
    //                 if (choiceResult.outcome === 'accepted') {
    //                     console.log('User accepted the A2HS prompt');
    //                 } else {
    //                     console.log('User dismissed the A2HS prompt');
    //                 }
    //                 deferredPrompt = null;
    //             });
    //         });
    //     });
    //
    //     if ('serviceWorker' in navigator) {
    //         await navigator.serviceWorker.register('/service-worker.js', {
    //             type: import.meta.env.VITE_ENV === 'Dev' ? 'module' : 'classic',
    //         });
    //     }

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
            $isMobile = true;
        }
    });
</script>

<div class="app w-screen bg-gray-100 text-textgray">
    {#if !$loadingAuth}
        {#if !$authUser}
            <header
                class="mb-4 flex justify-between bg-gray-200 p-2 py-4 align-middle text-textgray"
            >
                <div class="flex items-center align-baseline">
                    <a
                        href="/"
                        class="ml-3 text-2xl font-semibold uppercase tracking-widest text-yellow-lt"
                    >
                        Coachable
                    </a>
                </div>
            </header>
        {/if}

        <main class="m-0 w-screen">
            <slot />
        </main>
    {:else}
        <div class="fixed flex h-screen w-screen items-center justify-center">
            <LoadingSpinner
                spinnerColor="fill-yellow"
                spinnerBackground="text-gray-100"
                width="w-16"
                height="w-16"
            />
        </div>
    {/if}
</div>

<style>
    .app {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    main {
        flex: 1;
        display: flex;
        flex-direction: column;
        width: 100%;
        /*max-width: 64rem;*/
        margin: 0 auto;
        box-sizing: border-box;
    }

    footer {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        padding: 12px;
    }

    footer a {
        font-weight: bold;
    }

    @media (min-width: 480px) {
        footer {
            padding: 12px 0;
        }
    }
</style>
