<script lang="ts">
    import { Exercise } from '../../classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import { EffortIntensity } from '../../classes/program/exercise/enums';
    import Toggle from '$lib/components/shared/layout/Toggle.svelte';
    import { userDB } from '../../stores/authStore';
    import { getContext } from "svelte";

    export let exercise: Exercise;
    export let isDropSet: boolean = false;
    export let useWeightForAccessory: boolean = false;

    const { deleteExercise, getSelectedDay, getSelectedDayIdx, getProgram } = getContext('program');
       const selectedDayIdx = getSelectedDayIdx();
       const selectedDay = getSelectedDay();
       const program = getProgram();

    let unit: 'kg' | 'lb' = $userDB!.preferences.weight;
    $: unit = $userDB!.preferences.weight;

    const setComplexType = (ex: Exercise) => {
        ex.type = ExerciseType.COMPLEX;
        ex.nameArr = [exercise.name, ''];
        ex.repArr = [exercise.repsPerSet, 0];
        ex.name = '';
        ex.repsPerSet = 0;
        ex.dropSets.forEach(d => (d = setComplexType(d)));
        return ex;
    };

    const addToComplex = (ex: Exercise, nameIndex: number) => {
        ex.nameArr.splice(nameIndex + 1, 0, '');
        ex.repArr.splice(nameIndex + 1, 0, 0);
        ex.dropSets.forEach(d => (d = addToComplex(d, nameIndex)));
        return ex;
    };

    const removeComplexPart = (ex: Exercise, compIndex: number) => {
        ex.nameArr.splice(compIndex, 1);
        ex.repArr.splice(compIndex, 1);
        if (ex.nameArr.length === 1) {
            ex.type = ExerciseType.EXERCISE;
            ex.name = ex.nameArr[0];
            ex.repsPerSet = ex.repArr[0];
            ex.nameArr = [];
            ex.repArr = [];
        }
        ex.dropSets.forEach(e => removeComplexPart(e, compIndex));
        return ex;
    };

   const removeDropset = async () => {
        // if an ID exists, make a backend call to delete the exercise
        if (exercise.id) {
            try {
                await deleteExercise(exercise)
            } catch (e) {
                return
            }
        }

        // If no ID or if successfully deleted on the backend, splice from the frontend
        const dropSetIdx = $program.days[$selectedDayIdx].exercises
            .find((e: Exercise) => e.id === parentExerciseId).dropSets
            .findIndex((d: Exercise) => d.id === exercise.id)

        $program.days[$selectedDayIdx].exercises
            .find((e: Exercise) => e.id === parentExerciseId).dropSets
            .splice(dropSetIdx, 1)

        $program.days[$selectedDayIdx].exercises
            .find((e: Exercise) => e.id === parentExerciseId).dropSets
            .forEach((d, i) => d.order = i)

        $program = $program
        $selectedDay = $program.days[$selectedDayIdx]
   }

   const toggleMaxWeight = (isMax: boolean) => {
       exercise.isMax = isMax;
       exercise.weight = 0;
       exercise.sets = 1;
   }

    const toggleMaxReps = (isMaxReps: boolean) => {
        exercise.isMaxReps = isMaxReps;
    };

    const changeExerciseEffort = (
        effort: 'Easy' | 'Moderate' | 'Hard' | 'Max Effort'
    ) => {
        switch (effort) {
            case 'Easy':
                exercise.effortIntensity = EffortIntensity.EASY;
                break;
            case 'Moderate':
                exercise.effortIntensity = EffortIntensity.MODERATE;
                break;
            case 'Hard':
                exercise.effortIntensity = EffortIntensity.DIFFICULT;
                break;
            case 'Max Effort':
                exercise.effortIntensity = EffortIntensity.MAX;
                break;
        }
    };
</script>

{#if exercise.type === ExerciseType.EXERCISE}
    <div class="grid grid-cols-10 p-2 w-full gap-2">
        {#if !parentExerciseId}
            <input type="text"
                   placeholder="Exercise Name"
                   class="bg-gray-300 p-1 col-span-9"
                   bind:value={exercise.name}
            >
            <button class="col-span-1 flex items-center justify-center">
                <span
                    class="flex w-5 justify-center"
                    on:click="{() => (exercise = setComplexType(exercise))}"
                >
                    <FaPlus />
                </span>
            </button>
        {/if}
        {#if parentExerciseId}
            <button class="col-span-1 flex items-center justify-center">
                <span class="w-5 flex justify-center" on:click={removeDropset}>
                    <MdClose />
                </span>
            </button>
            {:else}
                <div class="col-span-1" />
        {/if}
        {#if exercise.isMax}
            <div class="col-span-3 flex flex-col items-end">
                <label class="text-sm">Rep Max</label>
                <input
                    type="text"
                    placeholder="Reps"
                    bind:value="{exercise.repsPerSet}"
                    class="w-full bg-gray-300 p-1 text-right"
                />
            </div>
        {:else}
            <div class="col-span-3 flex flex-col items-end">
                <label class="text-sm">Weight ({unit})</label>
                <input
                    type="text"
                    placeholder=""
                    value="{exercise.wgt(unit)}"
                    on:change="{e => exercise.setWgt(e.target.value, unit)}"
                    class="w-full bg-gray-300 p-1 text-right"
                />
            </div>
            <div class="col-span-3 flex flex-col items-end">
                <label class="text-sm">Sets</label>
                <input
                    type="text"
                    placeholder=""
                    bind:value="{exercise.sets}"
                    class="w-full bg-gray-300 p-1 text-right"
                />
            </div>
            {#if exercise.isMaxReps}
                <div class="col-span-3 flex items-end text-center">
                    <p
                        class="w-full text-lg font-medium tracking-wide text-yellow-lt"
                    >
                        AMRAP
                    </p>
                </div>
            {:else}
                <div class="col-span-3 flex flex-col items-end">
                    <label class="text-sm">Reps</label>
                    <input
                        type="text"
                        placeholder=""
                        bind:value="{exercise.repsPerSet}"
                        class="w-full bg-gray-300 p-1 text-right"
                    />
                </div>
            {/if}
        {/if}
        <div
            class="{(!exercise.isMax && !exercise.isMaxReps) ||
            exercise.isMaxReps
                ? 'col-span-4'
                : 'col-span-7'}"
        ></div>
        {#if !exercise.isMax}
            <div class="col-span-3 flex flex-col items-end justify-center">
                <Toggle
                    checked="{exercise.isMaxReps}"
                    onChange="{e => toggleMaxReps(e.target.checked)}"
                />
                <label class="text-sm"
                    >{exercise.isMaxReps
                        ? 'As many reps as possible'
                        : '# of reps'}</label
                >
            </div>
        {/if}
        {#if !exercise.isMaxReps}
            <div class="col-span-2 flex flex-col items-end justify-center">
                <Toggle
                    checked="{exercise.isMax}"
                    onChange="{e => toggleMaxWeight(e.target.checked)}"
                />
                <label class="text-sm"
                    >{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}</label
                >
            </div>
        {/if}
    </div>
{:else if exercise.type === ExerciseType.COMPLEX}
    <div class="grid w-full grid-cols-8 gap-2 p-2">
        {#if !parentExerciseId}
            {#each exercise.nameArr as name, idx}
                <button class="col-span-1 flex items-center justify-center">
                    <span
                        class="flex w-5 justify-center"
                        on:click="{() =>
                            (exercise = removeComplexPart(exercise, idx))}"
                    >
                        <MdClose />
                    </span>
                </button>
                <input
                    type="text"
                    placeholder="Exercise Name"
                    class="col-span-6 bg-gray-300 p-1"
                    bind:value="{exercise.nameArr[idx]}"
                />
                <button class="col-span-1 flex items-center justify-center">
                    <span
                        class="flex w-5 justify-center"
                        on:click="{() =>
                            (exercise = addToComplex(exercise, idx))}"
                    >
                        <FaPlus />
                    </span>
                </button>
            {/each}
        {/if}
        {#if !parentExerciseId}
            <div class="col-span-1"></div>
        {:else}
            <button class="col-span-1 flex items-center justify-center">
                <span class="w-5 flex justify-center" on:click={removeDropset}>
                    <MdClose />
                </span>
            </button>
        {/if}
        {#if !exercise.isMax}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Weight ({unit})</label>
                <input
                    type="number"
                    placeholder=""
                    value="{exercise.weight}"
                    on:change="{e => exercise.setWgt(e.target.value, unit)}"
                    class="w-full bg-gray-300 p-1"
                />
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Sets</label>
                <input
                    type="number"
                    placeholder=""
                    bind:value="{exercise.sets}"
                    class="w-full bg-gray-300 p-1"
                />
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Reps</label>
                {#each exercise.repArr as repGroup}
                    <input
                        type="number"
                        placeholder=""
                        bind:value="{repGroup}"
                        class="mb-2 w-full bg-gray-300 p-1"
                    />
                {/each}
            </div>
        {:else}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Rep Max</label>
                {#each exercise.repArr as repGroup}
                    <input
                        type="number"
                        placeholder=""
                        bind:value="{repGroup}"
                        class="mb-2 w-full bg-gray-300 p-1"
                    />
                {/each}
            </div>
        {/if}

        <div class="col-span-4"></div>
        <div class="col-span-3 flex flex-col items-end">
            <Toggle
                checked="{exercise.isMax}"
                onChange="{e => toggleMaxWeight(e.target.checked)}"
            />
            <label class="text-sm"
                >{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}</label
            >
        </div>
    </div>
{:else if exercise.type === ExerciseType.DURATION}{:else if exercise.type === ExerciseType.ACCESSORY}
    <div class="grid w-full grid-cols-6 gap-2 p-2">
        {#if !parentExerciseId}
            <input
                type="text"
                placeholder="Exercise Name"
                class="col-span-5 bg-gray-300 p-1"
                bind:value="{exercise.name}"
            />
        {/if}
        <div class="col-span-2 flex flex-col items-end">
            {#if useWeightForAccessory}
                <label class="text-sm">Weight ({unit})</label>
                <input
                    type="text"
                    placeholder=""
                    value="{exercise.weight}"
                    on:change="{e => exercise.setWgt(e.target.value, unit)}"
                    class="w-full bg-gray-300 p-1 text-right"
                />
            {:else}
                <label>Effort</label>
                <select
                    class="rounded bg-gray-300 p-1"
                    on:change="{e => changeExerciseEffort(e.target.value)}"
                >
                    <option
                        selected="{exercise.effortIntensity ===
                            EffortIntensity.EASY}">Easy</option
                    >
                    <option
                        selected="{exercise.effortIntensity ===
                            EffortIntensity.MODERATE}">Moderate</option
                    >
                    <option
                        selected="{exercise.effortIntensity ===
                            EffortIntensity.DIFFICULT}">Hard</option
                    >
                    <option
                        selected="{exercise.effortIntensity ===
                            EffortIntensity.MAX}">Max Effort</option
                    >
                </select>
            {/if}
        </div>
        <div class="col-span-2 flex flex-col items-end">
            <label class="text-sm">Sets</label>
            <input
                type="text"
                placeholder=""
                bind:value="{exercise.sets}"
                class="w-full bg-gray-300 p-1 text-right"
            />
        </div>
        <div class="col-span-2 flex flex-col items-end">
            <label class="text-sm">Reps</label>
            <input
                type="text"
                placeholder=""
                bind:value="{exercise.repsPerSet}"
                class="w-full bg-gray-300 p-1 text-right"
            />
        </div>
    </div>
{:else}{/if}

<style>
</style>
