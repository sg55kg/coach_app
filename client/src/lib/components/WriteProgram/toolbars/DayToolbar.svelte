<script lang="ts">
    import { getContext, onMount } from 'svelte';

    const {
        getDayClipboard,
        copyDay,
        pasteDay,
        getSelectedDayIdx,
        clearExercises,
        getSelectedDay,
        updateProgram,
        getProgram,
        getSelectedExerciseIdx,
        copyExercise,
        pasteExercise,
        getExerciseClipboard,
    } = getContext('program');
    const dayClipboard = getDayClipboard();
    const index = getSelectedDayIdx();
    const program = getProgram();
    const exerciseIndex = getSelectedExerciseIdx();
    const exerciseClipboard = getExerciseClipboard();

    const pressedKeys = {
        ctrl: false,
        e: false,
        v: false,
        d: false,
        c: false,
    };

    onMount(() => {
        // TODO: retry implementing this
        // if ($isMobile) {
        //     return;
        // }
        // const handleKeyDown = (event: KeyboardEvent) => {
        //     if (event.metaKey || event.ctrlKey) {
        //         pressedKeys.ctrl = true;
        //     }
        //     if (event.key === 'v') {
        //         pressedKeys.ctrl = true;
        //         pressedKeys.v = true;
        //     }
        //     if (event.key === 'e') {
        //         pressedKeys.ctrl = true;
        //         pressedKeys.e = true;
        //     }
        //     if (event.key === 'd') {
        //         pressedKeys.ctrl = true;
        //         pressedKeys.d = true;
        //     }
        // }
        //
        // const handleKeyUp = (event: KeyboardEvent) => {
        //     if () {
        //         pressedKeys.ctrl = false;
        //         console.log('ctrl false')
        //     } else if ((event.metaKey || event.ctrlKey) || event.key === 'v') {
        //         pressedKeys.v = false;
        //         console.log('v false')
        //     } else if (event.key === 'c') {
        //         pressedKeys.c = false;
        //         console.log('c false')
        //     } else if (event.key === 'e') {
        //         pressedKeys.e = false;
        //         console.log('e false')
        //     }
        // }
        // document.addEventListener('keydown', handleKeyDown);
        // document.addEventListener('keyup', handleKeyUp);
        // return () => {
        //     document.removeEventListener('keydown', handleKeyDown);
        //     document.removeEventListener('keyup', handleKeyUp);
        // }
    });
</script>

<div class="fixed bottom-0 left-0 right-0 flex justify-around bg-gray-100">
    {#if $exerciseIndex > -1}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => copyExercise($exerciseIndex)}"
        >
            Copy Exercise
        </button>
    {:else}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => copyDay($index)}"
        >
            Copy Day
        </button>
    {/if}
    {#if $dayClipboard.length > 0 && $exerciseIndex < 0}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => pasteDay($index)}"
        >
            Paste Day
        </button>
    {/if}
    {#if $exerciseClipboard.length > 0}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{pasteExercise}"
        >
            Paste Exercise
        </button>
    {/if}
    {#if $program.days[$index].exercises.length > 0 && $exerciseIndex < 0}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => clearExercises($index)}"
        >
            Clear Exercises
        </button>
    {/if}
    {#if $program.id}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{updateProgram}"
        >
            Save
        </button>
    {/if}
</div>

<style></style>
