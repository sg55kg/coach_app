<script lang="ts">
    import { getContext, onDestroy, onMount, setContext } from 'svelte';
    import { userDB } from '$lib/stores/authStore';
    import dayjs from 'dayjs';
    import { goto } from '$app/navigation';
    import { Program, type ProgramDTO } from '$lib/classes/program';
    import AthleteExpandedDay from '$lib/components/AthleteProgram/AthleteExpandedDay.svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { athleteProgramContext } from '../../../lib/contexts/athleteProgramContext.js';
    import { Day } from '$lib/classes/program/day';
    import MdClose from 'svelte-icons/md/MdClose.svelte';

    setContext('athlete-program', athleteProgramContext);

    const {
        getCurrentProgram,
        getCurrentDay,
        getAthleteProgramError,
        getAthleteProgramLoading,
        getAthleteProgramSuccess,
    } = athleteProgramContext;
    const currentProgram = getCurrentProgram();
    const currentDay = getCurrentDay();
    const error = getAthleteProgramError();
    const loading = getAthleteProgramLoading();
    const success = getAthleteProgramSuccess();

    const fetchCurrentProgram = async () => {
        if (!$userDB?.athleteData?.currentProgram) {
            return;
        }
        try {
            const today = dayjs();
            $currentProgram = $userDB.athleteData.currentProgram;
            const day = $currentProgram.days.find(d =>
                dayjs(d.date).isSame(today, 'days')
            );

            if (day && !day.isRestDay && day.exercises.length > 0) {
                day.exercises.sort((a, b) => a.order - b.order);
                $currentDay = day;
            }
        } catch (e) {
            console.log(e);
        }
    };

    onMount(async () => {
        if ($userDB && !$userDB?.athleteData) {
            await goto(`/home/athlete/get-started/${$userDB.id}`);
        }
    });

    onDestroy(() => {
        $currentDay = new Day();
        $currentProgram = new Program();
    });
</script>

<div class="flex flex-col">
    <div
        class="m-2 flex justify-center sm:text-center md:text-center lg:justify-start lg:text-start"
    >
        <h1 class="w-fit text-xl font-bold">
            Welcome {$userDB?.username ? $userDB.username : ''}
        </h1>
    </div>
    <div class="lg:m-4">
        {#await fetchCurrentProgram()}
            <div class="flex w-screen items-center justify-center p-4">
                <LoadingSpinner
                    spinnerColor="fill-yellow"
                    height="10"
                    width="10"
                />
            </div>
        {:then e}
            {#if $currentDay?.id && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
                <AthleteExpandedDay />
            {:else if $currentDay && $currentDay?.isRestDay}
                <div class="flex flex-col">
                    Rest Day
                    <a
                        class="text-md font-medium text-link"
                        href="/home/athlete/program">View Full Program</a
                    >
                </div>
            {:else}
                <div class="flex flex-col py-4 text-center md:text-left">
                    No programming available for today
                    <a
                        class="text-md font-medium text-link"
                        href="/home/athlete/program">View Full Program</a
                    >
                </div>
            {/if}
        {:catch e}
            <p>Error: could not retrieve your program</p>
        {/await}
    </div>
    <div class="m-4 flex flex-col">
        {#if $userDB?.athleteData?.team}
            <h4 class="text-center text-2xl">
                {$userDB?.athleteData?.team?.name
                    ? $userDB.athleteData.team.name
                    : 'Your Team'}
            </h4>
            <p class="m-2 text-center">
                As we continue to add features to the site, more information
                about your team will become available here like team-wide
                messages from your coach
            </p>
        {:else}
            <a
                class="text-center text-lg text-link underline md:text-left"
                href="/home/athlete/teams">Join a team</a
            >
        {/if}
    </div>
</div>

{#if $loading}
    <div
        class="fixed top-0 bottom-0 right-0 left-0 z-[110] bg-gray-300 opacity-25"
    ></div>
    <div
        class="fixed bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center"
    >
        <LoadingSpinner spinnerColor="fill-yellow" height="h-10" width="w-10" />
    </div>
{/if}
{#if $success}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-green bg-gray-200 p-4 text-green shadow-2xl shadow-black lg:w-4/12"
    >
        {$success}
        <button
            class="h-8 w-8 rounded-full p-1 text-green-dark hover:bg-gray-400 hover:text-green"
            on:click="{() => ($success = '')}"
        >
            <MdClose />
        </button>
    </div>
{:else if $error}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-red-shade bg-gray-200 p-4 text-red shadow-2xl shadow-black lg:w-4/12"
    >
        {$error}
        <button
            class="h-8 w-8 rounded-full p-1 hover:bg-gray-400 hover:text-red-shade"
            on:click="{() => ($error = '')}"
        >
            <MdClose />
        </button>
    </div>
{/if}

<style>
</style>
