<script lang="ts">

    import {onMount} from "svelte";
    import {DisplayTeam} from "$lib/classes/team";
    import {TeamService} from "$lib/service/TeamService";
    import {auth0Client} from "$lib/stores/authStore";
    import DisplayTeamCard from "$lib/components/DisplayTeams/DisplayTeamCard.svelte";

    let displayTeams: DisplayTeam[] = []

    onMount(async () => {
        if (!$auth0Client) {
            throw new Error('Not authenticated')
        }
        try {
            displayTeams = await TeamService.getDisplayTeams($auth0Client)
        } catch (e) {
            console.log(e)
        }

    })
</script>

{#each displayTeams as team}
    <DisplayTeamCard team={team} />
{/each}

<style>

</style>