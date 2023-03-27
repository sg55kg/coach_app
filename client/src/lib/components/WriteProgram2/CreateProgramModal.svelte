<script lang="ts">

    import dayjs from "dayjs";
    import {isMobile, userDB} from "$lib/stores/authStore.js";
    import {onMount} from "svelte";
    import {AthleteData} from "$lib/classes/user/athlete";
    import {Program} from "$lib/classes/program";
    import {ProgramService} from "$lib/service/ProgramService";
    import {goto} from "$app/navigation";

    export let show: boolean = false

    let athletes: AthleteData[] = []
    let programName = ''
    let selectedAthleteId: string = ''

    const currentYear: number = dayjs().year()
    let selectedMonth: string = ''
    let selectedDay: string = ''
    const monthOptions: string[] = [
        'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'November', 'December'
    ]
    let dayOptions: string[] = []

    $: isInvalid = () => {
        if (!programName.trim()) {
            return true
        } else return (selectedAthleteId && (!selectedMonth || !selectedDay));
    }

    const selectMonth = (month: string) => {
        selectedMonth = month
        let startNum = 0
        dayOptions = Array(
            dayjs(month + ' ' + currentYear).daysInMonth()
        ).fill(0).map(_ => ++startNum) as string[]
    }

    const selectDay = (day: string) => {
        selectedDay = day
    }

    const createProgram = async () => {
        try {
            const newProgram = new Program()
            newProgram.name = programName
            newProgram.athleteId = selectedAthleteId
            newProgram.startDate = dayjs(selectedMonth + ' ' + selectedDay + ', ' + currentYear)
            newProgram.endDate = newProgram.startDate

            const res = await ProgramService.createProgram(newProgram)
            if (!res.id) {
                throw new Error('Could not create program')
            }
            await goto(`/home/coach/program/${res.id}`)
        } catch (e) {

        }
    }

    onMount(() => {
        if (!$userDB?.coachData?.teams) {
            return
        }
        athletes = $userDB.coachData.athletes.map((a) => a)
    })
</script>

<div class="absolute w-screen h-screen bg-gray-300 opacity-50 z-[100] lg:-mt-[7em]" on:click={() => show = false}></div>
<div class="absolute bottom-10 top-10 left-10 lg:left-72 lg:right-72 lg:bottom-20 right-10 lg:items-center
            bg-gray-200 z-[105] flex flex-col rounded shadow-md shadow-black">
    <header class="flex justify-center p-4">
        <h1 class="text-2xl font-bold text-yellow-lt lg:text-4xl">Create A Program</h1>
    </header>
    <div class="p-3 w-full lg:flex lg:flex-col lg:items-center">
        <div class="flex flex-col my-2">
            <label class="text-sm">Program Name</label>
            <input type="text" class="bg-gray-300 p-1 lg:w-full" bind:value={programName}>
        </div>
        <div class="flex flex-col my-2">
            <label class="text-sm">Athlete</label>
            <select class="bg-gray-300 p-2 rounded lg:p-1 lg:w-full" on:change={(e) => selectedAthleteId = e.target.value}>
                <option disabled>Select Athlete</option>
                <option>No Athlete</option>
                {#each athletes as athlete}
                    <option value={athlete.id}>{athlete.name}</option>
                {/each}
            </select>
        </div>
        <div class="flex flex-col my-2 lg:w-2/12 {$isMobile ? 'items-center' : ''}">
            <label class="text-sm">Start Date</label>
            <div class="flex justify-between">
                <select class="bg-gray-300 p-2 lg:p-1 rounded w-fit {$isMobile ? 'mx-4' : ''}"
                        disabled={!selectedAthleteId}
                        on:change={(e) => selectMonth(e.target.value)}
                >
                    <option disabled selected>Month</option>
                    {#each monthOptions as month}
                        <option>{month}</option>
                    {/each}
                </select>
                <select class="bg-gray-300 p-2 lg:p-1 rounded w-fit {$isMobile ? 'mx-4' : ''}"
                        disabled={!selectedAthleteId || !selectedMonth}
                        on:change={(e) => selectDay(e.target.value)}
                >
                    <option disabled selected>Day</option>
                    {#each dayOptions as day}
                        <option>{day}</option>
                    {/each}
                </select>
            </div>
        </div>
        <div class="flex justify-center mt-8">
            <button class="bg-yellow rounded text-gray-300 p-2 px-4 m-auto text-lg font-bold hover:bg-yellow-shade disabled:bg-gray-100"
                    disabled={isInvalid()}
                    on:click={createProgram}
            >
                Create
            </button>
        </div>
    </div>
</div>

<style></style>