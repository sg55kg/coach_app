<script lang="ts">
    import {page} from "$app/stores";
    import {ProgramService} from "../../../../../../lib/service/ProgramService";
    import {AthleteProgramStats} from "../../../../../../lib/classes/program/stats";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {Chart} from "chart.js/auto";
    import {getRelativePosition} from "chart.js/helpers";
    import dayjs from "dayjs";

    let overallStats: AthleteProgramStats
    let weeklyStats: AthleteProgramStats[]
    let selectedWeek: AthleteProgramStats

    const fetchStats = async () => {
        if (overallStats) return

        const programId = $page.params.id
        console.log($page.params)
        try {
            const res = await ProgramService.getProgramStats(programId)
            console.log(res)
            overallStats = res
            return res
        } catch (e) {
            console.log(e)
        }
    }

    const fetchWeeklyStats = async () => {
        if (weeklyStats) return

        const programId = $page.params.id
        try {
            const res = await ProgramService.getWeeklyProgramStats(programId)
            console.log(res)
            weeklyStats = res
            return res
        } catch (e) {
            console.log(e)
        }
    }

    const renderOverallTotalVolumeChart = (weeks: AthleteProgramStats[]) => {
        const canvas = <HTMLCanvasElement>document.getElementById('overall-total-volume')
        const labels = weeks.map(w => w.startDate.format('MM/DD/YY') + ' - ' + w.endDate.format('MM/DD/YY'))
        const plannedData = weeks.map(w => w.plannedTotalVolume)
        const actualData = weeks.map(w => w.actualTotalVolume)
        const plannedColors = weeks.map(w => dayjs().diff(w.startDate, 'days') >= 0 && w.endDate.valueOf() > Date.now() ? '#fde577' : '#9333ea')

        const chart = new Chart(canvas, {
            data: {
                datasets: [{
                    type: 'bar',
                    label: 'Planned Total Volume By Week',
                    data: plannedData,
                    order: 2,
                    backgroundColor: plannedColors
                }, {
                    type: 'line',
                    label: 'Actual Total Volume By Week',
                    data: actualData,
                    order: 1,
                    backgroundColor: '#10b981',
                    borderColor: '#10b981'
                }],
                labels: labels
            },
            options: {
                plugins: {
                    tooltip: true
                },
                onClick: (e) => {
                    const pos = getRelativePosition(e, chart as Chart)
                    const dataX = chart.scales.x.getValueForPixel(pos.x)
                    const label = chart.scales.x.getLabelForValue(dataX)
                    const labelArr = label.split(' - ')
                    selectedWeek = weeklyStats.find(s => s.startDate.isSame(dayjs(labelArr[0]), 'date'))
                }
            },

        } as any)
    }

    const renderOverallIntensityChart = (weeks: AthleteProgramStats[]) => {
        const canvas = <HTMLCanvasElement>document.getElementById('overall-average-intensity')
        const labels = weeks.map(w => w.startDate.format('MM/DD/YY') + ' - ' + w.endDate.format('MM/DD/YY'))
        const plannedData = weeks.map(w => w.plannedAverageIntensity)
        const actualData = weeks.map(w => w.actualAverageIntensity)
        const plannedColors = weeks.map(w => dayjs().diff(w.startDate, 'days') >= 0 && w.endDate.valueOf() > Date.now() ? '#fde577' : '#9333ea')

        const chart = new Chart(canvas, {
            data: {
                datasets: [{
                    type: 'bar',
                    label: 'Planned Avg. Intensity By Week',
                    data: plannedData,
                    order: 2,
                    backgroundColor: plannedColors
                }, {
                    type: 'line',
                    label: 'Actual Avg. Intensity By Week',
                    data: actualData,
                    order: 1,
                    backgroundColor: '#10b981',
                    borderColor: '#10b981'
                }],
                labels: labels
            },
            options: {
                plugins: {
                    tooltip: true
                },
                onClick: (e) => {
                    const pos = getRelativePosition(e, chart as Chart)
                    const dataX = chart.scales.x.getValueForPixel(pos.x)
                    const label = chart.scales.x.getLabelForValue(dataX)
                    const labelArr = label.split(' - ')
                    selectedWeek = weeklyStats.find(s => s.startDate.isSame(dayjs(labelArr[0]), 'date'))
                }
            }
        } as any)
    }


    $: weeklyStats  ? setTimeout(() => renderOverallTotalVolumeChart(weeklyStats), 100) : null
    $: weeklyStats ? setTimeout(() => renderOverallIntensityChart(weeklyStats), 100) : null

</script>

<div>
    {#await fetchWeeklyStats()}
        <div class="w-screen h-screen flex flex-col items-center justify-center">
            <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            <p class="font-semibold">Fetching program stats...</p>
        </div>
    {:then stats}
        <div class="w-screen">
            <div class="p-4">
                <h2 class="text-2xl text-textblue font-semibold">{stats[0].programName} Overall</h2>
                <div class="flex w-screen">
                    <div class="p-2 w-full flex" id="overall-total-volume-container">
                        <div class="p-2 w-1/3">
                            <canvas id="overall-total-volume"></canvas>
                        </div>
                        <div class="p-2 w-1/3">
                            <canvas id="overall-average-intensity"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="p-4">
                <h2 class="text-2xl text-textblue font-semibold">Weekly Breakdowns</h2>
                <div class="grid grid-cols-4">
                    <div class="col-span-1 flex flex-col">
                        {#each stats as week}
                            <div class="{selectedWeek === week ? 'bg-gray-400' : 'bg-gray-200'} p-2 my-2 border-l-2 border-l-white cursor-pointer"
                                 on:click={() => selectedWeek = week}
                            >
                                <h3 class="text-lg {selectedWeek === week ? 'text-yellow-lt' : 'text-textblue'} font-bold py-2">
                                    {week.startDate.format('dddd DD MMM')} - {week.endDate.format('dddd DD MMM')}
                                </h3>
                                <div class="flex justify-between">
                                    <h5 class="text-md">Total Volume</h5>
                                    <p>{week.plannedTotalVolume}</p>
                                </div>
                                <div class="flex justify-between">
                                    <h5 class="text-md">Total Reps</h5>
                                    <p>{week.plannedTotalReps}</p>
                                </div>
                                <div class="flex justify-between">
                                    <h5 class="text-md">Average Intensity</h5>
                                    <p>{week.plannedAverageIntensity}</p>
                                </div>

                            </div>
                        {/each}
                    </div>
                    <div class="col-span-3">
                        {#if selectedWeek}
                            <div class="bg-gray-400 mt-2 h-[80vh] overflow-y-auto">
                                <h3 class="text-xl font-medium text-yellow-lt p-3">
                                    {selectedWeek.startDate.format('dddd DD MM')} - {selectedWeek.endDate.format('dddd DD MM')}
                                </h3>
                            </div>
                        {:else}
                            <div class="flex justify-center items-center">Select a week to view more</div>
                        {/if}
                    </div>
                </div>
            </div>
        </div>
                                <!--        <div>-->
<!--            <header class="p-3 w-screen">-->
<!--                <h1 class="text-3xl font-bold text-yellow-lt">Breakdown for {stats.programName}</h1>-->
<!--            </header>-->
<!--            <div class="flex flex-col w-screen">-->
<!--                <div class="flex flex-col p-2">-->
<!--                    <h2 class="text-xl font-semibold py-2">-->
<!--                        Overall Plan-->
<!--                    </h2>-->
<!--                    <div class="grid grid-cols-2 w-6/12">-->
<!--                        <h5 class="text-md">Total Volume</h5>-->
<!--                        <p>{stats.plannedTotalVolume}</p>-->
<!--                        <h5 class="text-md">Total Reps</h5>-->
<!--                        <p>{stats.plannedTotalReps}</p>-->
<!--                        <h5 class="text-md">Average Intensity</h5>-->
<!--                        <p>{stats.plannedAverageIntensity}</p>-->
<!--                    </div>-->
<!--                    <h2 class="text-xl font-semibold py-2">-->
<!--                        Overall Actual-->
<!--                    </h2>-->
<!--                    <div class="grid grid-cols-2 w-6/12">-->
<!--                        <h5 class="text-md">Total Volume</h5>-->
<!--                        <p>{stats.actualTotalVolume}</p>-->
<!--                        <h5 class="text-md">Total Reps</h5>-->
<!--                        <p class="">{stats.actualTotalReps}</p>-->
<!--                        <h5 class="text-md">Average Intensity</h5>-->
<!--                        <p>{stats.actualAverageIntensity}</p>-->
<!--                    </div>-->
<!--                </div>-->
<!--            </div>-->
<!--            {#await fetchWeeklyStats()}-->
<!--                <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />-->
<!--            {:then wStats}-->
<!--                <div class="flex flex-col p-2">-->
<!--                    <h2>Weekly Breakdowns</h2>-->
<!--                    {#each wStats as week}-->
<!--                        <h3 class="text-xl font-bold py-2">{week.startDate.format('dddd DD MMM')} - {week.endDate.format('dddd DD MMM')}</h3>-->
<!--                        <h2 class="text-xl font-semibold py-2">-->
<!--                            Planned-->
<!--                        </h2>-->
<!--                        <div class="grid grid-cols-2 w-6/12">-->
<!--                            <h5 class="text-md">Total Volume</h5>-->
<!--                            <p>{week.plannedTotalVolume}</p>-->
<!--                            <h5 class="text-md">Total Reps</h5>-->
<!--                            <p>{week.plannedTotalReps}</p>-->
<!--                            <h5 class="text-md">Average Intensity</h5>-->
<!--                            <p>{week.plannedAverageIntensity}</p>-->
<!--                        </div>-->
<!--                        <h2 class="text-xl font-semibold py-2">-->
<!--                            Actual-->
<!--                        </h2>-->
<!--                        <div class="grid grid-cols-2 w-6/12">-->
<!--                            <h5 class="text-md">Total Volume</h5>-->
<!--                            <p>{week.actualTotalVolume}</p>-->
<!--                            <h5 class="text-md">Total Reps</h5>-->
<!--                            <p class="">{week.actualTotalReps}</p>-->
<!--                            <h5 class="text-md">Average Intensity</h5>-->
<!--                            <p>{week.actualAverageIntensity}</p>-->
<!--                        </div>-->
<!--                    {/each}-->
<!--                </div>-->
<!--            {:catch err}-->
<!--                <p>Error</p>-->
<!--            {/await}-->
<!--        </div>-->
    {:catch err}
        <p>Error</p>
    {/await}
</div>

<style>

</style>