<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte';
    import logo from '../images/logo-yellow.png';
    import { authUser, userDB } from '$lib/stores/authStore.js';
    import FaAngleDoubleLeft from 'svelte-icons/fa/FaAngleDoubleLeft.svelte';
    import FaDoorOpen from 'svelte-icons/fa/FaDoorOpen.svelte';
    import FaWrench from 'svelte-icons/fa/FaWrench.svelte';
    import { loadingAuth } from '../stores/authStore';
    import { goto } from '$app/navigation';

    let showNav: boolean = false;

    const logout = async () => {
        $loadingAuth = true;
        try {
            const res = await fetch('/api/auth/logout', {
                method: 'POST',
            });
            const body = await res.json();
            if (body.redirectUrl) {
                await goto(body.redirectUrl);
                $loadingAuth = false;
            }
        } catch (e) {
            console.log(e);
        } finally {
            $loadingAuth = false;
        }
    };
</script>

<div class="flex h-full items-center justify-center">
    <div
        class="w-10 text-yellow-lt hover:cursor-pointer hover:text-yellow"
        on:click="{() => (showNav = true)}"
    >
        <MdMenu />
    </div>
</div>
<div
    class="overlay fixed inset-0 z-40 transform overflow-hidden ease-in-out
    {showNav
        ? 'translate-x-0 opacity-100 transition-opacity duration-100'
        : 'translate-x-full opacity-0 transition-all delay-500'}"
>
    <div
        class="absolute left-0 h-full w-9/12 transform bg-gray-100 shadow-xl transition-all delay-200 duration-200 ease-in-out lg:w-2/12 {showNav
            ? 'translate-x-0'
            : 'translate-x-[-30em]'}"
    >
        <div class="mt-2 flex w-full items-center p-2">
            <img class="m-auto ml-32 h-10" src="{logo}" alt="Coachable Logo" />
            <div
                class="w-8 text-yellow-lt hover:cursor-pointer hover:text-yellow"
                on:click="{() => (showNav = false)}"
            >
                <FaAngleDoubleLeft />
            </div>
        </div>
        <div class="mt-4 flex flex-col items-center">
            <a class="self-start pl-4 text-lg font-medium" href="/home/coach">
                <h3>My Teams</h3>
            </a>
            <hr class="my-2 h-1 w-full text-gray-400" />
            {#if $userDB?.coachData}
                {#each $userDB.coachData.teams as team}
                    <a
                        class="text-md font-medium text-textblue hover:cursor-pointer hover:text-link"
                        href="/home/coach/team/{team.id}/athletes"
                        on:click="{() => (showNav = false)}"
                    >
                        {team.name}
                    </a>
                {/each}
            {/if}
            <h3 class="mt-8 self-start pl-4 text-lg font-medium">Feed</h3>
            <hr class="my-2 h-1 w-full text-gray-400" />
            <div class="mt-12 flex w-full flex-col">
                <img
                    src="{$authUser?.picture}"
                    alt="profile"
                    class="mb-4 h-14 self-center rounded-full"
                />
                <div
                    class="flex w-full flex-col items-center lg:items-start lg:pl-4"
                >
                    <a
                        class="hover:text-yellow-lt"
                        on:click="{() => {
                            showNav = false;
                            logout();
                        }}"
                    >
                        <button
                            class="flex h-8 w-full items-center text-lg font-medium"
                        >
                            <span class="h-4 w-8 lg:mr-4">
                                <FaDoorOpen />
                            </span>
                            Sign Out
                        </button>
                    </a>
                    <a
                        class="hover:text-yellow-lt"
                        href="/home/user"
                        on:click="{() => (showNav = false)}"
                    >
                        <button
                            class="flex h-8 items-center text-lg font-medium"
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
    </div>
</div>
{#if showNav}
    <div
        class="absolute right-0 top-0 z-50 h-screen w-2/12 hover:cursor-pointer lg:w-9/12"
        on:click="{() => (showNav = false)}"
    ></div>
{/if}

<style></style>
