<script lang="ts">
    import {DisplayProgram} from "$lib/classes/program";
    import FaRegEdit from 'svelte-icons/fa/FaRegEdit.svelte';
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte'
    import {onMount} from "svelte";
    import dayjs from "dayjs";
    export let program: DisplayProgram

    let updateSeverity: 'low' | 'moderate' | 'severe'

    onMount(() => {
        const today = dayjs()
        const lastUpdatedDay = dayjs(program.lastEnteredDay)
        const diff = lastUpdatedDay.diff(today, 'days')

        if (diff <= 2 || today.valueOf() > lastUpdatedDay.valueOf()) {
            updateSeverity = 'severe'
        } else if (diff <= 6) {
            updateSeverity = 'moderate'
        } else {
            updateSeverity = 'low'
        }
    })
</script>

<div class=" m-2 bg-gray-200 px-4 py-6 lg:w-10/12 shadow-black shadow-md">
    <h1 class="text-lg text-yellow-lt my-1 font-semibold tracking-wider">{program.name}</h1>
    <hr class="text-gray-100">
    <div class="mt-2">
        {#if updateSeverity === 'severe'}
            <p class="m-0 text-red text-base font-normal tracking-wide">This program needs an update in 2 days or less</p>
        {:else if updateSeverity === 'moderate'}
            <p class="m-0 text-yellow-shade text-base font-normal tracking-wide">This program needs an update in 1 week or less</p>
        {:else}
            <p class="m-0 text-green text-base font-normal tracking-wide">This program is up to date</p>
        {/if}

    </div>
    <div class="mt-2 flex justify-around">
        <div class="h-6 text-link hover:text-link-shade duration-300">
            <a href={`/home/coach/program/${program.id}`}>
                <FaRegEdit></FaRegEdit>
            </a>
        </div>
        <div class="h-6 text-link hover:text-link-shade duration-300">
            <a href={`/home/coach/program/${program.id}/stats`}>
                <FaRegChartBar></FaRegChartBar>
            </a>
        </div>
    </div>

</div>

<style>

</style>