<script lang="ts">
    import { ProgramService } from '../../../service/ProgramService';
    import { AthleteProgramStats } from '../../../classes/program/stats';
    import { getContext, onMount } from 'svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import dayjs from 'dayjs';
    import { Chart } from 'chart.js/auto';
    import { getRelativePosition } from 'chart.js/helpers';
    import type { ChartConfiguration } from 'chart.js';

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getCurrentDayIdx,
    } = getContext('athlete-program');
    const currentProgram = getCurrentProgram();
    const currentDay = getCurrentDay();
    const currentIdx = getCurrentDayIdx();

    let weeklyStats: AthleteProgramStats[];

    const fetchWeeklyStats = async () => {
        const programId = $currentProgram.id;
        try {
            const res = await ProgramService.getWeeklyProgramStats(programId);
            weeklyStats = res;
            return res;
        } catch (e) {
            console.log(e);
        }
    };

    const renderWeeklyChart = (weeks: AthleteProgramStats[]) => {
        const canvas = <HTMLCanvasElement>(
            document.getElementById('weekly-chart')
        );
        const labels = weeks.map(w => ' ');
        console.log(weeks);
        const plannedData = weeks.map(w => w.plannedTotalVolume);
        const actualData = weeks.map(w => w.actualTotalVolume);
        const plannedColors = weeks.map(w => {
            if (
                dayjs().diff(w.startDate, 'days') >= 0 &&
                w.endDate.valueOf() > Date.now()
            ) {
                return '#fde577';
            } else if (w.startDate.valueOf() > Date.now()) {
                return '#505360';
            } else {
                return '#9333ea';
            }
        });

        const chart = new Chart(canvas, {
            data: {
                datasets: [
                    {
                        type: 'bar',
                        label: 'Planned Total Volume By Week',
                        data: plannedData,
                        order: 2,
                        backgroundColor: plannedColors,
                    },
                    {
                        type: 'line',
                        label: 'Actual Total Volume By Week',
                        data: actualData,
                        order: 1,
                        backgroundColor: '#10b981',
                        borderColor: '#10b981',
                    },
                ],
                labels: labels,
            },
            options: {
                plugins: {
                    tooltip: true,
                    title: {
                        display: true,
                        text: 'Total Volume by Week',
                        color: '#ADBDD2',
                    },
                },
                onClick: e => {
                    // const pos = getRelativePosition(e, chart as Chart);
                    // const dataX = chart.scales.x.getValueForPixel(pos.x);
                    // const label = chart.scales.x.getLabelForValue(dataX);
                    // const labelArr = label.split(' - ');
                    // selectedWeek = stats.find(s =>
                    //     s.startDate.isSame(dayjs(labelArr[0]), 'date')
                    // );
                    // fetchDailyStats();
                },
            },
        } as ChartConfiguration);
    };

    $: weeklyStats
        ? setTimeout(() => renderWeeklyChart(weeklyStats), 100)
        : null;
</script>

{#await fetchWeeklyStats()}
    <div class="m-3 flex justify-center">
        <LoadingSpinner spinnerColor="fill-yellow" height="h-10" width="w-10" />
    </div>
{:then weeklyStats}
    <div class="flex justify-center">
        <div class="w-full lg:w-1/2 lg:p-2" id="weekly-chart-container">
            <canvas id="weekly-chart"></canvas>
        </div>
    </div>
{:catch err}
    <p>Error Fetching data for chart</p>
{/await}

<style></style>
