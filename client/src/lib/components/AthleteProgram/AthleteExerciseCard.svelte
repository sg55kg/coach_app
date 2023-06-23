<script lang="ts">
    import { Exercise } from '../../classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte';
    import GoCheck from 'svelte-icons/go/GoCheck.svelte';
    import GoDash from 'svelte-icons/go/GoDash.svelte';
    import { EffortIntensity } from '../../classes/program/exercise/enums';
    import { getContext } from 'svelte';
    import { userDB } from '../../stores/authStore';
    import ReadOnlyExerciseRow from '$lib/components/shared/layout/ReadOnlyExerciseRow.svelte';

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
    <div class="flex w-full">
        <div class="flex flex-col flex-grow">
            {#each [exercise, ...exercise.dropSets] as row, i}
                <ReadOnlyExerciseRow
                        exercise="{row}"
                        showName="{i === 0}"
                        isComplete="{row.isComplete}"
                />
            {/each}
        </div>
        <button class="mx-2 h-4 w-4 self-center">
            <FaChevronDown />
        </button>
    </div>
    {#if $newRecordIds.length > 0 && $newRecordIds.includes(exercise.id)}
        <p class="my-2 self-center text-center text-lg text-green">
            New Record!
        </p>
    {/if}
</div>

<style></style>
