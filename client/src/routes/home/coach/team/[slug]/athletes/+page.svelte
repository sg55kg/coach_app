<script lang="ts">
    import { userDB } from '$lib/stores/authStore';

    import { TeamService } from '$lib/service/TeamService';
    import { Team } from '$lib/classes/team';
    import AthleteCard from '$lib/components/TeamDashboard/AthleteCard.svelte';
    import { onMount } from 'svelte';
    import { team } from '$lib/stores/teamStore';

    $: athleteList = $team ? $team.athletes : [];
</script>

<svelte:head>
    <title>{$team.name + ' Athletes'}</title>
    <meta
        name="description"
        content="Athlete dashboard for {$team?.name}. Manage your athletes"
    />
</svelte:head>

<div class="flex flex-col items-center">
    <h1 class="py-2 text-center text-3xl font-semibold text-white">
        {$team.name}
    </h1>
    <div class="flex w-screen flex-col px-3">
        {#each athleteList as athlete, idx (athlete.id)}
            <AthleteCard bind:athlete="{athlete}" />
        {/each}
    </div>
</div>

<style>
</style>
