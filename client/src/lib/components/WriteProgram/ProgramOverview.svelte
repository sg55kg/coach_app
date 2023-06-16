<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import DayCard from '$lib/components/WriteProgram/DayCard.svelte';
    import ProgramSearch from '$lib/components/WriteProgram/modals/ProgramSearch.svelte';
    import { Program } from '$lib/classes/program';
    import {getContext, setContext} from 'svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import ExpandedDay from '$lib/components/WriteProgram/ExpandedDay.svelte';
    import CreateProgramModal from '$lib/components/WriteProgram/modals/CreateProgramModal.svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import AssignAthleteModal from '$lib/components/WriteProgram/modals/AssignAthleteModal.svelte';
    import ProgramToolbarDesktop from "$lib/components/WriteProgram/toolbars/ProgramToolbarDesktop.svelte";
    import ProgramToolbarMobile from "$lib/components/WriteProgram/toolbars/ProgramToolbarMobile.svelte";

    let showActionContext: boolean = false;
    let contextCoordinates: { x: number; y: number } = { x: -1, y: -1 };
    let showCreateProgram: boolean = false;
    let showAssignAthlete: boolean = false;
    let daysContainer: HTMLDivElement;

    const {
        getProgram,
        getProgramError,
        getProgramSuccess,
        getProgramLoading,
        addDay,
        getSelectedDayIdx
    } = getContext('program');

    let program = getProgram();
    let programError = getProgramError();
    let programSuccess = getProgramSuccess();
    let programLoading = getProgramLoading();
    let selectedDayIdx = getSelectedDayIdx();

    $: $programError
        ? setTimeout(() => {
              programError.set('');
          }, 5000)
        : null;
    $: $programSuccess
        ? setTimeout(() => {
              programSuccess.set('');
          }, 5000)
        : null;
    $: $programLoading ? showActionContext = false : null;

</script>

<div
    bind:this="{daysContainer}"
    class="relative flex h-[90vh] w-screen flex-col overflow-y-auto pb-32"
>
    {#if !$isMobile}
        <ProgramToolbarDesktop bind:showAssignAthlete={showAssignAthlete} bind:showCreateProgram={showCreateProgram} />
    {/if}
    <header class="flex {$isMobile ? 'flex-row' : 'flex-col'} items-center p-3">
        {#if $isMobile}
            <input
                type="text"
                class="mx-4 my-2 w-11/12 bg-gray-300 p-1 font-semibold"
                placeholder="Program Name"
                id="program-name-input"
                bind:value="{$program.name}"
            />
        {/if}
        <ProgramSearch />
    </header>
    <div
        class="grid {$isMobile
            ? 'grid-cols-1 gap-10'
            : 'grid-cols-7 gap-4'} w-full p-3"
    >
        {#each $program.days as day, index (day.id + index)}
            <DayCard
                bind:day="{day}"
                idx="{index}"
                container="{daysContainer}"
                bind:contextCoordinates="{contextCoordinates}"
            />
        {/each}
        <div
            class="flex aspect-square justify-center bg-gray-200 {$isMobile
                ? 'mx-10'
                : ''} items-center hover:scale-105 hover:cursor-pointer"
            on:click="{addDay}"
        >
            <div class="h-7 w-7 text-yellow">
                <FaPlus />
            </div>
        </div>
    </div>
</div>
{#if $isMobile}
    <ProgramToolbarMobile bind:showCreateProgram={showCreateProgram} />
{/if}

{#if $selectedDayIdx > -1}
    <ExpandedDay />
{/if}

{#if showCreateProgram}
    <CreateProgramModal bind:show="{showCreateProgram}" programName="{$program.name}" />
{/if}
{#if showAssignAthlete}
    <AssignAthleteModal bind:show="{showAssignAthlete}" bind:showCreateProgram={showCreateProgram} />
{/if}
{#if $programLoading}
    <div
        class="absolute top-0 bottom-0 right-0 left-0 z-[110] bg-gray-300 opacity-25"
    ></div>
    <div
        class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center"
    >
        <LoadingSpinner spinnerColor="fill-yellow" height="h-10" width="w-10" />
    </div>
{/if}
{#if $programSuccess}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-green bg-gray-200 p-4 text-green shadow-2xl shadow-black lg:w-4/12"
    >
        {$programSuccess}
        <button
            class="h-8 w-8 rounded-full p-1 text-green-dark hover:bg-gray-400 hover:text-green"
            on:click="{() => ($programSuccess = '')}"
        >
            <MdClose />
        </button>
    </div>
{:else if $programError}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-red-shade bg-gray-200 p-4 text-red shadow-2xl shadow-black lg:w-4/12"
    >
        {$programError}
        <button
            class="h-8 w-8 rounded-full p-1 hover:bg-gray-400 hover:text-red-shade"
            on:click="{() => ($programError = '')}"
        >
            <MdClose />
        </button>
    </div>
{/if}

<style>
    li,
    button,
    h3 {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
    }

    :global(.error-focus:focus) {
        outline: none;
        border: 2px solid #f62222;
        border-radius: 5px;
    }
</style>
