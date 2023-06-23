<script lang="ts">
    import { getContext } from 'svelte';
    import { isMobile, userDB } from '$lib/stores/authStore.js';
    import AthleteExerciseCard from '$lib/components/AthleteProgram/AthleteExerciseCard.svelte';
    import AthleteExpandedExercise from '$lib/components/AthleteProgram/AthleteExpandedExercise.svelte';
    import UserService from '../../service/UserService';
    import RichTextEditor from '$lib/components/shared/texteditor/RichTextEditor.svelte';
    import AthleteDayToolbar from '$lib/components/AthleteProgram/toolbars/AthleteDayToolbar.svelte';

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getAthleteProgramLoading,
        getCurrentDayIdx,
        getAthleteId,
    } = getContext('athlete-program');
    const currentProgram = getCurrentProgram();
    const currentDay = getCurrentDay();
    const loading = getAthleteProgramLoading();
    const idx = getCurrentDayIdx();
    const athleteId = getAthleteId();

    let selectedExerciseIdx: number = -1;
    $: exercisesComplete = () => {
        let count = 0;
        for (const e of $currentDay.exercises) {
            if (e.isComplete) {
                if (
                    e.dropSets.length > 0 &&
                    e.dropSets.reduce(
                        (a = e.isComplete, b) => a && b.isComplete
                    )
                ) {
                    count++;
                } else {
                    count++;
                }
            }
        }
        return count;
    };
</script>

<div class="h-full w-full overflow-y-auto bg-gray-100">
    <AthleteDayToolbar />
    {#if $currentDay.isRestDay}
        <h4 class="text-center text-lg font-semibold text-textblue">
            Rest Day
        </h4>
    {:else if !$currentDay.exercises.length}
        <div class="flex h-full items-center justify-center">
            <h4 class="text-center text-lg font-semibold text-textblue">
                Nothing entered for today.
            </h4>
        </div>
    {:else}
        <div class="flex w-full flex-col">
            <h4 class="p-1 text-lg">Warm Up</h4>
            {#if $currentDay.warmUp}
                <RichTextEditor
                    editable="{false}"
                    content="{$currentDay.warmUp.instructions}"
                />
            {:else}
                <div
                    class="mr-6 border-l-2 border-l-textblue bg-gray-200 p-2 lg:mr-2"
                >
                    No warm up entered for today
                </div>
            {/if}
        </div>
        <div class="flex w-full flex-col">
            <h4 class="p-1 text-lg">Exercises</h4>
            {#each $currentDay.exercises as exercise, idx}
                {#if selectedExerciseIdx === idx}
                    <AthleteExpandedExercise
                        bind:exercise="{exercise}"
                        bind:selectedExerciseIdx="{selectedExerciseIdx}"
                    />
                {:else}
                    <AthleteExerciseCard
                        bind:exercise="{exercise}"
                        exerciseIndex="{idx}"
                        bind:selectedExerciseIdx="{selectedExerciseIdx}"
                    />
                {/if}
            {/each}
        </div>
        <div class="flex w-full flex-col">
            <h4 class="p-1 text-lg">Notes from your coach:</h4>
            <p class="p-2 text-textblue"><i>No notes entered for today</i></p>
        </div>
        <div class="flex flex-col items-center">
            <h3
                class="p-2 text-xl font-semibold {exercisesComplete()
                    ? 'text-green'
                    : ''}"
            >
                {exercisesComplete()}/{$currentDay.exercises.length} Exercises Complete
            </h3>
            {#if exercisesComplete() !== $currentDay.exercises.length}
                <div class="flex w-full items-center justify-center p-2">
                    <button
                        class="mx-2 rounded-sm border-2 border-red-shade p-2 text-red-shade"
                    >
                        Skip Day
                    </button>
                    <button
                        class="mx-2 rounded-sm border-2 border-yellow p-2 text-yellow-shade"
                        on:click="{() =>
                            markDayCompleteAsWritten(
                                $athleteId,
                                $currentDay,
                                $currentProgram
                            )}"
                    >
                        Mark Day Finished
                    </button>
                </div>
            {/if}
        </div>
    {/if}
</div>

<style></style>
