<script lang="ts">
    import {page} from "$app/stores";
    import {ProgramService} from "../../../../../../lib/service/ProgramService";
    import {AthleteProgramStats} from "../../../../../../lib/classes/program/stats";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";


    const fetchStats = async () => {
        const programId = $page.params.id
        console.log($page.params)
        try {
            const res = await ProgramService.getProgramStats(programId)
            console.log(res)
            return res
        } catch (e) {
            console.log(e)
        }
    }

    const fetchWeeklyStats = async () => {
        const programId = $page.params.id
        try {
            const res = await ProgramService.getWeeklyProgramStats(programId)
            console.log(res)
            return res
        } catch (e) {
            console.log(e)
        }
    }

</script>

<div>
    {#await fetchStats()}
        <div class="w-screen h-screen flex flex-col items-center justify-center">
            <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            <p class="font-semibold">Fetching program stats...</p>
        </div>
    {:then stats}
        <div>
            <header class="p-3 w-screen">
                <h1 class="text-3xl font-bold text-yellow-lt">Breakdown for {stats.programName}</h1>
            </header>
            <div class="flex flex-col w-screen">
                <div class="flex flex-col p-2">
                    <h2 class="text-xl font-semibold py-2">
                        Overall Plan
                    </h2>
                    <div class="grid grid-cols-2 w-6/12">
                        <h5 class="text-md">Total Volume</h5>
                        <p>{stats.plannedTotalVolume}</p>
                        <h5 class="text-md">Total Reps</h5>
                        <p>{stats.plannedTotalReps}</p>
                        <h5 class="text-md">Average Intensity</h5>
                        <p>{stats.plannedAverageIntensity}</p>
                    </div>
                    <h2 class="text-xl font-semibold py-2">
                        Overall Actual
                    </h2>
                    <div class="grid grid-cols-2 w-6/12">
                        <h5 class="text-md">Total Volume</h5>
                        <p>{stats.actualTotalVolume}</p>
                        <h5 class="text-md">Total Reps</h5>
                        <p class="">{stats.actualTotalReps}</p>
                        <h5 class="text-md">Average Intensity</h5>
                        <p>{stats.actualAverageIntensity}</p>
                    </div>
                </div>
            </div>
            {#await fetchWeeklyStats()}
                <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            {:then wStats}
                <div class="flex flex-col p-2">
                    <h2>Weekly Breakdowns</h2>
                    {#each wStats as week}
                        <h3 class="text-xl font-bold py-2">{week.startDate.format('dddd DD MMM')} - {week.endDate.format('dddd DD MMM')}</h3>
                        <h2 class="text-xl font-semibold py-2">
                            Planned
                        </h2>
                        <div class="grid grid-cols-2 w-6/12">
                            <h5 class="text-md">Total Volume</h5>
                            <p>{week.plannedTotalVolume}</p>
                            <h5 class="text-md">Total Reps</h5>
                            <p>{week.plannedTotalReps}</p>
                            <h5 class="text-md">Average Intensity</h5>
                            <p>{week.plannedAverageIntensity}</p>
                        </div>
                        <h2 class="text-xl font-semibold py-2">
                            Actual
                        </h2>
                        <div class="grid grid-cols-2 w-6/12">
                            <h5 class="text-md">Total Volume</h5>
                            <p>{week.actualTotalVolume}</p>
                            <h5 class="text-md">Total Reps</h5>
                            <p class="">{week.actualTotalReps}</p>
                            <h5 class="text-md">Average Intensity</h5>
                            <p>{week.actualAverageIntensity}</p>
                        </div>
                    {/each}
                </div>
            {:catch err}
                <p>Error</p>
            {/await}
        </div>
    {:catch err}
        <p>Error</p>
    {/await}
</div>

<style>

</style>