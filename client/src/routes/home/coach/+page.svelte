<script lang="ts">
    import {displayPrograms} from "../../../lib/stores/programStore";
</script>

<div class="flex flex-col justify-center items-center">
    {#if $displayPrograms.length > 0}
        {#each displayPrograms as program}
            <div class="bg-white rounded border-black m-8 p-4 shadow-black">
                <h1>{program.name}</h1>
                <hr>
                <a href={`/home/coach/program/${program.id}`}>View</a>
            </div>
        {/each}
        {:else}
        <h1 class="font-light m-8">It doesn't look like you have any programs right now</h1>
    {/if}
    <div>
        <a href="/home/coach/create-program">
            <button class="text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
                Add New Program
            </button>
        </a>
    </div>
</div>

<style>

</style>