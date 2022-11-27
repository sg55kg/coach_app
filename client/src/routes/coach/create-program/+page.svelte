<script lang="ts">
    import {program} from "./store";
    import {Day, Exercise, Program} from "../../../lib/classes/program";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {onMount, onDestroy} from "svelte";

    let selectedIndex: number = -1
    let section: HTMLDivElement

    const addDay = () => {
        let day = new Day()
        console.log($program.days.length)
        if($program.days.length > 0) {
            let today = new Date()
            day.date.setDate(today.getDate() + $program.days.length)
        }

        program.update(p => {
            p.days.push(day)
            return p
        })
        selectedIndex = $program.days.length - 1
    }

    const addExercise = () => {
        program.update(p => {
            p.days[selectedIndex].exercises.push(new Exercise())
            return p
        })
    }

    const removeLastExercise = () => {
        program.update(p => {
            p.days[selectedIndex].exercises.pop()
            return p
        })
    }

    const incrementSelectedIndex = () => selectedIndex === $program.days.length - 1 ? null : selectedIndex++
    const decrementSelectedIndex = () => selectedIndex === 0 ? null : selectedIndex--

    const handleHotKeys = (e: KeyboardEvent) => {
        e.stopPropagation()
        console.log(e.key)
        switch (e.key) {
            case '+':
                if (selectedIndex > -1)
                    addExercise()
                break;
            case '^':
                addDay()
                break;
            case '-':
                removeLastExercise()
                break;
            case 'ArrowRight':
                if (selectedIndex > -1)
                    incrementSelectedIndex()
                break;
            case 'ArrowLeft':
                if(selectedIndex > -1)
                    decrementSelectedIndex()
                break;
        }
    }

    onMount(() => {
        document.addEventListener('keyup', handleHotKeys)
    })

    onDestroy(() => {
        document.removeEventListener('keyup', handleHotKeys)
        program.update(() => {
            return new Program()
        })
    })

</script>

<section bind:this={section}>
    <div class="p-4">
        <div class="flex justify-center">
            <input type="text" placeholder="Program Name" bind:value={$program.name} class="p-1">
        </div>
        <div class="p-4 flex justify-around w-9/12 m-auto">
            <div class="flex flex-col">
                <label>Start Date</label>
                <input type="date"
                       bind:value={$program.startDate}
                       on:change={(e) => $program.startDate = new Date(e.target.value)}>
            </div>
            <div class="flex flex-col">
                <label>End Date</label>
                <input type="date"
                       bind:value={$program.endDate}
                       on:change={(e) => $program.endDate = new Date(e.target.value)}>
            </div>
        </div>

    </div>
    <hr>
    <div class="flex justify-between">
        <div>
            <button  class="bg-blue-500 text-white hover:bg-blue-600 p-3" on:click={addDay}>
                Add Day (Shift ^)
            </button>
        </div>
        {#if $program && $program.days.length > 0 && selectedIndex > -1}
            <div class="flex justify-center align-middle">
                <div class="w-8 text-white hover:text-gray-200 hover:cursor-pointer"
                     on:click={decrementSelectedIndex}>
                    <FaAngleLeft />
                </div>
                    <h4 class="m-0 mx-2 self-center text-xl">{$program.days[selectedIndex].date.toDateString()}</h4>
                <div class="w-8 text-white hover:text-gray-200 hover:cursor-pointer"
                     on:click={incrementSelectedIndex}>
                    <FaAngleRight />
                </div>
            </div>
            <div>
                <button class="bg-blue-500 text-white hover:bg-blue-600 p-2" on:click={addExercise}>
                    Add exercise (Shift +)
                </button>
            </div>
        {/if}
    </div>
    {#if selectedIndex > -1}
        {#each $program.days[selectedIndex].exercises as exercise}
            <div class="flex flex-col p-2 justify-items-center">
                <div class="flex flex-row p-2 justify-between">
                    <input type="text" placeholder="Exercise name" class="exercise-input" bind:value={exercise.name}>
                    <input type="number" placeholder="Weight" class="exercise-input" bind:value={exercise.weight}>
                    <input type="number" placeholder="Sets" class="exercise-input" bind:value={exercise.sets}>
                    <input type="number" placeholder="Reps" class="exercise-input" bind:value={exercise.repsPerSet}>
                </div>
                <textarea placeholder="Notes" class="exercise-input"></textarea>
            </div>
        {/each}
        <div class="flex">
            <button class="bg-blue-500 text-white hover:bg-blue-600 p-2 mx-2" on:click={addExercise}>
                Add exercise (Shift +)
            </button>
            <button class="bg-blue-500 text-white hover:bg-blue-600 p-2 mx-2">
                Make rest day (Shift *)
            </button>
        </div>
        <footer class="flex mt-2 justify-end w-full">
            <button class="bg-emerald-500 hover:bg-emerald-700 mx-2 p-2 rounded-md">
                Save (Ctrl s)
            </button>
            <button class="bg-emerald-500 hover:bg-emerald-700 mx-2 p-2 rounded-md">
                Download CSV
            </button>
        </footer>
    {/if}
</section>

<style>
    .exercise-input {
        background: transparent;
        padding: .5em;
        font-weight: bold;
    }

    .exercise-input:focus {
        outline: none;
        background: lightgray;
    }
</style>