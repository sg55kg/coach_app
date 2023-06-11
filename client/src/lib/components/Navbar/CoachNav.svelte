<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte';
    import logo from '../../images/logo-yellow.png';
    import { authUser, userDB } from '$lib/stores/authStore.js';
    import FaAngleDoubleLeft from 'svelte-icons/fa/FaAngleDoubleLeft.svelte';
    import FaDoorOpen from 'svelte-icons/fa/FaDoorOpen.svelte';
    import FaWrench from 'svelte-icons/fa/FaWrench.svelte';
    import { loadingAuth } from '../../stores/authStore';
    import { goto } from '$app/navigation';
    import { page } from '$app/stores';

    export let showNav: boolean = false;
    let selectedTeamId: string = '';

    const selectTeam = (id: string) => {
        if (selectedTeamId === id) {
            selectedTeamId = '';
        } else {
            selectedTeamId = id;
        }
    };

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

<div class="mt-4 flex flex-col items-center">
    <a class="self-start pl-4 text-lg font-medium" href="/home/coach">
        <h3>My Teams</h3>
    </a>
    <hr class="my-2 h-1 w-full text-gray-400" />
    {#if $userDB?.coachData}
        {#each $userDB.coachData.teams as team}
            <div class="flex w-full flex-col border-b border-b-gray-400">
                <div
                    class="flex w-full justify-between p-2"
                    on:click="{() => selectTeam(team.id)}"
                >
                    <h3
                        class="text-md tracking-wider {$page.url.pathname.includes(
                            team.id
                        )
                            ? 'text-yellow-lt'
                            : ''}"
                    >
                        {team.name}
                    </h3>
                    <button>
                        {selectedTeamId === team.id ? '^' : 'v'}
                    </button>
                </div>
                {#if selectedTeamId === team.id}
                    <div class="flex flex-col p-2 font-medium text-textblue">
                        <a
                            class="my-1 {$page.url.pathname.includes(
                                `home/coach/team/${team.id}/athletes`
                            )
                                ? 'text-link'
                                : ''}"
                            on:click="{() => (showNav = false)}"
                            href="/home/coach/team/{team.id}/athletes"
                            >Athletes</a
                        >
                        <a
                            class="my-1 {$page.url.pathname.includes(
                                `home/coach/team/${team.id}/programs`
                            )
                                ? 'text-link'
                                : ''}"
                            on:click="{() => (showNav = false)}"
                            href="/home/coach/team/{team.id}/programs"
                            >Programs</a
                        >
                        <a
                            class="my-1 {$page.url.pathname.includes(
                                `home/coach/team/${team.id}/settings`
                            )
                                ? 'text-link'
                                : ''}"
                            on:click="{() => (showNav = false)}"
                            href="/home/coach/team/{team.id}/settings"
                            >Settings</a
                        >
                    </div>
                {/if}
            </div>
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
        <div class="flex w-full flex-col items-center lg:items-start lg:pl-4">
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
                <button class="flex h-8 items-center text-lg font-medium">
                    <span class="h-4 w-8 lg:mr-4">
                        <FaWrench />
                    </span>
                    Settings
                </button>
            </a>
        </div>
    </div>
</div>

<style></style>
