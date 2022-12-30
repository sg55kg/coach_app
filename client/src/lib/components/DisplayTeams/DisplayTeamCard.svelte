<script lang="ts">

    import {DisplayTeam} from "$lib/classes/team";

    export let team: DisplayTeam

    const joinTeam = async () => {

    }
    
</script>

<div class="bg-gray-300 p-2 m-2">
    <div class="flex justify-between">
        <h1 class="font-bold text-xl">{team.name}</h1>
        <p>{team.numAthletes} Athletes</p>
    </div>
    <h2 class="font-bold text-lg">Coach: {team.coachName}</h2>
    <div class="my-2">
        <p>{team.description}</p>
    </div>
    <div class="my-2 flex justify-center">
        <button class="bg-yellow hover:bg-yellow-shade rounded p-1 text-black font-bold" on:click={joinTeam}>
            Join {team.name}
        </button>
    </div>
</div>

<style>

</style>