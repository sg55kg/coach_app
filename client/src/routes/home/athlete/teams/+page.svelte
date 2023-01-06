<script lang="ts">

    import {onMount} from "svelte";
    import {DisplayTeam} from "$lib/classes/team";
    import {TeamService} from "$lib/service/TeamService";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import DisplayTeamCard from "$lib/components/DisplayTeams/DisplayTeamCard.svelte";

    let displayTeams: DisplayTeam[] = []
    let loadingDisplayTeams: boolean = false

    onMount(async () => {
        if (!$auth0Client) {
            return
        }
        loadingDisplayTeams = true
        try {
            // need to limit this query eventually to 20 results or so
            const displayTeamsRes: DisplayTeam[] = await TeamService.getDisplayTeams($auth0Client)
            if ($userDB?.coachData !== null) {
                displayTeams = displayTeamsRes.filter(t => t.coachId !== $userDB!.coachData.id)
            } else {
                displayTeams = displayTeamsRes
            }
        } catch (e) {
            console.log(e)
        }
        loadingDisplayTeams = false
    })
</script>

{#each displayTeams as team}
    <DisplayTeamCard team={team} />
{/each}
{#if displayTeams.length < 1 && !loadingDisplayTeams}
    <div class="flex flex-col items-center justify-center">
        <h3 class="text-2xl font-bold my-12">We're sorry, we could not retrieve a list of teams to join at this time.</h3>
    </div>
{/if}

<style>

</style>