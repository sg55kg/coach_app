<script lang="ts">
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte';
    import { getContext } from 'svelte';
    import { generateCSV } from '$lib/components/WriteProgram/util/functions.js';

    export let showCreateProgram: boolean = false;
    export let showAssignAthlete: boolean = false;

    const { getProgram, updateProgram } = getContext('program');
    let program = getProgram();

    let showActionContext: boolean = false;

    const focusNameInput = () => {
        showActionContext = false;
        const input = document.getElementById('program-name-input');
        input.focus();
        input.classList.add('error-focus');
        input.addEventListener('focusout', () => {
            input.classList.remove('error-focus');
        });
    };
</script>

<button
    class="fixed bottom-5 left-10 right-10 rounded bg-yellow p-4 font-bold text-gray-300"
    on:click="{() => (showActionContext = !showActionContext)}"
>
    Options
</button>

{#if showActionContext}
    <div
        class="fixed top-0 bottom-0 right-0 left-0 z-0"
        on:click="{() => (showActionContext = false)}"
    ></div>
    <div
        class="fixed bottom-0 right-0 left-0 z-10 flex w-screen transform flex-col bg-gray-400 text-center transition-all ease-in-out
        {showActionContext ? ' -translate-y-0' : ' translate-y-0'}"
    >
        <button
            class="h-6 w-full pt-2"
            on:click="{() => (showActionContext = false)}"
        >
            <FaChevronDown />
        </button>
        {#if !$program.id}
            <button
                class="w-full p-4 text-lg"
                on:click="{() => (showCreateProgram = true)}"
            >
                Create Program
            </button>
        {/if}
        {#if !$program.athleteId}
            <button
                class="w-full p-4 text-lg"
                on:click="{() =>
                    $program.name
                        ? (showAssignAthlete = true)
                        : focusNameInput()}"
            >
                Assign To Athlete
            </button>
        {/if}
        {#if $program.id}
            <button class="w-full p-4 text-lg"> Make a Copy </button>
        {/if}
        <button class="w-full p-4 text-lg" on:click="{generateCSV}">
            Export to CSV
        </button>
        {#if $program.id}
            <button class="w-full p-4 text-lg" on:click="{updateProgram}">
                Save
            </button>
        {/if}
    </div>
{/if}

<style></style>
