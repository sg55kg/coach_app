<script lang="ts">
    import {page} from "$app/stores"
    import {team} from "$lib/stores/teamStore";
    import {Team} from "$lib/classes/team";
    import {TeamService} from "$lib/service/TeamService";
    import CardLoadingSkeleton from "$lib/components/shared/loading/CardLoadingSkeleton.svelte";

    const fetchTeam = async () => {
        let pathArr = location.pathname.split('/')
        const teamId = pathArr[pathArr.length-2]
        if ($team !== null && $team.id === teamId) {
            return
        }
        try {
            const teamRes: Team = await TeamService.getTeam(teamId);
            $team = teamRes
            return teamRes
        } catch (e) {
            console.log(e)
        }
    }
    console.log($page.url.pathname.includes('athletes'))
</script>

<div class="flex flex-col items-center">
    <div class="bg-gray-200 tracking-widest w-screen -mt-4 flex flex-col lg:flex-row lg:justify-between pt-4 items-center">
        <div class="grid grid-cols-3 w-full text-lg text-center">
            <a href="athletes" class="p-1 py-2 rounded text-[#a0a0a0]" class:selected={$page.url.pathname.includes('athletes')}>
                Athletes
            </a>
            <a href="programs" class="p-1 py-2 rounded text-[#a0a0a0]" class:selected={$page.url.pathname.includes('programs')}>
                Programs
            </a>
            <a href="settings" class="p-1 py-2 rounded text-[#a0a0a0]" class:selected={$page.url.pathname.includes('settings')}>
                Settings
            </a>
        </div>
    </div>
    {#await fetchTeam()}
        <div class="flex flex-col w-screen">
                <h1 class="font-bold text-3xl text-white text-center">Loading...</h1>
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

    html, body, p, div, h3, button, header {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>