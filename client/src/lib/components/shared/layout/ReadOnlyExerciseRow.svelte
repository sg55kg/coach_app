<script lang="ts">
    import { Exercise } from '../../../classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import { userDB } from '../../../stores/authStore';
    import { EffortIntensity } from '../../../classes/program/exercise/enums';
    import GoCheck from 'svelte-icons/go/GoCheck.svelte';
    import GoDash from 'svelte-icons/go/GoDash.svelte';

    export let exercise: Exercise;
    export let showName: boolean = true;

    let unit: 'kg' | 'lb' = $userDB!.preferences.weight;
    $: unit = $userDB!.preferences.weight;

    const formatEffortString = (effort: EffortIntensity) => {
        switch (effort) {
            case EffortIntensity.EASY:
                return 'Easy';
            case EffortIntensity.MODERATE:
                return 'Moderate';
            case EffortIntensity.DIFFICULT:
                return 'Hard';
            case EffortIntensity.MAX:
                return 'Max Effort';
        }
    };
</script>

<div class="{showName ? 'mb-2' : 'ml-10 border-l-2 border-l-textblue p-1'} flex h-6">
    {#if exercise.isComplete && exercise.weightCompleted < 1 && exercise.totalRepsCompleted < 1}
        <span class="mr-1 h-6 w-6 text-orange-shade"><GoDash /></span>
    {:else if exercise.isComplete}
        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
    {/if}
    {#if exercise.type === ExerciseType.EXERCISE}
        {#if exercise.isMax}
            {#if showName}
                {exercise.name ? exercise.name : 'No Name'}: {exercise.repsPerSet}RM
            {:else}
                {exercise.repsPerSet}RM
            {/if}
        {:else if exercise.isMaxReps}
            {#if showName}
                {exercise.name ? exercise.name : 'No Name'}: {exercise.wgt(
                    unit
                )}{unit}{exercise.sets}sets AMRAP
            {:else}
                {exercise.wgt(unit)}{unit}{exercise.sets}sets AMRAP
            {/if}
        {:else if showName}
            {exercise.name ? exercise.name : 'No Name'}: {exercise.wgt(
                unit
            )}{unit}
            {exercise.sets}sets {exercise.repsPerSet}reps
        {:else}
            {exercise.wgt(unit)}{unit}
            {exercise.sets}sets {exercise.repsPerSet}reps
        {/if}
    {:else if exercise.type === ExerciseType.COMPLEX}
        {#if exercise.isMax}
            {#if showName}
                {exercise.nameArr.join(' + ')}: {exercise.repArr.join(' + ')}RM
            {:else}
                {exercise.repArr.join(' + ')}RM
            {/if}
        {:else if showName}
            {exercise.nameArr.join(' + ')}: {exercise.wgt(unit)}{unit}
            {exercise.sets}sets {exercise.repArr.join(' + ')}reps
        {:else}
            {exercise.wgt(unit)}{unit}
            {exercise.sets}sets {exercise.repArr.join(' + ')}reps
        {/if}
    {:else if exercise.type === ExerciseType.ACCESSORY}
        {#if showName}
            {exercise.name ? exercise.name : 'No Name'}:
            {exercise.weight
                ? exercise.wgt(unit) + unit
                : formatEffortString(exercise.effortIntensity)}
            {exercise.sets}sets {exercise.repsPerSet}reps
        {:else}
            {exercise.weight
                ? exercise.wgt(unit) + unit
                : formatEffortString(exercise.effortIntensity)}
            {exercise.sets}sets {exercise.repsPerSet}reps
        {/if}
    {/if}
</div>

<style></style>
