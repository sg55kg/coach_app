<script lang="ts">
    import { page } from '$app/stores';
    import { team } from '$lib/stores/teamStore';
    import { Team } from '$lib/classes/team';
    import { TeamService } from '$lib/service/TeamService';
    import CardLoadingSkeleton from '$lib/components/shared/loading/CardLoadingSkeleton.svelte';
    import { afterUpdate, onMount } from 'svelte';

    let loadingTeam: boolean = true;

    const fetchTeam = async teamId => {
        if ($team && $team.id === teamId) {
            loadingTeam = false;
            return $team;
        }
        const teamRes: Team = await TeamService.getTeam(teamId);
        loadingTeam = false;
        return teamRes;
    };

    onMount(async () => {
        let pathArr = $page.url.pathname.split('/');
        const teamId = pathArr[pathArr.length - 2];
        $team = await fetchTeam(teamId);
    });

    afterUpdate(async () => {
        let pathArr = $page.url.pathname.split('/');
        const teamId = pathArr[pathArr.length - 2];
        if ($team && $team.id !== teamId) {
            $team = await fetchTeam(teamId);
        }
    });
</script>

<div class="flex flex-col items-center">
    <div
        class="-mt-4 flex w-screen flex-col items-center bg-gray-200 pt-4 tracking-widest lg:flex-row lg:justify-between"
    >
        <div class="grid w-full grid-cols-3 text-center text-lg">
            <a
                href="athletes"
                class="rounded p-1 py-2 text-[#a0a0a0]"
                class:selected="{$page.url.pathname.includes('athletes')}"
            >
                Athletes
            </a>
            <a
                href="programs"
                class="rounded p-1 py-2 text-[#a0a0a0]"
                class:selected="{$page.url.pathname.includes('programs')}"
            >
                Programs
            </a>
            <a
                href="settings"
                class="rounded p-1 py-2 text-[#a0a0a0]"
                class:selected="{$page.url.pathname.includes('settings')}"
            >
                Settings
            </a>
        </div>
    </div>
    <!--{#await fetchTeam()}-->
    {#if loadingTeam}
        <div class="flex w-screen flex-col">
            <h1 class="text-center text-3xl font-bold text-white">
                Loading...
            </h1>
            <CardLoadingSkeleton />
            <CardLoadingSkeleton />
            <CardLoadingSkeleton />
        </div>
    {:else if $team}
        <slot />
    {/if}
    <!--{:then teamRes}-->
    <!--{:catch err}-->
    <!--    <h1>We're sorry, we couldn't fetch your team at this time</h1>-->
    <!--{/await}-->
</div>

<style>
    .selected {
        font-weight: bold;
        color: #bae6fd;
        background-color: #1f232f;
    }

    html,
    body,
    p,
    div,
    h3,
    button,
    header {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
    }
</style>
