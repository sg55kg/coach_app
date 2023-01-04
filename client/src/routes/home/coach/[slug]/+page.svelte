<script lang="ts">
    import type {Program} from "$lib/classes/program";

    export let data
    console.log('data', data)
    const { programs }: { programs: Program} = data
</script>

<h1>Coach Dashboard</h1>
{#if programs}
    {#each programs as program}
        <div id="program-display-card">
            <a href={`/coach/program/${program.id}`}>
                <h4 class="font-bold">{program.name}</h4>
            </a>
            <hr>
            <p>Athlete name here</p>
        </div>
    {/each}
{/if}
<button>
    <a href="create-program">Create New Program</a>
</button>

<style>
    #program-display-card {
        border: 1px solid black;
        background: white;
    }
</style>