<script lang="ts">
    import dayjs from 'dayjs';
    import { isMobile, userDB } from '$lib/stores/authStore.js';
    import {getContext, onMount} from 'svelte';
    import { AthleteData } from '$lib/classes/user/athlete';
    import { ProgramService } from '$lib/service/ProgramService';
    import { goto } from '$app/navigation';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';

    export let show: boolean = false;
    export let programName: string = '';

    let { getProgram } = getContext('program');
    let program = getProgram();

    let athletes: AthleteData[] = [];
    let selectedAthleteId: string = '';

    const currentYear: number = dayjs().year();
    let selectedMonth: string = '';
    let selectedDay: string = '';
    const monthOptions: string[] = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'November',
        'December',
    ];
    let dayOptions: string[] = [];

    let loading: boolean = false;
    let success: boolean = false;
    let error: boolean = false;

    $: isInvalid = () => {
        if (!programName.trim()) {
            return true;
        } else return selectedAthleteId && (!selectedMonth || !selectedDay);
    };

    const selectMonth = (month: string) => {
        selectedMonth = month;
        let startNum = 0;
        dayOptions = Array(dayjs(month + ' ' + currentYear).daysInMonth())
            .fill(0)
            .map(_ => ++startNum) as string[];
    };

    const selectDay = (day: string) => {
        selectedDay = day;
    };

    const createProgram = async () => {
        error = false;
        success = false;
        loading = true;
        try {
            const newProgram = $program;
            newProgram.name = programName;
            newProgram.athleteId = selectedAthleteId;
            newProgram.startDate = dayjs(
                selectedMonth + ' ' + selectedDay + ', ' + currentYear
            );
            newProgram.endDate = newProgram.startDate;
            newProgram.coachId = $userDB.coachData.id;

            const res = await ProgramService.createProgram(newProgram);
            if (!res.id) {
                throw new Error('Could not create program');
            }
            success = true;
            await goto(`/home/coach/program/${res.id}`);
        } catch (e) {
            console.log(e);
            error = true;
        } finally {
            loading = false;
        }
    };

    onMount(() => {
        if (!$userDB?.coachData?.teams) {
            return;
        }
        athletes = $userDB.coachData.athletes.map(a => a);
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
        <h1 class="text-2xl font-bold text-yellow-lt lg:text-4xl">
            Create A Program
        </h1>
    </header>
    <div class="w-full p-3 lg:flex lg:flex-col lg:items-center">
        <div class="my-2 flex flex-col">
            <label class="text-sm">Program Name</label>
            <input
                type="text"
                class="bg-gray-300 p-1 lg:w-full"
                bind:value="{programName}"
            />
        </div>
        <div class="my-2 flex flex-col">
            <label class="text-sm">Athlete</label>
            <select
                class="rounded bg-gray-300 p-2 lg:w-full lg:p-1"
                on:change="{e => (selectedAthleteId = e.target.value)}"
            >
                <option disabled>Select Athlete</option>
                <option>No Athlete</option>
                {#each athletes as athlete}
                    <option value="{athlete.id}">{athlete.name}</option>
                {/each}
            </select>
        </div>
        <div
            class="my-2 flex flex-col lg:w-2/12 {$isMobile
                ? 'items-center'
                : ''}"
        >
            <label class="text-sm">Start Date</label>
            <div class="flex justify-between">
                <select
                    class="w-fit rounded bg-gray-300 p-2 lg:p-1 {$isMobile
                        ? 'mx-4'
                        : ''}"
                    disabled="{!selectedAthleteId}"
                    on:change="{e => selectMonth(e.target.value)}"
                >
                    <option disabled selected>Month</option>
                    {#each monthOptions as month}
                        <option>{month}</option>
                    {/each}
                </select>
                <select
                    class="w-fit rounded bg-gray-300 p-2 lg:p-1 {$isMobile
                        ? 'mx-4'
                        : ''}"
                    disabled="{!selectedAthleteId || !selectedMonth}"
                    on:change="{e => selectDay(e.target.value)}"
                >
                    <option disabled selected>Day</option>
                    {#each dayOptions as day}
                        <option>{day}</option>
                    {/each}
                </select>
            </div>
        </div>
        <div class="mt-8 flex justify-center">
            <button
                class="m-auto flex rounded bg-yellow p-2 px-4 text-lg font-bold
                           text-gray-300 hover:bg-yellow-shade disabled:bg-yellow-lt"
                disabled="{isInvalid() || loading}"
                on:click="{createProgram}"
            >
                {#if loading}
                    <LoadingSpinner
                        spinnerColor="fill-gray-300"
                        spinnerBackground="text-yellow-lt"
                    />
                {/if}
                Create
            </button>
        </div>
        {#if error}
            <div
                class="my-8 rounded border border-red p-2 px-4 text-center font-semibold text-red"
            >
                There was an error creating your program
            </div>
        {/if}
        {#if success}
            <div
                class="my-8 rounded border border-green p-2 px-4 text-center font-semibold text-green"
            >
                Created!
            </div>
        {/if}
    </div>
</div>

<style></style>
