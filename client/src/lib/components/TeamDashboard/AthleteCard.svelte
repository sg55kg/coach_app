<script lang="ts">
    import { onMount } from 'svelte';
    import dayjs from 'dayjs';
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte';
    import { userDB } from '$lib/stores/authStore';
    import { ProgramService } from '$lib/service/ProgramService';
    import { Program } from '$lib/classes/program';
    import { AthleteData } from '$lib/classes/user/athlete';
    import FaRegPlusSquare from 'svelte-icons/fa/FaRegPlusSquare.svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import FaPen from 'svelte-icons/fa/FaPen.svelte';
    import { team } from '$lib/stores/teamStore.js';
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte';
    import UserService from '../../service/UserService';

    export let athlete: AthleteData;

    let updateSeverity: 'low' | 'moderate' | 'severe' | 'none' | 'over';
    let lastDay: dayjs = dayjs();
    let editProgramName: boolean = false;
    let showAthleteOptions: boolean = false;

    const saveProgramName = async () => {
        try {
            const updatedProgram = {
                ...athlete.currentProgram,
            } as Program;
            const program: Program = await ProgramService.updateProgram(
                updatedProgram
            );
            userDB.update(prev => {
                prev!.coachData!.programs = prev!.coachData!.programs!.map(p =>
                    p.id === program.id ? program : p
                );
                return prev;
            });
            athlete.currentProgram = program;
            editProgramName = false;
        } catch (e) {
            console.log(e);
        }
    };

    const handleRemoveAthlete = async () => {
        try {
            const updatedAthlete = AthleteData.createFrom(
                JSON.parse(JSON.stringify(athlete))
            );
            updatedAthlete.coachId = '';
            updatedAthlete.team = null;
            await UserService.updateAthleteData(updatedAthlete);
            $team.athletes = $team.athletes.filter(
                a => a.id !== updatedAthlete.id
            );
            for (const t of $userDB.coachData.teams) {
                if (t.id === $team.id) {
                    t.athletes = t.athletes.filter(
                        a => a.id !== updatedAthlete.id
                    );
                }
            }
        } catch (e) {
            console.log(e);
        }
    };

    onMount(() => {
        if (!athlete.currentProgram) {
            return (updateSeverity = 'none');
        }
        const today = dayjs();
        const lastEntered = athlete.currentProgram.days.find(
            d => d.exercises.length < 1 && !d.isRestDay
        );
        if (!lastEntered) {
            if (athlete.currentProgram.days.length > 0) {
                if (
                    dayjs(
                        athlete.currentProgram.days[
                            athlete.currentProgram.days.length - 1
                        ].date
                    ).valueOf() <= today.valueOf()
                ) {
                    lastDay = dayjs(
                        athlete.currentProgram.days[
                            athlete.currentProgram.days.length - 1
                        ].date
                    );

                    return (updateSeverity = 'over');
                }
                return (updateSeverity = 'low');
            }

            return (updateSeverity = 'none');
        }
        const lastUpdatedDay = dayjs(lastEntered.date);
        lastDay = dayjs(
            athlete.currentProgram.days[athlete.currentProgram.days.length - 1]
                .date
        );
        if (today.valueOf() >= lastUpdatedDay.valueOf()) {
            return (updateSeverity = 'over');
        }
        const diff = dayjs(lastUpdatedDay).diff(today, 'days');

        if (diff <= 2) {
            updateSeverity = 'severe';
        } else if (diff <= 6) {
            updateSeverity = 'moderate';
        } else {
            updateSeverity = 'low';
        }
    });
</script>

<div class="relative my-2 rounded bg-gray-200 p-2">
    <div class="flex flex-col items-center lg:flex-row lg:justify-between">
        <a href="/home/coach/team/athlete/{athlete.id}">
            <h2 class="text-xl font-semibold">
                {athlete.name}
            </h2>
        </a>
    </div>

    {#if athlete.currentProgram}
        <div
            class="flex flex-col items-center justify-start text-lg lg:items-start"
        >
            {#if !editProgramName}
                <div class="flex justify-center">
                    {#if updateSeverity === 'severe'}
                        <div class="flex items-center">
                            <div
                                class="mr-2 h-3 w-3 rounded-full bg-red-shade"
                            ></div>
                            <h5
                                class="text-center text-xl text-red-shade {$isMobile
                                    ? 'w-12'
                                    : 'w-full'} overflow-hidden"
                            >
                                {athlete.currentProgram.name}
                            </h5>
                        </div>
                    {:else if updateSeverity === 'moderate'}
                        <div class="flex items-center">
                            <div
                                class="mr-2 h-3 w-3 rounded-full bg-yellow"
                            ></div>
                            <h5 class="text-center text-xl text-yellow">
                                {athlete.currentProgram.name}
                            </h5>
                        </div>
                    {:else if updateSeverity === 'low'}
                        <div class="flex items-center">
                            <div
                                class="mr-2 h-3 w-3 rounded-full bg-green"
                            ></div>
                            <h5 class="text-center text-xl text-green">
                                {athlete.currentProgram.name}
                            </h5>
                        </div>
                    {:else if updateSeverity === 'none'}
                        <p class="m-0 text-center text-base font-normal">
                            {athlete.name} does not have a current program!
                        </p>
                    {:else}
                        <div class="flex items-center">
                            <div
                                class="mr-2 h-3 w-3 rounded-full bg-orange"
                            ></div>
                            <h5
                                class="h-content max-w-[16em] overflow-hidden truncate text-center text-xl text-orange"
                            >
                                {athlete.currentProgram.name}
                            </h5>
                        </div>
                    {/if}
                </div>
                {#if athlete.currentProgram && $isMobile}
                    {dayjs(athlete.currentProgram.startDate).format(
                        'ddd MMM DD'
                    )} - {dayjs(athlete.currentProgram.endDate).format(
                        'ddd MMM DD'
                    )}
                {/if}
            {:else}
                <input
                    class="bg-gray-300 text-textblue"
                    bind:value="{athlete.currentProgram.name}"
                />
                <button
                    class="mx-2 px-2 text-sm hover:bg-textblue hover:text-black"
                    on:click="{saveProgramName}">Save</button
                >
                <button
                    class="mx-2 px-2 text-sm text-red hover:bg-gray-100 hover:text-red-shade"
                    on:click="{() => (editProgramName = false)}">Cancel</button
                >
            {/if}
        </div>
        <div class="flex justify-center lg:justify-start">
            {#if athlete?.currentProgram}
                <a href="{`/home/coach/program/${athlete.currentProgram.id}`}">
                    <button
                        class="my-2 flex items-center rounded-sm bg-yellow px-1 py-px text-gray-300 hover:bg-yellow-shade"
                    >
                        <span class="mx-1 h-3 w-3"><FaPen /></span>
                        Edit Program
                    </button>
                </a>
            {/if}
        </div>
    {:else}
        <p class="m-0 text-center text-base font-normal lg:text-left">
            {athlete.name} does not have a current program
        </p>
        <div class="mt-2 flex justify-around">
            <div class="h-6 text-link duration-300 hover:text-link-shade">
                <a href="{`/home/coach/team/${$team.id}/programs`}">
                    <button class="flex">
                        <span class="mr-1 h-6 w-6 lg:mr-4">
                            <FaRegPlusSquare />
                        </span>
                        <span>New Program</span>
                    </button>
                </a>
            </div>
        </div>
    {/if}
    <div class="absolute right-0 top-0">
        <div class="relative">
            <button
                class="h-10 w-10 p-2"
                on:click="{() => (showAthleteOptions = !showAthleteOptions)}"
            >
                <FaChevronDown />
            </button>
            {#if showAthleteOptions}
                <div class="absolute right-0 bg-gray-100 p-2">
                    <button
                        class="w-72 rounded border border-red bg-gray-100 p-2 text-red hover:bg-gray-200"
                        on:click="{handleRemoveAthlete}"
                    >
                        Remove {athlete.name}
                    </button>
                </div>
            {/if}
        </div>
    </div>
</div>

<style>
</style>
