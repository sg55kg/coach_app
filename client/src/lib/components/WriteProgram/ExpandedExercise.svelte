<script lang="ts">
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte';
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import { Exercise } from '$lib/classes/program/exercise';
    import { getContext, onDestroy } from 'svelte';
    import ExerciseRow from '$lib/components/WriteProgram/ExerciseRow.svelte';
    import { ExerciseType } from '../../classes/program/exercise/enums';

    export let exercise: Exercise;

    const {
        getProgram,
        getSelectedDay,
        getSelectedExerciseIdx,
        getProgramError,
    } = getContext('program');
    const program = getProgram();
    const selectedDay = getSelectedDay();
    const selectedExerciseIdx = getSelectedExerciseIdx();
    const error = getProgramError();

    let useWeightForAccessory: boolean = false;

    const addDropSet = () => {
        const newExercise = new Exercise();
        newExercise.type = exercise.type;
        newExercise.name = exercise.name;
        newExercise.nameArr = [...exercise.nameArr];
        newExercise.repArr = [...exercise.repArr];
        newExercise.order = exercise.dropSets.length;
        exercise.dropSets = [...exercise.dropSets, newExercise];
    };

    const toggleAccessory = (makeAccessory: boolean) => {
        if (exercise.type === ExerciseType.COMPLEX) {
            $error = 'Cannot make a complex an accessory exercise';
            return;
        }
        if (makeAccessory) {
            exercise.type = ExerciseType.ACCESSORY;
            exercise.dropSets.forEach(d => (d.type = ExerciseType.ACCESSORY));
        } else {
            exercise.type = ExerciseType.EXERCISE;
            exercise.dropSets.forEach(d => (d.type = ExerciseType.EXERCISE));
        }
    };

    const toggleUseWeightForAccessory = () => {
        useWeightForAccessory = !useWeightForAccessory;
        if (!useWeightForAccessory) {
            exercise.weight = 0;
            exercise.dropSets.forEach(d => (d.weight = 0));
        }
    };

    onDestroy(() => {
        // sync the program with any potential changes that might have been made to this day
        if (!$selectedDay) return;
        const day = $program.days.find(d => d.id === $selectedDay.id);
        let ex: Exercise = day.exercises[$selectedExerciseIdx];
        ex = exercise;
        $program = $program;
        $selectedExerciseIdx = -1;
    });
    $: console.log(useWeightForAccessory);
</script>

<div
    class="absolute right-1 left-1 z-20 flex h-[70vh] flex-col items-center overflow-y-auto bg-gray-200 lg:p-8"
>
    <ExerciseRow
        bind:exercise="{exercise}"
        useWeightForAccessory="{useWeightForAccessory}"
    />
    {#each exercise.dropSets as dropSet}
        <ExerciseRow
            bind:exercise="{dropSet}"
            parentExerciseId="{exercise.id ? exercise.id : '_'}"
            useWeightForAccessory="{useWeightForAccessory}"
        />
    {/each}
    <button class="my-2 h-7 w-7 text-yellow" on:click="{addDropSet}">
        <FaPlus />
    </button>
    <div class="mt-2">
        <label class="switch">
            <input
                type="checkbox"
                checked="{exercise.type === ExerciseType.ACCESSORY}"
                on:change="{e => toggleAccessory(e.target.checked)}"
            />
            <span class="slider round"></span>
        </label>
        <label class="text-sm"
            >{exercise.type === ExerciseType.ACCESSORY
                ? 'Accessory'
                : 'Main Lift'}</label
        >
    </div>
    {#if exercise.type === ExerciseType.ACCESSORY}
        <select
            class="my-2 bg-gray-200 text-sm"
            on:change="{toggleUseWeightForAccessory}"
        >
            <option selected="{useWeightForAccessory}">Weight (kg)</option>
            <option selected="{useWeightForAccessory === false}">Effort</option>
        </select>
    {/if}
    <div class="flex w-full flex-col items-start p-2">
        <label>Exercise Notes</label>
        <textarea class="w-full bg-gray-300 p-px" bind:value="{exercise.notes}"
        ></textarea>
    </div>
    <button
        class="mx-4 mt-6 mb-2 h-6 w-6 text-yellow"
        on:click="{() => ($selectedExerciseIdx = -1)}"
    >
        <FaChevronUp />
    </button>
</div>

<style>
    .switch {
        position: relative;
        display: inline-block;
        width: 55px;
        height: 28px;
    }

    /* Hide default HTML checkbox */
    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: 0.4s;
        transition: 0.4s;
    }

    .slider:before {
        position: absolute;
        content: '';
        height: 20px;
        width: 20px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: 0.4s;
        transition: 0.4s;
    }

    input:checked + .slider {
        background-color: #fde577;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #fde577;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }
</style>
