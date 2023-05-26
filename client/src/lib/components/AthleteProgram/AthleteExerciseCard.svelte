<script lang="ts">
    import { Exercise } from '../../classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte';
    import GoCheck from 'svelte-icons/go/GoCheck.svelte';
    import GoDash from 'svelte-icons/go/GoDash.svelte';
    import { EffortIntensity } from '../../classes/program/exercise/enums';
    import { getContext } from 'svelte';
    import { userDB } from '../../stores/authStore';

    export let exercise: Exercise;
    export let exerciseIndex: number;
    export let selectedExerciseIdx: number;

    const { getNewRecordExerciseIds } = getContext('athlete-program');
    const newRecordIds = getNewRecordExerciseIds();
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

<div
    class="relative mr-6 mb-2 flex flex-col items-start
            justify-center border-l-2 border-l-textblue bg-gray-200 p-2 hover:cursor-pointer lg:mr-2"
    on:click="{() => (selectedExerciseIdx = exerciseIndex)}"
>
    {#if exercise.type === ExerciseType.EXERCISE}
        {#each [exercise, ...exercise.dropSets] as row}
            {#if row.isMax}
                <div class="flex w-11/12">
                    {#if row.isComplete && row.weightCompleted < 1 && row.totalRepsCompleted < 1}
                        <span class="mr-1 h-6 w-6 text-orange-shade"
                            ><GoDash /></span
                        >
                    {:else if row.isComplete}
                        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}: {row.repsPerSet}RM {row.isComplete
                            ? `- ${row.weightCompleted}kg`
                            : ''}
                    </h4>
                </div>
            {:else if row.isMaxReps}
                <div class="flex w-11/12">
                    {#if row.isComplete && row.weightCompleted < 1 && row.totalRepsCompleted < 1}
                        <span class="mr-1 h-6 w-6 text-orange-shade"
                            ><GoDash /></span
                        >
                    {:else if row.isComplete}
                        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}:
                        {row.wgtComp(unit)}/{row.wgt(unit)}{unit}
                        {row.setsCompleted}/{row.sets}sets AMRAP {row.isComplete
                            ? `- ${row.totalRepsCompleted}reps`
                            : ''}
                    </h4>
                </div>
            {:else}
                <div class="flex w-11/12">
                    {#if row.isComplete && row.weightCompleted < 1 && row.totalRepsCompleted < 1}
                        <span class="mr-1 h-6 w-6 text-orange-shade"
                            ><GoDash /></span
                        >
                    {:else if row.isComplete}
                        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}: {row.wgtComp(
                            unit
                        )}/{row.wgt(unit)}{unit}
                        {row.setsCompleted}/{row.sets}sets
                        {row.totalRepsCompleted && row.setsCompleted
                            ? row.totalRepsCompleted / row.setsCompleted
                            : 0}/{row.repsPerSet}reps
                    </h4>
                </div>
            {/if}
        {/each}
    {:else if exercise.type === ExerciseType.COMPLEX}
        {#each [exercise, ...exercise.dropSets] as row}
            <div class="flex w-11/12">
                {#if row.isMax}
                    {#if row.isComplete && row.weightCompleted < 1 && !row.repCompletedArr[0]}
                        <span class="mr-1 h-6 w-6 text-orange-shade"
                            ><GoDash /></span
                        >
                    {:else if row.isComplete}
                        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.nameArr.join(' + ')}: {row.repArr.join(' + ')}RM
                    </h4>
                {:else}
                    {#if row.isComplete && row.weightCompleted < 1 && !row.repCompletedArr[0]}
                        <span class="mr-1 h-6 w-6 text-orange-shade"
                            ><GoDash /></span
                        >
                    {:else if row.isComplete}
                        <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.nameArr.join(' + ')}: {row.wgt(unit)}{unit}
                        {row.sets}sets {row.repArr.join(' + ')}reps
                    </h4>
                {/if}
            </div>
        {/each}
    {:else if exercise.type === ExerciseType.DURATION}
        <h4>Test</h4>
    {:else}
        {#each [exercise, ...exercise.dropSets] as row}
            <div class="flex">
                {#if row.isComplete && row.weightCompleted < 1 && row.totalRepsCompleted < 1}
                    <span class="mr-1 h-6 w-6 text-orange-shade"
                        ><GoDash /></span
                    >
                {:else if row.isComplete}
                    <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
                {/if}
                <h4>
                    {row.name ? row.name : 'No Name'}:
                    {row.weight
                        ? row.wgt(unit) + unit
                        : formatEffortString(row.effortIntensity)}
                    {row.sets}sets {row.repsPerSet}reps
                </h4>
            </div>
        {/each}
    {/if}
    {#if $newRecordIds.length > 0 && $newRecordIds.includes(exercise.id)}
        <p class="text-green">New Record!</p>
    {/if}
    <button class="absolute right-3 h-4 w-4">
        <FaChevronDown />
    </button>
</div>

<style></style>
