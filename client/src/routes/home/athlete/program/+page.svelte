<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import CurrentProgram from "$lib/components/CurrentProgram/CurrentProgram.svelte";

console.log($userDB?.athleteData?.currentProgram)
</script>

{#if $userDB?.athleteData?.currentProgram}
    <CurrentProgram currentProgramId={$userDB.athleteData.currentProgram.id} />
{:else}
    <div>
        You do not have a program currently
    </div>
{/if}

<style>

</style>