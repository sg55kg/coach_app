<script lang="ts">
    import {DisplayTeam} from "$lib/classes/team";
    import DisplayTeamCard from "$lib/components/DisplayTeam/DisplayTeamCard.svelte";
    import {userDB} from "$lib/stores/authStore";

    export let data
    const { teams }: { teams: DisplayTeam[] } = data
    let displayTeams: DisplayTeam[] = []

    if (teams) {
        displayTeams = teams.filter(t => t.coachId !== $userDB?.coachData?.id)
    }

</script>

{#each displayTeams as team}
    <DisplayTeamCard bind:team={team} />
{/each}
{#if displayTeams.length < 1}
    <div class="flex flex-col items-center justify-center">
        <h3 class="text-2xl m-6 font-bold my-12">We're sorry, we could not retrieve a list of teams to join at this time.</h3>
    </div>
{/if}

<style>

</style>