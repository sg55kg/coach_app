<script lang="ts">
    import {LineChart, type LineChartData} from 'chartist'
    import {onMount} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import 'chartist/dist/index.css';

    let recordChart: LineChart
    let moreCheckOptions: any[] = []
    let basicCheckOptions: string[] = []
    let series: number[] = []
    let labels: string[]
    let selectedOptions: string = 'back_squat'

    const changeRecordExercise = (name: string) => {
        if (!$userDB?.athleteData?.records) return

        name = name.split(' ').join('_')
        series = $userDB.athleteData.records.map(record => {
            return record.records.get(name)
        })
        recordChart = new LineChart('#record-chart', {
            series: [series],
            labels: labels,
        }, {
            showLine: true,
            showArea: false,
            low: 0,
            high: 500,
            height: 400,
        })
        selectedOptions = name
    }

    onMount(() => {
        if (!$userDB?.athleteData?.records) return

        if ($userDB.athleteData.records.length > 0) {
            $userDB.athleteData.records[0].records.forEach((val, key) => {
                if (key === 'snatch' || key === 'clean_and_jerk' || key === 'back_squat' || key === 'front_squat' || key === 'deadlift' || key === 'bench_press') {
                    basicCheckOptions.push(key.split('_').join(' '))
                } else {
                    moreCheckOptions.push(key.split('_').join(' '))
                }

            })
            basicCheckOptions = basicCheckOptions
            moreCheckOptions = moreCheckOptions
        }
        series = $userDB.athleteData.records.map(record => {
            return record.records.get('back_squat')
        })
        labels = $userDB.athleteData.records.map(record => {
            return dayjs().format('ddd MMM D')
        })
        recordChart = new LineChart('#record-chart', {
            series: [series],
            labels: labels,
        }, {
            showLine: true,
            showArea: false,
            low: 0,
            high: 500,
            height: 400,

        })
    })
</script>

<div class="flex flex-col">
    <h1 class="text-2xl font-bold mx-60">Personal Records</h1>
    <div class="flex flex-col items-center justify-auto">
        <div class="flex flex-row flex-wrap">
            {#each basicCheckOptions as option, index (index)}
                <div class="px-2">
                    <input checked={selectedOptions === option.split(' ').join('_')}
                           type="checkbox"
                           on:change={() => changeRecordExercise(option)}
                    >
                    <label>{option}</label>
                </div>
            {/each}
            <div class="px-2 text-black">
                <select>
                    <option disabled selected>More...</option>
                    {#each moreCheckOptions as option, index (index)}
                        <option>{option}</option>
                    {/each}
                </select>
            </div>
        </div>
        <div class="w-9/12">
            <div class="stroke-red" id="record-chart"></div>
        </div>
    </div>

</div>

<style>
    :global(.ct-grid) {
        stroke: #adbdd2 !important;
    }
    :global(.ct-label) {
        stroke: red !important;
        color: #adbdd2 !important;
    }
    :global(.ct-point) {
        stroke: #fddd4c !important;
    }

</style>