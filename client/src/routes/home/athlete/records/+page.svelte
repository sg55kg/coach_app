<script lang="ts">
    import { AthleteRecord } from '../../../../lib/classes/user/athlete/records';
    import UserService from '../../../../lib/service/UserService';
    import { userDB } from '../../../../lib/stores/authStore';
    import { onMount } from 'svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { Chart } from 'chart.js/auto';
    import { getRelativePosition } from 'chart.js/helpers';
    import type { ChartConfiguration } from 'chart.js';
    import { AthleteProgramStats } from '../../../../lib/classes/program/stats';
    import dayjs from 'dayjs';
    import AddRecordModal from '$lib/components/modals/AddRecordModal.svelte';

    let recordsKeyList: string[] = [];
    let selectedRecordKey: string = '';
    let selectedRecord: AthleteRecord;
    let loading: boolean = true;
    let loadingNameSearch: boolean = false;
    let cachedRecordHistories: Map<string, AthleteRecord[]> = new Map<
        string,
        AthleteRecord[]
    >();
    let noResults: string = '';
    let showAddRecords: boolean = false;

    const toggleRecord = (recordKey: string) => {
        if (selectedRecordKey === recordKey) {
            selectedRecordKey = '';
        } else {
            selectedRecordKey = recordKey;
        }
    };

    const fetchCommonRecords = async () => {
        loading = true;
        try {
            const res = await UserService.searchAthleteRecords(
                $userDB!.athleteData.id,
                { current: false }
            );
            console.log(res);
            res.forEach((record: AthleteRecord) => {
                addToCachedRecords(record.exerciseName, record);
                if (!recordsKeyList.includes(record.exerciseName)) {
                    recordsKeyList.push(record.exerciseName);
                }
            });
        } catch (e) {
            console.log(e); // TODO: handle error
        } finally {
            loading = false;
        }
    };

    const processSearch = debounce(e => searchRecordsByName(e), 1000);

    function debounce(func, delay = 1000) {
        let timeoutId;

        return (...args) => {
            clearTimeout(timeoutId);

            timeoutId = setTimeout(() => {
                console.log('Inside set timeout');
                func.apply(this, args);
            }, delay);
        };
    }

    const searchRecordsByName = async (name: string) => {
        loadingNameSearch = true;
        try {
            const res = await UserService.searchAthleteRecordsByExerciseName(
                $userDB!.athleteData.id,
                {
                    current: false,
                    name: name,
                    reps: 1,
                }
            );
            if (!res.length) {
                noResults = `No results found for: ${name}`;
            }
            res.forEach((record: AthleteRecord) => {
                addToCachedRecords(record.exerciseName, record);
                if (!recordsKeyList.includes(record.exerciseName)) {
                    recordsKeyList.push(record.exerciseName);
                }
            });
            recordsKeyList = recordsKeyList;
        } catch (e) {
            console.log(e); // TODO: handle error
        } finally {
            loadingNameSearch = false;
        }
    };

    const addToCachedRecords = (key: string, record: AthleteRecord) => {
        let records: AthleteRecord[] = cachedRecordHistories.get(key);
        if (!records) {
            records = [];
        }
        records.push(record);
        records.sort((a, b) => a.createdAt - b.createdAt);
        // Remove duplicate records
        records = records.filter(
            (r, idx, arr) => arr.findIndex(e => e.id === r.id) === idx
        );
        cachedRecordHistories.set(key, records);
        cachedRecordHistories = cachedRecordHistories;
    };

    onMount(async () => {
        await fetchCommonRecords();
    });

    const getRecord = (key: string) => {
        const records = cachedRecordHistories.get(key);
        let record = records.find(r => r.isCurrent === true);
        if (!record) {
            record = records[records.length - 1];
        }
        return record;
    };

    $: selectedRecordKey
        ? (selectedRecord = getRecord(selectedRecordKey))
        : null;

    let chart: Chart;

    const renderHistoryChart = (recordKey: string) => {
        if (chart) {
            chart.destroy();
        }
        const canvas = <HTMLCanvasElement>(
            document.getElementById('record-history')
        );

        const records = cachedRecordHistories.get(recordKey);

        const labels = records.map(r => r.createdAt.format("MMM DD 'YY"));
        const data = records.map(w => w.weight);

        chart = new Chart(canvas, {
            data: {
                datasets: [
                    {
                        type: 'line',
                        label: 'PR History',
                        data: data,
                        order: 1,
                        backgroundColor: '#fde577',
                        borderColor: '#fde577',
                    },
                ],
                labels: labels,
            },
            options: {
                plugins: {
                    tooltip: true,
                    title: {
                        display: true,
                        text: 'PR History',
                        color: '#ADBDD2',
                    },
                },
                maintainAspectRatio: false,
                onClick: e => {},
            },
        } as ChartConfiguration);
    };

    $: noResults ? setTimeout(() => (noResults = ''), 5000) : null;
</script>

<header class="flex justify-between">
    <h1 class="p-2 text-2xl">My Progress</h1>
    <button
        class="p-2 text-sm text-link"
        on:click="{() => (showAddRecords = !showAddRecords)}"
        >Add Records</button
    >
</header>
{#if !loading}
    <div class="relative w-full p-2">
        <input
            type="text"
            class="w-full rounded bg-gray-300 p-1"
            placeholder="Search for records"
            on:keyup="{e => processSearch(e.target.value)}"
        />
        {#if loadingNameSearch}
            <div class="loading-bar"></div>
        {/if}
    </div>
    <div class="h-4 w-full text-center text-yellow-lt">
        <i>{noResults}</i>
    </div>
    <div class="mt-2 grid w-full grid-cols-12 gap-3 p-2">
        {#each recordsKeyList as recordKey}
            <div
                on:click="{() => {
                    toggleRecord(recordKey);
                    renderHistoryChart(recordKey);
                }}"
                class="lg:text-normal col-span-4 rounded-xl border border-textblue p-1 text-center text-xs text-textblue hover:cursor-pointer lg:col-span-1"
                class:selected-record="{selectedRecordKey === recordKey}"
            >
                {recordKey}
            </div>
        {/each}
    </div>
    <div>
        {#if !selectedRecord}
            <p class="mt-4 text-center text-textblue">
                Select exercises above to see your records
            </p>
        {:else}
            <div class="p-2">
                <h2 class="text-lg text-textblue">
                    {selectedRecord.exerciseName}
                </h2>
                <p>Current PR: {selectedRecord.weight}kg</p>
                <p>Set on: {selectedRecord.createdAt.format('MMMM DD YYYY')}</p>
            </div>
        {/if}
    </div>
    <div id="record-history-container" class="h-96 p-4">
        <canvas id="record-history"></canvas>
    </div>
{:else}
    <div class="flex w-full items-center justify-center">
        <LoadingSpinner spinnerColor="fill-yellow" height="h-10" width="w-10" />
    </div>
{/if}
{#if showAddRecords}
    <div
        class="fixed top-0 right-0 left-0 bottom-0 flex flex-col items-center justify-center"
    >
        <AddRecordModal bind:show="{showAddRecords}" />
    </div>
{/if}

<style>
    .selected-record {
        border-color: #fde577 !important;
        color: #fde577 !important;
    }

    .loading-bar {
        position: absolute;
        bottom: 0;
        left: 0;
        height: 2px;
        width: 100%;
        background-color: inherit;
        overflow: hidden;
    }

    .loading-bar::before {
        content: '';
        display: block;
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background-color: #fde577;
        animation: loadingAnimation 2s infinite linear;
    }

    @keyframes loadingAnimation {
        0% {
            left: -100%;
        }

        100% {
            left: 100%;
        }
    }
</style>
