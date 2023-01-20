<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import CurrentProgram from "$lib/components/CurrentProgram/CurrentProgram.svelte";


</script>

<svelte:head>
    <title>Current Program</title>
    <meta name="description" content="View your current program" />
</svelte:head>

{#if $userDB?.athleteData?.currentProgram}
    <CurrentProgram currentProgramId={$userDB.athleteData.currentProgram.id} />
{:else}
    <div class="text-center">
        You do not have a program currently
    </div>
{/if}

<style>

</style>