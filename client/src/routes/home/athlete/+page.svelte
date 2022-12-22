<script lang="ts">

    import {userDB} from "$lib/stores/authStore";
    import CurrentProgram from "$lib/components/CurrentProgram/CurrentProgram.svelte";


</script>

<h1>Athlete Page</h1>
{#if $userDB?.athleteData?.currentProgram}
    <CurrentProgram currentProgramId={$userDB.athleteData.currentProgram.id} />
{:else}
    <div>
        You do not have a program currently
    </div>
{/if}

<style>

</style>