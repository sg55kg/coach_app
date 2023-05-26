<script lang="ts">
    import { page } from '$app/stores';
    import { team } from '$lib/stores/teamStore';
    import { Team } from '$lib/classes/team';
    import { TeamService } from '$lib/service/TeamService';
    import CardLoadingSkeleton from '$lib/components/shared/loading/CardLoadingSkeleton.svelte';

    const fetchTeam = async () => {
        let pathArr = location.pathname.split('/');
        const teamId = pathArr[pathArr.length - 2];
        if ($team !== null && $team.id === teamId) {
            return;
        }
        try {
            const teamRes: Team = await TeamService.getTeam(teamId);
            $team = teamRes;
            return teamRes;
        } catch (e) {
            console.log(e);
        }
    };
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
    {#await fetchTeam()}
        <div class="flex w-screen flex-col">
            <h1 class="text-center text-3xl font-bold text-white">
                Loading...
            </h1>
            <CardLoadingSkeleton />
            <CardLoadingSkeleton />
            <CardLoadingSkeleton />
        </div>
    {:then teamRes}
        <slot />
    {:catch err}
        <h1>We're sorry, we couldn't fetch your team at this time</h1>
    {/await}
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
