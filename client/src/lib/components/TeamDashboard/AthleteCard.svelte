<script lang="ts">
    import {AthleteData} from "$lib/classes/user";
    import {onMount} from "svelte";
    import dayjs from "dayjs";
    import FaRegEdit from 'svelte-icons/fa/FaRegEdit.svelte';
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte'
    import {Team} from "$lib/classes/team";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'

    export let athlete: AthleteData
    export let team: Team

    let updateSeverity: 'low' | 'moderate' | 'severe' | 'none'
    let editProgramName: boolean = false

    onMount(() => {
        if (!athlete.currentProgram) {
            return updateSeverity = 'none'
        }
        const today = dayjs()
        const lastEntered = athlete.currentProgram.days.find(d => d.exercises.length < 1 && !d.isRestDay)
        if (!lastEntered) {
            return updateSeverity = 'low'
        }
        const lastUpdatedDay = dayjs(lastEntered.date)
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

<div class="rounded border-2 border-gray-400 p-2">
    <h2 class="font-semibold text-xl">
        {athlete.name}
    </h2>
    {#if athlete.currentProgram}
        <div class="flex justify-start text-lg">
            {#if !editProgramName}
                <h4>Current Program: {athlete.currentProgram.name}</h4>
                <div class="h-6 mx-2" on:click={() => editProgramName = !editProgramName}><FaPen /></div>
            {:else }
                <input bind:value={athlete.currentProgram.name}>
                <button>Save</button>
            {/if}
        </div>
        <div>
            {#if updateSeverity === 'severe'}
                <p class="m-0 text-red text-base font-normal tracking-wide">{athlete.name}'s program needs an update in 2 days or less</p>
            {:else if updateSeverity === 'moderate'}
                <p class="m-0 text-yellow-shade text-base font-normal tracking-wide">{athlete.name}'s program needs an update in 1 week or less</p>
            {:else if updateSeverity === 'low'}
                <p class="m-0 text-green text-base font-normal tracking-wide">{athlete.name}'s program is up to date</p>
            {:else}
                <p class="m-0 text-base font-normal">{athlete.name} does not have a current program</p>
            {/if}
        </div>
        <div class="mt-2 flex justify-around">
            {#if athlete.currentProgram}
                <div class="h-6 text-link hover:text-link-shade duration-300">
                    <a href={`/home/coach/program/${athlete.currentProgram.id}`}>
                        <FaRegEdit></FaRegEdit>
                    </a>
                </div>
                <div class="h-6 text-link hover:text-link-shade duration-300">
                    <a href={`/home/coach/program/${athlete.currentProgram.id}/stats`}>
                        <FaRegChartBar></FaRegChartBar>
                    </a>
                </div>
            {/if}
        </div>
    {:else}
        <p class="m-0 text-base font-normal">{athlete.name} does not have a current program</p>
    {/if}
</div>

<style>

</style>