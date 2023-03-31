<script lang="ts">
    import {getContext, onMount} from "svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {AthleteData} from "$lib/classes/user/athlete";
    import {userDB} from "$lib/stores/authStore";
    import dayjs, {Dayjs} from "dayjs";
    import {ProgramService} from "$lib/service/ProgramService";
    import {goto} from "$app/navigation";
    import {team} from "$lib/stores/teamStore";

    export let show: boolean = false

    const { getProgram } = getContext('program')
    const program = getProgram()

    let loading: boolean = false
    let success: boolean = false
    let error: boolean = false

    let selectedAthlete: AthleteData | undefined
    let selectedStartDate: string = ''
    let athletes: AthleteData[] = []
    let newProgramName: string = $program.name
    let isCurrent: boolean = true

    const selectAthlete = (id: string) => {
        const athlete = athletes.find(a => a.id === id)
        selectedAthlete = athlete
    }

    const assignToAthlete = async (makeCopy: boolean = true) => {
        loading = true
        success = false
        error = false
        try {
            const programCopy = JSON.parse(JSON.stringify($program))
            programCopy.startDate = dayjs(selectedStartDate)
            programCopy.id = ''
            let currentDate = programCopy.startDate
            programCopy.days.forEach(d => {
                d.date = currentDate
                currentDate = currentDate.add(1, 'day')
            })
            programCopy.endDate = currentDate.subtract(1, 'day')
            programCopy.athleteId = selectedAthlete.id
            programCopy.coachId = $userDB.coachData.id
            programCopy.teamId = $team.id
            if (makeCopy) {
                programCopy.days.forEach(d => {
                    d.id = ''
                    d.exercises.forEach(e => {
                        e.id = ''
                        e.dropSets.forEach(d => d.id = '')
                    })
                })
                console.log(programCopy)
                const res = await ProgramService.createProgram({ ...programCopy, isCurrent })
                show = false
                await goto(`/home/coach/program/${res.id}`)
            } else {
                const res = await ProgramService.updateProgram({ ...programCopy, isCurrent })
                $program = res
                show = false
            }
        } catch (e) {
            console.log(e)
            error = true
        } finally {
            loading = false
        }
    }

    onMount(() => {
        athletes = $userDB?.coachData?.athletes
    })

</script>

<div class="absolute w-screen h-screen bg-gray-300 opacity-50 z-[100] lg:-mt-[7em]" on:click={() => show = false}></div>
<div class="absolute bottom-10 top-10 left-10 lg:left-72 lg:right-72 lg:bottom-20 right-10 lg:items-center
            bg-gray-200 z-[105] flex flex-col rounded shadow-md shadow-black">
    <header class="flex justify-center p-4">
        <h1 class="text-2xl font-bold text-yellow-lt lg:text-4xl text-center">Assign Athlete to {$program.name}</h1>
    </header>
    <div class="flex flex-col items-center p-2">
        <select class="bg-gray-300 p-1 rounded" on:change={(e) => selectAthlete(e.target.value)}>
            <option disabled selected>Select an athlete</option>
            {#each athletes as athlete}
                <option value={athlete.id}>{athlete.name}</option>
            {/each}
        </select>
        <div class="flex flex-col my-3">
            <label for="program-start-date-picker">Start Date</label>
            <input id="program-start-date-picker" class="bg-gray-300 w-full" type="date" bind:value={selectedStartDate}>
        </div>
        <div class="flex flex-col my-3">
            <label>Rename Program {selectedAthlete?.name ? 'for ' + selectedAthlete.name : ''} (optional)</label>
            <input id="rename-program-input" class="bg-gray-300 p-1" type="text" bind:value={newProgramName}>
        </div>
        <div class="flex my-3">
            <p>Make this {selectedAthlete?.name ? `${selectedAthlete.name}'s current program?` : 'program current?'}</p>
            <input type="checkbox" class="mx-2" checked={isCurrent} on:change={(e) => isCurrent = e.target.checked} />
        </div>
    </div>
    <div class="flex flex-col items-center mt-8">
        <button class="bg-yellow rounded text-gray-300 p-2 my-2 px-4 m-auto text-lg font-bold
                           hover:bg-yellow-shade disabled:bg-yellow-lt flex disabled:bg-yellow-lt"
                disabled={!selectedAthlete || !selectedStartDate || loading}
                on:click={assignToAthlete}
        >
            {#if loading}
                <LoadingSpinner spinnerColor="fill-gray-300" spinnerBackground="text-yellow-lt" />
            {:else}
                Assign {selectedAthlete?.name ? `to ${selectedAthlete.name}` : ''} as a new program
            {/if}
        </button>
        {#if $program.id}
            <button class="bg-yellow rounded text-gray-300 p-2 my-2 px-4 m-auto text-lg font-bold
                           hover:bg-yellow-shade disabled:bg-yellow-lt flex disabled:bg-yellow-lt"
                    disabled={!selectedAthlete || !selectedStartDate || loading}
                    on:click={assignToAthlete}
            >
                {#if loading}
                    <LoadingSpinner spinnerColor="fill-gray-300" spinnerBackground="text-yellow-lt" />
                {:else}
                    Assign {selectedAthlete?.name ? `to ${selectedAthlete.name}` : ''}
                {/if}
            </button>
        {/if}
    </div>

    {#if error}
        <div class="rounded my-4 mx-2 p-2 px-4 border border-red text-red font-semibold text-center">
            There was an error assigning this program
        </div>
    {/if}
    {#if success}
        <div class="rounded my-4 mx-2 p-2 px-4 border border-green text-green font-semibold text-center">
            Success!
        </div>
    {/if}
</div>

<style></style>