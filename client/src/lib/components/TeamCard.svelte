<script lang="ts">
    import type { Team } from '$lib/classes/team';
    import { onMount } from 'svelte';
    import dayjs from 'dayjs';
    import { userDB } from '$lib/stores/authStore';

    export let team: Team;
    let numLowUpdateSeverity: number = 0;
    let numModerateUpdateSeverity: number = 0;
    let numSevereUpdateSeverity: number = 0;
    let numNoneUpdateSeverity: number = 0;

    onMount(() => {
        if (!$userDB?.coachData?.athletes) return;

        const teamAthletes = $userDB.coachData.athletes.filter(
            a => a.team?.id === team.id
        );

        teamAthletes.forEach(athlete => {
            if (!athlete.currentProgram) {
                return (numNoneUpdateSeverity = numNoneUpdateSeverity + 1);
            }
            if (!athlete.currentProgram.days) return;
            const today = dayjs();
            const lastEntered = athlete.currentProgram.days.find(
                d => d.exercises.length < 1 && !d.isRestDay
            );
            if (!lastEntered) {
                return (numLowUpdateSeverity = numLowUpdateSeverity + 1);
            }
            const lastUpdatedDay = dayjs(lastEntered.date);
            const diff = lastUpdatedDay.diff(today, 'days');

            if (diff <= 2 || today.valueOf() > lastUpdatedDay.valueOf()) {
                numSevereUpdateSeverity = numSevereUpdateSeverity + 1;
            } else if (diff <= 6) {
                numModerateUpdateSeverity = numModerateUpdateSeverity + 1;
            } else {
                numLowUpdateSeverity = numLowUpdateSeverity + 1;
            }
        });
    });
</script>

<div class=" m-2 bg-gray-200 px-4 py-6 shadow-md shadow-black lg:w-10/12">
    <h1 class="my-1 text-lg font-semibold tracking-wider text-yellow-lt">
        {team.name}
    </h1>
    <hr class="text-gray-100" />
    <ul class="mt-2 flex flex-col pl-4">
        {#if numNoneUpdateSeverity > 0}
            <li class="text-textblue">
                {numNoneUpdateSeverity} athletes do no have programs
            </li>
        {/if}
        {#if numSevereUpdateSeverity > 0}
            <li class="text-red">
                {numSevereUpdateSeverity} athletes need a new day in less than two
                days
            </li>
        {/if}
        {#if numModerateUpdateSeverity > 0}
            <li class="text-yellow">
                {numModerateUpdateSeverity} athletes need a new day in less than
                a week
            </li>
        {/if}
        {#if numLowUpdateSeverity > 0}
            <li class="text-green">
                {numLowUpdateSeverity} athletes are up to date
            </li>
        {/if}
    </ul>
    <div class="mt-2">
        <a
            class="text-link underline"
            href="{`/home/coach/team/${team.id}/athletes`}">View</a
        >
    </div>
</div>

<style>
</style>
