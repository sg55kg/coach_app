<script lang="ts">
    import { getContext, onMount } from 'svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { AthleteData } from '$lib/classes/user/athlete';
    import { userDB } from '$lib/stores/authStore';
    import dayjs, { Dayjs } from 'dayjs';
    import { ProgramService } from '$lib/service/ProgramService';
    import { goto } from '$app/navigation';
    import { team } from '$lib/stores/teamStore';

    export let show: boolean = false;

    const { getProgram } = getContext('program');
    const program = getProgram();

    let loading: boolean = false;
    let success: boolean = false;
    let error: boolean = false;

    let selectedAthlete: AthleteData | undefined;
    let selectedStartDate: string = '';
    let athletes: AthleteData[] = [];
    let newProgramName: string = $program.name;
    let isCurrent: boolean = true;

    const selectAthlete = (id: string) => {
        const athlete = athletes.find(a => a.id === id);
        selectedAthlete = athlete;
    };

    const assignToAthlete = async (makeCopy: boolean = true) => {
        loading = true;
        success = false;
        error = false;
        try {
            const programCopy = JSON.parse(JSON.stringify($program));
            programCopy.startDate = dayjs(selectedStartDate);
            programCopy.id = '';
            let currentDate = programCopy.startDate;
            programCopy.days.forEach(d => {
                d.date = currentDate;
                currentDate = currentDate.add(1, 'day');
            });
            programCopy.endDate = currentDate.subtract(1, 'day');
            programCopy.athleteId = selectedAthlete.id;
            programCopy.coachId = $userDB.coachData.id;
            programCopy.teamId = selectedAthlete.team.id;
            if (makeCopy) {
                programCopy.days.forEach(d => {
                    d.id = '';
                    d.exercises.forEach(e => {
                        e.id = '';
                        e.dropSets.forEach(d => (d.id = ''));
                    });
                });
                const res = await ProgramService.createProgram({
                    ...programCopy,
                    isCurrent,
                });
                show = false;
                await goto(`/home/coach/program/${res.id}`);
            } else {
                const res = await ProgramService.updateProgram({
                    ...programCopy,
                    isCurrent,
                });
                $program = res;
                show = false;
            }
        } catch (e) {
            console.log(e);
            error = true;
        } finally {
            loading = false;
        }
    };

    onMount(() => {
        athletes = $userDB?.coachData?.athletes;
    });
</script>

<div
    class="absolute z-[100] h-screen w-screen bg-gray-300 opacity-50 lg:-mt-[7em]"
    on:click="{() => (show = false)}"
></div>
<div
    class="absolute bottom-10 top-10 left-10 right-10 z-[105] flex flex-col rounded
            bg-gray-200 shadow-md shadow-black lg:left-72 lg:right-72 lg:bottom-20 lg:items-center"
>
    <header class="flex justify-center p-4">
        <h1 class="text-center text-2xl font-bold text-yellow-lt lg:text-4xl">
            Assign Athlete to {$program.name}
        </h1>
    </header>
    <div class="flex flex-col items-center p-2">
        <select
            class="rounded bg-gray-300 p-1"
            on:change="{e => selectAthlete(e.target.value)}"
        >
            <option disabled selected>Select an athlete</option>
            {#each athletes as athlete}
                <option value="{athlete.id}">{athlete.name}</option>
            {/each}
        </select>
        <div class="my-3 flex flex-col">
            <label for="program-start-date-picker">Start Date</label>
            <input
                id="program-start-date-picker"
                class="w-full bg-gray-300"
                type="date"
                bind:value="{selectedStartDate}"
            />
        </div>
        <div class="my-3 flex flex-col">
            <label
                >Rename Program {selectedAthlete?.name
                    ? 'for ' + selectedAthlete.name
                    : ''} (optional)</label
            >
            <input
                id="rename-program-input"
                class="bg-gray-300 p-1"
                type="text"
                bind:value="{newProgramName}"
            />
        </div>
        <div class="my-3 flex">
            <p>
                Make this {selectedAthlete?.name
                    ? `${selectedAthlete.name}'s current program?`
                    : 'program current?'}
            </p>
            <input
                type="checkbox"
                class="mx-2"
                checked="{isCurrent}"
                on:change="{e => (isCurrent = e.target.checked)}"
            />
        </div>
    </div>
    <div class="mt-8 flex flex-col items-center">
        <button
            class="m-auto my-2 flex rounded bg-yellow p-2 px-4 text-lg font-bold
                           text-gray-300 hover:bg-yellow-shade disabled:bg-yellow-lt disabled:bg-yellow-lt"
            disabled="{!selectedAthlete || !selectedStartDate || loading}"
            on:click="{assignToAthlete}"
        >
            {#if loading}
                <LoadingSpinner
                    spinnerColor="fill-gray-300"
                    spinnerBackground="text-yellow-lt"
                />
            {:else}
                Assign {selectedAthlete?.name
                    ? `to ${selectedAthlete.name}`
                    : ''} as a new program
            {/if}
        </button>
        {#if $program.id}
            <button
                class="m-auto my-2 flex rounded bg-yellow p-2 px-4 text-lg font-bold
                           text-gray-300 hover:bg-yellow-shade disabled:bg-yellow-lt disabled:bg-yellow-lt"
                disabled="{!selectedAthlete || !selectedStartDate || loading}"
                on:click="{assignToAthlete}"
            >
                {#if loading}
                    <LoadingSpinner
                        spinnerColor="fill-gray-300"
                        spinnerBackground="text-yellow-lt"
                    />
                {:else}
                    Assign {selectedAthlete?.name
                        ? `to ${selectedAthlete.name}`
                        : ''}
                {/if}
            </button>
        {/if}
    </div>

    {#if error}
        <div
            class="my-4 mx-2 rounded border border-red p-2 px-4 text-center font-semibold text-red"
        >
            There was an error assigning this program
        </div>
    {/if}
    {#if success}
        <div
            class="my-4 mx-2 rounded border border-green p-2 px-4 text-center font-semibold text-green"
        >
            Success!
        </div>
    {/if}
</div>

<style></style>
