<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte';
    import logo from '../../images/logo-yellow.png';
    import { authUser, userDB } from '$lib/stores/authStore.js';
    import FaDoorOpen from 'svelte-icons/fa/FaDoorOpen.svelte';
    import FaWrench from 'svelte-icons/fa/FaWrench.svelte';

    let showNav: boolean = false;
    //w-screen max-w-lg right-0 absolute bg-white h-full shadow-xl delay-400 duration-500 ease-in-out transition-all transform
    //absolute top-0 left-0 w-7/12 bg-gray-100 h-full z-50 opacity-100

    const logout = async () => {
        const res = await fetch('/api/auth/logout', {
            method: 'POST',
        });
        const body = await res.json();
        if (body.redirectUrl) {
            window.location.replace(body.redirectUrl);
        }
    };
</script>

<div class="mt-4 flex flex-col items-center">
    <a
        class="my-2 text-lg font-bold text-textblue hover:text-link"
        href="/home/athlete"
        on:click="{() => (showNav = false)}"
    >
        Athlete Home
    </a>
    <a
        class="my-2 text-lg font-bold text-textblue hover:text-link"
        href="/home/athlete/program"
        on:click="{() => (showNav = false)}"
    >
        My Program
    </a>
    <a
        class="my-2 text-lg font-bold text-textblue hover:text-link"
        href="{`/home/athlete/${$userDB?.athleteData?.id}`}"
        on:click="{() => (showNav = false)}"
    >
        My Progress
    </a>
    {#if $userDB?.athleteData?.team}
        <a
            class=" my-2 text-lg font-bold text-gray-400 hover:cursor-default"
            href=""
        >
            {$userDB?.athleteData?.team?.name
                ? $userDB.athleteData.team.name
                : 'Your Team'}
        </a>
        <a
            class="my-2 text-lg font-bold text-textblue hover:text-link"
            href="/home/athlete/teams">Browse Teams</a
        >
    {:else}
        <a
            class="my-2 text-center text-lg text-textblue underline hover:text-link"
            href="/home/athlete/teams">Join a team</a
        >
    {/if}
    <div class="mt-12 flex w-full flex-col">
        <img
            src="{$authUser.picture}"
            alt="profile"
            class="mb-4 h-14 self-center rounded-full"
        />
        <div class="flex w-full flex-col items-center pl-4">
            <a
                class="my-2 rounded border border-link p-1 text-link hover:text-link-shade lg:pr-8"
                on:click="{() => {
                    showNav = false;
                    logout();
                }}"
            >
                <button class="my-1 flex h-8 items-center text-lg font-medium">
                    <span class="h-4 w-8 lg:mr-4">
                        <FaDoorOpen />
                    </span>
                    Sign Out
                </button>
            </a>
            <a
                class="my-2 rounded border border-link p-1 text-link hover:text-link-shade"
                href="/home/user"
                on:click="{() => (showNav = false)}"
            >
                <button
                    class="flex h-8 items-center text-lg font-medium lg:mr-8"
                >
                    <span class="h-4 w-8 lg:mr-4">
                        <FaWrench />
                    </span>
                    Settings
                </button>
            </a>
        </div>
    </div>
</div>

<style>
    .overlay {
        background-color: rgba(0, 0, 0, 0.63);
    }
</style>
