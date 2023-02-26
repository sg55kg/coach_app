<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import CurrentProgram from "$lib/components/CurrentProgram/CurrentProgram.svelte";
import CurrentProgramOverview from "$lib/components/CurrentProgram/CurrentProgramOverview.svelte";

let viewOverview: boolean = true

</script>

<svelte:head>
    <title>Current Program</title>
    <meta name="description" content="View your current program" />
</svelte:head>

{#if $userDB?.athleteData?.currentProgram}
    {#if viewOverview}
        <CurrentProgram currentProgramId={$userDB.athleteData.currentProgram.id} />
    {:else}
        <CurrentProgramOverview />
    {/if}
{:else}
    <div class="text-center">
        You do not have a program currently
    </div>
{/if}

<style>

</style>