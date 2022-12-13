<script lang="ts">
    import {Program} from "$lib/classes/program";
    import dayjs, {Dayjs} from "dayjs";
    import {onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {Day} from "$lib/classes/day";

    export let currentProgramId: string
    let currentProgram: Program
    const today: Dayjs = dayjs()

    const setCurrentDay = (day: Dayjs) => {
        if (!currentProgram) return
        console.log(currentProgram)
        const programDay = currentProgram?.days?.find(d => dayjs(d.date).isSame(day, 'day'))
        console.log(programDay)
        return programDay
    }

    let currentDay: Day

    onMount(async () => {
        if (!$auth0Client) return
        const p = await ProgramService.getProgram($auth0Client, currentProgramId)
        currentProgram = p
        currentDay = setCurrentDay(today)
    })
</script>

{#if currentProgram}
    <div>
        <h1>{currentProgram.name}</h1>
        <h2>{today.format('dddd MMMM D, YYYY')}</h2>
        {#if currentDay}
            {#each currentDay.exercises as exercise}
                <div class="flex-col">
                    <div class="flex flex-row justify-around">
                        <p class="w-fit m-0">{exercise.name}</p>
                        <p class="w-fit m-0">{exercise.weight}</p>
                        <p class="w-fit m-0">{exercise.sets}</p>
                        <p class="w-fit m-0">{exercise.repsPerSet}</p>
                    </div>
                    <div>
                        <p>{exercise.notes}</p>
                    </div>
                </div>
            {/each}
        {/if}
    </div>
{/if}


<style>

</style>