<script lang="ts">
    import {DisplayProgram} from "$lib/classes/program";
    import FaRegEdit from 'svelte-icons/fa/FaRegEdit.svelte';
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte'
    export let program: DisplayProgram
</script>

<div class=" m-2 bg-gray-200 px-4 py-6 lg:w-10/12 shadow-black shadow-md">
    <h1 class="text-3xl text-yellow-lt my-1 font-bold">{program.name}</h1>
    <hr class="text-orange">
    <div class="mt-2">
        <p class="m-0 text-textblue text-lg font-semibold">More data here?</p>
    </div>
    <div class="mt-2 flex justify-around">
        <div class="h-8 text-link hover:text-link-shade">
            <a href={`/home/coach/program/${program.id}`}>
                <FaRegEdit></FaRegEdit>
            </a>
        </div>
        <div class="h-8 text-link hover:text-link-shade">
            <a href={`/home/coach/program/${program.id}/stats`}>
                <FaRegChartBar></FaRegChartBar>
            </a>
        </div>



    </div>

</div>

<style>

</style>