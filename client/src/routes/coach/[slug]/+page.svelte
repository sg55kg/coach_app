<script lang="ts">
    import type {Program} from "../../../lib/classes/program";

    export let data
    const { programs }: { programs: Program} = data
</script>

<h1>Coach Dashboard</h1>
{#if programs}
    {#each programs as program}
        <div id="program-display-card">
            <p>Card with basic info/link to view and edit</p>
            <p>{program.name}</p>
            <p>Athlete name here</p>
        </div>
    {/each}
{/if}

<style>
    #program-display-card {
        border: 1px solid black;
    }
</style>