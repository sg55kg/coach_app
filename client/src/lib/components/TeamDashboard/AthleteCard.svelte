<script lang="ts">
    import {onMount} from "svelte";
    import dayjs from "dayjs";
    import FaRegEdit from 'svelte-icons/fa/FaRegEdit.svelte';
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte'
    import {Team} from "$lib/classes/team";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {Program} from "$lib/classes/program";
    import {AthleteData} from "$lib/classes/user/athlete";

    export let athlete: AthleteData
    export let team: Team

    let updateSeverity: 'low' | 'moderate' | 'severe' | 'none' | 'over'
    let lastDay: dayjs = dayjs()
    let editProgramName: boolean = false

    const saveProgramName = async () => {
        if (!$auth0Client) return

        try {
            const updatedProgram = {
                ...athlete.currentProgram
            } as Program
            const program = await ProgramService.updateProgram(updatedProgram)
            userDB.update(prev => {
                prev!.coachData!.programs = prev!.coachData!.programs!.map(p => p.id === program.id ? program : p)
                return prev
            })
            athlete.currentProgram = program
            editProgramName = false
        } catch (e) {
            console.log(e)
        }
    }

    onMount(() => {
        if (!athlete.currentProgram) {
            return updateSeverity = 'none'
        }
        const today = dayjs()
        const lastEntered = athlete.currentProgram.days.find(d => d.exercises.length < 1 && !d.isRestDay)
        if (!lastEntered) {
            if (athlete.currentProgram.days.length > 0) {
                if (athlete.currentProgram.days[athlete.currentProgram.days.length-1].date.valueOf() <= today.valueOf()) {
                    updateSeverity = 'over'
                    return lastDay = athlete.currentProgram.days[athlete.currentProgram.days.length - 1].date
                }
                return updateSeverity = 'low'
            }

            return updateSeverity = 'none'
        }
        const lastUpdatedDay = dayjs(lastEntered.date)
        const diff = lastUpdatedDay.diff(today, 'days')

        if (today.valueOf() >= lastUpdatedDay.valueOf() || diff <= 2) {
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
                <div class="h-5 mx-2" on:click={() => editProgramName = !editProgramName}><FaPen /></div>
            {:else }
                <input class="text-textblue bg-gray-300" bind:value={athlete.currentProgram.name}>
                <button class="mx-2 text-sm" on:click={saveProgramName}>Save</button>
            {/if}
        </div>
        <div>
            {#if updateSeverity === 'severe'}
                <p class="m-0 text-red text-base font-normal tracking-wide">{athlete.name}'s program needs an update in 2 days or less</p>
            {:else if updateSeverity === 'moderate'}
                <p class="m-0 text-yellow-shade text-base font-normal tracking-wide">{athlete.name}'s program needs an update in 1 week or less</p>
            {:else if updateSeverity === 'low'}
                <p class="m-0 text-green text-base font-normal tracking-wide">{athlete.name}'s program is up to date</p>
            {:else if updateSeverity === 'none'}
                <p class="m-0 text-base font-normal">{athlete.name} does not have a current program</p>
            {:else}
                <p class="m-0 text-base text-orange-shade tracking-wide">{athlete.name}'s program's last day was {lastDay.format('ddd MMM DD YYYY')}</p>
            {/if}
        </div>
        <div class="mt-2 flex justify-around">
            <div class="h-6 text-link hover:text-link-shade duration-300">
                <a href={`/home/coach/program/${athlete.currentProgram.id}`}>
                    <FaRegEdit></FaRegEdit>
                </a>
            </div>
            <div class="h-6 text-link hover:text-link-shade duration-300">
                <a href={`/home/coach/athlete-stats/${athlete.id}`}>
                    <FaRegChartBar></FaRegChartBar>
                </a>
            </div>
        </div>
    {:else}
        <p class="m-0 text-base font-normal">{athlete.name} does not have a current program</p>
        <div class="mt-2 flex justify-around">
            <div class="h-6 text-link hover:text-link-shade duration-300">
                <a href={`/home/coach/create-program?athlete=${athlete.id}`}>
                    <FaRegEdit></FaRegEdit>
                </a>
            </div>
            <div class="h-6 text-link hover:text-link-shade duration-300">
                <a href={`/home/coach/athlete-stats/${athlete.id}`}>
                    <FaRegChartBar></FaRegChartBar>
                </a>
            </div>
        </div>
    {/if}
</div>

<style>

</style>