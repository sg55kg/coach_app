<script lang="ts">
    import CalendarCard from "$lib/components/shared/layout/CalendarCard.svelte";
    import {getContext} from "svelte";
    import {Day} from "../../classes/program/day";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {isMobile} from "$lib/stores/authStore.js";
    import {EffortIntensity} from "../../classes/program/exercise/enums";

    export let idx: number
    export let day: Day
    export let container: HTMLDivElement
    export let contextCoordinates: {x: number, y: number} = {x: -1, y: -1}

    const { getCurrentProgram, getCurrentDay, markDayCompleteAsWritten, getCurrentDayIdx, getAthleteProgramLoading } = getContext('athlete-program')
    const currentProgram = getCurrentProgram()
    const currentDayIdx = getCurrentDayIdx()
    const currentDay = getCurrentDay()
    const loading = getAthleteProgramLoading()

    const formatEffortString = (effort: EffortIntensity) => {
        switch (effort) {
            case EffortIntensity.EASY:
                return 'Easy'
            case EffortIntensity.MODERATE:
                return 'Moderate'
            case EffortIntensity.DIFFICULT:
                return 'Hard'
            case EffortIntensity.MAX:
                return 'Max Effort'
        }
    }
</script>

<CalendarCard
        onClick={() => {
         $currentDay = day
         $currentDayIdx = idx
     }}
        onContextmenu={null}
>
    <div class="w-full flex justify-between py-1 px-2">
        <h3 class="font-semibold text-lg">{idx+1}</h3>
        {#if $currentProgram?.days[idx]?.date && $currentProgram?.athleteId}
            <h3>{$currentProgram.days[idx].date.format('ddd MMM DD')}</h3>
        {/if}
    </div>
    <div class="px-2 py-1">
        <ul class="text-sm">
            {#if $currentProgram.days[idx]}
                {#if $currentProgram.days[idx].isRestDay}
                    <div class="text-center w-full">
                        <h4 class="text-lg font-medium text-textblue py-2">Rest Day</h4>
                    </div>
                {:else}
                    {#each $currentProgram.days[idx]?.exercises as exercise, idx (exercise?.id+idx)}
                        {#if exercise.type === ExerciseType.EXERCISE}
                            {#if exercise.isMax}
                                <h4>
                                    {exercise.name ? exercise.name : 'No Name'}: {exercise.repsPerSet}RM
                                </h4>
                            {:else if exercise.isMaxReps}
                                <h4>
                                    {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets AMRAP
                                </h4>
                            {:else}
                                <h4>
                                    {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets {exercise.repsPerSet}reps
                                </h4>
                            {/if}
                        {:else if exercise.type === ExerciseType.COMPLEX}
                            {#if exercise.isMax}
                                <h4>
                                    {exercise.nameArr.join(' + ')}: {exercise.repArr.join(' + ')}RM
                                </h4>
                            {:else}
                                <h4>
                                    {exercise.nameArr.join(' + ')}: {exercise.weight}kg {exercise.sets}sets {exercise.repArr.join(' + ')}reps
                                </h4>
                            {/if}
                        {:else if exercise.type === ExerciseType.DURATION}
                            <h4>Test</h4>
                        {:else}
                            <h4>
                                {exercise.name ? exercise.name : 'No Name'}: {exercise.weight ? exercise.weight + 'kg' : formatEffortString(exercise.effortIntensity)} {exercise.sets}sets {exercise.repsPerSet}reps
                            </h4>
                        {/if}
                    {/each}
                {/if}
            {/if}
        </ul>
    </div>
</CalendarCard>

<style></style>