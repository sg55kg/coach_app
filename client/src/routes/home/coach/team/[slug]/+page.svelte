<script lang="ts">
    import {displayPrograms} from "$lib/stores/programStore";
    import ProgramCard from "$lib/components/ProgramCard.svelte";

</script>

<a href="/home/coach">Back to teams</a>
<div class="flex flex-col items-center">
{#if $displayPrograms.length > 0}
    {#each $displayPrograms as program}
        <ProgramCard program={program}  />
    {/each}
{:else}
    <h1 class="font-light m-8">It doesn't look like you have any programs right now</h1>
{/if}
<div class="flex justify-center">
    <a href="/home/coach/create-program">
        <button class="mt-5 text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
            Add New Program
        </button>
    </a>
</div>
</div>

<style>

</style>