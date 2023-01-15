<script lang="ts">
    import {LineChart, type LineChartData} from 'chartist'
    import {onMount} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import 'chartist/dist/index.css';
    import {AthleteData, athleteRecordFields} from "$lib/classes/user";

    export let athlete: AthleteData

    let recordChart: LineChart
    let moreCheckOptions: any[] = []
    let basicCheckOptions: string[] = []
    let series: number[] = []
    let labels: string[]
    let selectedOptions: string = 'back_squat'
    let recordsError: boolean = false

    const changeRecordExercise = (name: string) => {
        if (!$userDB?.athleteData?.records) return

        name = name.split(' ').join('_')
        series = athlete.records.map(record => {
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
        if (!athlete || athlete.records.length < 1) {
            return recordsError = true
        }

        athleteRecordFields.forEach(label => {
            if (label === 'snatch' ||
                label === 'clean and jerk' ||
                label === 'back squat' ||
                label === 'front squat' ||
                label === 'deadlift' ||
                label === 'bench press'
            ) {
                basicCheckOptions.push(label)
            } else {
                moreCheckOptions.push(label)
            }
        })
        basicCheckOptions = basicCheckOptions
        moreCheckOptions = moreCheckOptions

        athlete.records.sort((a, b) => a.lastUpdated.toDate().valueOf() - b.lastUpdated.toDate().valueOf())

        series = athlete.records.map(record => {
            return record.records.get('back_squat')
        })

        labels = athlete.records.map(record => dayjs(record.lastUpdated).format('MMM D'))

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

{#if !recordsError}
    <div class="flex flex-col">
        <h1 class="text-2xl font-bold lg:mx-60 self-start">Personal Records</h1>
        <div class="flex flex-col items-center justify-auto">
            <div class="flex flex-row flex-wrap">
                <div class="grid grid-cols-3">
                {#each basicCheckOptions as option, index (index)}
                    <div class="lg:px-2">
                        <input checked={selectedOptions === option.split(' ').join('_')}
                               type="checkbox"
                               on:change={() => changeRecordExercise(option)}
                               class="text-lg"
                        >
                        <label class="text-lg">{option}</label>
                    </div>
                {/each}
                </div>
                <div class="px-2 text-black">
                    <select on:change={(e) => changeRecordExercise(e.target.value)}>
                        <option disabled selected>More...</option>
                        {#each moreCheckOptions as option, index (index)}
                            <option selected={selectedOptions === option.split(' ').join('_')}>{option}</option>
                        {/each}
                    </select>
                </div>
            </div>
            <div class="w-9/12">
                <div class="stroke-red " id="record-chart"></div>
            </div>
        </div>
    </div>
{:else}
    <div class="flex flex-col text-center font-semibold text-lg m-auto p-4">
        It doesn't look like we've recorded any data for {athlete.name} yet
        <a class="text-link hover:text-link-shade hover:cursor-pointer mt-4" href="/home/coach">Back to coach dashboard</a>
    </div>
{/if}

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
    :global(.ct-line) {
        stroke: #fddd4c !important;
    }

</style>