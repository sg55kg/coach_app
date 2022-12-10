<script lang="ts">
    import {program} from "../stores/programStore";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {Exercise, Program} from "../classes/program";
    import {onDestroy, onMount} from "svelte";
    import {Day} from "../classes/day";

    export let handleSubmit
    export let initialIndex = -1
    let selectedIndex = initialIndex

    const generateCSV = () => {
        let str = "Day 1\nExercise,Weight,Sets,Reps,Notes\n";
        for(let i=0;i< $program.days.length; i++) {
            if($program.days[i].exercises.length < 1) continue
            if (i > 0) str += "Day " + (i+1) + "\n"
            const d = $program.days[i]
            for(let j = 0; j < d.exercises.length; j++) {
                str += d.exercises[j].name + "," +
                    d.exercises[j].weight + "," +
                    d.exercises[j].repsPerSet + "," +
                    d.exercises[j].sets + "," +
                    d.exercises[j].notes + "\n"
            }
        }

        //const downloadLink = document.getElementById('download-btn');
        const csv = str
        const blob = new Blob(["\ufeff", csv], {type: 'text/csv'});
        const url = URL.createObjectURL(blob);
        // const exportFilename = programInput.value;
        // downloadLink.href = url;
        // downloadLink.setAttribute('download', exportFilename);
        // downloadLink.hidden = false;
    }

    const addDay = () => {
        let day = new Day()
        console.log($program.days.length)
        if($program.days.length > 0) {
            let today = $program.days[$program.days.length - 1].date
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

    const incrementSelectedIndex = () => {
        console.log(selectedIndex)
        console.log($program.days.length)
        selectedIndex === $program.days.length - 1 ? null : selectedIndex++
        console.log(selectedIndex)
    }
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
        if (document !== undefined)
            document.addEventListener('keyup', handleHotKeys)
    })

    onDestroy(() => {
        program.set(new Program())
    })
</script>

<div>
    <div class="p-4">
        <div class="flex justify-center">
            <input type="text"
                   placeholder="Program Name"
                   name="name"
                   bind:value={$program.name}
                   class="p-1">
        </div>
        <div class="p-4 flex justify-around w-9/12 m-auto">
            <div class="flex flex-col">
                <label>Start Date</label>
                <input type="date"
                       name="startDate"
                       bind:value={$program.startDate}
                       on:change={(e) => $program.startDate = new Date(e.target.value)}>
            </div>
            <div class="flex flex-col">
                <label>End Date</label>
                <input type="date"
                       name="endDate"
                       bind:value={$program.endDate}
                       on:change={(e) => $program.endDate = new Date(e.target.value)}>
            </div>
        </div>

    </div>
    <hr>
    <div class="flex justify-between">
        <div>
            <button type="button" class="bg-yellow text-white hover:bg-yellow-shade p-3" on:click={addDay}>
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
                <button type="button" class="bg-blue-500 text-white hover:bg-blue-600 p-2" on:click={addExercise}>
                    Add exercise (Shift +)
                </button>
            </div>
        {/if}
    </div>
    {#if selectedIndex > -1}
        {#each $program.days[selectedIndex].exercises as exercise, idx}
            <div class="flex flex-col p-2 justify-items-center">
                <div class="flex flex-row p-2 justify-between">
                    <input type="text"
                           name="name"
                           placeholder="Exercise name"
                           class="exercise-input"
                           bind:value={exercise.name}>
                    <input type="number"
                           name="weight"
                           placeholder="Weight"
                           class="exercise-input"
                           bind:value={exercise.weight}>
                    <input type="number"
                           name="sets"
                           placeholder="Sets"
                           class="exercise-input"
                           bind:value={exercise.sets}>
                    <input type="number"
                           name="repsPerSet"
                           placeholder="Reps"
                           class="exercise-input"
                           bind:value={exercise.repsPerSet}>
                </div>
                <textarea placeholder="Notes" name="notes" bind:value={exercise.notes} class="exercise-input"></textarea>
            </div>
        {/each}
        <div class="flex">
            <button type="button" class="bg-blue-500 text-white hover:bg-blue-600 p-2 mx-2">
                Make rest day (Shift *)
            </button>
        </div>
        <footer class="flex mt-2 justify-end w-full">
            <button type="button"
                    on:click={(e) => handleSubmit(e, $program)}
                    class="bg-emerald-500 hover:bg-emerald-700 mx-2 p-2 rounded-md">
                Save (Ctrl s)
            </button>
            <button type="button" class="bg-blue-500 hover:bg-blue-700 mx-2 p-2 rounded-md">
                Download CSV
            </button>
        </footer>
    {/if}
</div>

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